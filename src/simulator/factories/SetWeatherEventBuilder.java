package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetWeatherEvent;
import simulator.model.RoadMap;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event>{	

	protected RoadMap roadMap;
	
	public SetWeatherEventBuilder() {
		super("set_weather");
	}

	@Override
	protected NewSetWeatherEvent createTheInstance(JSONObject data) {		
		
		List<Pair<String, Weather>> sw = new ArrayList<Pair<String,Weather>>();
		JSONArray info = data.getJSONArray("info");
		JSONObject carretera = null;
		String first;
		Weather second;
		Pair<String, Weather> carreteras = null;
		for(int i = 0; i < info.length(); i++) {
			
			carretera = info.getJSONObject(i);
			
			first = carretera.getString("road");
			second = carretera.getEnum(Weather.class, "weather");
			
			carreteras = new Pair<String, Weather>(first, second);
	
			sw.add(carreteras);
		}
		
		NewSetWeatherEvent weather = new NewSetWeatherEvent(data.getInt("time"), sw);
		
		return weather;
	}

}
