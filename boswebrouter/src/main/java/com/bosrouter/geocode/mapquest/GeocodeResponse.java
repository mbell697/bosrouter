package com.bosrouter.geocode.mapquest;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement
public class GeocodeResponse {
	public String place_id;
	public String licence;
	public String osm_type;
	public Integer osm_id;
	public Double[] boundingbox;
	public Double lat;
	public Double lon;
	public String display_name;
	@JsonProperty("class")
	public String _class;
	public String type;
	
	
	
	public String getPlace_id() {
		return place_id;
	}
	public void setPlace_id(String place_id) {
		this.place_id = place_id;
	}
	public String getLicence() {
		return licence;
	}
	public void setLicence(String licence) {
		this.licence = licence;
	}
	public String getOsm_type() {
		return osm_type;
	}
	public void setOsm_type(String osm_type) {
		this.osm_type = osm_type;
	}
	public Integer getOsm_id() {
		return osm_id;
	}
	public void setOsm_id(Integer osm_id) {
		this.osm_id = osm_id;
	}
	public Double[] getBoundingbox() {
		return boundingbox;
	}
	public void setBoundingbox(Double[] boundingbox) {
		this.boundingbox = boundingbox;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLon() {
		return lon;
	}
	public void setLon(Double lon) {
		this.lon = lon;
	}
	public String getDisplay_name() {
		return display_name;
	}
	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}
	public String get_class() {
		return _class;
	}
	public void set_class(String _class) {
		this._class = _class;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
