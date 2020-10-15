package com.example.findit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<data_spot> MainImageUploadListInfo;

    public RecyclerViewAdapter(Context context, List<data_spot> TempList){

        this.MainImageUploadListInfo = TempList;

        this.context = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        data_spot dataSpot = MainImageUploadListInfo.get(position);

        Glide.with(context).load(dataSpot.getImageURL_1()).into(holder.gambar_tampil);

        ((CardView) holder.cardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainImageUploadListInfo.size()>position) {
                    Intent intent = new Intent(context, DetailInfoSpot.class);
                    intent.putExtra("IDSpot", MainImageUploadListInfo.get(position).getIDSpot());
                    intent.putExtra("imageURL_1", MainImageUploadListInfo.get(position).getImageURL_1());
                    intent.putExtra("imageURL_2", MainImageUploadListInfo.get(position).getImageURL_2());
                    intent.putExtra("imageURL_3", MainImageUploadListInfo.get(position).getImageURL_3());
                    intent.putExtra("nama_spot", MainImageUploadListInfo.get(position).getNama_spot());
                    intent.putExtra("deskripsi", MainImageUploadListInfo.get(position).getDeskripsi());
                    intent.putExtra("biaya", MainImageUploadListInfo.get(position).getBiaya());
                    intent.putExtra("rekom_wkt", MainImageUploadListInfo.get(position).getRekom_wkt());
                    intent.putExtra("kategori", MainImageUploadListInfo.get(position).getKategori());
                    intent.putExtra("Lat", MainImageUploadListInfo.get(position).getLat());
                    intent.putExtra("Long", MainImageUploadListInfo.get(position).getLong());
                    intent.putExtra("alamat",MainImageUploadListInfo.get(position).getAlamat());
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return MainImageUploadListInfo.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView gambar_tampil;
        public Object cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gambar_tampil = (ImageView) itemView.findViewById(R.id.gambar_tampil);

            cardView = (CardView) itemView.findViewById(R.id.cardview);

        }
    }
}
