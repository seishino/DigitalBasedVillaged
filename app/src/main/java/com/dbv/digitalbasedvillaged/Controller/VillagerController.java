package com.dbv.digitalbasedvillaged.Controller;
import com.dbv.digitalbasedvillaged.DAO.VillagerDataAccess;
import com.dbv.digitalbasedvillaged.Entity.Family;
import com.dbv.digitalbasedvillaged.Entity.Villager;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListener;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListenerFamily;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListenerString;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class VillagerController {
    private VillagerDataAccess villagerDataAccess = new VillagerDataAccess();
    public void fetchDataVillager(final OnGetDataListenerFamily listenerFamily)
    {
        villagerDataAccess.fetchDataVillager(new OnGetDataListener() {
            @Override
            public void onComplete(DataSnapshot ds) {
                Family family = new Family();
                family.setRt(ds.child("RT").getValue().toString());
                family.setRw(ds.child("RW").getValue().toString());
                family.setNoKK(ds.getKey());
                family.setAddress(ds.child("Address").getValue().toString());
                List<Villager> villagerList = new ArrayList<>();
                for(DataSnapshot v : ds.child("Member").getChildren())
                {
                    Villager villager = new Villager();
                    villager.setNik(v.getKey());
                    villager.setName(v.child("Name").getValue().toString());
                    villager.setGender(v.child("Gender").getValue().toString());
                    villager.setDateOfBirth(v.child("DateOfBirth").getValue().toString());
                    villager.setPlaceOfBirth(v.child("PlaceOfBirth").getValue().toString());
                    villager.setRole(v.child("Role").getValue().toString());
                    villager.setJob(v.child("Job").getValue().toString());
                    villagerList.add(villager);
                }
                family.setFamilyList(villagerList);
                listenerFamily.onComplete(family);
            }
        });
    }
    public void checkVillager(final OnGetDataListenerFamily listenerFamily)
    {
        villagerDataAccess.checkVillager(new OnGetDataListener() {
            @Override
            public void onComplete(DataSnapshot ds) {
                Family family = new Family();
                family.setRt(ds.child("RT").getValue().toString());
                family.setRw(ds.child("RW").getValue().toString());
                family.setNoKK(ds.getKey());
                family.setAddress(ds.child("Address").getValue().toString());
                List<Villager> villagerList = new ArrayList<>();
                for(DataSnapshot v : ds.child("Member").getChildren())
                {
                    Villager villager = new Villager();
                    villager.setNik(v.getKey());
                    villager.setName(v.child("Name").getValue().toString());
                    villager.setGender(v.child("Gender").getValue().toString());
                    villager.setDateOfBirth(v.child("DateOfBirth").getValue().toString());
                    villager.setPlaceOfBirth(v.child("PlaceOfBirth").getValue().toString());
                    villager.setRole(v.child("Role").getValue().toString());
                    villager.setJob(v.child("Job").getValue().toString());
                    villagerList.add(villager);
                }
                family.setFamilyList(villagerList);
                listenerFamily.onComplete(family);
            }
        });
    }
    public void addData(Family family)
    {
        villagerDataAccess.addData(family);
    }
    public void removeMember(String nik)
    {
        villagerDataAccess.removeData(nik);
    }
    public void updateData(Villager villager)
    {
        villagerDataAccess.updateData(villager);
    }
    public void addMember(Villager villager)
    {
        villagerDataAccess.addMember(villager);
    }
    public void updateFamily(Family family)
    {
        villagerDataAccess.updateDataFamily(family);
    }
    public void changePassword(String newPassword)
    {
        villagerDataAccess.changePassword(newPassword);
    }
    public void getOldPassword(final OnGetDataListenerString listenerString)
    {
        villagerDataAccess.fetchOldPassword(new OnGetDataListenerString() {
            @Override
            public void onComplete(String ans) {
                listenerString.onComplete(ans);
            }
        });
    }
}
