package com.bosrouter.server;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bosrouter.geo.GeoMath;
import com.bosrouter.router.AStar;
import com.bosrouter.util.MbtaRouteDataImport;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;

public class RoutingService {
	private static final Logger LOG = LoggerFactory.getLogger(RoutingService.class);
    private static RoutingService instance;
    private Graph graph;

    private RoutingService() {
        init();
    }

    public static RoutingService instance() {
        if (instance == null)
            instance = new RoutingService();
        return instance;
    }

	public void init() {
		MbtaRouteDataImport importer = new MbtaRouteDataImport();
		LOG.info("Importing MBTA GTFS Data");
		importer.loadData("/resources/data/mbta/");
		LOG.info("Import Complete");
		
		LOG.info("Building TinkerGraph");
		graph = importer.buildTinkerGraph();
	}
	
	public List<Vertex> route (double fromLat, double fromLng, double toLat, double toLng) {
		List<Vertex> result = new LinkedList<Vertex>();
		
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
