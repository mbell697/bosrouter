package com.bosrouter.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bosrouter.router.AStar;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.pgm.util.graphml.GraphMLWriter;

public class MbtaRouteDataImportTest implements ServletContextListener{

	private static final Logger LOG = LoggerFactory.getLogger(MbtaRouteDataImportTest.class);
	
	
	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
		loadGraphDataTest(contextEvent);
		//geocodeTest();
		//routeTest(contextEvent);	
		
		
	}
	
	private void routeTest(ServletContextEvent contextEvent) {
		TinkerGraph graph = loadGraphDataTest(contextEvent);
		Vertex start = graph.getVertex("2845");
		Vertex end = graph.getVertex("2835");		
		routeTest(start, end);
		start = graph.getVertex("2731");
		end = graph.getVertex("2189");		
		routeTest(start, end);
		start = graph.getVertex("2845");
		end = graph.getVertex("2835");		
		routeTest(start, end);
		start = graph.getVertex("1661");
		end = graph.getVertex("16539");		
		routeTest(start, end);
		start = graph.getVertex("1613");
		end = graph.getVertex("1275");		
		routeTest(start, end);
		start = graph.getVertex("place-river");
		end = graph.getVertex("place-state");		
		routeTest(start, end);
		start = graph.getVertex("9155");
		end = graph.getVertex("9118");		
		routeTest(start, end);
	}
	
	private void routeTest(Vertex start, Vertex end) {
		AStar router = new AStar();
		LOG.info("Route From " + (String)start.getProperty("stopName") + " -> " + (String)start.getProperty("stopName"));
		LOG.info("Router Start");
		long startTime = System.currentTimeMillis();
		List<Vertex> result = router.route(start, end);
		long endTime = System.currentTimeMillis();
		LOG.info("Router Time " + (endTime - startTime));
		if (result == null) {
			LOG.info("No Result Found");
			return;
		}
		for (Vertex v : result) {
			LOG.info((String)v.getProperty("stopName"));
		}
	}
	
	private TinkerGraph loadGraphDataTest(ServletContextEvent contextEvent) {
		MbtaRouteDataImport importer = new MbtaRouteDataImport();
		LOG.info("Importing MBTA GTFS Data");
		importer.loadData(contextEvent.getServletContext().getRealPath("/resources/data/mbta/") +"/");
		LOG.info("Import Complete");
		
		LOG.info("Building TinkerGraph");
		TinkerGraph tGraph = importer.buildTinkerGraph();
		OutputStream out;
		GraphMLWriter writer;
		try {
			out = new FileOutputStream("C:/Users/mbell/Desktop/tgraph.graphml");
			writer = new GraphMLWriter(tGraph);
			writer.outputGraph(out);
			out.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return tGraph;
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
