package com.example.app.bayotemman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;

import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQViewHolder> {

    private Context context;  // Added context for inflating layouts
    private List<FAQ> faqList;

    // Constructor that takes Context and List<FAQ>
    public FAQAdapter(Context context, List<FAQ> faqList) {
        this.context = context;
        this.faqList = faqList;
    }

    @NonNull
    @Override
    public FAQViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.faq_item, parent, false);
        return new FAQViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQViewHolder holder, int position) {
        FAQ faq = faqList.get(position);
        holder.textViewQuestion.setText(faq.getQuestion());

        // Optionally: You can add click listeners here to expand/collapse the answer
        // Example: Implement expand/collapse behavior on click if needed in the future
    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }

    public static class FAQViewHolder extends RecyclerView.ViewHolder {
        TextView textViewQuestion;
        ImageView imageViewArrow;

        public FAQViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewQuestion = itemView.findViewById(R.id.textViewQuestion);
            imageViewArrow = itemView.findViewById(R.id.imageViewArrow);
        }
    }
}
