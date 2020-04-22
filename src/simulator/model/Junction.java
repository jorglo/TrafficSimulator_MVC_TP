package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.error.SimulatorError;

public class Junction extends SimulatedObject
{
	
	private List<Road> incomingRoadsList;  			// lista de carreteras entrantes al cruce
	private List<List<Vehicle>> listQueues;			// lista de colas de vehiculos para las carreteras entrantes
	private Map<Road,List<Vehicle>> roadQueue;		// mapa de carretera-colas de vehiculos para hacer busquedas en la cola de una carretera dada
	private Map<Junction, Road> overcomingRoadsMap; // mapa de carreteras salientes y cruce destino
	private int greenRoadIndex;						// indice de la carretera entrante que tiene el semaforo verde
	private int lastChange;							// ultimo paso del cambio de semaforo
	private LightSwitchingStrategy lsStrategy;		// estrategia de tipo LightSwitchingStrategy
	private DequeuingStrategy dqStrategy;			// estratedia de tipo DequeuingStrategy
	
	// coordenadas para dibujar 
	// el cruce en la practica 2 (map).
	private int _xCoor;								// coordenada x
	private int _yCoor;								// coordenada y
	
	// coordenadas para dibujar 
	// el cruce en la practica 2 (map).
	private int _xCoorBR;								// coordenada x
	private int _yCoorBR;								// coordenada y
	
	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		
		super(id);
		
		if(lsStrategy==null || dqStrategy==null) System.err.println("Error2");
		this.lsStrategy = lsStrategy;
		this.dqStrategy = dqStrategy;
		
		if(xCoor<0 || yCoor<0) System.err.println("Error");
		_xCoor = xCoor;
		_yCoor = yCoor;
		
		this.greenRoadIndex = -1;
		this.lastChange = 0;
		
		this.overcomingRoadsMap = new HashMap<Junction, Road>();
		this.incomingRoadsList = new ArrayList<Road>();
		this.listQueues = new ArrayList<List<Vehicle>>();
		this.roadQueue = new HashMap<Road,List<Vehicle>>();
	}

	/**
	 * Avanza el estado del cruce:
	 * - estrategia de extraccion de la cola para calcular la lista de vehiculos a avanzar.
	 * - estrategia de cambio de semaforo para las siguientes carreteras con el semaforo en verde.
	 */
	@Override
	void advance(int time) {
		
		List<Vehicle> cola = new ArrayList<Vehicle>();
		
		//si el semaforo está en verde
		if(this.greenRoadIndex!=-1 && this.listQueues.get(greenRoadIndex).size()!=0) { 
			//calculo la lista de vehiculos que deben avanzar
			cola = this.dqStrategy.dequeue(this.listQueues.get(greenRoadIndex));
		}	
		
		//si la cola NO esta vacía
		if(!cola.isEmpty()) {
			//recorro la cola
			for(Vehicle v: cola) {
				//muevo el coche a la siguiente carretera
				v.moveToNextRoad();
				//lo elimino de la cola de la carretera, que acaba de abandonar
				this.listQueues.get(greenRoadIndex).remove(v);
			}	
		}
		
		//cambio de semaforo
		int aux = this.lsStrategy.chooseNextGreen(incomingRoadsList, listQueues, greenRoadIndex, lastChange, time);
		if(this.greenRoadIndex != aux) {
			this.greenRoadIndex = aux;
			lastChange = time;
		}			

	}

	/**
	 * Devuelve el estado del cruce en el siguiente formato JSON
	 */
	@Override
	public JSONObject report() {
		
		JSONObject jo = new JSONObject();
		
		jo.put("id", _id);
		if(this.greenRoadIndex != -1) {
			jo.put("green", this.incomingRoadsList.get(this.greenRoadIndex)._id);
		}
		else {
			jo.put("green", "none");
		}
		
		JSONArray arrayQueues = new JSONArray();


		for(int i = 0; i < this.incomingRoadsList.size(); i++) {
			
			JSONArray arrayVehicles = new JSONArray();
			JSONObject queue = new JSONObject();
			queue.put("road", this.incomingRoadsList.get(i)._id);
			for(int j = 0; j < this.incomingRoadsList.get(i).getVehicleList().size();j++) {
				if(this.incomingRoadsList.get(i).getVehicleList().get(j).getStatus() == VehicleStatus.WAITING)
					arrayVehicles.put(this.incomingRoadsList.get(i).getVehicleList().get(j)._id);
			}
			queue.put("vehicles", arrayVehicles);
			arrayQueues.put(queue);
		}
		
		jo.put("queues", arrayQueues);

		return jo;
	}
	
	/**
	 * Anadimos una carretera entrante al cruce actual.
	 * @param r
	 */
	void addIncommingRoad(Road r) {
		
		// comprobar que sea una carretera entrante
		if(!r.getDestJunc().equals(this))  
			 throw new SimulatorError("El cruce origen y el destino son iguales");
		
		// anade r al final de la lista de carreteras entrantes
		this.incomingRoadsList.add(r);
		
		// crea una cola para r
		List<Vehicle> queueForRoad = new LinkedList<Vehicle>();
		
		// anadimos la cola queueForRoad al final de la lista de colas
		this.listQueues.add(queueForRoad);
		
		// anadimos la lista-cola al mapa
		this.roadQueue.put(r, queueForRoad);
		
	}
	
	/**
	 * Anade el par(j,r) al mapa de carreteras salientes
	 * donde j es el cruce destino de la carretera r.
	 * @param r
	 */
	void addOutGoingRoad(Road r) { 		

		if(this.getOvercomingRoadsMap().containsValue(r)) {
			throw new SimulatorError("Hay mas carreteras que van al cruce destino desde este cruce");
		}
		if(r.getDestJunc().equals(this))  
			 throw new SimulatorError("El cruce origen y el destino son iguales");
				
		this.overcomingRoadsMap.put(r.getDestJunc(), r);
	}
	
	/**
	 * Aï¿½ade el vehiculo v a la cola de la carretera r, 
	 * siendo r la carretera actual del vehiculo v.
	 * @param r
	 * @param v
	 */
	void enter(Vehicle v) {
		//busco el indice de la carretera
		int indexRoad = this.incomingRoadsList.indexOf(v.getRoad()); 
		//anado el vehiculo a la cola de su carretera
		this.listQueues.get(indexRoad).add(v);
		//anado la cola al mapa de road-queue
		this.roadQueue.put(v.getRoad(), this.listQueues.get(indexRoad));
	}
	
	/**
	 * Devuelve la carretera del mapa de carreteras salientes 
	 * que va desde el cruce actual al j
	 * @param j
	 * @return Road
	 */
	Road roadTo(Junction j) {
		return this.getOvercomingRoadsMap().get(j);
	}
	
	/**
	 * Obtener el mapa de carreteras salientes al cruce.
	 * @return overcomingRoadsMap
	 */
	public Map<Junction, Road> getOvercomingRoadsMap() {
		return overcomingRoadsMap;
	}
	
	public Map<Road,List<Vehicle>> getRoadQueue(){
		return this.roadQueue;
	}

	public int getX() {
		return _xCoor;
	}
	
	public int getY() {
		return _yCoor;
	}

	public int getGreenLightIndex() {
		return this.greenRoadIndex;
	}

	public List<Road> getInRoads() {
		return this.incomingRoadsList;
	}

	public int get_xCoorBR() {
		return _xCoorBR;
	}

	public void set_xCoorBR(int _xCoorBR) {
		this._xCoorBR = _xCoorBR;
	}

	public int get_yCoorBR() {
		return _yCoorBR;
	}

	public void set_yCoorBR(int _yCoorBR) {
		this._yCoorBR = _yCoorBR;
	}	
	
}
