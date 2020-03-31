package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;
import simulator.model.RoadMap;

public class NewVehicleEventBuilder extends Builder<Event> {
	
	protected RoadMap roadMap; 
	
	public NewVehicleEventBuilder() {
		super("new_vehicle");
	}

	
	@Override
	protected NewVehicleEvent createTheInstance(JSONObject data) {
		
		JSONArray itinerary = data.getJSONArray("itinerary");
		List<String> itinerario = new ArrayList<>();
		
		for(int i = 0; i < itinerary.length(); i++) {
			itinerario.add(itinerary.getString(i));			
		}
		
		NewVehicleEvent vehicle = new NewVehicleEvent(
				data.getInt("time"), 
				data.getString("id"),
				data.getInt("maxspeed"),
				data.getInt("class"),
				itinerario);
		
		return vehicle;
	}



}
