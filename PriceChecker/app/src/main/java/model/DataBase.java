package model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by david.cara on 3/2/16.
 */
public class DataBase extends SQLiteOpenHelper {

    final String sqlCreate = "create table if not exists "
            + " Product (ID integer primary key, "
            + " Name text not null, Price integer," +
            "Description text not null, Establishment text not null);";
    final String sqlDrop = "DROP TABLE Product;";
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Products";

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creem la base de dades  amb la taula Incidents al onCreate
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        db.execSQL(sqlDrop);
        db.execSQL(sqlCreate);
    }

}
