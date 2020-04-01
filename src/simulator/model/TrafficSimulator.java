package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.error.ExecutionException;
import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver>, TrafficSimObserver{
	
	private RoadMap _roadMap;
	private List<Event> _eventsList;
	private int _simulationTime;
	private List<TrafficSimObserver> _observersList;
	
	public TrafficSimulator() {
		_roadMap = new RoadMap();
		_eventsList = new SortedArrayList<Event>();
		_simulationTime = 0;
		_observersList = new ArrayList<TrafficSimObserver>();
	}
	
	/**
	 * anadir eventos ordenados por el "paso de ejecucion"
	 * @param e
	 */
	public void addEvent(Event e){		
		_eventsList.add(e);
	}
	
	/**
	 * avanza el estado de la simulacion
	 */
	public void advance(){
		_simulationTime++;
		onAdvanceStart(_roadMap, _eventsList, _simulationTime);
		executeEvents();
		_roadMap.advanceJuntion(_simulationTime);
		_roadMap.advanceRoad(_simulationTime);
		onAdvanceEnd(_roadMap, _eventsList, _simulationTime);
	}
	
	/**
	 * ejecuta los eventos de la lista de un tiempo determinado llamando a su
	 * metodo execute(). Por ejemplo, el metodo execute() de NewVehicleEvent
	 * suma un vehiculo a map.
	 */
	private void executeEvents() {
		ArrayList<Event> eventsToRemove = new ArrayList<>();
		for (Event event : _eventsList) {
			try {
				if(event._time == _simulationTime) {
					event.execute(_roadMap);
					eventsToRemove.add(event);
					onEventAdded(_roadMap, _eventsList, event, _simulationTime);
				}
			}catch(ExecutionException e) {
				onError(e.getMessage());
				_eventsList.removeAll(eventsToRemove);
				throw e;
			}
		}
		//elimina los eventos ejecutados
		_eventsList.removeAll(eventsToRemove);
	}
	
	public void reset() {
		_eventsList.clear();
		_roadMap = null;
		_simulationTime = 0;
		onReset(_roadMap, _eventsList, _simulationTime);
	}

	public JSONObject report() {		
		JSONObject simulator = new JSONObject();

		simulator.put("time", _simulationTime);
		simulator.put("state", _roadMap.report());
		
		return simulator;
	}

	/* - INTERFACE OBSERBABLE - */
	@Override
	public void addObserver(TrafficSimObserver o) {
		_observersList.add(o);
		onRegister(_roadMap, _eventsList, _simulationTime);
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		_observersList.remove(o);
	}

	/* - INTERFACE TRAFFICOBSERVER - */
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		
	}

	@Override
	public void onError(String err) {
		
	}
}
