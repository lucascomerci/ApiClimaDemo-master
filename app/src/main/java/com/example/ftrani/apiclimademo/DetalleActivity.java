package com.example.ftrani.apiclimademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetalleActivity extends AppCompatActivity {

    TextView tvTemperatura, tvHumedad, tvPresion, tvUbicacion, tvPais, tvTemperaturaMin, tvTemperaturaMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_main);

        Bundle parametros = getIntent().getExtras();  			// accede a todos los extras utilizados y declarados en el Intent
        int id = parametros.getInt("id");

        tvTemperatura = (TextView) findViewById(R.id.tvTemperatura);
        tvTemperaturaMin = (TextView) findViewById(R.id.tvTemperaturaMin);
        tvTemperaturaMax = (TextView) findViewById(R.id.tvTemperaturaMax);

        tvPresion = (TextView) findViewById(R.id.tvPresion);
        tvHumedad = (TextView) findViewById(R.id.tvHumedad);
        tvUbicacion = (TextView) findViewById(R.id.tvUbicacion);
        tvPais = (TextView) findViewById(R.id.tvPais);

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

        cityCall.enqueue(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {
                City city = response.body();

                tvUbicacion.setText(String.valueOf(city.getName()));
                tvTemperatura.setText(String.valueOf(city.getClima().getTemp())+"º Centigrados");

                tvTemperaturaMin.setText(String.valueOf(city.getClima().getTemp_min())+"º Centigrados");
                tvTemperaturaMax.setText(String.valueOf(city.getClima().getTemp_max())+"º Centigrados");

                tvHumedad.setText(String.valueOf(city.getClima().getHumidity())+" %");
                tvPresion.setText(String.valueOf(city.getClima().getPressure())+" HPA");
                tvPais.setText(String.valueOf(city.getSys().getCountry()));

            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {
                Toast.makeText(DetalleActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
