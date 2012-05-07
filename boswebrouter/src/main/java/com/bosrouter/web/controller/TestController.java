package com.bosrouter.web.controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.bosrouter.web.service.RoutingService;
import com.tinkerpop.blueprints.pgm.Vertex;

@Named
@SessionScoped
public class TestController implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<Vertex> route;
	
	private String from;
	
	private String to;
	
	@Inject
	private RoutingService routingService;
	
	public void route() {
		route = routingService.route(from, to);
	}

	public List<Vertex> getRoute() {
		return route;
	}

	public void setRoute(List<Vertex> route) {
		this.route = route;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
	
	
}
