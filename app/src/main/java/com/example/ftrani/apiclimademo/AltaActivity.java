package com.example.ftrani.apiclimademo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ftrani.apiclimademo.SQLite.CiudadesOpenHelper;
//mport com.mobilesiri.SQLite.DispositivoOpenHelper;

public class AltaActivity extends AppCompatActivity {

    private EditText altaId;
 //   private EditText altaDescripcion;
 //   private EditText altaIP;
 //   private Button botonCargar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta);
        //   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //   setSupportActionBar(toolbar);
        // context = MainActivity.this;

        Button botonCargar = (Button) findViewById(R.id.botonCargar);
        botonCargar.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View v) {
                cargarCiudad();
            }
        });

    }

    public void cargarCiudad() {
        //comprobaciones

        altaId = (EditText) findViewById(R.id.altaId);
     //   altaDescripcion = (EditText) findViewById(R.id.altaDescripcion);
      //  altaIP = (EditText) findViewById(R.id.altaIP);

        altaEnDB(Integer.parseInt(altaId.getText().toString()));
        Toast.makeText(AltaActivity.this, R.string.ciudad_agregada, Toast.LENGTH_SHORT).show();
        MostrarMainActivity();

    }

    public void altaEnDB(int id) {
        CiudadesOpenHelper BaseDeDatos = new CiudadesOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = BaseDeDatos.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("id", id);
     //   registro.put("ciudad", "re f dcopada");
        // registro.put("ip", ip);
        // registro.put("MAC", "algo");
        bd.insert("TCiudades", null, registro);
        bd.close();
        //   Toast.makeText(MainActivity.this, "Smartux dado de alta correctaemtte", Toast.LENGTH_LONG).show();
    }


    public void MostrarMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}