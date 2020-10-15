package com.example.algorithms;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONException;

public class Count_Bobot_Tambah_Simpul {

    double bobot = 0;

    public void Count_Bobot_Tambah_Simpul(int index, int limit, JSONArray jArrCoordinates) throws JSONException{

        // cuma dijalanin sekali, kalo limit 1, maka 1-0 = 0
        // 0 == 0 (limit)
        if (index == limit) {
            JSONArray latlang = jArrCoordinates.getJSONArray(index);

            double lat_0 = latlang.getDouble(0);
            double lng_0 = latlang.getDouble(1);

            Location simpulAwal = new Location("");
            simpulAwal.setLatitude(lat_0);
            simpulAwal.setLongitude(lng_0);

            //get coordinate again

            JSONArray latlang1 = jArrCoordinates.getJSONArray(++index);

            double lat_1 = latlang1.getDouble(0);
            double lng_1 = latlang1.getDouble(1);

            Location simpulTengah = new Location("");
            simpulTengah.setLatitude(lat_1);
            simpulTengah.setLongitude(lng_1);

            //simpan jarak
            bobot += simpulAwal.distanceTo(simpulTengah);

            }else{
                    for (int i = 0; i < 1; i++){

                        //get JSON Coordinate
                        JSONArray latlang = jArrCoordinates.getJSONArray(index);

                        double lat_0 = latlang.getDouble(0);
                        double lng_0 = latlang.getDouble(1);

                        Location simpulAwal = new Location("");
                        simpulAwal.setLatitude(lat_0);
                        simpulAwal.setLongitude(lng_0);

                        //get coordinate again gaes
                        JSONArray latlang1 = jArrCoordinates.getJSONArray(++index);

                        double lat_1 = latlang1.getDouble(0);
                        double lng_1 = latlang1.getDouble(1);

                        Location simpulTengah = new Location("");
                        simpulTengah.setLatitude(lat_1);
                        simpulTengah.setLongitude(lng_1);

                        //simpen eys jaraknye
                        bobot += simpulAwal.distanceTo(simpulTengah);

                        if (index == limit)
                            break; //jika dah smpe ke tengah, break; misal 0-72

                        else --i;
                    }
        }

    }

}
