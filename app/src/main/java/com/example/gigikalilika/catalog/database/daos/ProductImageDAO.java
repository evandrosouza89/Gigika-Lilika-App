package com.example.gigikalilika.catalog.database.daos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gigikalilika.catalog.database.DatabaseHelper;
import com.example.gigikalilika.catalog.entities.product.ProductImage;

import java.util.ArrayList;
import java.util.List;

public class ProductImageDAO {

    public List<ProductImage> findByProductId(DatabaseHelper databaseHelper, long id) {

        List<ProductImage> productImageList = new ArrayList<>();

        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String sqlQuery = "SELECT * FROM " + ProductImage.TABLE_NAME
                + " WHERE PRODUCT_ID = " + id;

        Cursor c = database.rawQuery(sqlQuery, null);
        if (c.moveToFirst()) {
            do {
                ProductImage productImage = new ProductImage(c.getLong(0),
                        c.getString(1));

                productImageList.add(productImage);
            } while (c.moveToNext());
        }
        c.close();
        database.close();

        return productImageList;
    }
}