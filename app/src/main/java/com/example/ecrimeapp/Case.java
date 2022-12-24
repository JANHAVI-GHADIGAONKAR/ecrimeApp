package com.example.ecrimeapp;

public class Case {
    String fdate;
    String rid;
    String fname;
    String ph;
    String cr;


    public Case(String fdate, String rid, String fname, String ph, String cr) {
        this.fdate = fdate;
        this.rid = rid;
        this.fname = fname;
        this.ph = ph;
        this.cr = cr;
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

    public String getCr() {
        return cr;
    }
}
