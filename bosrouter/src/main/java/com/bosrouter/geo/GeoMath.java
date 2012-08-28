package com.bosrouter.geo;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class GeoMath {

	static double EARTH_RADIUS = 6371;
	
	
	//TODO this is an aproximation based on a sphere...we may need to use Vinventy's algorithm with a more accurate eliptoid
	public static double distance(double lat1, double lng1, double lat2, double lng2) {
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		           Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * 
		           Math.sin(dLng/2) * Math.sin(dLng/2); 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return EARTH_RADIUS * c;		
	}
	
	public static List<Vertex> getNearByVertices(Graph graph, double lat, double lng, double range) {
		List<Vertex> result = new LinkedList<Vertex>();
		Iterator<Vertex> itr = graph.getVertices().iterator();
		while (itr.hasNext()) {
			Vertex c = itr.next();
			double distance = distance(lat,lng,(Double)c.getProperty("latitude"), (Double)c.getProperty("longitude"));
			if (distance <= range)
				result.add(c);
				
		}
		return result;
	}
}
