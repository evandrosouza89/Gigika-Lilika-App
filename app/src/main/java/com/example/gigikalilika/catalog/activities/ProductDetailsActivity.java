package com.example.gigikalilika.catalog.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gigikalilika.catalog.R;
import com.example.gigikalilika.catalog.adapters.ViewPagerProductPicturesAdapter;
import com.example.gigikalilika.catalog.constants.Constants;
import com.example.gigikalilika.catalog.constants.EnumProductSize;
import com.example.gigikalilika.catalog.customviews.CustomNumberPicker;
import com.example.gigikalilika.catalog.entities.order.OrderItem;
import com.example.gigikalilika.catalog.entities.product.Product;
import com.example.gigikalilika.catalog.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class ProductDetailsActivity extends AppCompatActivity {

    private Product product;

    private TextView textViewToolbarProductName;

    private ViewPager viewPagerProductPictures;

    private CircleIndicator circleIndicatorProductPictures;

    private TextView textViewProductDescription;

    private TextView textViewProductReference;

    private TextView textViewProductPrice;

    private Button buttonSize1;

    private Button buttonSize2;

    private Button buttonSize3;

    private Button buttonSize4;

    private Button buttonSize5;

    private Button buttonCustomSize;

    private String selectedSize;

    private CustomNumberPicker customNumberPickerQuantity;

    private Double selectedSizePrice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        setup();

        initComponents();
    }

    private void setup() {
        product = (Product) getIntent().getSerializableExtra("product");

        textViewToolbarProductName = findViewById(R.id.textViewToolbarProductName);

        viewPagerProductPictures = findViewById(R.id.viewPagerProductPictures);

        circleIndicatorProductPictures = findViewById(R.id.circleIndicatorProductPictures);

        textViewProductDescription = findViewById(R.id.textViewProductDescription);

        textViewProductReference = findViewById(R.id.textViewProductReference);

        textViewProductPrice = findViewById(R.id.textViewProductPrice);

        buttonSize1 = findViewById(R.id.buttonSize1);

        buttonSize2 = findViewById(R.id.buttonSize2);

        buttonSize3 = findViewById(R.id.buttonSize3);

        buttonSize4 = findViewById(R.id.buttonSize4);

        buttonSize5 = findViewById(R.id.buttonSize5);

        buttonCustomSize = findViewById(R.id.buttonCustomSize);

        customNumberPickerQuantity = findViewById(R.id.customNumberPickerQuantity);
    }

    private void initComponents() {
        initToolbar();

        initSlider();

        initProductNameAndPrice();

        initProductDescriptionAndReference();

        initProductSizes();

        customNumberPickerQuantity.setValue(Constants.ORDER_DEFAULT_QUANTITY);
    }

    public void buttonSizeClick(View view) {
        deselectAllButtons();
        setSizeButtonSelected(findViewById(view.getId()), true);
        updatePriceTag(view.getId());
    }

    public void buttonAddOrderListClick(View view) {
        Intent intent = new Intent();

        OrderItem orderItem = createOrderItem();

        intent.putExtra("orderItem", orderItem);

        setResult(RESULT_OK, intent);
        finish();
    }

    private OrderItem createOrderItem() {
        OrderItem orderItem = new OrderItem();

        orderItem.setProduct(product);

        orderItem.setSize(selectedSize);

        orderItem.setQuantity(customNumberPickerQuantity.getValue());

        orderItem.setValue(selectedSizePrice);

        return orderItem;
    }

    public void fontTextViewHelpClick(View view) {
        Intent intent = new Intent(getApplicationContext(), SizingInformationActivity.class);
        startActivity(intent);
    }

    private void updatePriceTag(int buttonId) {
        if (buttonId == buttonSize1.getId()) {
            selectedSize = buttonSize1.getText().toString();
            selectedSizePrice = product.getProductPriceList().get(0).getProductPrice();
            textViewProductPrice.setText(Utils.formatPrice(selectedSizePrice));
        } else if (buttonId == buttonSize2.getId()) {
            selectedSize = buttonSize2.getText().toString();
            selectedSizePrice = product.getProductPriceList().get(1).getProductPrice();
            textViewProductPrice.setText(Utils.formatPrice(selectedSizePrice));
        } else if (buttonId == buttonSize3.getId()) {
            selectedSize = buttonSize3.getText().toString();
            selectedSizePrice = product.getProductPriceList().get(2).getProductPrice();
            textViewProductPrice.setText(Utils.formatPrice(selectedSizePrice));
        } else if (buttonId == buttonSize4.getId()) {
            selectedSize = buttonSize4.getText().toString();
            selectedSizePrice = product.getProductPriceList().get(3).getProductPrice();
            textViewProductPrice.setText(Utils.formatPrice(selectedSizePrice));
        } else if (buttonId == buttonSize5.getId()) {
            selectedSize = buttonSize5.getText().toString();
            selectedSizePrice = product.getProductPriceList().get(4).getProductPrice();
            textViewProductPrice.setText(Utils.formatPrice(selectedSizePrice));
        } else if (buttonId == buttonCustomSize.getId()) {
            selectedSize = buttonCustomSize.getText().toString();
            selectedSizePrice = null;
            textViewProductPrice.setText("Valor sob consulta");
        }
    }

    private void deselectAllButtons() {
        setSizeButtonSelected(buttonSize1, false);
        setSizeButtonSelected(buttonSize2, false);
        setSizeButtonSelected(buttonSize3, false);
        setSizeButtonSelected(buttonSize4, false);
        setSizeButtonSelected(buttonSize5, false);
        setSizeButtonSelected(buttonCustomSize, false);
    }

    private void setSizeButtonSelected(Button b, boolean selected) {
        if (selected) {
            b.setBackgroundColor(Color.DKGRAY);
        } else {
            b.setBackgroundColor(Color.BLACK);
        }
    }

    private void initToolbar() {
        setSupportActionBar(findViewById(R.id.toolbarProductDetails));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textViewToolbarProductName.setText(product.getName());
    }

    private void initSlider() {
        List<String> productImagePaths = new ArrayList();

        if (product.getProductImageList() != null) {
            product.getProductImageList().forEach(productImage -> productImagePaths.add(productImage.getSourcePath()));
        }

        viewPagerProductPictures.setAdapter(new ViewPagerProductPicturesAdapter(this, productImagePaths));

        circleIndicatorProductPictures.setViewPager(viewPagerProductPictures);
    }

    private void initProductNameAndPrice() {
        if (product.getProductPriceList() != null && product.getProductPriceList().get(0) != null) {
            textViewProductPrice.setText(Utils.formatPrice(product.getProductPriceList().get(0).getProductPrice()));
        }
    }

    private void initProductDescriptionAndReference() {
        textViewProductDescription.setText(product.getDescription());

        textViewProductReference.setText("Código de referência: " + product.getReferenceCode());
    }

    private void initProductSizes() {
        buttonSize1.setBackgroundColor(Color.DKGRAY);

        buttonSize1.setText(EnumProductSize.SIZE_01.getText());

        buttonSize2.setText(EnumProductSize.SIZE_02.getText());

        buttonSize3.setText(EnumProductSize.SIZE_03.getText());

        buttonSize4.setText(EnumProductSize.SIZE_04.getText());

        buttonSize5.setText(EnumProductSize.SIZE_05.getText());

        buttonCustomSize.setText(EnumProductSize.SIZE_OTHER.getText());

        updatePriceTag(buttonSize1.getId());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}