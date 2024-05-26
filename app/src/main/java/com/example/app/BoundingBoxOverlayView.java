package com.example.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

public class BoundingBoxOverlayView extends View {
    private List<PestDetector.Detection> detections;

    private final Paint textPaint;
    private final Paint boxPaint;

    public BoundingBoxOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        textPaint = new Paint();
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(40);
        textPaint.setStyle(Paint.Style.FILL);

        boxPaint = new Paint();
        boxPaint.setColor(Color.GREEN);
        boxPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setStrokeWidth(5);
    }

    public void setDetections(List<PestDetector.Detection> detections) {
        this.detections = detections;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (detections != null) {
            for (PestDetector.Detection detection : detections) {
                // Draw bounding box if available
                if (detection.boundingBox != null) {
                    drawBoundingBox(canvas, detection.boundingBox);
                }
                // Draw label and confidence
                drawLabel(canvas, detection);
            }
        }
    }

    private void drawBoundingBox(Canvas canvas, RectF boundingBox) {
        canvas.drawRect(boundingBox, boxPaint);
    }

    private void drawLabel(Canvas canvas, PestDetector.Detection detection) {
        float x = (detection.boundingBox != null) ? detection.boundingBox.left : 10;
        float y = (detection.boundingBox != null) ? detection.boundingBox.top - 10 : 50;
        canvas.drawText(detection.label + " (" + detection.confidence + ")", x, y, textPaint);
    }
}