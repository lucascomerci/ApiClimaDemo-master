package com.example.ftrani.apiclimademo;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by vicentico on 9/6/17.
 */

public class MensajeAdapter extends BaseAdapter {

  private List<City> citys;

    public MensajeAdapter(List<City> citys){
        this.citys = citys;
    }



    @Override
    public int getCount() {
        return citys.size();
    }

    @Override
    public Object getItem(int position) {
        return citys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return citys.get(position).getId();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        String recorta_cadena;
        //optimizacion de reutilizacion de recursos
        if(convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensaje,parent,false);
        }else{
            view = convertView;
        }

        City city = (City) getItem(position);

       // TextView idRemitente = (TextView) view.findViewById(R.id.idRemitente);
        TextView pais = (TextView) view.findViewById(R.id.pais);
        TextView ciudad    = (TextView) view.findViewById(R.id.ciudad);
        TextView temp = (TextView) view.findViewById(R.id.temp);


        int flagOffset = 0x1F1E6;
        int asciiOffset = 0x41;

        String country = city.getSys().getCountry();

        int firstChar = Character.codePointAt(country, 0) - asciiOffset + flagOffset;
        int secondChar = Character.codePointAt(country, 1) - asciiOffset + flagOffset;

        String flag = new String(Character.toChars(firstChar))
                + new String(Character.toChars(secondChar));






        ciudad.setText(city.getName());
       // pais.setText(city.getSys().getCountry());
        pais.setText(flag);

        float f = (city.getClima().getTemp());
        String s = Float.toString(f);
        s = s+"º Cent.";




        temp.setText(s);

        return view;
    }
}
