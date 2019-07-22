package com.example.gigikalilika.catalog.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.gigikalilika.catalog.R;

import java.util.List;

public class ViewPagerProductPicturesAdapter extends PagerAdapter {

    private final Context context;

    private final List<String> imageSourceList;

    private final LayoutInflater inflater;

    public ViewPagerProductPicturesAdapter(Context context, List<String> imageSourceList) {
        this.context = context;
        this.imageSourceList = imageSourceList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageSourceList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.layout_slidingimages, view, false);

        final ImageView imageView = imageLayout.findViewById(R.id.productImage);

        imageView.setImageResource(context.getResources().getIdentifier(imageSourceList.get(position),
                R.drawable.class.getSimpleName(), context.getPackageName()));

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}