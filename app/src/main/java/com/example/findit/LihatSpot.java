package com.example.findit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LihatSpot extends AppCompatActivity {

    DatabaseReference db_ref;

    RecyclerView recycler_view;


    RecyclerView.Adapter adapter;

    ProgressDialog progressDialog;

    List<data_spot> list = new ArrayList<>();

    SearchView caridata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_spot);

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);

//        caridata = (SearchView) findViewById(R.id.search);

    /**    caridata = (EditText) findViewById(R.id.cari);
        caridata.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){

                    search(s.toString());

                } else {

                    search("");
                }
            }
        }); **/

        recycler_view.setHasFixedSize(true);

        RecyclerView.LayoutManager my_LayoutManager = new GridLayoutManager(LihatSpot.this, 2);
        recycler_view.setLayoutManager(my_LayoutManager);
        //recycler_view.addItemDecoration(new LihatSpot.GridSpacingItemDecorate(2, dpToPx(8), true));
        recycler_view.setItemAnimator(new DefaultItemAnimator());

        progressDialog = new ProgressDialog(LihatSpot.this);

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

                adapter = new RecyclerViewAdapter(getApplicationContext(), list);

                recycler_view.setAdapter(adapter);

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressDialog.dismiss();
            }
        });

       /** if (caridata != null) {
            caridata.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    SearchData(s);
                    return true;
                }
            });
        }**/

    }

    private void SearchData(String str){
       ArrayList<data_spot> mylist = new ArrayList<>();
       for(data_spot object : list)
       {
           if (object.getNama_spot().toLowerCase().contains(str.toLowerCase())) {
               mylist.add(object);
           }
       }
        adapter = new RecyclerViewAdapter(getApplicationContext(), mylist);
        recycler_view.setAdapter(adapter);
    }

   /** private void search(String toString) {

        Query query = db_ref.orderByChild()

    } **/

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
