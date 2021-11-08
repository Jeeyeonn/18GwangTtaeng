package com.example.imageuploader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.base.MoreObjects;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText TitleEt, startPrice, endPrice, pricePoint;
    TextView Enddate, EndTime;
    Button uploadBtn, showBtn;
    ProgressBar progressBar;
    ImageView addIv1;
    DatabaseReference root = FirebaseDatabase.getInstance().getReference("database");
    StorageReference reference = FirebaseStorage.getInstance().getReference();
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TitleEt = findViewById(R.id.TitleEt);
        uploadBtn = findViewById(R.id.uploadBtn);
        showBtn = findViewById(R.id.showBtn);
        addIv1 = findViewById(R.id.addIv1);
        startPrice = findViewById(R.id.StartPriceEt);
        endPrice = findViewById(R.id.EndPriceEt);
        pricePoint = findViewById(R.id.PricePointEt);
        Enddate = findViewById(R.id.Dateselect);
        EndTime = findViewById(R.id.Timeselect);
        progressBar = findViewById(R.id.progressbar);

        Spinner categorySpinner = (Spinner)findViewById(R.id.category);
        ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);



        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        Enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = year+"년 "+month+"월 "+day+"일";
                        Enddate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        EndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        MainActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        int Hour = hour;
                        int Minute = minute;
                        String time = Hour + "시 " + Minute + "분";

                        EndTime.setText(time);

                        SimpleDateFormat f24Hours = new SimpleDateFormat(
                                "HH:mm"
                        );
                        try{
                            Date date = f24Hours.parse(time);
                            SimpleDateFormat f12Hours = new SimpleDateFormat(
                                    "hh:mm: aa"
                            );
                            EndTime.setText(f12Hours.format(date));
                        }catch (ParseException e){
                            e.printStackTrace();
                        }
                    }
                }, 12, 0, false
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();
            }
        });


        addIv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = TitleEt.getText().toString();
                String category = categorySpinner.getSelectedItem().toString();
                String enddate = Enddate.getText().toString();
                String endtime = EndTime.getText().toString();
                String startprice = startPrice.getText().toString();
                String endprice = endPrice.getText().toString();
                String pricepoint = pricePoint.getText().toString();

                if(imageUri != null){
                    if(title.length()!=0){
                        if (enddate.equals("날짜 선택하기")) {
                            Toast.makeText(MainActivity.this, "마감 날짜를 선택하시오", Toast.LENGTH_SHORT).show();
                        }else{
                            if(endtime.equals("시간 선택하기")){
                                Toast.makeText(MainActivity.this, "마감 시간을 선택하시오", Toast.LENGTH_SHORT).show();
                            }else{
                                if(startprice.length() !=0){
                                    if (endprice.length() != 0){
                                        if (pricepoint.length() != 0){
                                            uploadToFirebase(imageUri, title, enddate, endtime, category, startprice, endprice, pricepoint);
                                        }else
                                            Toast.makeText(MainActivity.this, "입찰단위를 입력하시오", Toast.LENGTH_SHORT).show();
                                    }else
                                        Toast.makeText(MainActivity.this, "상한가를 입력하시오", Toast.LENGTH_SHORT).show();
                                }else
                                    Toast.makeText(MainActivity.this, "하한가를 입력하시오", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else
                        Toast.makeText(MainActivity.this, "물품명을 입력하시오", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(MainActivity.this, "사진을 선택하시오", Toast.LENGTH_SHORT).show();

            }
        });

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2 && resultCode == RESULT_OK && data != null){

            imageUri = data.getData();

            addIv1.setImageURI(imageUri);

        }
    }

    private void uploadToFirebase(Uri uri, String ttitle, String eendDate, String eendTime, String ccategory, String sstartPrice, String eendPrice, String ppricepoint ){
        StorageReference fileRef = reference.child(System.currentTimeMillis()+"."+getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Model model = new Model(uri.toString(), ttitle, eendDate, eendTime, ccategory, sstartPrice, eendPrice, ppricepoint);
                        String modelID = root.push().getKey();
                        root.child(modelID).setValue(model);
                        Toast.makeText(MainActivity.this, "업로드 성공", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, ListActivity.class));
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "업로드 실패 !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}