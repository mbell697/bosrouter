package com.bosrouter.mbta.gtfs.realtime;

import java.io.IOException;
import java.net.URL;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.transit.realtime.GtfsRealtime;
import com.google.transit.realtime.GtfsRealtime.TripUpdate;
import com.google.transit.realtime.GtfsRealtime.VehiclePosition;

public class FeedReader implements Job{
	public static String TRIP_UPDATE_URL = "http://developer.mbta.com/lib/gtrtfs/Passages.pb";
	public static String VEHICLE_POSITION_URL = "http://developer.mbta.com/lib/gtrtfs/Vehicles.pb";
	
	private static final Logger LOG = LoggerFactory.getLogger(FeedReader.class);

	private void getTripUpdate() {
		TripUpdate update = null;
		try {
			URL url = new URL(TRIP_UPDATE_URL);
			update = GtfsRealtime.TripUpdate.parseFrom(url.openStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (update != null)
			LOG.info(update.toString());
	}
	
	private void getVehiclePositionUpdate() {
		VehiclePosition update = null;
		try {
			URL url = new URL(VEHICLE_POSITION_URL);
			update = GtfsRealtime.VehiclePosition.parseFrom(url.openStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (update != null)
			LOG.info(update.toString());
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		getTripUpdate();
		getVehiclePositionUpdate();
	}
}
