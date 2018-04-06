package com.alriftech.mobileresto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbhelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "db_mobileresto.db";
    private Context ctx = null;

    public dbhelper(Context context) {
        super(context, DB_NAME, null, 1);
        ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void buatDatabase(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS pembeli (no_telp TEXT PRIMARY KEY, nama TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS transaksi (waktu TEXT, no_telp TEXT, id_menu INTEGER, jumlah INTEGER, status INTEGER, PRIMARY KEY (waktu, no_telp, id_menu));");
        db.execSQL("CREATE TABLE IF NOT EXISTS menu (id_menu INTEGER PRIMARY KEY, nama_menu TEXT, harga TEXT, stock INTEGER, gambar_menu TEXT, deskripsi_menu TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS cart (id_cart INTEGER PRIMARY KEY, id_menu INTEGER);");
    }

    public void deleteTabase() {
        ctx.deleteDatabase(DB_NAME);
    }
}