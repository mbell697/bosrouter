package com.bosrouter.mbta.gtfs;

public class StopTimes {
	private String trip_id;
	private String arrival_time;
	private String departure_time;
	private String stop_id;
	
	private Integer stop_sequence;
	private String stop_headsign;
	private Integer pickup_type;
	private Integer drop_off_type;
	
	public String getTrip_id() {
		return trip_id;
	}
	public void setTrip_id(String trip_id) {
		this.trip_id = trip_id;
	}
	public String getArrival_time() {
		return arrival_time;
	}
	public void setArrival_time(String arrival_time) {
		this.arrival_time = arrival_time;
	}
	public String getDeparture_time() {
		return departure_time;
	}
	public void setDeparture_time(String departure_time) {
		this.departure_time = departure_time;
	}
	public String getStop_id() {
		return stop_id;
	}
	public void setStop_id(String stop_id) {
		this.stop_id = stop_id;
	}
	public Integer getStop_sequence() {
		return stop_sequence;
	}
	public void setStop_sequence(Integer stop_sequence) {
		this.stop_sequence = stop_sequence;
	}
	public String getStop_headsign() {
		return stop_headsign;
	}
	public void setStop_headsign(String stop_headsign) {
		this.stop_headsign = stop_headsign;
	}
	public Integer getPickup_type() {
		return pickup_type;
	}
	public void setPickup_type(Integer pickup_type) {
		this.pickup_type = pickup_type;
	}
	public Integer getDrop_off_type() {
		return drop_off_type;
	}
	public void setDrop_off_type(Integer drop_off_type) {
		this.drop_off_type = drop_off_type;
	}
	
	
}
