package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder> {

    private String[] data3, data4;
    private int[] images;
    private Context context;

    public MyAdapter2(Context ct, String[] s3, String[] s4, int[] img) {
        this.context = ct;
        this.data3 = s3;
        this.data4 = s4;
        this.images = img;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_laninarow, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Perform a bounds check
        if (position < data3.length && position < data4.length && position < images.length) {
            holder.myText3.setText(data3[position]);
            holder.myText4.setText(data4[position]);
            holder.myImageView3.setImageResource(images[position]);

            holder.laninarowlayout.setOnClickListener(view -> {
                Intent intent = new Intent(context, RVPThirdActivity.class);
                intent.putExtra("data3", data3[position]);
                intent.putExtra("data4", data4[position]);
                intent.putExtra("myImage", images[position]);
                context.startActivity(intent);
            });
        } else {
            // Handle the error scenario, maybe log an error or show a default message/image
            Log.e("MyAdapter2", "Invalid position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        // Return the minimum length to avoid index out of bounds
        return Math.min(data3.length, Math.min(data4.length, images.length));
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView myText3, myText4;
        ImageView myImageView3;
        ConstraintLayout laninarowlayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myText3 = itemView.findViewById(R.id.myText3);
            myText4 = itemView.findViewById(R.id.myText4);
            myImageView3 = itemView.findViewById(R.id.myImageView3);
            laninarowlayout = itemView.findViewById(R.id.laninarowlayout);
        }
    }
}
