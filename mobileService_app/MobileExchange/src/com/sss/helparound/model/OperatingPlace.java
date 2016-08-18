package com.sss.helparound.model;

import com.sss.helparound.utils.StringUtils;

public class OperatingPlace {

    private String address;
    private String city;
    private String zip;

    private String perimeter;

    private boolean remoteService;

    public OperatingPlace() {

    }

    public OperatingPlace(final String address, final String city, final String zip,
                          final String perimeter, final boolean isRemoteService) {
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.perimeter = StringUtils.getPerimeterFromSpinner(perimeter);
        this.remoteService = isRemoteService;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPerimeter() {
        return perimeter;
    }

    public void setPerimeter(final String perimeter) {
        this.perimeter = perimeter;
    }

    public boolean isRemoteService() {
        return remoteService;
    }

    public void setRemoteService(boolean remoteService) {
        this.remoteService = remoteService;
    }

}
