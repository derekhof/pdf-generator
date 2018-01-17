package com.derekhome.pdfgenerator.model;

import java.util.List;

public class Invoice {


    private List<Bill> bills;
    private String company_name;
    private String zip_code;
    private String city;
    private String address;
    private int housenumber;
    public String bill_period;
    public String invoice_date;

    public String getBill_period() {
        return bill_period;
    }

    public void setBill_period(String bill_period) {
        this.bill_period = bill_period;
    }

    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
    }
    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public int getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(int housenumber) {
        this.housenumber = housenumber;
    }


    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

}
