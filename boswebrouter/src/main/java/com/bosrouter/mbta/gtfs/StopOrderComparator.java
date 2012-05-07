package com.bosrouter.mbta.gtfs;

import java.util.Comparator;

public class StopOrderComparator implements Comparator<StopTimes> {

	@Override
	public int compare(StopTimes arg0, StopTimes arg1) {
		if (arg0.getTrip_id().compareTo(arg1.getTrip_id()) != 0) {
			//TODO Error, not comparable
		}
		
		return arg0.getStop_sequence() - arg1.getStop_sequence();
	}

}
