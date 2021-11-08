package com.example.imageuploader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private ArrayList<Model> mList;
    private Context context;

    public ListAdapter(Context context, ArrayList<Model> mList){
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.model_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(mList.get(position).getImageUrl()).into(holder.imageView);
        holder.textView.setText(mList.get(position).getTitle());
        holder.EndDateTv.setText(mList.get(position).getEnddate());
        holder.Nowprice.setText(mList.get(position).getStartPrice());
        holder.Endprice.setText(mList.get(position).getEndPrice());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        TextView EndDateTv;
        TextView Endprice, Nowprice;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.imageview);
            textView = itemView.findViewById(R.id.rName);
            EndDateTv = itemView.findViewById(R.id.EndDateTv);
            Endprice = itemView.findViewById(R.id.endpriceTv);
            Nowprice = itemView.findViewById(R.id.nowprice);
        }

    }
}
