package com.alriftech.mobileresto;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class ActivityMenu extends AppCompatActivity {

    private SQLiteDatabase db = null;
    private dbhelper data = null;
    private int id_pembeli = 0;
    private ListView lstmenu;
    private MyAdapter adapter;

    private void buatHandleDb() {
        data = new dbhelper(this);
        db = data.getWritableDatabase();
        data.buatDatabase(db);
    }

    private String getDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        buatHandleDb();
        lstView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menumenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.mnuCheckOut:
                startActivity(new Intent(this, ActivityCheckout.class));
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void lstView() {
        Cursor raw = db.rawQuery("SELECT * FROM menu", null);

        ArrayList<String> a1 = new ArrayList<String>();
        ArrayList<String> b1 = new ArrayList<String>();
        ArrayList<Integer> c1 = new ArrayList<Integer>();
        ArrayList<String> d1 = new ArrayList<String>();

        try {
            if (raw.moveToFirst()) {
                for (; !raw.isAfterLast(); raw.moveToNext()) {
                    a1.add(raw.getString(1).toString());
                    b1.add(raw.getString(2).toString());
                    c1.add(raw.getInt(3));
                    d1.add(raw.getString(4).toString());
                }
            }
        }

        catch (NullPointerException e) {
            Toast.makeText(this, "Data pelangan masih kosong!", Toast.LENGTH_SHORT).show();
        }

        String[] aa = a1.toArray(new String[a1.size()]);
        String[] bb = b1.toArray(new String[b1.size()]);
        String[] dd = d1.toArray(new String[d1.size()]);

        int[] cc = new int[c1.size()];
        for (int i = 0; i < cc.length; i++)
            cc[i] = c1.get(i).intValue();

        lstmenu = (ListView)findViewById(R.id.lstMenu);
        adapter = new MyAdapter(this, aa, bb, cc, dd);
        lstmenu.setAdapter(adapter);

        a1.clear();
        b1.clear();
        c1.clear();
        d1.clear();
    }

    public void pesanMenu(String makanan, int stok, int index) {
        if (stok <= 0)
            Toast.makeText(this, "Stok telah habis!", Toast.LENGTH_SHORT).show();
        else {
            ContentValues cv = new ContentValues();
            String no_telp = getIntent().getExtras().getString("nomor");

            cv.put("waktu", getDate());
            cv.put("no_telp", no_telp);
            cv.put("jumlah", 1);
            cv.put("status", 0);

            db.insert("transaksi", "waktu", cv);
            db.execSQL("UPDATE menu SET stock=" + (stok - 1) + " WHERE id_menu=" + (index + 1) + ";");
            db.execSQL("INSERT INTO cart (id_menu) VALUES (" + (index + 1) + ");");

            Toast.makeText(this, "Anda telah memesan " + makanan, Toast.LENGTH_SHORT).show();
        }

        lstView();
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String nama[];
        String harga[];
        int stok[];
        String gambar[];

        MyAdapter(Context c, String[] title, String[] harga, int[] stok, String[] gambar) {
            super(c, R.layout.row, R.id.txtNama, title);

            this.context = c;
            this.nama = title;
            this.harga = harga;
            this.stok   = stok;
            this.gambar = gambar;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup groupView) {
            LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.row, groupView, false);

            ImageView img = (ImageView)row.findViewById(R.id.icon);
            TextView txtNama = (TextView)row.findViewById(R.id.txtNama);
            TextView txtHarga = (TextView)row.findViewById(R.id.txtHarga);
            TextView txtStock = (TextView)row.findViewById(R.id.txtStock);
            Button btnPesan = (Button)row.findViewById(R.id.btnPesan);

            txtNama.setText(nama[i]);
            txtHarga.setText("Rp. " + harga[i]);
            txtStock.setText(stok[i] + " porsi tersisa");

            int id = getResources().getIdentifier(gambar[i], "drawable", getPackageName());
            img.setImageResource(id);

            if (stok[i] <= 10)
                txtStock.setTextColor(Color.rgb(255, 0, 0));

            btnPesan.setClickable(false);
            btnPesan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pesanMenu(nama[i], stok[i], i);
                }
            });

            return row;
        }
    }
}
