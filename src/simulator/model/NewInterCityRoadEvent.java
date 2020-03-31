package simulator.model;

import simulator.error.ExecutionException;

public class NewInterCityRoadEvent extends NewRoadEvent{
	
	public NewInterCityRoadEvent(int time, String id, String srcJun, String
			destJunc, int length, int co2Limit, int maxSpeed, Weather weather){
		super(time);
		this.id = id;
		this.src = srcJun;
		this.dest = destJunc;
		this.length = length;
		this.co2Limit = co2Limit;
		this.maxSpeed = maxSpeed;
		this.weather = weather;
	}
	
	@Override
	void execute(RoadMap map) {
		src_j = map.getJunction(this.src);
        if(src_j==null) throw new ExecutionException("cruce fuente "+this.src+" no existe");
        
        dest_j = map.getJunction(this.dest);
        if(dest_j==null) throw new ExecutionException("cruce destino "+this.dest+" no existe");
        
        map.addRoad(new InterCityRoad(id, src_j, dest_j, maxSpeed, co2Limit, length, weather));
	}
	
}
