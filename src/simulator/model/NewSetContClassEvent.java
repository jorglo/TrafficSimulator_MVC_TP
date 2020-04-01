package simulator.model;

import java.util.List;

import simulator.error.ExecutionException;
import simulator.error.ParseException;
import simulator.misc.Pair;

public class NewSetContClassEvent extends Event{
	
	private List<Pair<String, Integer>> _cs;

	public NewSetContClassEvent(int time, List<Pair<String, Integer>> cs) {
		super(time);
		if(cs == null)
			throw new ParseException("cs no puede ser nulo");
		_cs = cs;
	}

	@Override
	void execute(RoadMap map) {
		for(Pair<String, Integer> c: _cs) {
			if(map.getVehicle(c.getFirst())==null) 
				throw new ExecutionException("El vehiculo no existe en RoadMap");
			map.getVehicle(c.getFirst()).setContaminationClass(c.getSecond());
		}
	}
	
	@Override
	public String toString() {
	return "New Set Cont Class '" + _id + "'" ;
	}
	
}
