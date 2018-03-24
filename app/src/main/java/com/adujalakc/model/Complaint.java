package com.adujalakc.model;


/**
 * Created by Anadara on 12/06/2017.
 */

public class Complaint {
    String name, img, comp_date, process_date , status, location, desc, comp_id, photo_confirm, date_finish;

    public Complaint(){

    }

    public Complaint(String name, String img, String comp_date, String status, String location, String desc, String comp_id){
        this.name = name;
        this.img = img;
        this.comp_date = comp_date;
        this.status = status;
        this.location = location;
        this.desc = desc;
        this.comp_id = comp_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getComp_date() {
        return comp_date;
    }

    public void setComp_date(String comp_date) {
        this.comp_date = comp_date;
    }

    public String getProcess_date() {
        return process_date;
    }

    public void setProcess_date(String process_date) {
        this.process_date = process_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public String getPhoto_confirm() {
        return photo_confirm;
    }

    public void setPhoto_confirm(String photo_confirm) {
        this.photo_confirm = photo_confirm;
    }

    public String getDate_finish() {
        return date_finish;
    }

    public void setDate_finish(String date_finish) {
        this.date_finish = date_finish;
    }
}
