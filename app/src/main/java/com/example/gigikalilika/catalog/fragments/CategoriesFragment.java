package com.example.gigikalilika.catalog.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gigikalilika.catalog.R;
import com.example.gigikalilika.catalog.activities.SearchActivity;
import com.example.gigikalilika.catalog.entities.Category;
import com.example.gigikalilika.catalog.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriesFragment extends Fragment implements View.OnClickListener {

    private static final int SEARCH_ACTIVITY = 0;

    private List<Category> categoryList;

    private LinearLayout linearLayoutBannerList;

    private Map<View, Category> categoryBannerMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setup();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_banner_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initComponents(view);
    }

    private void setup() {
        categoryList = (List<Category>) getArguments().getSerializable("categoryList");
        categoryBannerMap = new HashMap<>();
    }

    private void initComponents(View view) {
        linearLayoutBannerList = view.findViewById(R.id.linearLayoutBannerList);
        initCategories();
    }

    private void initCategories() {
        if (categoryList != null) {
            categoryList.forEach(category -> {
                FrameLayout frameLayoutCategoryBanner = (FrameLayout) getLayoutInflater().inflate(R.layout.layout_category_banner, null);

                initImageViewCategory(category, frameLayoutCategoryBanner);

                initTextViewCategoryName(category, frameLayoutCategoryBanner);

                frameLayoutCategoryBanner.setLayoutParams(getLayoutParams());

                linearLayoutBannerList.addView(frameLayoutCategoryBanner);

                categoryBannerMap.put(frameLayoutCategoryBanner, category);

                frameLayoutCategoryBanner.setClickable(true);
                frameLayoutCategoryBanner.setOnClickListener(this);
            });
        }
    }

    private void initImageViewCategory(Category category, FrameLayout frameLayoutCategoryBanner) {
        ImageView imageViewCategory = frameLayoutCategoryBanner.findViewById(R.id.imageViewCategory);
        imageViewCategory.setImageResource(getResources().getIdentifier(category.getBackgroundImage(),
                R.drawable.class.getSimpleName(), getContext().getPackageName()));
    }

    private void initTextViewCategoryName(Category category, FrameLayout frameLayoutCategoryBanner) {
        TextView textViewCategoryName = frameLayoutCategoryBanner.findViewById(R.id.textViewCategoryName);
        textViewCategoryName.setText(category.getName());
    }

    private LinearLayout.LayoutParams getLayoutParams() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        if (linearLayoutBannerList.getChildCount() > 0) {
            layoutParams.setMargins(0, Utils.dpToPixel(getContext(), 5), 0, 0);
        }

        return layoutParams;
    }

    @Override
    public void onClick(View view) {
        Category category = categoryBannerMap.get(view);

        if (category != null) {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            intent.putExtra("categoryQuery", category);
            getActivity().startActivityForResult(intent, SEARCH_ACTIVITY);
        }
    }
}