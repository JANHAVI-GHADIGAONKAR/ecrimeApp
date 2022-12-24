package com.example.ecrimeapp;

public class model
{
    String name,contact,pimage,address,criminaltype,previousrecord,habits,remaddate,rid ;
    model()
    {

    }
    public model(String name, String contact, String address,String criminaltype,String previousrecord,String habits,String remaddate, String pimage,String rid) {
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.criminaltype=criminaltype;
        this.previousrecord=previousrecord;
        this.habits=habits;
        this.remaddate=remaddate;
        this.pimage = pimage;
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String course) {
        this.address = course;
    }

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }
    public String getCriminaltype() {
        return criminaltype;
    }

    public void setCriminaltype(String course) {
        this.criminaltype = course;
    }
    public String getPreviousrecord() {
        return previousrecord;
    }

    public void setPreviousrecord(String course) {
        this.previousrecord = course;
    }
    public String getHabits() {
        return habits;
    }

    public void setHabits(String course) {
        this.habits = course;
    }
    public String getRemaddate() {
        return remaddate;
    }

    public void setRemaddate(String course) {
        this.remaddate = course;
    }
    public String getRid() {
        return rid;
    }
}

