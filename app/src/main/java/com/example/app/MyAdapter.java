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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private String[] data1, data2;
    private int[] images;
    private Context context;

    public MyAdapter(Context ct, String[] s1, String[] s2, int[] img) {
        this.context = ct;
        this.data1 = s1;
        this.data2 = s2;
        this.images = img;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Perform a bounds check
        if (position < data1.length && position < data2.length && position < images.length) {
            holder.myText1.setText(data1[position]);
            holder.myText2.setText(data2[position]);
            holder.myImage.setImageResource(images[position]);

            holder.mainLayout.setOnClickListener(view -> {
                Intent intent = new Intent(context, RVPSecondActivity.class);
                intent.putExtra("data1", data1[position]);
                intent.putExtra("data2", data2[position]);
                intent.putExtra("myImage", images[position]);
                context.startActivity(intent);
            });
        } else {
            // Handle the error scenario, maybe log an error or show a default message/image
            Log.e("MyAdapter", "Invalid position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        // Return the minimum length to avoid index out of bounds
        return Math.min(data1.length, Math.min(data2.length, images.length));
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView myText1, myText2;
        ImageView myImage;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.myText1);
            myText2 = itemView.findViewById(R.id.myText2);
            myImage = itemView.findViewById(R.id.myImageView);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
