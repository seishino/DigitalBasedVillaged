package com.dbv.digitalbasedvillaged;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowDataPenduduk extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_all_anggota);
        final ListView listAnggota = (ListView)findViewById(R.id.listAnggota);
        final Map<String,Map<String,String>> dataAllAnggota = new HashMap<>();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = db.getReference();
        Intent intent = getIntent();
        String noKK = intent.getStringExtra("NoKK");
        final List<String> listNoKK = new ArrayList<>();
        final List<String> listNama = new ArrayList<>();
        myRef.child("Penduduk").child(noKK).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    listNoKK.add(ds.getKey());
                    listNama.add(ds.child("Nama").getValue().toString());
                    Map<String,String> dataPerorang = new HashMap<>();
                    dataPerorang.put("Nama",ds.child("Nama").getValue().toString());
                    dataPerorang.put("Gender",ds.child("Gender").getValue().toString());
                    dataPerorang.put("Umur",ds.child("Umur").getValue().toString());
                    dataAllAnggota.put(ds.getKey().toString(),dataPerorang);
                }
                ListAnggotaScanAdapter listAnggotaAdapter = new ListAnggotaScanAdapter(ShowDataPenduduk.this, listNoKK,listNama);
                listAnggota.setAdapter(listAnggotaAdapter);
                listAnggota.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //final String value = (String)parent.getItemAtPosition(position);
                        LayoutInflater inflater = (LayoutInflater) ShowDataPenduduk.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View layout = inflater.inflate(R.layout.show_data_penduduk,
                                (ViewGroup) findViewById(R.id.popup));
                        final PopupWindow pw = new PopupWindow(layout, 800, 1300, true);
                        final TextView txtNama= (TextView)layout.findViewById(R.id.txtNama);
                        final TextView txtKK = (TextView)layout.findViewById(R.id.txtNoKK);
                        final TextView txtUmur = (TextView)layout.findViewById(R.id.txtUmur);
                        final TextView txtGender = (TextView)layout.findViewById(R.id.txtGender);

                        txtNama.setText("Nama : " + dataAllAnggota.get(listNoKK.get(position)).get("Nama"));
                        txtKK.setText("No KK : " + listNoKK.get(position));
                        txtUmur.setText("Umur : " + dataAllAnggota.get(listNoKK.get(position)).get("Umur"));
                        txtGender.setText("Gender : " + dataAllAnggota.get(listNoKK.get(position)).get("Gender"));
                        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
