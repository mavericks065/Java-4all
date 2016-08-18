package com.sss.helparound.utils;

public class StringUtils {

    public static String getPerimeterFromSpinner(final String perimeter) {
        System.out.println("perimeter : " + perimeter);
        if ("0 km".equals(perimeter)) {
            return "0";
        } else if ("1 km".equals(perimeter))  {
            return "1";
        } else if ("5 km".equals(perimeter)) {
            return "5";
        } else if ("10 km".equals(perimeter)) {
            return "10";
        } else if ("20 km".equals(perimeter)) {
            return "20";
        } else if ("50 km".equals(perimeter)) {
            return "50";
        } else if ("100 km".equals(perimeter)) {
            return "100";
        } else if ("+ 100 km".equals(perimeter)) {
            return "1000000";
        } else
            return "0";
    }
}
