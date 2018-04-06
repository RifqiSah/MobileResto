package com.alriftech.mobileresto;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ActivityCheckout extends AppCompatActivity {

    private SQLiteDatabase db = null;
    private dbhelper data = null;
    private ListView lstData;
    private TextView txtTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        lstData = (ListView)findViewById(R.id.lstCart);
        txtTotal = (TextView)findViewById(R.id.txtHarga);

        buatHandleDb();

//        ContentValues cv = new ContentValues();
//
//        cv.put("id_menu", 3);
//        db.insert("cart", "id_menu", cv);
//
//        cv.put("id_menu", 1);
//        db.insert("cart", "id_menu", cv);
//
//        cv.put("id_menu", 5);
//        db.insert("cart", "id_menu", cv);

        lihatData();
    }

    private void buatHandleDb() {
        data = new dbhelper(this);
        db = data.getWritableDatabase();
        data.buatDatabase(db);
    }

    public void lihatData() {
        long total = 0;

        ArrayAdapter<String> adapter;
        ArrayList<String> arrayList;

        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.whitetext, arrayList);

        lstData.setAdapter(adapter);

        Cursor raw = db.rawQuery("SELECT nama_menu, harga FROM cart JOIN menu ON cart.id_menu = (menu.id_menu)", null);

        try {
            if (raw.moveToFirst()) {
                for (; !raw.isAfterLast(); raw.moveToNext()) {
                    arrayList.add(raw.getString(0).toString());
                    total += raw.getLong(1);
                    adapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(this, "Pesanan Anda masih kosong!", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (NullPointerException e) {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

        txtTotal.setText("Rp. " + total);
    }

    public void checkOut(View v) {
        db.execSQL("DELETE FROM cart");
        Toast.makeText(this, "Pesanan sudah diterima. Terima kasih", Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
