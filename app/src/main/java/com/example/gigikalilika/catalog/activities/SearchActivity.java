package com.example.gigikalilika.catalog.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.gigikalilika.catalog.R;
import com.example.gigikalilika.catalog.adapters.ProductListRecyclerViewAdapter;
import com.example.gigikalilika.catalog.constants.Constants;
import com.example.gigikalilika.catalog.database.DatabaseHelper;
import com.example.gigikalilika.catalog.database.daos.ProductDAO;
import com.example.gigikalilika.catalog.entities.Category;
import com.example.gigikalilika.catalog.entities.order.OrderItem;
import com.example.gigikalilika.catalog.entities.product.Product;
import com.example.gigikalilika.catalog.fragments.PopUpItemAddedFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements ProductListRecyclerViewAdapter.ProductItemClickListener, PopUpItemAddedFragment.ButtonClickListener, SearchView.OnQueryTextListener {

    private static final int PRODUCT_DETAILS_ACTIVITY = 0;

    private List<OrderItem> orderItemList;

    private String searchQuery;

    private ArrayList<String> offerQuery;

    private Category categoryQuery;

    private SearchView searchViewProductSearch;

    private TextView textViewToolbarQueryTitle;

    private ProductListRecyclerViewAdapter productListRecyclerViewAdapter;

    private RecyclerView recyclerViewProductList;

    private PopUpItemAddedFragment popUpItemAddedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setup();
        initComponents();
    }

    private void setup() {
        searchQuery = getIntent().getStringExtra("searchQuery");

        offerQuery = (ArrayList<String>) getIntent().getSerializableExtra("offerQuery");

        categoryQuery = (Category) getIntent().getSerializableExtra("categoryQuery");

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());

        ProductDAO productDAO = new ProductDAO();

        orderItemList = new ArrayList<>();

        searchViewProductSearch = findViewById(R.id.searchViewProductSearch);

        textViewToolbarQueryTitle = findViewById(R.id.textViewToolbarQueryTitle);

        productListRecyclerViewAdapter = new ProductListRecyclerViewAdapter(this, productDAO.listAll(databaseHelper));

        recyclerViewProductList = findViewById(R.id.recyclerViewProductList);

        popUpItemAddedFragment = new PopUpItemAddedFragment();
    }

    private void initComponents() {
        initToolbar();

        productListRecyclerViewAdapter.setProductItemClickListener(this);

        popUpItemAddedFragment.setButtonClickListener(this);

        searchViewProductSearch.setOnQueryTextListener(this);

        initRecyclerViewProductList();
    }

    private void initToolbar() {
        setSupportActionBar(findViewById(R.id.toolbarSearch));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (offerQuery != null) {
            productListRecyclerViewAdapter.filterByOffer(offerQuery);

            textViewToolbarQueryTitle.setText("Ofertas");

            textViewToolbarQueryTitle.setVisibility(View.VISIBLE);
            searchViewProductSearch.setVisibility(View.GONE);
        } else if (categoryQuery != null) {
            productListRecyclerViewAdapter.filterByCategory(categoryQuery.getId());

            textViewToolbarQueryTitle.setText(categoryQuery.getName());

            textViewToolbarQueryTitle.setVisibility(View.VISIBLE);
            searchViewProductSearch.setVisibility(View.GONE);
        } else {
            textViewToolbarQueryTitle.setVisibility(View.GONE);
            searchViewProductSearch.setVisibility(View.VISIBLE);

            if (searchQuery != null) {
                searchViewProductSearch.setQuery(searchQuery, true);
            } else {
                searchViewProductSearch.requestFocus();
            }
        }
    }

    private void initRecyclerViewProductList() {
        recyclerViewProductList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProductList.setAdapter(productListRecyclerViewAdapter);
        if (offerQuery != null) {
            productListRecyclerViewAdapter.filterByOffer(offerQuery);
        } else if (categoryQuery != null) {
            productListRecyclerViewAdapter.filterByCategory(categoryQuery.getId());
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        productListRecyclerViewAdapter.filter(newText);
        return false;
    }

    @Override
    public void onProductListRecyclerViewItemClick(View view, View itemView, int position) {
        if (view.getId() == R.id.materialIconViewAddToOrder) {
            Product product = productListRecyclerViewAdapter.getItem(position);

            orderItemList.add(createOrderItem(product));

            setTextAndShowPopUpItemAdded(product.getName());
        } else {
            Intent intent = new Intent(getApplicationContext(), ProductDetailsActivity.class);

            intent.putExtra("product", productListRecyclerViewAdapter.getItem(position));

            startActivityForResult(intent, PRODUCT_DETAILS_ACTIVITY);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent();
        intent.putExtra("orderItemList", (Serializable) orderItemList);
        setResult(RESULT_CANCELED, intent);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("orderItemList", (Serializable) orderItemList);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void buttonShowOrderListClick(View view) {
        Intent intent = new Intent();
        intent.putExtra("orderItemList", (Serializable) orderItemList);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setTextAndShowPopUpItemAdded(String productName) {
        if (!popUpItemAddedFragment.isAdded()) {
            popUpItemAddedFragment.setText(productName + " adicionado à sua lista de pedidos!");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(popUpItemAddedFragment, popUpItemAddedFragment.getTag());
            transaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == PRODUCT_DETAILS_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                OrderItem orderItem = (OrderItem) intent.getSerializableExtra("orderItem");
                orderItemList.add(orderItem);
                setTextAndShowPopUpItemAdded(orderItem.getProduct().getName());
            }
        }
    }

    private OrderItem createOrderItem(Product product) {
        OrderItem orderItem = new OrderItem();

        orderItem.setProduct(product);
        orderItem.setQuantity(Constants.ORDER_DEFAULT_QUANTITY);
        orderItem.setSize(product.getProductPriceList().get(0).getProductSize());
        orderItem.setValue(product.getProductPriceList().get(0).getProductPrice() * Constants.ORDER_DEFAULT_QUANTITY);

        return orderItem;
    }
}