package com.example.ftrani.apiclimademo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ftrani.apiclimademo.SQLite.CiudadesOpenHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ListView listaCitys;
    List<City> citys = new ArrayList<>();
    private SwipeRefreshLayout sflLista;
    private Context context;
    private MensajeAdapter adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuAlta:

                MostrarAltaActivity();      //reload
                return true;
            case R.id.menuRefrescar:

                cargarLista();      //reload
                return true;
            case R.id.menuCerrar:
                finish();          //cerrar app
                System.exit(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.contextmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            //int pos= info.position;

            case R.id.disp_eliminar:
                // Tareas a realizar
                baja(citys.get(info.position).getId());
                Toast.makeText(MainActivity.this, R.string.ciudad_eliminada, Toast.LENGTH_LONG).show();
                cargarLista();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    public void baja(long id){
        CiudadesOpenHelper BaseDeDatos = new CiudadesOpenHelper(this,"administracion",null,1);
        SQLiteDatabase bd = BaseDeDatos.getWritableDatabase();
        bd.delete("TCiudades","id="+id,null);
        bd.close();
    }



    private void cargarLista(){

        insertaCiudadesFijas();
        cargarDesdeDB();
        Toast.makeText(MainActivity.this, R.string.ciudades_cargadas, Toast.LENGTH_LONG).show();

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


    void insertaCiudadesFijas(){
        altaEnDB(524901); //moscu
        altaEnDB(3835994); //santa rosa
        altaEnDB(3834310);  //toay
        altaEnDB(3164603);  //venezia
        altaEnDB(2643743);  //london
        altaEnDB(6251999);  //canada
        altaEnDB(5188351);  //egypto
        altaEnDB(1850147);
       // altaEnDB(524901);
       // altaEnDB(524901);

    }


    public void obtenerDatos(final int id){
        final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
        final String KEY_API = "0be0a9fa8d1c76ff5882268d3ffe5b07";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherService service = retrofit.create(WeatherService.class);

        Call<City> cityCall = service.getCity(id, KEY_API, "metric", "es");  //Santa Rosa
        //Call<City> cityCall = service.getCity(3860259, KEY_API, "metric", "es"); //Cordoba - 3860259
        //Call<City> cityCall = service.getCity(3836564, KEY_API, "metric", "es"); //San Salvador Jujuy 3836564

        /**/




        cityCall.enqueue(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {
                City city = response.body();

                Clima climaactual = new Clima();
                Sys sysactual = new Sys();
                String ciudad2 = String.valueOf(city.getName());
                sysactual.setCountry(String.valueOf(city.getSys().getCountry()));
                climaactual.setTemp((city.getClima().getTemp()));

                citys.add(new City(id,sysactual,ciudad2,climaactual));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void cargarDesdeDB() {
        CiudadesOpenHelper BaseDeDatos = new CiudadesOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = BaseDeDatos.getReadableDatabase();
        Cursor cursor = bd.rawQuery("SELECT * FROM TCiudades", null);
        citys.clear();
        if (cursor.moveToFirst()) {  //recorro la base de datos
            while (cursor.isAfterLast() == false) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                obtenerDatos(id);
                cursor.moveToNext();
            }
        }
        bd.close();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowHomeEnabled(true);  //Esto es para mostrar el icono a la izquierda del titulo en el action bar
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        listaCitys = (ListView) findViewById (R.id.lista_citys);

        adapter = new MensajeAdapter(citys); //ver
        listaCitys.setAdapter(adapter);      //ver


        sflLista = (SwipeRefreshLayout) findViewById(R.id.sflLista);
        registerForContextMenu(listaCitys);

        cargarLista();

        listaCitys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City item = (City) listaCitys.getItemAtPosition(position);
                Intent i;
                i = new Intent(context, DetalleActivity.class);
                i.putExtra("id",item.getId());
            //    i.putExtra("asunto",item.getAsunto().toString());
            //    i.putExtra("mensaje",item.getMensaje().toString());
                startActivity(i);
            }
        });

       sflLista.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
  //              cargarLista();
                sflLista.setRefreshing(false);
            }
        });
    }



    public void MostrarAltaActivity() {
        Intent i = new Intent(this, AltaActivity.class);
        startActivity(i);
    }


}
