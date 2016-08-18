package com.sss.utils;

public class StringUtils {

    public static int getPerimeterFromSpinner(final String perimeter) {
        switch (perimeter) {
            case "0 km":
                return 0;

            case "1 km":
                return 1;

            case "5 km":
                return 5;

            case "10 km":
                return 10;

            case "20 km":
                return 20;

            case "50 km":
                return 50;

            case "100 km":
                return 100;

            case "+ de 100 Km":
                return 1000000;
        }
        return 0;
    }
}
