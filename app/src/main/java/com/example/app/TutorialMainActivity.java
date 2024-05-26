package com.example.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TutorialMainActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_layout);

        // To remove ActionBar Title
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("");
        }

        ImageView backButton = findViewById(R.id.tutorialBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call method to handle back button click
                onBackButtonClick();
            }
        });
    }

    private void onBackButtonClick() {
        // Handle back button click, for example:
        finish();
    }

    public static class BoundingBoxOverlayView extends View {
        private List<RectF> boundingBoxes = new ArrayList<>();

        public BoundingBoxOverlayView(Context context) {
            super(context);
        }

        public BoundingBoxOverlayView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public BoundingBoxOverlayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public void setBoundingBoxes(List<RectF> boundingBoxes) {
            this.boundingBoxes = boundingBoxes;
            invalidate(); // Trigger redraw
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);

            for (RectF rect : boundingBoxes) {
                canvas.drawRect(rect, paint);
            }
        }
    }
}

