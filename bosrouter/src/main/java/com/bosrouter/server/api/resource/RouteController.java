package com.bosrouter.server.api.resource;

import com.bosrouter.server.RoutingService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/route")
public class RouteController {

    @GET
    @Path("/")
    public Response route(@QueryParam("fromLat") double fromLat,
                          @QueryParam("fromLong") double fromLong,
                          @QueryParam("toLat") double toLat,
                          @QueryParam("toLong") double toLong) {
        return Response.ok().entity(RoutingService.route(fromLat,fromLong,toLat,toLong)).build();
    }
}
