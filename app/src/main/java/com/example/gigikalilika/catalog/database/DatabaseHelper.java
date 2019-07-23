package com.example.gigikalilika.catalog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.gigikalilika.catalog.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "GIGIKA_LILIKA.DB";

    private static final int DB_VERSION = 16;

    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            executeFromFile(db, R.raw.ddl);
            executeFromFile(db, R.raw.dml_categories);
            executeFromFile(db, R.raw.dml_products);
            executeFromFile(db, R.raw.dml_products_images);
            executeFromFile(db, R.raw.dml_products_categories);
            executeFromFile(db, R.raw.dml_products_prices);
            executeFromFile(db, R.raw.dml_offers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            try {
                executeFromFile(db, R.raw.ddl_drop_all);
                onCreate(db);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int executeFromFile(SQLiteDatabase db, int resourceId) throws IOException {
        int executedStatements = 0;

        InputStream insertsStream = context.getResources().openRawResource(resourceId);
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));

        while (insertReader.ready()) {
            String sql = insertReader.readLine();
            if (sql != null && !sql.isEmpty()) {
                db.execSQL(sql);
                executedStatements++;
            }
        }
        insertReader.close();

        return executedStatements;
    }

}