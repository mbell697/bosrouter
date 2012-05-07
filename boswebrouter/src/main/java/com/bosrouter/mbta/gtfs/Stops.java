package com.bosrouter.mbta.gtfs;

public class Stops {
	private String stop_id;
	private String stop_code;
	private String stop_name;
	private String stop_desc;
	
	private Double latitude;
	private Double longitude;
	private String zone_id;
	private String stop_url;
	
	private Integer location_type;
	private String parent_station;
	
	public String getStop_id() {
		return stop_id;
	}
	public void setStop_id(String stop_id) {
		this.stop_id = stop_id;
	}
	public String getStop_code() {
		return stop_code;
	}
	public void setStop_code(String stop_code) {
		this.stop_code = stop_code;
	}
	public String getStop_name() {
		return stop_name;
	}
	public void setStop_name(String stop_name) {
		this.stop_name = stop_name;
	}
	public String getStop_desc() {
		return stop_desc;
	}
	public void setStop_desc(String stop_desc) {
		this.stop_desc = stop_desc;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getZone_id() {
		return zone_id;
	}
	public void setZone_id(String zone_id) {
		this.zone_id = zone_id;
	}
	public String getStop_url() {
		return stop_url;
	}
	public void setStop_url(String stop_url) {
		this.stop_url = stop_url;
	}
	public Integer getLocation_type() {
		return location_type;
	}
	public void setLocation_type(Integer location_type) {
		this.location_type = location_type;
	}
	public String getParent_station() {
		return parent_station;
	}
	public void setParent_station(String parent_station) {
		this.parent_station = parent_station;
	}
	
	
}
