package com.example.findit;

import javax.xml.transform.sax.TemplatesHandler;

public class data_spot {


    private String IDSpot;
    private String nama_spot;
    private String deskripsi;
    private String biaya;
    private String kategori;
    private String rekom_wkt;
    private String Lat;
    private String Long;
    private String alamat;

    private String imageURL_1;
    private String imageURL_2;
    private String imageURL_3;

    public data_spot(){

    }
    public data_spot(String IDSpot, String nama_spot, String deskripsi, String biaya, String kategori, String rekom_wkt, String Lat, String Long, String alamat, String imageURL_1, String imageURL_2, String imageURL_3) {

        this.setIDSpot(IDSpot);
        this.setNama_spot(nama_spot);
        this.setDeskripsi(deskripsi);
        this.setBiaya(biaya);
        this.setKategori(kategori);
        this.setRekom_wkt(rekom_wkt);
        this.setLat(Lat);
        this.setLong(Long);
        this.setAlamat(alamat);
        this.setImageURL_1(imageURL_1);
        this.setImageURL_2(imageURL_2);
        this.setImageURL_3(imageURL_3);
    }


    public String getIDSpot () {
        return  IDSpot;
    }
    public void setIDSpot(String IDSpot){
        this.IDSpot = IDSpot;
    }

    public String getNama_spot(){
        return nama_spot;
    }
    public void setNama_spot(String nama_spot){
        this.nama_spot = nama_spot;
    }

    public String getDeskripsi(){
        return deskripsi;
    }
    public void setDeskripsi(String deskripsi){
        this.deskripsi = deskripsi;
    }

    public String getBiaya(){
        return biaya;
    }
    public void setBiaya(String biaya){
        this.biaya = biaya;
    }

    public String getKategori(){
        return kategori;
    }
    public void setKategori(String kategori){
        this.kategori = kategori;
    }

    public String getRekom_wkt(){
        return rekom_wkt;
    }
    public void setRekom_wkt(String rekom_wkt){
        this.rekom_wkt = rekom_wkt;
    }

    public String getLat(){
        return Lat;
    }
    public void setLat(String Lat){
        this.Lat = Lat;
    }

    public String getLong(){
        return Long;
    }
    public void setLong(String Long){
        this.Long = Long;
    }

    public String getAlamat(){
        return alamat;
    }
    public void setAlamat(String alamat){
        this.alamat = alamat;
    }

    public String getImageURL_1(){
        return imageURL_1;
    }

    public void setImageURL_1(String imageURL_1) {
        this.imageURL_1 = imageURL_1;
    }

    public String getImageURL_2(){
        return imageURL_2;
    }

    public void setImageURL_2(String imageURL_2) {
        this.imageURL_2 = imageURL_2;
    }

    public String getImageURL_3(){
        return imageURL_3;
    }

    public void setImageURL_3(String imageURL_3) {
        this.imageURL_3 = imageURL_3;
    }
}


