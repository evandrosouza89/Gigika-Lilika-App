package com.example.gigikalilika.catalog.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.gigikalilika.catalog.R;
import com.example.gigikalilika.catalog.adapters.TabAdapter;
import com.example.gigikalilika.catalog.database.DatabaseHelper;
import com.example.gigikalilika.catalog.database.daos.CategoryDAO;
import com.example.gigikalilika.catalog.database.daos.OfferDAO;
import com.example.gigikalilika.catalog.entities.order.OrderItem;
import com.example.gigikalilika.catalog.fragments.CategoriesFragment;
import com.example.gigikalilika.catalog.fragments.OffersShowcaseFragment;
import com.example.gigikalilika.catalog.fragments.OrderDetailsFragment;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int SEARCH_ACTIVITY = 0;

    private DatabaseHelper databaseHelper;

    private OfferDAO offerDAO;

    private CategoryDAO categoryDAO;

    private Toolbar toolbarMain;

    private TabAdapter tabAdapter;

    private TabLayout tabLayoutMain;

    private ViewPager viewPagerSwipeTab;

    private OrderDetailsFragment orderDetailsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setup();

        initComponents();
    }

    private void setup() {
        databaseHelper = new DatabaseHelper(getApplicationContext());

        offerDAO = new OfferDAO();

        categoryDAO = new CategoryDAO();

        toolbarMain = findViewById(R.id.toolbarMain);

        viewPagerSwipeTab = findViewById(R.id.viewPagerSwipeTab);

        tabLayoutMain = findViewById(R.id.tabLayoutMain);

        orderDetailsFragment = new OrderDetailsFragment();
    }

    private void initComponents() {
        initToolbar();

        OffersShowcaseFragment offersShowcaseFragment = new OffersShowcaseFragment();

        initOffersShowcaseFragment(offersShowcaseFragment);

        CategoriesFragment categoriesFragment = new CategoriesFragment();

        initCategoriesFragment(categoriesFragment);

        initTabAdapter(offersShowcaseFragment, categoriesFragment, orderDetailsFragment);

        viewPagerSwipeTab.setAdapter(tabAdapter);

        tabLayoutMain.setupWithViewPager(viewPagerSwipeTab);
    }


    private void initToolbar() {
        setSupportActionBar(toolbarMain);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initOffersShowcaseFragment(OffersShowcaseFragment offersShowcaseFragment) {
        Bundle args = new Bundle();
        args.putSerializable("offerList", (Serializable) offerDAO.listAll(databaseHelper));
        offersShowcaseFragment.setArguments(args);
    }

    private void initCategoriesFragment(CategoriesFragment categoriesFragment) {
        Bundle args = new Bundle();
        args.putSerializable("categoryList", (Serializable) categoryDAO.listAll(databaseHelper));
        categoriesFragment.setArguments(args);
    }

    private void initTabAdapter(OffersShowcaseFragment offersShowcaseFragment, CategoriesFragment categoriesFragment, OrderDetailsFragment orderDetailsFragment) {
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(offersShowcaseFragment, "Destaques");
        tabAdapter.addFragment(categoriesFragment, "Catálogo");
        tabAdapter.addFragment(orderDetailsFragment, "Pedido");
    }

    public void materialIconViewSearchClick(View view) {
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivityForResult(intent, SEARCH_ACTIVITY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SEARCH_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                switchToOrderTab();
                addOrderItem(intent);
            } else if (resultCode == RESULT_CANCELED) {
                addOrderItem(intent);
            }
        }
    }

    private void switchToOrderTab() {
        viewPagerSwipeTab.setCurrentItem(2, true);
    }

    private void addOrderItem(Intent intent) {
        List<OrderItem> orderItemList = (List<OrderItem>) intent.getSerializableExtra("orderItemList");

        if (orderItemList != null) {
            orderItemList.forEach(orderItem -> orderDetailsFragment.addOrderItem(orderItem));
        }
    }

}