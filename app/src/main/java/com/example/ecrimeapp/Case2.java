package com.example.ecrimeapp;

public class Case2 {
    String fdate;
    String rid;
    String fname;
    String ph;
    String address;
    String incident;


    public Case2(String fdate, String rid, String fname, String ph,String address,String incident) {
        this.fdate = fdate;
        this.rid = rid;
        this.fname = fname;
        this.ph = ph;
        this.address = address;
        this.incident= incident;
    }

    public String getFdate() {
        return fdate;
    }

    public String getRid() {
        return rid;
    }

    public String getFname() {
        return fname;
    }

    public String getPh() {
        return ph;
    }

    public String getAddress() {
        return address;
    }
    public String getIncident() {
        return incident;
    }
}
