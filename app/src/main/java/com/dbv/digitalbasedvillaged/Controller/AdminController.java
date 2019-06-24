package com.dbv.digitalbasedvillaged.Controller;

import com.dbv.digitalbasedvillaged.DAO.AdminDataAccess;
import com.dbv.digitalbasedvillaged.Entity.Family;
import com.dbv.digitalbasedvillaged.Entity.Users;
import com.dbv.digitalbasedvillaged.Entity.Villager;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListener;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListenerFamily;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListenerListFamily;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListenerListUser;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListenerListVillager;
import com.dbv.digitalbasedvillaged.Listener.OnGetDataListenerString;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminController {
    private AdminDataAccess adminDataAccess = new AdminDataAccess();
    public void fetchVerificationDataAdmin(final OnGetDataListenerListUser listenerListUser)
    {
        adminDataAccess.fetchAllDataForVerification(new OnGetDataListener() {
            @Override
            public void onComplete(DataSnapshot ds) {
                List<Users> usersList = new ArrayList<>();


                for(DataSnapshot v : ds.getChildren())
                {
                    if(v.child("Status").getValue().toString().equals("NoVerification"))
                    {
                        Users u = new Users();
                        u.setNoKK(v.getKey());
                        u.setRole(v.child("Role").getValue().toString());
                        u.setStatus(v.child("Status").getValue().toString());
                        usersList.add(u);
                    }
                }
                listenerListUser.onComplete(usersList);
            }
        });
    }
    public void fetchCancelVerification(final OnGetDataListenerListUser listenerListUser)
    {
        adminDataAccess.fetchAllDataForVerification(new OnGetDataListener() {
            @Override
            public void onComplete(DataSnapshot ds) {
                List<Users> usersList = new ArrayList<>();


                for(DataSnapshot v : ds.getChildren())
                {
                    if(v.child("Status").getValue().toString().equals("Verification") && !v.child("Role").getValue().toString().equals("Admin"))
                    {
                        Users u = new Users();
                        u.setNoKK(v.getKey());
                        u.setRole(v.child("Role").getValue().toString());
                        u.setStatus(v.child("Status").getValue().toString());
                        usersList.add(u);
                    }
                }
                listenerListUser.onComplete(usersList);
            }
        });
    }
    public void fetchMemberData(final OnGetDataListenerListVillager listenerListVillager, String noKK)
    {
        adminDataAccess.fetchFamilyMember(new OnGetDataListener() {
            @Override
            public void onComplete(DataSnapshot ds) {
                List<Villager> villagerList = new ArrayList<>();
                for(DataSnapshot v : ds.getChildren())
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
                listenerListVillager.onComplete(villagerList);
            }
        },noKK);
    }
    public void fetchDataAdmin(final OnGetDataListenerFamily listenerFamily)
    {
        adminDataAccess.fetchDataVillager(new OnGetDataListener() {
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
        adminDataAccess.checkVillager(new OnGetDataListener() {
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
    public void fetchAllDataVillager(final OnGetDataListenerListFamily listenerListFamily)
    {
        adminDataAccess.fetchAllDataVillager(new OnGetDataListener() {
            @Override
            public void onComplete(DataSnapshot ds) {
                List<Family> familyList = new ArrayList<>();
                for(DataSnapshot f : ds.getChildren())
                {
                    Family family = new Family();
                    family.setNoKK(f.getKey());
                    List<Villager> villagerList = new ArrayList<>();
                    for(DataSnapshot v : f.child("Member").getChildren())
                    {
                        Villager villager = new Villager();
                        villager.setNik(v.getKey());
                        villager.setName(v.child("Name").getValue().toString());
                        villager.setGender(v.child("Gender").getValue().toString());
                        villager.setDateOfBirth(v.child("DateOfBirth").getValue().toString());
                        villager.setPlaceOfBirth(v.child("PlaceOfBirth").getValue().toString());
                        villager.setJob(v.child("Job").getValue().toString());
                        villager.setRole(v.child("Role").getValue().toString());
                        villagerList.add(villager);
                    }
                    family.setFamilyList(villagerList);
                    familyList.add(family);
                }

                listenerListFamily.onComplete(familyList);
            }
        });
    }
    public void fetchDataVillageByNoKK(final OnGetDataListenerFamily listenerFamily, String noKK)
    {
        adminDataAccess.fetchDataVillagerByNoKK(new OnGetDataListener() {
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
        },noKK);
    }
    public void addData(Family family)
    {
        adminDataAccess.addData(family);
    }
    public void removeMember(String nik)
    {
        adminDataAccess.removeData(nik);
    }
    public void updateData(Villager villager)
    {
        adminDataAccess.updateData(villager);
    }
    public void addMember(Villager villager)
    {
        adminDataAccess.addMember(villager);
    }
    public void updateFamily(Family family)
    {
        adminDataAccess.updateDataFamily(family);
    }
    public void verificationFamily(String noKK)
    {
        adminDataAccess.verificationFamily(noKK);
    }
    public void cancelVerificationFamily(String noKK)
    {
        adminDataAccess.noVerificationFamily(noKK);
    }
    public void deleteFamily(String noKK)
    {
        adminDataAccess.deleteFamily(noKK);
    }
    public void changePassword(String newPassword)
    {
        adminDataAccess.changePassword(newPassword);
    }
    public void getOldPassword(final OnGetDataListenerString listenerString)
    {
        adminDataAccess.fetchOldPassword(new OnGetDataListenerString() {
            @Override
            public void onComplete(String ans) {
                listenerString.onComplete(ans);
            }
        });
    }
    public void resetPassword(String noKK)
    {
        adminDataAccess.resetPassword(noKK);
    }
    public void givePrivillege(String noKK)
    {
        adminDataAccess.givePrivillege(noKK);
    }
    public void removePrivillege(String noKK)
    {
        adminDataAccess.removePrivillege(noKK);
    }
}
