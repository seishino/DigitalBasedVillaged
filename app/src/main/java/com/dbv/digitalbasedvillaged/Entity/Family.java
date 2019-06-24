package com.dbv.digitalbasedvillaged.Entity;

import java.util.List;

public class Family {
    private String noKK;
    private List<Villager> familyList;

    private String rt;
    private String rw;
    private String address;

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public String getRw() {
        return rw;
    }

    public void setRw(String rw) {
        this.rw = rw;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNoKK() {
        return noKK;
    }

    public void setNoKK(String noKK) {
        this.noKK = noKK;
    }

    public List<Villager> getFamilyList() {
        return familyList;
    }

    public void setFamilyList(List<Villager> familyList) {
        this.familyList = familyList;
    }
}
