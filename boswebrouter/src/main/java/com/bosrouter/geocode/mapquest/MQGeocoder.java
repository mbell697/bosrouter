package com.bosrouter.geocode.mapquest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class MQGeocoder {
	private static String GEOCODER_URL = "http://open.mapquestapi.com/nominatim/v1/search";
	
	private static final Logger LOG = LoggerFactory.getLogger(MQGeocoder.class);
		
	public static GeocodeResponse[] geocode(String query) {
		ClientConfig cc = new DefaultClientConfig(); 
		cc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true); 
		Client client = Client.create(cc);
		
		WebResource webResource = client.resource(GEOCODER_URL)
										.queryParam("q", query)
										.queryParam("format", "json");
		
		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
		
		if (response.getStatus() != 200) {
			LOG.error("HTTP Error " + response.getStatus());
			return null;
		} else {
			return response.getEntity(GeocodeResponse[].class);
		}		
	}
}
