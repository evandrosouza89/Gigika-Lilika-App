package com.example.gigikalilika.catalog.database.daos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gigikalilika.catalog.database.DatabaseHelper;
import com.example.gigikalilika.catalog.entities.product.ProductCategory;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDAO {

    public List<ProductCategory> findByProductId(DatabaseHelper databaseHelper, long id) {

        List<ProductCategory> productCategoryList = new ArrayList<>();

        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String sqlQuery = "SELECT * FROM " + ProductCategory.TABLE_NAME
                + " WHERE PRODUCT_ID = " + id;

        Cursor c = database.rawQuery(sqlQuery, null);
        if (c.moveToFirst()) {
            do {
                ProductCategory productCategory = new ProductCategory(c.getLong(0),
                        c.getLong(1));

                productCategoryList.add(productCategory);
            } while (c.moveToNext());
        }
        c.close();
        database.close();

        return productCategoryList;
    }

}