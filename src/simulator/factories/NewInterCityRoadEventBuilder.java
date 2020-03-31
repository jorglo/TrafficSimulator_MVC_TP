package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewInterCityRoadEvent;
import simulator.model.Weather;

public class NewInterCityRoadEventBuilder extends Builder<Event>{

	public NewInterCityRoadEventBuilder() {
		super("new_inter_city_road");
	}

	@Override
	protected NewInterCityRoadEvent createTheInstance(JSONObject data) {
		
		if(data != null) {
			  NewInterCityRoadEvent road = new NewInterCityRoadEvent(
					data.getInt("time"), 
					data.getString("id"), 
					data.getString("src"), 
					data.getString("dest"), 
					data.getInt("length"),
					data.getInt("co2limit"),
					data.getInt("maxspeed"),
					data.getEnum(Weather.class, "weather")
					);
			  return road;
			}
			return null;
	}

}
