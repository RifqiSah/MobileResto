package com.alriftech.mobileresto;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ContentFrameLayout;
import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db = null;
    private dbhelper data = null;
    private TextView txtNomor;
    private TextView txtNama;

    private void buatHandleDb() {
        data = new dbhelper(this);
        db = data.getWritableDatabase();
        data.buatDatabase(db);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buatHandleDb();
        // dummyMenu();

        txtNomor = (TextView)findViewById(R.id.txtNomor);
        txtNama = (TextView)findViewById(R.id.txtNama);
    }

    private int getIdPembeli(String str) {
        int id = 0;

        Cursor raw = db.rawQuery("SELECT * FROM pembeli WHERE no_telp='" + str + "'", null);

        try {
            if (raw.moveToFirst()) {
                for (; !raw.isAfterLast(); raw.moveToNext()) {
                    id = 1;
                }
            }
        }

        catch (NullPointerException e) {
            Toast.makeText(this, "Terjadi kesalahan!", Toast.LENGTH_SHORT).show();
            id = 0;
        }

        return id;
    }

    public void simpanData(View v) {
        int status = 0;

        if (txtNomor.getText().toString().trim().equals("")) {
            txtNomor.setError( "Harap isi nomor terlebih dahulu!" );
            return;
        }

        if (txtNama.getText().toString().trim().equals("")) {
            txtNama.setError( "Harap isi nama lengkap terlebih dahulu!" );
            return;
        }

        status = getIdPembeli(txtNomor.getText().toString());
        if (status == 0) {
            ContentValues cv = new ContentValues();

            cv.put("no_telp", txtNomor.getText().toString());
            cv.put("nama", txtNama.getText().toString());

            db.insert("pembeli", "no_telp", cv);
        }

        db.execSQL("DELETE FROM cart");

        Intent i = new Intent(getApplicationContext(), ActivityMenu.class);
        i.putExtra("nomor", txtNomor.getText().toString());
        startActivity(i);
    }

    public void developerMode(View v) {
        Toast.makeText(this, "Anda masuk developer mode!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), ActivityDeveloper.class);
        startActivity(i);
    }
}
