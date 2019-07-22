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
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.example.gigikalilika.catalog.R;
import com.example.gigikalilika.catalog.activities.SearchActivity;
import com.example.gigikalilika.catalog.constants.Constants;
import com.example.gigikalilika.catalog.constants.EnumOfferBannerType;
import com.example.gigikalilika.catalog.entities.offer.Offer;
import com.example.gigikalilika.catalog.entities.offer.OfferText;
import com.example.gigikalilika.catalog.utils.Utils;
import com.lid.lib.LabelImageView;
import com.tomer.fadingtextview.FadingTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OffersShowcaseFragment extends Fragment implements View.OnClickListener {

    private static final int SEARCH_ACTIVITY = 0;

    public static final int FADING_TIMEOUT = 5;

    private List<Offer> offerList;

    private LinearLayout linearLayoutBannerList;

    private Map<View, Offer> offerBannerMap;

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
        offerList = (List<Offer>) getArguments().getSerializable("offerList");
        offerBannerMap = new HashMap<>();
    }

    private void initComponents(View view) {
        linearLayoutBannerList = view.findViewById(R.id.linearLayoutBannerList);
        initBanners(view);
    }

    private void initBanners(View view) {
        if (offerList != null) {
            offerList.forEach(offer -> {
                if (offer.getBannerType() != null) {
                    EnumOfferBannerType enumOfferBannerType = EnumOfferBannerType.getByValue(offer.getBannerType());
                    if (enumOfferBannerType == EnumOfferBannerType.FADING) {
                        initOfferBannerFading(view, offer);
                    } else {
                        initOfferBanner(view, offer);
                    }
                }
            });
        }
    }

    private void initOfferBannerFading(View view, Offer offer) {
        ViewGroup parent = view.findViewById(R.id.container);

        FrameLayout frameLayoutOfferBannerFading = (FrameLayout) getLayoutInflater().inflate(R.layout.layout_offer_banner_fading, parent, false);

        FadingTextView fadingTextViewBonusBanner = frameLayoutOfferBannerFading.findViewById(R.id.fadingTextViewBonusBanner);

        initFadingTextViewBonusBanner(offer, fadingTextViewBonusBanner);

        ImageView imageViewBonusBanner = frameLayoutOfferBannerFading.findViewById(R.id.imageViewBonusBanner);
        imageViewBonusBanner.setImageResource(getResources().getIdentifier(offer.getBackgroundSourcePath(),
                R.drawable.class.getSimpleName(), getContext().getPackageName()));

        frameLayoutOfferBannerFading.setLayoutParams(getLayoutParams());

        linearLayoutBannerList.addView(frameLayoutOfferBannerFading);

        frameLayoutOfferBannerFading.setClickable(true);
        frameLayoutOfferBannerFading.setOnClickListener(this);

        offerBannerMap.put(frameLayoutOfferBannerFading, offer);
    }

    private void initFadingTextViewBonusBanner(Offer offer, FadingTextView fadingTextViewBonusBanner) {
        ArrayList<String> offerTexts = new ArrayList();

        for (OfferText offerText : offer.getOfferTextList()) {
            offerTexts.add(offerText.getOfferText());
        }

        fadingTextViewBonusBanner.setTexts(offerTexts.toArray(new String[0]));
        fadingTextViewBonusBanner.setTimeout(FADING_TIMEOUT, TimeUnit.SECONDS);
    }

    private void initOfferBanner(View view, Offer offer) {

        FrameLayout frameLayoutOfferBanner = getFrameLayoutOfferBanner(view, offer);

        initLabelImageViewOfferLabel(offer, frameLayoutOfferBanner);

        initTextViewLines(offer, frameLayoutOfferBanner);

        frameLayoutOfferBanner.setLayoutParams(getLayoutParams());

        linearLayoutBannerList.addView(frameLayoutOfferBanner);

        frameLayoutOfferBanner.setClickable(true);
        frameLayoutOfferBanner.setOnClickListener(this);

        offerBannerMap.put(frameLayoutOfferBanner, offer);
    }

    private void initLabelImageViewOfferLabel(Offer offer, FrameLayout frameLayoutOfferBanner) {
        LabelImageView labelImageViewOfferLabel = frameLayoutOfferBanner.findViewById(R.id.labelImageViewOfferLabel);

        if (offer.getLabelText() == null) {
            labelImageViewOfferLabel.setLabelVisual(false);
        }
        labelImageViewOfferLabel.setLabelText(offer.getLabelText());
        labelImageViewOfferLabel.setImageResource(getResources().getIdentifier(offer.getBackgroundSourcePath(),
                R.drawable.class.getSimpleName(), getContext().getPackageName()));
    }

    private FrameLayout getFrameLayoutOfferBanner(View view, Offer offer) {
        FrameLayout frameLayoutOfferBanner;

        ViewGroup parent = view.findViewById(R.id.container);

        EnumOfferBannerType enumOfferBannerType = EnumOfferBannerType.getByValue(offer.getBannerType());

        if (enumOfferBannerType == EnumOfferBannerType.NO_LABEL) {
            frameLayoutOfferBanner = (FrameLayout) getLayoutInflater().inflate(R.layout.layout_offer_banner_label_pink, parent, false);
        } else {
            if (linearLayoutBannerList.getChildCount() % 2 == 0) {
                frameLayoutOfferBanner = (FrameLayout) getLayoutInflater().inflate(R.layout.layout_offer_banner_label_pink, parent, false);
            } else {
                frameLayoutOfferBanner = (FrameLayout) getLayoutInflater().inflate(R.layout.layout_offer_banner_label_blue, parent, false);
            }
        }

        return frameLayoutOfferBanner;
    }

    private void initTextViewLines(Offer offer, FrameLayout frameLayoutOfferBanner) {
        if (offer != null && offer.getOfferTextList() != null) {
            if (!offer.getOfferTextList().isEmpty() && offer.getOfferTextList().get(0) != null) {
                TextView textViewFirstLine = frameLayoutOfferBanner.findViewById(R.id.textViewFirstLine);
                textViewFirstLine.setText(offer.getOfferTextList().get(0).getOfferText());
            }

            if (offer.getOfferTextList().size() > 1 && offer.getOfferTextList().get(1) != null) {
                TextView textViewSecondLine = frameLayoutOfferBanner.findViewById(R.id.textViewSecondLine);
                textViewSecondLine.setText(offer.getOfferTextList().get(1).getOfferText());
            }
        }
    }

    private LayoutParams getLayoutParams() {
        LayoutParams layoutParams = new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        if (linearLayoutBannerList.getChildCount() > 0) {
            layoutParams.setMargins(0, Utils.dpToPixel(getContext(), 5), 0, 0);
        }

        return layoutParams;
    }

    @Override
    public void onClick(View view) {
        Offer offer = offerBannerMap.get(view);

        if (offer != null && offer.getSearchQuery() != null && !offer.getSearchQuery().isEmpty()) {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            intent.putExtra("offerQuery", new ArrayList(Arrays.asList(offer.getSearchQuery().split(Constants.OFFER_REFERENCE_SEPARATOR))));
            getActivity().startActivityForResult(intent, SEARCH_ACTIVITY);
        }
    }
}