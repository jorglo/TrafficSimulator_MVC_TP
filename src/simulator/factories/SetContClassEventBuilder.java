package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event> {

	public SetContClassEventBuilder() {
		super("set_cont_class");
	}

	@Override
	protected NewSetContClassEvent createTheInstance(JSONObject data) {

		List<Pair<String, Integer>> contClass = new ArrayList<Pair<String,Integer>>();
		JSONArray info = data.getJSONArray("info");
		JSONObject vehiculo = null;
		String first;
		Integer second;
		Pair<String, Integer> coche = null;
		for(int i = 0; i < info.length(); i++) {
			
			vehiculo = info.getJSONObject(i);
			
			first = vehiculo.getString("vehicle");
			second = vehiculo.getInt("class");
			
			coche = new Pair<String, Integer>(first, second);
			
			contClass.add(coche);
		}
		
		NewSetContClassEvent contClassEvent = new NewSetContClassEvent(data.getInt("time"), contClass);
		
		return contClassEvent;
	}

	

}
