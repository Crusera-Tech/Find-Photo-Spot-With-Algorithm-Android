package com.example.findit;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterNearby  extends ArrayAdapter {



    int groupid;
    ArrayList<String> records;
    Context context;
    RelativeLayout linear;
    DecimalFormat form = new DecimalFormat("0.00");

    public AdapterNearby(@NonNull Context context, int vg, int id, ArrayList<String> records) {
        super(context, vg, id, records);
        this.context = context;
        groupid = vg;
        this.records = records;

    }

    public View getView(int position, View convertView, ViewGroup parent){
        int posisi;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(groupid, parent, false);

        String[] row_items = records.get(position).split("__");
        linear = (RelativeLayout) itemView.findViewById(R.id.linear);

        TextView tmpt_id = (TextView) itemView.findViewById(R.id.textid);
        tmpt_id.setText("No : " +row_items[0]);

        TextView tmpt_NmSpot = (TextView) itemView.findViewById(R.id.NamaSpotFoto);
        tmpt_NmSpot.setText(row_items[1]);

        TextView tmpt_jarak = (TextView) itemView.findViewById(R.id.jarak);
        tmpt_jarak.setText("Jarak : " +row_items[2] + " Km");
      //  linear.setBackgroundColor(Color.parseColor());

        return itemView;
    }
}
