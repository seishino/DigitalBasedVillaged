package com.dbv.digitalbasedvillaged.DAO;

import android.support.annotation.NonNull;

import com.dbv.digitalbasedvillaged.Entity.Family;
import com.dbv.digitalbasedvillaged.Entity.Villager;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListener;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListenerString;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class VillagerDataAccess {
    private static String nik;

    public static String getNik() {
        return nik;
    }

    public static void setNik(String nik) {
        VillagerDataAccess.nik = nik;
    }

    public void fetchDataVillager(final OnGetDataListener listener)
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference();
        myRef.child("Villager").child(nik).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onComplete(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void checkVillager(final OnGetDataListener listener)
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference();
        myRef.child("Villager").child(nik).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onComplete(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void addData(Family family)
    {
        Map<String,String> familyData = new HashMap<>();
        familyData.put("Address",family.getAddress());
        familyData.put("RT",family.getRt());
        familyData.put("RW",family.getRw());
        familyData.put("Status","NoVerification");
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference();
        myRef.child("Villager").child(family.getNoKK()).setValue(familyData);
        Map<String,Map<String,String>> data = new HashMap<>();
        for(Villager v : family.getFamilyList())
        {
            Map<String,String> data2 = new HashMap<>();
            data2.put("Name", v.getName());
            data2.put("Gender",v.getGender());
            data2.put("Role",v.getRole());
            data2.put("DateOfBirth",v.getDateOfBirth());
            data2.put("PlaceOfBirth",v.getPlaceOfBirth());
            data2.put("Job",v.getJob());
            data.put(v.getNik(),data2);
        }
        myRef.child("Villager").child(family.getNoKK()).child("Member").setValue(data);
        myRef.child("Users").child(family.getNoKK()).child("Password").setValue(family.getNoKK());
        myRef.child("Users").child(family.getNoKK()).child("Role").setValue("Villager");
        myRef.child("Users").child(family.getNoKK()).child("Status").setValue("NoVerification");
    }
    public void removeData(String nikRequest)
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference();
        myRef.child("Villager").child(nik).child("Member").child(nikRequest).removeValue();
    }
    public void updateData(Villager v)
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference();
        Map<String,String> data2 = new HashMap<>();
        data2.put("Name", v.getName());
        data2.put("Gender",v.getGender());
        data2.put("Role",v.getRole());
        data2.put("DateOfBirth",v.getDateOfBirth());
        data2.put("PlaceOfBirth",v.getPlaceOfBirth());
        data2.put("Job",v.getJob());
        myRef.child("Villager").child(nik).child("Member").child(v.getNik()).setValue(data2);
    }
    public void addMember(Villager v)
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference();
        Map<String,String> data2 = new HashMap<>();
        data2.put("Name", v.getName());
        data2.put("Gender",v.getGender());
        data2.put("Role",v.getRole());
        data2.put("DateOfBirth",v.getDateOfBirth());
        data2.put("PlaceOfBirth",v.getPlaceOfBirth());
        data2.put("Job",v.getJob());
        myRef.child("Villager").child(nik).child("Member").child(v.getNik()).setValue(data2);
    }
    public void updateDataFamily(Family family)
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference();
        myRef.child("Villager").child(nik).child("RT").setValue(family.getRt());
        myRef.child("Villager").child(nik).child("RW").setValue(family.getRw());
        myRef.child("Villager").child(nik).child("Address").setValue(family.getAddress());
    }
    public void fetchOldPassword(final OnGetDataListenerString listener)
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference();
        myRef.child("Users").child(nik).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onComplete(dataSnapshot.child("Password").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void changePassword(String newPassword)
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference();
        myRef.child("Users").child(nik).child("Password").setValue(newPassword);
    }
}
