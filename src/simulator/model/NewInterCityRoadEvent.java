package simulator.model;

import simulator.error.ExecutionException;

public class NewInterCityRoadEvent extends NewRoadEvent{
	
	public NewInterCityRoadEvent(int time, String id, String srcJun, String
			destJunc, int length, int co2Limit, int maxSpeed, Weather weather){
		super(time);
		_id = id;
		_src = srcJun;
		_dest = destJunc;
		_length = length;
		_co2Limit = co2Limit;
		_maxSpeed = maxSpeed;
		_weather = weather;
	}
	
	@Override
	void execute(RoadMap map) {
		_src_j = map.getJunction(_src);
        if(_src_j==null) throw new ExecutionException("cruce fuente "+_src+" no existe");
        
        _dest_j = map.getJunction(_dest);
        if(_dest_j==null) throw new ExecutionException("cruce destino "+_dest+" no existe");
        
        map.addRoad(new InterCityRoad(_id, _src_j, _dest_j, _maxSpeed, _co2Limit, _length, _weather));
	}
	
	@Override
	public String toString() {
	return "New Inter City Road '" + _id + "'" ;
	}
	
}
