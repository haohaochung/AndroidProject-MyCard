package com.cardcamera;

import static android.provider.BaseColumns._ID;//�o�ӬO��Ʈw���|���Ӱߤ@��ID

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

 

public class MyDatabase extends SQLiteOpenHelper {

   //�٧i���α`��(final)   
   public static SQLiteDatabase database;
   public static final String DATABASE_TABLE_NAME = "table_shop";  //���W��
   public static final String TABLE_SHOPS = "shops";
   public static final String COLUMN_DATABASE_NAME = "name";
   public static final String COLUMN_ID = "id";
   private final static String DATABASE_NAME = "db01.db";  //��Ʈw�W��
   private final static int DATABASE_VERSION = 1;  //��Ʈw����

   //public MyDatabase (Context context,String name,CursorFactory factory,int version) {
   public MyDatabase (Context context){
       super(context, DATABASE_NAME, null, DATABASE_VERSION);

  }

 

   @Override

   //�إ�table,��NAME �@�����

   public void onCreate(SQLiteDatabase db) {

	   final String INIT_TABLE = "CREATE TABLE " +  DATABASE_TABLE_NAME + " (" +_ID 
    		 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DATABASE_NAME + " TEXT);";

	   db.execSQL(INIT_TABLE);

  }

 

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	   db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME);

	   onCreate(db);

  }
   
   public static SQLiteDatabase getDatabase(Context context) {
       if (database == null || !database.isOpen()) {
           database = new MyDatabase(context).getWritableDatabase();
       }

       return database;
   }
   
   public void addShop(shop shop) {
       SQLiteDatabase db = this.getWritableDatabase();
       ContentValues values = new ContentValues();
       values.put(COLUMN_DATABASE_NAME, shop.getName());
       values.put(COLUMN_ID, shop.getId());
       db.insert(TABLE_SHOPS, null, values);
       db.close();
//       long studId = database.insert(TABLE_SHOPS, null, values);
       // now that the student is created return it ...
/*       Cursor cursor = database.query(TABLE_SHOPS,
       		SHOP_TABLE_COLUMNS, MyDatabase.ID + " = "
                       + studId, null, null, null, null);
       cursor.moveToFirst();
       shop newComment = parseShop(cursor);
       cursor.close();
       return newComment;*/
   }
   
   public ArrayList<shop> getAllShops() {
	   ArrayList<shop> shops = new ArrayList<shop>();
 /*      Cursor cursor = database.query(TABLE_SHOPS,
       		SHOP_TABLE_COLUMNS, null, null, null, null, null);
       cursor.moveToFirst();
       while (!cursor.isAfterLast()) {
           shop shop = parseShop(cursor);
           shops.add(shop);
           cursor.moveToNext();
       }
       cursor.close();
       return shops;*/
       String query = "Select * FROM " + TABLE_SHOPS + " ORDER BY _ID ASC";
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		int rows_num = cursor.getCount();
		 if(rows_num != 0) {
		  cursor.moveToFirst();   
		  for(int i=0; i<rows_num; i++) {
		   shop store = new shop();
		   store.setId(Integer.parseInt(cursor.getString(0)));
		   store.setName(cursor.getString(1));
		   shops.add(store);
		   cursor.moveToNext();
		  }
		 }
		 cursor.close();
		
		return shops;
   }

   public void deleteShop(String shopname) {
	   
	   String name = shopname.toString();
   	
   	   SQLiteDatabase db = this.getWritableDatabase();
   	   db.delete(TABLE_SHOPS, COLUMN_DATABASE_NAME + "=" + name, null);
		
		/*boolean result = false;
		
		String query = "Select * FROM " + TABLE_SHOPS + " WHERE " + COLUMN_DATABASE_NAME + " =  \"" + productname + "\"";

		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.rawQuery(query, null);
		
		shop store = new shop();
		
		if (cursor.moveToFirst()) {
			store.setId(Integer.parseInt(cursor.getString(0)));
			db.delete(TABLE_SHOPS, COLUMN_ID + " = ?",
		            new String[] { String.valueOf(store.getId()) });
			cursor.close();
			result = true;
		}
	        db.close();
		return result;*/
	}

}