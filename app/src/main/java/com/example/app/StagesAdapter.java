package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StagesAdapter extends RecyclerView.Adapter<StagesAdapter.StageViewHolder> {

    private List<Stage> stages;
    private Context context;

    public StagesAdapter(Context context, List<Stage> stages) {
        this.context = context;
        this.stages = stages;
    }

    @NonNull
    @Override
    public StageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stage, parent, false);
        return new StageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StageViewHolder holder, int position) {
        Stage stage = stages.get(position);
        holder.tvStageName.setText(stage.getName());
        holder.imageView.setImageResource(stage.getImageResId());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, StageDetailActivity.class);
            intent.putExtra("name", stage.getName());
            intent.putExtra("description", stage.getDescription());
            intent.putExtra("suggestions", stage.getSuggestions());
            intent.putExtra("imageResId", stage.getImageResId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return stages.size();
    }

    static class StageViewHolder extends RecyclerView.ViewHolder {
        TextView tvStageName;
        ImageView imageView;

        public StageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStageName = itemView.findViewById(R.id.tvStageName);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}

