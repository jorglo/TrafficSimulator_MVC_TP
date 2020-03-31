package simulator.model;

import simulator.model.Weather;

public class NewRoadEvent extends Event{
	
	protected Road _rEvent;
	protected String id, src, dest;
	protected int maxSpeed, length, co2Limit;
	protected Weather weather;
	protected Junction src_j, dest_j;
	
	NewRoadEvent(int time) {
		super(time);
	}

	@Override
	void execute(RoadMap map) {
		
	}
	
}
