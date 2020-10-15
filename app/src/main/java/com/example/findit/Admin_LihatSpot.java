package com.example.findit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin_LihatSpot extends AppCompatActivity {

    DatabaseReference db_ref;

    RecyclerView recycler_view;


    RecyclerView.Adapter adapter;

    ProgressDialog progressDialog;

    List<data_spot> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_lihat_spot);

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);

        recycler_view.setHasFixedSize(true);

        RecyclerView.LayoutManager my_LayoutManager = new GridLayoutManager(Admin_LihatSpot.this, 2);
        recycler_view.setLayoutManager(my_LayoutManager);
        //recycler_view.addItemDecoration(new LihatSpot.GridSpacingItemDecorate(2, dpToPx(8), true));
        recycler_view.setItemAnimator(new DefaultItemAnimator());

        progressDialog = new ProgressDialog(Admin_LihatSpot.this);

        progressDialog.setMessage("Loading . . . ");

        progressDialog.show();

        db_ref = FirebaseDatabase.getInstance().getReference(TambahSpot.Database_Path);

        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    data_spot uploadDataSpot = postSnapshot.getValue(data_spot.class);

                    list.add(uploadDataSpot);

                }

                adapter = new Admin_RecyclerViewAdapter(getApplicationContext(), list);

                recycler_view.setAdapter(adapter);

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressDialog.dismiss();
            }
        });

    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
