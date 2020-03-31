package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NewVehicleEvent extends Event{
	
	private String _id;
	private int _maxSpeed;
	private int _contClass;
	private List<String> _itinerary;
	
	public NewVehicleEvent(int time, String id, int maxSpeed, int
			contClass, List<String> itinerary) {
		super(time);
		_id = id;
		_maxSpeed = maxSpeed;
		_contClass = contClass;
		_itinerary = itinerary;
	}

	@Override
	void execute(RoadMap map) {
		List<Junction> itinerary = new ArrayList<Junction>();
		
		for(int i = 0; i < _itinerary.size(); i++){
			itinerary.add(map.getJunction(_itinerary.get(i)));
		}
		
		Vehicle vehicleEvent = new Vehicle(_id, _maxSpeed, _contClass, itinerary);
		map.addVehicle(vehicleEvent);
		
		vehicleEvent.moveToNextRoad();
	}
	
}
