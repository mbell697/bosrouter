package com.bosrouter.web.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bosrouter.geo.GeoMath;
import com.bosrouter.geocode.mapquest.GeocodeResponse;
import com.bosrouter.geocode.mapquest.MQGeocoder;
import com.bosrouter.router.AStar;
import com.bosrouter.util.MbtaRouteDataImport;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.pgm.util.graphml.GraphMLWriter;

@Named
@ApplicationScoped
public class RoutingService {
	private Graph graph;
	private static final Logger LOG = LoggerFactory.getLogger(RoutingService.class);
	
	@Inject
	private ServletContext context;
	
	@PostConstruct
	public void init() {
		MbtaRouteDataImport importer = new MbtaRouteDataImport();
		LOG.info("Importing MBTA GTFS Data");
		importer.loadData(context.getRealPath("/resources/data/mbta/") +"/");
		LOG.info("Import Complete");
		
		LOG.info("Building TinkerGraph");
		graph = importer.buildTinkerGraph();
	}
	
	public List<Vertex> route (String from, String to) {
		List<Vertex> result = new LinkedList<Vertex>();
		
		double fromLat;
		double fromLng;
		
		GeocodeResponse[] gFrom = MQGeocoder.geocode(from);
		if (gFrom.length == 0) {
			LOG.error("No Geocode Result for From Address");
			return result;
		} else {
			fromLat = gFrom[0].lat;
			fromLng = gFrom[0].lon;
			LOG.info("Found " + gFrom.length + " Geocode Results for the From Address");
		}
		
		double toLat;
		double toLng;
		
		GeocodeResponse[] gTo = MQGeocoder.geocode(to);
		if (gTo.length == 0) {
			LOG.error("No Geocode Result for To Address");
			return result;
		} else {
			toLat = gTo[0].lat;
			toLng = gTo[0].lon;
			LOG.info("Found " + gTo.length + " Geocode Results for the To Address");
		}
		
		List<Vertex> fromList = GeoMath.getNearByVertices(graph, fromLat, fromLng, 0.5);
		List<Vertex> toList = GeoMath.getNearByVertices(graph, toLat, toLng, 0.5);
		
		if (fromList.size() == 0) {
			LOG.error("No Stations Found Near From Address");
			return result;
		}
		
		if (toList.size() == 0) {
			LOG.error("No Stations Found Near To Address");
			return result;
		}
		
		LOG.info("Found " + fromList.size() + " Stops near the From Address");
		LOG.info("Found " + fromList.size() + " Stops near the To Address");
		
		
		AStar router = new AStar();
		
		result = router.route(fromList.get(0), toList.get(0));
		if (result.size() == 0)
			LOG.info("No Route Found");
		return result;
	}
	
}
