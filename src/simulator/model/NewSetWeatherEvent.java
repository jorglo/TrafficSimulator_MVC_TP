package simulator.model;

import java.util.List;

import simulator.error.SimulatorError;
import simulator.misc.Pair;

public class NewSetWeatherEvent extends Event{

	private List<Pair<String, Weather>> _ws;
	
	public NewSetWeatherEvent(int time, List<Pair<String, Weather>> ws) {
		super(time);
		if(ws == null) throw new SimulatorError("ws es null");
		_ws = ws;	
	}

	@Override
	void execute(RoadMap map) {
		for(Pair<String, Weather> w: _ws) {
			if(map.getRoad(w.getFirst())==null) 
				throw new SimulatorError("La carretera no existe en el mapa de carreteras.");
			map.getRoad(w.getFirst()).setWeatherConditions(w.getSecond());
		}
	}
	
	@Override
	public String toString() {
		String text = "";
		
		for (Pair<String, Weather> pair : _ws) {
			text += "("+pair.getFirst()+","+pair.getSecond()+"),";
		}
		
		return "Change Weather: [" + text + "]";
	}
	
}
