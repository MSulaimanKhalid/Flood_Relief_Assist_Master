package org.example.zereaab;

public class test {
    public static void main(String[] args) {
        double latref = 0;
        double longref = 0;
        double lat1 = 31.5364952911611693;
        double long1 = 74.33501072998048;
        latref = (0+lat1)/2;
        longref = (0+long1)/2;
        System.out.println("lat: "+latref+"  long: "+longref);
        double lat2 = 31.51776596093965;
        double long2 = 74.32299443359376;
        latref = (0+lat2)/2;
        longref = (0+long2)/2;
        System.out.println("lat: "+latref+"  long: "+longref);
        double lat3 = 31.510156093965;
        double long3 = 74.39989873046876;
        latref = (0+lat3)/2;
        longref = (0+long3)/2;
        System.out.println("lat: "+latref+"  long: "+longref);
        double lat4 = 31.53327645520129;
        double long4 = 74.38788243408204;
        latref = (0+lat4)/2;
        longref = (0+long4)/2;
        System.out.println("lat: "+latref+"  long: "+longref);
        double lat5 = 31.52449724648222;
        double long5 = 74.35870000000001;
        latref = (0+lat5)/2;
        longref = (0+long5)/2;
        System.out.println("lat: "+latref+"  long: "+longref);
        System.out.println((lat1+lat2+lat3+lat4+lat5)/5);
        System.out.println((long1+long2+long3+long4+long5)/5);
    }

}
