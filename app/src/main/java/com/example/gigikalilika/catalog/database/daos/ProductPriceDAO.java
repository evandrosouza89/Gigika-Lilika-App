package com.example.gigikalilika.catalog.database.daos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gigikalilika.catalog.database.DatabaseHelper;
import com.example.gigikalilika.catalog.entities.product.ProductPrice;

import java.util.ArrayList;
import java.util.List;

public class ProductPriceDAO {

    public List<ProductPrice> findByProductId(DatabaseHelper databaseHelper, long id) {

        List<ProductPrice> productPriceList = new ArrayList<>();

        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String sqlQuery = "SELECT * FROM " + ProductPrice.TABLE_NAME
                + " WHERE PRODUCT_ID = " + id;

        Cursor c = database.rawQuery(sqlQuery, null);
        if (c.moveToFirst()) {
            do {
                ProductPrice productPrice = new ProductPrice(c.getLong(0),
                        c.getString(1),
                        c.getDouble(2));

                productPriceList.add(productPrice);
            } while (c.moveToNext());
        }
        c.close();
        database.close();

        return productPriceList;
    }

}