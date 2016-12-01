package com.similan.framework.geo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GeoHaversineDistance {
	
	
	public static boolean computeProximity(Double lat1, Double lon1, Double lat2,  
			                            Double lon2, Double radius) {
		
	    final int R = 6371; // Radious of the earth
	    Double latDistance = toRad(lat2-lat1);
	    Double lonDistance = toRad(lon2-lon1);
	    Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
	               Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
	               Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    Double distance = R * c;
		         
	    log.info("The distance between two lat and long is " + distance);
	    
	    if(distance > radius){
	    	return false;
	    }
	    
	    return true;
		 
	 }
		     
	 private static Double toRad(Double value) {
		        return value * Math.PI / 180;
	 }
}
