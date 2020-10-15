package com.example.algorithms;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.example.findit.DbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Get_Koordinat_awal_akhir extends Activity {

    //DB Sql Lite
    Cursor cursor;


    int fix_simpul_awal = 0;
    String explode_lat_only = "";
    Location posisiUser = new Location("");
    ArrayList<String> a_tmp_graph = new ArrayList<String>();

    //return json
    JSONObject jadi_json = new JSONObject();

    //ambil node dari field simpul awal dan tujuan di tabel graph, lalu digabung contohnya : 1,0 dan masukkan kedalam array
    List<String> barusDobel = new ArrayList<String>();
    List<String> indexBarisYgDikerjakan = new ArrayList<String>();

    public JSONObject Get_simpul(double latx, double longx, Context context) throws JSONException {

        //kordinat saya
        posisiUser.setLatitude(latx);
        posisiUser.setLongitude(longx);

        //tampung data node dari field simpul awal dan tujuan di tabel graph, lalu digabung; contoh 1,0 dan masukkan ke array
        List<String> barisDobel = new ArrayList<String>();
        List<String> indexBarisYgDikerjakan = new ArrayList<String>();

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //filter simpul yang akan di kerjain
        cursor = db.rawQuery("SELECT * FROM graph WHERE simpul_awal != '' AND simpul_tujuan != '' AND jalur != '' AND bobot != ''", null);
        cursor.moveToFirst();

        //ambil node dari field simpul awal dan simpul tujuan di tabel graph, kemudian digabung, contoh 1,0 dan masukkan ke array
        //looping di bawah ini untuk memeriksa baris dobel (yang simpulnya dobel) misal 1,0 dan 0,1 maka 1,0 dihitung akan tetapi 0,1 tidak
        for (int i = 0; i < cursor.getCount(); i++) {

            cursor.moveToPosition(i);

            //node dari field simpul awal
            String fieldSimpulAwal = cursor.getString(1).toString();

            //node dari field simpul akhir
            String fieldSimpulTujuan = cursor.getString(2).toString();

            String gabungSimpul = fieldSimpulAwal + "," + fieldSimpulTujuan;
            String gabung_balikSimpul = fieldSimpulTujuan + "," + fieldSimpulAwal;

            //seleksi ruas yang dobel, contoh : 1,0 == 0,1
            //kemudian pilih salah satunya
            if (barisDobel.isEmpty()) {

                barisDobel.add(gabung_balikSimpul);

                //field id pada tabel graph
                indexBarisYgDikerjakan.add(cursor.getString(0).toString());
            } else {

                if (!barisDobel.contains(gabungSimpul)) {
                    barisDobel.add(gabung_balikSimpul);

                    //field id pada tabel graph
                    indexBarisYgDikerjakan.add(cursor.getString(0).toString());

                }
            }
        }

        //list simpul yang dikerjakan
        StringBuilder indexBarisYgDikerjakan1 = new StringBuilder();
        for (int j = 0; j < indexBarisYgDikerjakan.size(); j++) {

            if (indexBarisYgDikerjakan1.length() == 0) {

                //field id lagi pada tabel graph (khusus untuk bagian stringbuilder)
                indexBarisYgDikerjakan1.append(indexBarisYgDikerjakan.get(j));
            } else {
                indexBarisYgDikerjakan1.append("," + indexBarisYgDikerjakan.get(j)); //untuk where in ('0,1)

            }
        }

        //Query baris gak dobel
        cursor = db.rawQuery("SELECT * FROM graph WHERE id IN (" + indexBarisYgDikerjakan1 + ")", null);
        cursor.moveToFirst();

        JSONObject obj = new JSONObject();

        //CARI JARAK
        //LOOPING SEMUA RECORDNYA
        for (int k = 0; k < cursor.getCount(); k++) {

            //variabel buat cari 1 jarak dalam 1 record (1 record isinya banyak koordinat)
            //simpan jarak user ke koordinat simpul dalam meter

            List<Double> jarakUserKeKoordinatSimpul = new ArrayList<Double>();

            cursor.moveToPosition(k);

            //dapatkan koordinat Lat, Lang dari field koordinat (3)
            String json = cursor.getString(3).toString();

            //manipulasi JSON
            JSONObject jObject = new JSONObject(json);
            JSONArray jArrCoordinates = jObject.getJSONArray("coordinates");
            JSONArray jArrNodes = jObject.getJSONArray("nodes");

            //get coordinates
            for (int w = 0; w < jArrCoordinates.length(); w++) {
                JSONArray latlongs = jArrCoordinates.getJSONArray(w);
                Double lats = latlongs.getDouble(0);
                Double longs = latlongs.getDouble(1);

                //set lat long
                Location koordinatSimpul = new Location("");
                koordinatSimpul.setLatitude(lats);
                koordinatSimpul.setLongitude(longs);

                //cari jarak dari posisi user ke koordinat sekitar SIMPUL (dalam meter)
                double jarak = posisiUser.distanceTo(koordinatSimpul);

                jarakUserKeKoordinatSimpul.add(jarak);
            }

            //cari bobot yang paling kecil
            int index_koordinatSimpul = 0;
            for (int m = 0; m < jarakUserKeKoordinatSimpul.size(); m++) {

                if (jarakUserKeKoordinatSimpul.get(m) <= jarakUserKeKoordinatSimpul.get(0)) {
                    jarakUserKeKoordinatSimpul.set(0, jarakUserKeKoordinatSimpul.get(m));

                    //index array dari value yang terkecil

                    index_koordinatSimpul = m;
                }
            }

            //field id dari table graph
            int row_id = cursor.getInt(0);

            JSONObject list = new JSONObject();

            //Memasukkan index koordinat array, bobot terkecil dan jumlah koordinat ke JSON
            list.put("row_id",row_id);
            list.put("index", index_koordinatSimpul);
            list.put("bobot", jarakUserKeKoordinatSimpul.get(0));
            list.put("nodes", jArrNodes.getString(0));
            list.put("count_koordinat", (jArrCoordinates.length() - 1));

            JSONArray ja = new JSONArray();
            ja.put(list);

            //create json
            //contoh ouputnya :
            //// {"0" : [{"row_id":17, "index":"7", "bobot":"427.66", "count_koordinat":"15", "nodes":"0-1"}]}
            obj.put(""+ k, ja);

        }// akhir looping baris DB

        double x = 0;
        double y = 0;
        int rowId_json = 0;
        int indexCoordinate_json = 0;
        int countCoordinate_json = 0;
        String nodes_json = "";

        // cari bobot terkecil dari JSON
        for (int s = 0; s < obj.length(); s++){

            if (s == 0){
                //first
                JSONArray a = obj.getJSONArray("0");
                JSONObject b = a.getJSONObject(0);
                x = Double.parseDouble(b.getString("bobot"));

                // ==========
                // row id field
                rowId_json = Integer.parseInt(b.getString("row_id"));
                //index coordinate sekitar simpul
                indexCoordinate_json = Integer.parseInt(b.getString("index"));
                //jumlah coordinate
                countCoordinate_json = Integer.parseInt(b.getString("count_koordinat"));
                //nodes
                nodes_json = b.getString("nodes").toString();

                // ==========
            }else {
                //second, dst
                JSONArray c = obj.getJSONArray("" + s );
                JSONObject d = c.getJSONObject(0);
                y = Double.parseDouble(d.getString("bobot"));

                //dapatkan value terkecil (bobot)
                if (y <= x){
                    //bobot
                    x = y;

                    // ==========
                    //row id field
                    rowId_json = Integer.parseInt(d.getString("row_id"));
                    //index coordinate sekitar simpul
                    indexCoordinate_json = Integer.parseInt(d.getString("index"));
                    //jumlah kordinat
                    countCoordinate_json = Integer.parseInt(d.getString("count_koordinat"));
                    //nodes
                    nodes_json = d.getString("nodes").toString();
                    // ==========

                }
            }
        }

        //nodes : 0-1

        String[] exp_nodes = nodes_json.split("-");

        int field_simpul_awal = Integer.parseInt(exp_nodes[0]);
        int field_simpul_tujuan = Integer.parseInt(exp_nodes[1]);

        //koordinat yang didapat diawal atau diakhir, maka gak perlu ada tambah simpul
        if (indexCoordinate_json == 0 || indexCoordinate_json == countCoordinate_json) {

            //tentukan simpul awal atau akhir yang dekat dengan posisi user
            if (indexCoordinate_json == 0 ){

                //nodes di field simpul awal
                fix_simpul_awal = field_simpul_awal;
            }else if (indexCoordinate_json == countCoordinate_json){

                //nodes di field simpul akhir
                fix_simpul_awal = field_simpul_tujuan;
            }
            jadi_json.put("status", "jalur_none");

        }
        //koordinat yang didapat berada di tengah2 simpul 0-1 (misal)
        else{
            //cari simpul dobel, simpulnya dibalik
            cursor = db.rawQuery("SELECT id FROM graph WHERE simpul_awal = "+field_simpul_tujuan +" AND simpul_tujuan = "+field_simpul_awal, null);
            cursor.moveToFirst();
            cursor.moveToPosition(0);

            int dobel = cursor.getCount();

            //ada simpul yang double (1,0) dan (0,1)
            if (dobel == 1){
                jadi_json.put("status", "jalur_double");

            } //tidak double, hanya (1,0)
            else if (dobel == 0) {
                jadi_json.put("status", "jalur_single");
            }
        }

        //JSON
        jadi_json.put("node_simpul_awal0", field_simpul_awal);
        jadi_json.put("node_simpul_awal1", field_simpul_tujuan);
        jadi_json.put("index_coordinate_json", indexCoordinate_json);
        jadi_json.put("explode_lat_only", explode_lat_only);

        return  jadi_json;

    } //public
}
