package vun.zisplaymerchant.managers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import vun.zisplaymerchant.Zisplay;
import vun.zisplaymerchant.utils.AppConstants;

/**
 * Created by ashok on 3/27/15.
 */
public class CacheManager {


    SQLLiteManager mDbHelper = new SQLLiteManager(Zisplay.getInstance().getApplicationContext());

    private static CacheManager ourInstance = new CacheManager();

    public static CacheManager getInstance() {
        return ourInstance;
    }

    private CacheManager() {
    }

    public long addRecord(String entity,String value)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(SQLLiteManager.FeedEntry.COLUMN_NAME_ENTRY_ID, entity);
        values.put(SQLLiteManager.FeedEntry.COLUMN_NAME_TITLE, value);

// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                SQLLiteManager.FeedEntry.TABLE_NAME,
                null,
                values);
        return newRowId;
    }

    public String readData(int id)
    {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                SQLLiteManager.FeedEntry._ID,
                SQLLiteManager.FeedEntry.COLUMN_NAME_TITLE,
                SQLLiteManager.FeedEntry.COLUMN_NAME_ENTRY_ID,

        };

// How you want the results sorted in the resulting Cursor
//        String sortOrder =
//                SQLLiteManager.FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        String[] values=new String[1];
        values[0]=id+"";
        Cursor cursor = db.query(
                SQLLiteManager.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                SQLLiteManager.FeedEntry._ID,                                // The columns for the WHERE clause
                values,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        cursor.moveToFirst();
        long itemId = cursor.getLong(
                cursor.getColumnIndexOrThrow(SQLLiteManager.FeedEntry._ID)
        );
       return cursor.getString(2);
    }

}
