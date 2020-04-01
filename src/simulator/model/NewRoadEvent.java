package simulator.model;

public class NewRoadEvent extends Event{
	
	protected Road _rEvent;
	protected String _src, _dest;
	protected int _maxSpeed, _length, _co2Limit;
	protected Weather _weather;
	protected Junction _src_j, _dest_j;
	
	NewRoadEvent(int time) {
		super(time);
	}

	@Override
	void execute(RoadMap map) {
		
	}
	
}
