package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.model.TrafficSimObserver;
import simulator.error.ExecutionException;
import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver>{
	
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
		onEventAdded(_roadMap, _eventsList, e, _simulationTime); 
	}
	
	/**
	 * avanza el estado de la simulacion
	 */
	public void advance(){
		
		_simulationTime++;
		onAdvanceStart(_roadMap, _eventsList, _simulationTime);
		executeEvents();
		
		try {
			_roadMap.advanceJuntion(_simulationTime);
			_roadMap.advanceRoad(_simulationTime);
			onAdvanceEnd(_roadMap, _eventsList, _simulationTime);
		} catch (Exception e) {
			onError(e.getMessage());
			throw new ExecutionException(e.getMessage());
		}
		
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
		_roadMap = new RoadMap();
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
	
	/**
	 * se invoca cuando se ejecuta el metodo advance de TrafficSimulator, 
	 * inmediatamente despues de incrementar el tiempo y antes de ejecutar los eventos.
	 * 
	 * @param map
	 * @param events
	 * @param time
	 */
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		for(TrafficSimObserver observer : _observersList) 
			observer.onAdvanceEnd(map, events, time);
	}

	/**
	 * se invoca cuando termina el m�todo advance de TrafficSimulator 
	 * avanzando el estado (es decir, al final del m�todo).
	 * 
	 * @param map
	 * @param events
	 * @param time
	 */
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		for(TrafficSimObserver observer : _observersList) 
			observer.onAdvanceEnd(map, events, time);
	}

	/**
	 * se invoca cuando se a�ade un evento al simulador (despu�s de a�adir el evento 
	 * a la cola). Su tercer par�metro e es el evento que se ha a�adido a la cola.
	 * 
	 * @param map
	 * @param events
	 * @param e
	 * @param time
	 */
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		for(TrafficSimObserver observer : _observersList) 
			observer.onEventAdded(map, events, e, time);
	}
	
	/**
	 * se invoca al final del metodo reset (es decir, despues de hacer el reset).
	 * 
	 * @param map
	 * @param events
	 * @param time
	 */
	public void onReset(RoadMap map, List<Event> events, int time) {
		for(TrafficSimObserver observer : _observersList) 
			observer.onReset(map, events, time);
	}

	/**
	 * se invoca cuando se registra un observador en la clase TrafficSimulator .
	 * 
	 * @param map
	 * @param events
	 * @param time
	 */
	public void onRegister(RoadMap map, List<Event> events, int time) {
		for(TrafficSimObserver observer : _observersList) 
			observer.onRegister(map, events, time);
	}

	/**
	 * se invoca cuando ocurre un error. Cada vez que se lanza una excepci�n en la 
	 * clase TrafficSimulator, primero se debe invocar onError(...) con el
	 * correspondiente mensaje y posteriormente lanzar la excepcion. Si se lanza una
	 * excepcion e en el metodo advance de las carreteras y los cruces, se debe capturar en
	 * TrafficSimulator , invocar onError con el mensaje e.getMessage() , y por ultimo
	 * volver a lanzarla.
	 * 
	 * @param err
	 */
	public void onError(String err) {
		for(TrafficSimObserver observer : _observersList) 
			observer.onError(err);
	}
}
