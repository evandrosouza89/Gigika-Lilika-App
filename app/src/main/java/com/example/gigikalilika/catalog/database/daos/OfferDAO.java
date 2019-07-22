package com.example.gigikalilika.catalog.database.daos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gigikalilika.catalog.database.DatabaseHelper;
import com.example.gigikalilika.catalog.entities.offer.Offer;
import com.example.gigikalilika.catalog.entities.offer.OfferText;

import java.util.ArrayList;
import java.util.List;

public class OfferDAO {

    public Offer findById(DatabaseHelper databaseHelper, long id) {
        Offer offer = null;

        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String sqlQuery = "SELECT * FROM " + Offer.TABLE_NAME
                + " WHERE OFFER.ID = " + id;

        Cursor c = database.rawQuery(sqlQuery, null);
        if (c.moveToFirst()) {
            do {

                offer = new Offer(c.getLong(0),
                        c.getInt(1),
                        c.getString(2),
                        c.getString(3),
                        c.getString(4),
                        new ArrayList<>());


                List<OfferText> offerTextList = fetchOfferTextList(databaseHelper, offer);
                offer.getOfferTextList().addAll(offerTextList);

            } while (c.moveToNext());
        }
        c.close();
        database.close();

        return offer;
    }

    public List<Offer> listAll(DatabaseHelper databaseHelper) {
        List<Offer> offerList = new ArrayList<>();

        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String sqlQuery = "SELECT * FROM " + Offer.TABLE_NAME;

        Cursor c = database.rawQuery(sqlQuery, null);
        if (c.moveToFirst()) {
            do {
                Long id = c.getLong(0);

                Offer offer = findById(databaseHelper, id);

                if (offer != null) {
                    offerList.add(offer);
                }

            } while (c.moveToNext());
        }

        c.close();
        database.close();

        return offerList;
    }

    private List<OfferText> fetchOfferTextList(DatabaseHelper databaseHelper, Offer offer) {
        OfferTextDAO offerTextDAO = new OfferTextDAO();
        return offerTextDAO.findByOfferId(databaseHelper, offer.getId());
    }
}