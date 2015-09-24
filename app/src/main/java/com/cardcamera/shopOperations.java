package com.cardcamera;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class shopOperations {
    // Database fields
    private MyDatabase dbHelper;
    private String[] SHOP_TABLE_COLUMNS = { MyDatabase.COLUMN_ID,
    		MyDatabase.COLUMN_DATABASE_NAME };
    private SQLiteDatabase database;
    public static final String CREATE_TABLE = 
            "CREATE TABLE " + MyDatabase.DATABASE_TABLE_NAME + " (" + 
            MyDatabase.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MyDatabase.COLUMN_DATABASE_NAME +" TEXT)";

    public shopOperations(Context context) {

    	database = MyDatabase.getDatabase(context);

    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
    	database.close();
    }

    public shop addShop(String name) {
        ContentValues values = new ContentValues();
        values.put(MyDatabase.COLUMN_DATABASE_NAME, name);
        long studId = database.insert(MyDatabase.TABLE_SHOPS, null, values);

        // now that the student is created return it ...
        Cursor cursor = database.query(MyDatabase.TABLE_SHOPS,
        		SHOP_TABLE_COLUMNS, MyDatabase.COLUMN_ID + " = "
                        + studId, null, null, null, null);
        cursor.moveToFirst();
        shop newComment = parseShop(cursor);
        cursor.close();
        return newComment;
    }

    public void deleteShop(shop comment) {
        long id = comment.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MyDatabase.TABLE_SHOPS, MyDatabase.COLUMN_ID
                + " = " + id, null);
    }

    public List getAllShops() {
        List shops = new ArrayList();
        Cursor cursor = database.query(MyDatabase.TABLE_SHOPS,
        		SHOP_TABLE_COLUMNS, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            shop shop = parseShop(cursor);
            shops.add(shop);
            cursor.moveToNext();
        }
        cursor.close();
        return shops;
    }

    private shop parseShop(Cursor cursor) {
        shop shop = new shop();
        shop.setId((cursor.getInt(0)));
        shop.setName(cursor.getString(1));
        return shop;
    }
}
