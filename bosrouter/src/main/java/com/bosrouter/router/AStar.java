package com.bosrouter.router;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;

public class AStar {

	public List<Vertex> route(Vertex start, Vertex end) {
		Set<Vertex> closedSet = new HashSet<Vertex>();
		Set<Vertex> openSet = new HashSet<Vertex>();
		
		Map<Vertex, Double> gScore = new HashMap<Vertex, Double>();
		Map<Vertex, Double> hScore = new HashMap<Vertex, Double>();
		Map<Vertex, Double> fScore = new HashMap<Vertex, Double>();
		Map<Vertex, Vertex> cameFrom = new HashMap<Vertex, Vertex>();
		
		openSet.add(start);
		
		
		gScore.put(start, 0.0);
		hScore.put(start, calculateHeuristic(start, end));
		fScore.put(start, hScore.get(start));
		
		while (!openSet.isEmpty()) {
			Iterator<Vertex> openSetIterator = openSet.iterator();
			Vertex x = openSetIterator.next();
			while (openSetIterator.hasNext()) {
				Vertex t = openSetIterator.next();
				Double tScore = fScore.get(t);
				Double xScore = fScore.get(x);
				if (tScore == null) {
					continue;
				}
				if (tScore < xScore)
					x = t;
			}
			
			if (x == end) {
				return buildPath(cameFrom, end);
			}
			
			openSet.remove(x);
			closedSet.add(x);
			Iterator<Edge> neighbors = x.getOutEdges("travel", "connection").iterator();
			
			while (neighbors.hasNext()) {
				Edge e = neighbors.next();
				Vertex t = e.getInVertex();
				if (closedSet.contains(t))
					continue;
				
				Double cost = ((Integer)e.getProperty("travelTime")).doubleValue();
				if (cost == null)
					cost = 0.0;
				double tGscore = gScore.get(x) + cost;
				boolean improvement;
				if (!openSet.contains(t)) {
					openSet.add(t);
					improvement = true;
				} else if (tGscore < gScore.get(t)) {
					improvement = true;
				} else {
					improvement = false;
				}
				
				if (improvement) {
					cameFrom.put(t,x);
					gScore.put(t, tGscore);
					hScore.put(t, calculateHeuristic(t,end));
					fScore.put(t, gScore.get(t) + hScore.get(t));
				}
			}
			
			
		}
		
		return null;
	}
	
	public List<Vertex> buildPath(Map<Vertex, Vertex> map, Vertex v) {
		List<Vertex> result = new LinkedList<Vertex>();
		Vertex currentStep = v;
		result.add(v);
		while (true) {
			Vertex next = map.get(currentStep);
			if (next == null)
				break;
			result.add(next);
			currentStep = next;
		}
		Collections.reverse(result);
		return result;
	}
	
	public double calculateHeuristic(Vertex start, Vertex end) {
		return 0.0;
	}
}
