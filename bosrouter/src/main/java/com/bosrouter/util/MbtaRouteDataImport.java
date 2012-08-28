package com.bosrouter.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bosrouter.geo.GeoMath;
import com.bosrouter.mbta.gtfs.MbtaGtfsFile;
import com.bosrouter.mbta.gtfs.Routes;
import com.bosrouter.mbta.gtfs.StopTimes;
import com.bosrouter.mbta.gtfs.Stops;
import com.bosrouter.mbta.gtfs.Trips;

public class MbtaRouteDataImport {
	
	private static final Logger LOG = LoggerFactory.getLogger(MbtaRouteDataImport.class);
	
	MbtaGtfsFile source = new MbtaGtfsFile();
	private static final DateTimeFormatter format = DateTimeFormat.forPattern("k:m:s");
	
	public void loadData(String dir) {
		String stopFile = dir + "stops.txt";
		String routeFile = dir + "routes.txt";
		String tripFile = dir + "trips.txt";
		String stopTimeFile = dir + "stop_times.txt";		
		try {
			source.loadDataSet(stopFile, routeFile, tripFile, stopTimeFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public Graph buildTinkerGraph() {
		Neo4jGraph graph = new Neo4jGraph("data/graph");

		
		LOG.info("Building All Tinker Vertices");
		//create all nodes
		for (Stops stop: source.getStops()) {
			Vertex vertex = graph.addVertex(stop.getStop_id());
			vertex.setProperty("latitude", stop.getLatitude());
			vertex.setProperty("longitude", stop.getLongitude());
			vertex.setProperty("stopName", stop.getStop_name());
			
			//The following two fields are only used for ploting the nodes for visualization in yEd
			vertex.setProperty("yCenter", 1000000 - (stop.getLatitude() * 100000));
			vertex.setProperty("xCenter", 1000000 - (-1 * stop.getLongitude() * 100000));
		}
		
		LOG.info("Building All Tinker Edges");
		//build connections between nodes
		for (Trips trip: source.getTrips()) {
			List<StopTimes> times = source.findStops(trip.getTrip_id());
			Vertex lastVertex = graph.getVertex( (times.get(0)).getStop_id());
			StopTimes lastTime = times.get(0);
			for(StopTimes time: times) {
				Vertex cVert = graph.getVertex(time.getStop_id());
				if (cVert != lastVertex) {
					Routes route = source.findRoute(trip.getRoute_id());
					String edgeIndex = (String)lastVertex.getId() + "_" + 
							           (String)cVert.getId() + "_" +
							           (String)route.getRoute_id();
					if (graph.getEdge(edgeIndex) == null) {
						Edge edge = graph.addEdge(edgeIndex, lastVertex, cVert, "travel");
						edge.setProperty("routeId", route.getRoute_id());
						edge.setProperty("routeNameShort", route.getRoute_short_name());
						edge.setProperty("routeNameLong", route.getRoute_long_name());
						edge.setProperty("travelTime", difference(lastTime.getDeparture_time(), time.getArrival_time()));
					}
				}
				lastVertex = cVert;
				lastTime = time;
			}
		}
		LOG.info("Building All Connections");
		buildConnections(graph);
		
		return graph;
	}
	
	private static final double CONNECTION_DISTANCE = 0.33;
	private void buildConnections(Graph graph) {
		Iterator<Vertex> itr = graph.getVertices().iterator();
		
		while (itr.hasNext()) {
			Vertex v = itr.next();
			Iterator<Vertex> vitr = graph.getVertices().iterator();
			while (vitr.hasNext()) {
				Vertex t = vitr.next();
				if (GeoMath.distance((Double)v.getProperty("latitude"), 
									 (Double)v.getProperty("longitude"), 
									 (Double)t.getProperty("latitude"), 
									 (Double)t.getProperty("longitude")) < CONNECTION_DISTANCE) {
					Edge edge = graph.addEdge(UUID.randomUUID(), v, t, "connection");
					edge.setProperty("travelTime", 5);
				}
			}
		}
		
	}

	//returns the number of seconds between two times in MBTA GTFS format
	private Integer difference(String beginTime, String endTime) {
		DateTime bInstant = convertToDateTime(beginTime);
		DateTime eInstant = convertToDateTime(endTime);
		return Seconds.secondsBetween(bInstant, eInstant).getSeconds();
	}
	
	private DateTime convertToDateTime(String time) {
		String[] bTokens = time.split(":");	
		int bHour = Integer.parseInt(bTokens[0]);
		int bMinute = Integer.parseInt(bTokens[1]);
		int bSecond = Integer.parseInt(bTokens[2]);
		
		DateTime bInstant;		
		if (bHour >= 24) {
			bHour -= 24;
			LocalTime lTime = new LocalTime(bHour,bMinute,bSecond);
			bInstant = lTime.toDateTimeToday(DateTimeZone.forID("US/Eastern"));
			bInstant = bInstant.plusDays(1); //handle the date change
		} else {
			LocalTime lTime = new LocalTime(bHour,bMinute,bSecond);
			bInstant = lTime.toDateTimeToday(DateTimeZone.forID("US/Eastern"));
		}
		return bInstant;
	}
}
