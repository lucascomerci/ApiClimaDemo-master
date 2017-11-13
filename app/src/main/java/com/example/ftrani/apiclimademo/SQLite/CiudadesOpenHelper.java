package com.example.ftrani.apiclimademo.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vicentico on 10/7/17.
 */

public class CiudadesOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    public static String getCiudadesTableName() {
        return CIUDADES_TABLE_NAME;
    }

    private static final String CIUDADES_TABLE_NAME = "TCiudades";
    private String sqlCrear = "CREATE TABLE TCiudades (id INTEGER PRIMARY KEY  NOT NULL, ciudad TEXT)";


    public CiudadesOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCrear);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int anterior, int nueva) {
        db.execSQL("DROP TABLE IF EXISTS TCiudades");
        db.execSQL(sqlCrear);
    }
}
