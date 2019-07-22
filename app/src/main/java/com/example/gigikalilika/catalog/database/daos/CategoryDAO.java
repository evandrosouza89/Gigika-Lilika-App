package com.example.gigikalilika.catalog.database.daos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gigikalilika.catalog.database.DatabaseHelper;
import com.example.gigikalilika.catalog.entities.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public Category findById(DatabaseHelper databaseHelper, long id) {
        Category category = null;

        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String sqlQuery = "SELECT * FROM " + Category.TABLE_NAME + " CATEGORY"
                + " WHERE CATEGORY.ID = " + id;

        Cursor c = database.rawQuery(sqlQuery, null);
        if (c.moveToFirst()) {
            do {
                category = new Category(c.getLong(0),
                        c.getString(1),
                        c.getString(2));

            } while (c.moveToNext());
        }
        c.close();
        database.close();

        return category;
    }

    public List<Category> listAll(DatabaseHelper databaseHelper) {
        List<Category> categoryList = new ArrayList<>();

        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String sqlQuery = "SELECT * FROM " + Category.TABLE_NAME + " CATEGORY";

        Cursor c = database.rawQuery(sqlQuery, null);
        if (c.moveToFirst()) {
            do {
                Category category = new Category(c.getLong(0),
                        c.getString(1),
                        c.getString(2));

                categoryList.add(category);
            } while (c.moveToNext());
        }
        c.close();
        database.close();

        return categoryList;
    }
}