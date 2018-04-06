package com.alriftech.mobileresto;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityDeveloper extends AppCompatActivity {

    private SQLiteDatabase db = null;
    private dbhelper data = null;
    private ListView lstData;

    private void buatHandleDb() {
        data = new dbhelper(this);
        db = data.getWritableDatabase();
        data.buatDatabase(db);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        lstData = (ListView)findViewById(R.id.lstData);

        buatHandleDb();
        lihatPelangan();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.developermenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.mnuDevPelanggan:
                lihatPelangan();
                break;

            case R.id.mnuDevTransaksi:
                lihatTransaksi();
                break;

            case R.id.mnuDevMakanan:
                lihatMenu();
                break;

            case R.id.mnuDevDelDb:
                data.deleteTabase();
                Toast.makeText(this, "Database sudah dihapus! Mohon restart aplikasi terlebih dahulu", Toast.LENGTH_SHORT).show();
                break;

            case R.id.mnuGenMenu:
                dummyMenu();
                Toast.makeText(this, "Menu sudah digenerate", Toast.LENGTH_SHORT).show();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void lihatPelangan() {
        ArrayAdapter<String> adapter;
        ArrayList<String> arrayList;

        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.blacktext, arrayList);

        lstData.setAdapter(adapter);

        Cursor raw = db.rawQuery("SELECT * FROM pembeli", null);

        try {
            if (raw.moveToFirst()) {
                for (; !raw.isAfterLast(); raw.moveToNext()) {
                    arrayList.add(raw.getString(0).toString() + " [" + raw.getString(1).toString() + "]");
                    adapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(this, "Data pelanggan masih kosong!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (NullPointerException e) {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void lihatTransaksi() {
        ArrayAdapter<String> adapter;
        ArrayList<String> arrayList;

        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.blacktext, arrayList);

        lstData.setAdapter(adapter);

        Cursor raw = db.rawQuery("SELECT * FROM transaksi", null);

        try {
            if (raw.moveToFirst()) {
                for (; !raw.isAfterLast(); raw.moveToNext()) {
                    arrayList.add(raw.getString(0).toString());
                    adapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(this, "Data transaksi masih kosong!", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void lihatMenu() {
        ArrayAdapter<String> adapter;
        ArrayList<String> arrayList;

        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.blacktext, arrayList);

        lstData.setAdapter(adapter);

        Cursor raw = db.rawQuery("SELECT * FROM menu", null);

        try {
            if (raw.moveToFirst()) {
                for (; !raw.isAfterLast(); raw.moveToNext()) {
                    arrayList.add(raw.getString(1).toString() + " [Rp. " + raw.getString(2).toString() + "]");
                    adapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(this, "Data makanan masih kosong!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (NullPointerException e) {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void dummyMenu() {
        insertMenu("Ayam Bakar Rica-Rica", "10000", 50, "ayambakar", "Ayam bakar ini hanya dijual di Purbalingga dan stoknya sangat terbatas!");
        insertMenu("Nasi Goreng", "12000", 10, "nasigoreng", "Nasi goreng kaya akan protein dan mengenyangkan");
        insertMenu("Mie Ayam Mantab Jiwa", "9000", 15, "mieayam", "Mie ayam yang berbeda dengan yang lain, terdiri dari mie dan ayam");
        insertMenu("Nasi Uduk", "5000", 30, "nasiuduk", "Nasi uduk dibuat dengan bumbu yang sangat enak di lidah!");
        insertMenu("Es Teh", "2500", 100, "esteh", "Es teh yang nikmat dan segar di mulut");
    }

    private void insertMenu(String nama_menu, String harga, int stock, String gambar, String deskripsi) {
        ContentValues cv = new ContentValues();

        cv.put("nama_menu", nama_menu);
        cv.put("harga", harga);
        cv.put("stock", stock);
        cv.put("gambar_menu", gambar);
        cv.put("deskripsi_menu", deskripsi);

        db.insert("menu", "nama_menu", cv);
    }
}
