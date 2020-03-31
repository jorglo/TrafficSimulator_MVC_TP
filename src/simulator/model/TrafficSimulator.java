package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.error.ExecutionException;
import simulator.misc.SortedArrayList;

public class TrafficSimulator {
	
	private RoadMap mapRoad;
	private List<Event> listEvent;
	private int simulationTime;
	
	public TrafficSimulator() {
		this.mapRoad = new RoadMap();
		this.listEvent = new SortedArrayList<Event>();
		this.simulationTime = 0;
	}
	
	/**
	 * anadir eventos ordenados por el "paso de ejecucion"
	 * @param e
	 */
	public void addEvent(Event e){		
		this.listEvent.add(e);
	}
	
	/**
	 * avanza el estado de la simulacion
	 */
	public void advance(){
		this.simulationTime++;
		executeEvents();
		mapRoad.advanceJuntion(this.simulationTime);
		mapRoad.advanceRoad(this.simulationTime);
	}
	
	/**
	 * ejecuta los eventos de la lista de un tiempo determinado llamando a su
	 * metodo execute(). Por ejemplo, el metodo execute() de NewVehicleEvent
	 * suma un vehiculo a map.
	 */
	private void executeEvents() {
		ArrayList<Event> eventsToRemove = new ArrayList<>();
		for (Event event : listEvent) {
			try {
				if(event._time == this.simulationTime) {
					event.execute(mapRoad);
					eventsToRemove.add(event);
				}
			}catch(ExecutionException e) {
				listEvent.removeAll(eventsToRemove);
				throw e;
			}
		}
		//elimina los eventos ejecutados
		listEvent.removeAll(eventsToRemove);
	}
	
	public void reset() {
		this.listEvent.clear();
		this.mapRoad = null;
		this.simulationTime = 0;
	}

	public JSONObject report() {		
		
		JSONObject simulator = new JSONObject();

		simulator.put("time", this.simulationTime);
		simulator.put("state", this.mapRoad.report());
		return simulator;
	}
	
	public RoadMap getMapRoad() {
		return mapRoad;	
	}
}
