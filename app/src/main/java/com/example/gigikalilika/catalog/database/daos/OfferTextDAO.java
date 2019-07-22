package com.example.gigikalilika.catalog.database.daos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gigikalilika.catalog.database.DatabaseHelper;
import com.example.gigikalilika.catalog.entities.offer.OfferText;

import java.util.ArrayList;
import java.util.List;

public class OfferTextDAO {

    public List<OfferText> findByOfferId(DatabaseHelper databaseHelper, long id) {

        List<OfferText> offerTextList = new ArrayList<>();

        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String sqlQuery = "SELECT * FROM " + OfferText.TABLE_NAME
                + " WHERE OFFER_ID = " + id;

        Cursor c = database.rawQuery(sqlQuery, null);
        if (c.moveToFirst()) {
            do {
                OfferText offerText = new OfferText(c.getLong(0),
                        c.getString(1));

                offerTextList.add(offerText);
            } while (c.moveToNext());
        }
        c.close();
        database.close();

        return offerTextList;
    }

}
