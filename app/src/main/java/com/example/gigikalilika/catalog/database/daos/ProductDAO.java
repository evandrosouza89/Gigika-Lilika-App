package com.example.gigikalilika.catalog.database.daos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gigikalilika.catalog.database.DatabaseHelper;
import com.example.gigikalilika.catalog.entities.product.Product;
import com.example.gigikalilika.catalog.entities.product.ProductCategory;
import com.example.gigikalilika.catalog.entities.product.ProductImage;
import com.example.gigikalilika.catalog.entities.product.ProductPrice;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public Product findById(DatabaseHelper databaseHelper, long id) {

        Product product = null;

        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String sqlQuery = "SELECT * FROM " + Product.TABLE_NAME
                + " WHERE PRODUCT.ID = " + id;

        Cursor c = database.rawQuery(sqlQuery, null);
        if (c.moveToFirst()) {
            do {

                product = new Product(c.getLong(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>());

                List<ProductImage> productImageList = fetchProductImageList(databaseHelper, product);
                product.getProductImageList().addAll(productImageList);

                List<ProductPrice> productPriceList = fetchProductPriceList(databaseHelper, product);
                product.getProductPriceList().addAll(productPriceList);

                List<ProductCategory> productCategoryList = fetchProductCategoryList(databaseHelper, product);
                product.getProductCategoryList().addAll(productCategoryList);

            } while (c.moveToNext());
        }

        c.close();
        database.close();

        return product;
    }

    public List<Product> listAll(DatabaseHelper databaseHelper) {

        List<Product> productList = new ArrayList<>();

        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String sqlQuery = "SELECT * FROM " + Product.TABLE_NAME;

        Cursor c = database.rawQuery(sqlQuery, null);
        if (c.moveToFirst()) {
            do {
                Long id = c.getLong(0);

                Product product = findById(databaseHelper, id);

                if (product != null) {
                    productList.add(product);
                }

            } while (c.moveToNext());
        }

        c.close();
        database.close();

        return productList;
    }

    private List<ProductImage> fetchProductImageList(DatabaseHelper databaseHelper, Product product) {
        ProductImageDAO productImageDAO = new ProductImageDAO();
        return productImageDAO.findByProductId(databaseHelper, product.getId());
    }

    private List<ProductPrice> fetchProductPriceList(DatabaseHelper databaseHelper, Product product) {
        ProductPriceDAO productPriceDAO = new ProductPriceDAO();
        return productPriceDAO.findByProductId(databaseHelper, product.getId());
    }

    private List<ProductCategory> fetchProductCategoryList(DatabaseHelper databaseHelper, Product product) {
        ProductCategoryDAO productCategoryDAO = new ProductCategoryDAO();
        return productCategoryDAO.findByProductId(databaseHelper, product.getId());
    }

}