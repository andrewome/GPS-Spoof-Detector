package distance;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SKY WING
 */
public class distance {
    public static void main(String args[]) {
        System.out.println(distance.EuclideanDistance(39.941,116.45,39.94,116.451));
    }
    private static int R=6371000;
    private static double rad=Math.PI/180;
    
    public static double EuclideanDistance(double lat1, double long1, double lat2, double long2) {
        double lat1_rad = lat1 * rad;
        double lat2_rad = lat2 * rad;
        double a=Math.sin(lat1_rad)*Math.sin(lat2_rad)+Math.cos(lat1_rad)*Math.cos(lat2_rad)*Math.cos((long1 - long2)*rad);
        double distance=R*Math.acos((Math.min(a,1)));
        if(distance>200000) return distance;
        else{ //haversine formula
              double dx = (long1 - long2) * rad;
              double dy = (lat1 - lat2) * rad;
              double theta = 
                Math.sin(dy/2) * Math.sin(dy/2) +
                Math.cos(lat1_rad) * Math.cos(lat2_rad) * 
                Math.sin(dx/2) * Math.sin(dx/2);
              double c = 2 * Math.atan2(Math.sqrt(theta), Math.sqrt(1-theta)); 
              return R * c;
        }
    }
    
    public static double ManhattanDistance(double lat1, double long1, double lat2, double long2) {
        double dx = (long1 - long2) * rad;
        double dy = (lat1 - lat2) * rad;
        double lat_mean = (lat1 + lat2) * rad/2;
        double Lx = dx * R * Math.cos(lat_mean); // east-west
        double Ly = R * dy; // south-north
        return Lx+Ly;
    }
}