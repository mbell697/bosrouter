package com.bosrouter.mbta.gtfs;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bosrouter.util.MbtaRouteDataImport;

import au.com.bytecode.opencsv.CSVReader;

public class MbtaGtfsFile {
	
	private static final Logger LOG = LoggerFactory.getLogger(MbtaGtfsFile.class);
	
	private Set<Stops> stops;
	private Set<Routes> routes;
	private Set<Trips> trips;
	private Set<StopTimes> stopTimes;
	private Map<String, List<StopTimes>> tripMap;
	
	public MbtaGtfsFile() {
		stops = new HashSet<Stops>();
		routes = new HashSet<Routes>();
		trips = new HashSet<Trips>();
		stopTimes = new HashSet<StopTimes>();
		tripMap = new HashMap<String, List<StopTimes>>();
		
	}
	
	public void loadDataSet (String stopFile, String routeFile, String tripFile, String stopTimeFile) throws IOException {	
		loadStopsData(stopFile);
		loadRoutesData(routeFile);
		loadTripsData(tripFile);
		loadStopTimesData(stopTimeFile);
	}

	private void loadStopsData(String filename) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(filename));
	    String [] nextLine;
	    reader.readNext(); //throw away header line
	    while ((nextLine = reader.readNext()) != null) {
	        if (nextLine.length != 10) {
	        	//TODO file format error
	        }
	        
	        //TODO format error detection
	        Stops stop = new Stops();
	        stop.setStop_id(nextLine[0]);
	        stop.setStop_code(nextLine[1]);
	        stop.setStop_name(nextLine[2]);
	        stop.setStop_desc(nextLine[3]);
	        stop.setLatitude(Double.parseDouble(nextLine[4]));
	        stop.setLongitude(Double.parseDouble(nextLine[5]));
	        stop.setZone_id(nextLine[6]);
	        stop.setStop_url(nextLine[7]);
	        if (nextLine[8].length() > 0)
	        	stop.setLocation_type(Integer.parseInt(nextLine[8]));
	        stop.setParent_station(nextLine[9]);
	        stops.add(stop);      
	    }
	    reader.close();
		
	}
	
	private void loadRoutesData(String filename) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(filename));
	    String [] nextLine;
	    reader.readNext(); //throw away header line
	    while ((nextLine = reader.readNext()) != null) {
	        if (nextLine.length != 9) {
	        	//TODO file format error
	        }
	        
	        //TODO format error detection
	        Routes route = new Routes();
	        route.setRoute_id(nextLine[0]);
	        route.setAgency_id(nextLine[1]);
	        route.setRoute_short_name(nextLine[2]);
	        route.setRoute_long_name(nextLine[3]);
	        route.setRoute_desc(nextLine[4]);
	        route.setRoute_type(Integer.parseInt(nextLine[5]));
	        route.setRoute_url(nextLine[6]);
	        route.setRoute_color(nextLine[7]);
	        route.setRoute_text_color(nextLine[8]);
	        routes.add(route);      
	    }
	    reader.close();
	}
	
	private void loadTripsData(String filename) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(filename));
	    String [] nextLine;
	    reader.readNext(); //throw away header line
	    while ((nextLine = reader.readNext()) != null) {
	        if (nextLine.length != 7) {
	        	//TODO file format error
	        }
	        
	        //TODO format error detection
	        Trips trip = new Trips();
	        trip.setRoute_id(nextLine[0]);
	        trip.setService_id(nextLine[1]);
	        trip.setTrip_id(nextLine[2]);
	        trip.setTrip_headsign(nextLine[3]);
	        trip.setDirection_id(Integer.parseInt(nextLine[4]));
	        trip.setBlock_id(nextLine[5]);
	        trip.setShape_id(nextLine[6]);
	        trips.add(trip);      
	    }
	    reader.close();
	}
	
	private void loadStopTimesData(String filename) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(filename));
	    String [] nextLine;
	    reader.readNext(); //throw away header line
	    LOG.info("Loading StopTimes Data");
	    while ((nextLine = reader.readNext()) != null) {
	        if (nextLine.length != 8) {
	        	//TODO file format error
	        }
	        
	        //TODO format error detection
	        StopTimes stopTime = new StopTimes();
	        stopTime.setTrip_id(nextLine[0]);
	        stopTime.setArrival_time(nextLine[1]);
	        stopTime.setDeparture_time(nextLine[2]);
	        stopTime.setStop_id(nextLine[3]);
	        stopTime.setStop_sequence(Integer.parseInt(nextLine[4]));
	        stopTime.setStop_headsign(nextLine[5]);
	        stopTime.setPickup_type(Integer.parseInt(nextLine[6]));
	        stopTime.setDrop_off_type(Integer.parseInt(nextLine[7]));
	        stopTimes.add(stopTime);
	        
	        List<StopTimes> tripStops = tripMap.get(stopTime.getTrip_id());
	        if (tripStops == null) {
	        	tripStops = new LinkedList<StopTimes>();
	        	tripMap.put(stopTime.getTrip_id(), tripStops);
	        }
	        tripStops.add(stopTime);
	        
	    }
	    reader.close();
	    LOG.info("Sorting Trip Maps");  
	    for (List<StopTimes> stops: tripMap.values()) {
	    	Collections.sort(stops, new StopOrderComparator());
	    }
	}
	
	public Stops findStop(String stopId) {
		for (Stops stop : stops) {
			if (stopId.compareTo(stop.getStop_id()) == 0)
				return stop;
		}
		return null;
	}
	
	public List<StopTimes> findStops(String tripId) {
		List<StopTimes> stops = tripMap.get(tripId);
		if (stops == null)
			return new LinkedList<StopTimes>();
		else
			return stops; 
	}
	
	public Trips findTrip(String tripId) {
		for (Trips trip: trips) {
			if (tripId.compareTo(trip.getTrip_id()) == 0)
				return trip;
		}
		return null;
	}
	
	public Routes findRoute(String routeId) {
		for (Routes route: routes) {
			if (routeId.compareTo(route.getRoute_id()) == 0)
				return route;
		}
		return null;
	}
	

	public Set<Stops> getStops() {
		return stops;
	}

	public void setStops(Set<Stops> stops) {
		this.stops = stops;
	}

	public Set<Routes> getRoutes() {
		return routes;
	}

	public void setRoutes(Set<Routes> routes) {
		this.routes = routes;
	}

	public Set<Trips> getTrips() {
		return trips;
	}

	public void setTrips(Set<Trips> trips) {
		this.trips = trips;
	}

	public Set<StopTimes> getStopTimes() {
		return stopTimes;
	}

	public void setStopTimes(Set<StopTimes> stopTimes) {
		this.stopTimes = stopTimes;
	}
	
	
	
	
}
