package com.example.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    int images[] = {
            R.drawable.smartphone_guy,
            R.drawable.calendar_guy,
            R.drawable.chatbot_guy,
            R.drawable.pest_guy,
    };

    int headings[] = {
            R.string.heading_one,
            R.string.heading_two,
            R.string.heading_three,
            R.string.heading_four,

    };

    int description[] = {
            R.string.desc_one,
            R.string.desc_two,
            R.string.desc_three,
            R.string.desc_four,

    };

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout, container, false);

        ImageView slidetitleimage = view.findViewById(R.id.titleImage);
        TextView slideHeading = view.findViewById(R.id.texttitle);
        TextView slideDescription = view.findViewById(R.id.textdescription);

        // Logging the position and resource values for debugging
        Log.d("ViewPagerAdapter", "Position: " + position);
        Log.d("ViewPagerAdapter", "Heading: " + context.getString(headings[position]));
        Log.d("ViewPagerAdapter", "Description: " + context.getString(description[position]));

        slidetitleimage.setImageResource(images[position]);
        slideHeading.setText(headings[position]);
        slideDescription.setText(description[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
