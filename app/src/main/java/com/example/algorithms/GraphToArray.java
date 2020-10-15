package com.example.algorithms;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.findit.DbHelper;

public class GraphToArray {


    //DB
    DbHelper dbHelper;
    SQLiteDatabase db;
    protected Cursor cursor;

    //array graph
    String[][] graph = new String[100][100];

    /* Convert Graph from DB to Array
     * parameter mainContext : context MainActivity
     * return array String[][]
     */

    public String[][] convertToArray (Context mainContext) {

        dbHelper = new DbHelper(mainContext);
        db = dbHelper.getReadableDatabase();

        //PINDAHIN GRAPH DARI DB KE GRAPH ARRAY
        cursor = db.rawQuery("SELECT * FROM graph ORDER BY simpul_awal,simpul_tujuan ASC", null);
        cursor.moveToFirst();

        String temp_index_baris = "";
        int index_kolom = 0;
        int jml_baris = cursor.getCount();

        for (int i = 0; i < jml_baris; i++) {

            //baris
            cursor.moveToPosition(i);

            //cari index kolom
            int simpulAwalDB = Integer.parseInt(cursor.getString(1));

            if (temp_index_baris == "") {
                temp_index_baris = String.valueOf(simpulAwalDB);
            } else {
                //simpul awal berikutnya tidak sama dengan sebelumnya, reset index kolom = 0
                if (Integer.parseInt(temp_index_baris) != simpulAwalDB) {
                    index_kolom = 0;
                    temp_index_baris = String.valueOf(simpulAwalDB);
                }
            }

            //masukkan ke graph array
            String simpulTujuan_dan_bobot = "";
            if (cursor.getString(2).equals("") && cursor.getString(3).equals("") && cursor.getString(4).equals("")) { //tidak ada derajat keluar
                simpulTujuan_dan_bobot = ";";

            } //ada derajat keluar
            else {
                //example output : ->789.98
                simpulTujuan_dan_bobot = cursor.getString(2).toString() + "->" + cursor.getString(4).toString(); //simpul tujuan dan bobot
            }

            graph[simpulAwalDB][index_kolom] = simpulTujuan_dan_bobot;
            index_kolom++;
        }//for

        return graph;
    }
}
