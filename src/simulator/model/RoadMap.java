package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.error.SimulatorError;

public class RoadMap {
	
	private List<Junction> listJunctions;
	private List<Road> listRoads;
	private List<Vehicle> listVehicles;
	
	private Map<String,Junction> mapJunctions;
	private Map<String,Road> mapRoads;
	private Map<String,Vehicle> mapVehicles;
	
	public RoadMap(){ 
		
		this.listVehicles = new ArrayList<>();
		this.listRoads = new ArrayList<>();
		this.listJunctions = new ArrayList<>();
		
		this.mapJunctions = new HashMap<>();
		this.mapRoads = new HashMap<>();
		this.mapVehicles = new HashMap<>();
		
	}
	
	void addJunction(Junction j) {
		
		if(this.mapJunctions.containsKey(j._id))
			throw new SimulatorError("The Junction already exist!!");
		
		listJunctions.add(j);
		mapJunctions.put(j._id, j);		
	}
	
	void addRoad(Road r){
	
		if(this.mapRoads.containsKey(r._id))
			throw new SimulatorError("The Road already exist!!");
		if(!this.mapJunctions.containsValue(r.getSrcJunc()) && !this.mapJunctions.containsValue(r.getDestJunc()))
			throw new SimulatorError("The Junction doesn´t exist!!");
		
		listRoads.add(r);
		mapRoads.put(r._id, r);
		
		r.getSrcJunc().addOutGoingRoad(r);
		r.getDestJunc().addIncommingRoad(r);
	}
	
	void addVehicle(Vehicle v){
		
		if(this.mapVehicles.containsKey(v._id)) 
			throw new SimulatorError("The Vehicle already exist!!");
		if(!checkItinerary(v))
			throw new SimulatorError("The Itinerary is not possible!!");
		
		listVehicles.add(v);
		mapVehicles.put(v._id,v);
		
	}
	

	private boolean checkItinerary(Vehicle v) {
		
		boolean correctItinerary = true;
		List<Junction> JuntionListVehicule = new ArrayList<>();
		JuntionListVehicule = v.getItinerary();
		
		Map<Junction, Road> overcomingRoadsMap = new HashMap<>();
		
		for(int i = 0; i < JuntionListVehicule.size(); i++) {
			overcomingRoadsMap = JuntionListVehicule.get(i).getOvercomingRoadsMap();
			if((i + 1 < JuntionListVehicule.size()) && !overcomingRoadsMap.containsKey(JuntionListVehicule.get(i+1))){
				correctItinerary = false;
			}
		}
		return correctItinerary;
	}
	
	void reset() {
		
		listVehicles.clear();
		listRoads.clear();
		listJunctions.clear();
		
		mapRoads.clear();
		mapVehicles.clear();
		mapJunctions.clear();
		
	}
	public JSONObject report() {
		
		JSONArray juntions = new JSONArray();
		for(int i = 0; i<listJunctions.size(); i++) {
			juntions.put(listJunctions.get(i).report());
		}
		
		JSONArray roads = new JSONArray();
		for(int i = 0; i<listRoads.size(); i++) {
			roads.put(listRoads.get(i).report());
		}
		
		JSONArray vehicles = new JSONArray();
		for(int i = 0; i<listVehicles.size(); i++) {
			vehicles.put(listVehicles.get(i).report());
		}
		
		JSONObject roadMap = new JSONObject();
		roadMap.put("roads", roads);
		roadMap.put("vehicles", vehicles);	
		roadMap.put("junctions", juntions);		
		
		
		return roadMap;
		
	}

	public void advanceJuntion(int time) {
		for(Junction j: listJunctions) {
			j.advance(time);
		}
	}

	public void advanceRoad(int time) {
		for(Road r : listRoads) {
			r.advance(time);
		}
	}

	public Junction getJunction(String id){
		return mapJunctions.get(id);	
	}
	
	public Road getRoad(String id){
		return mapRoads.get(id);
		
	}
	
	public Vehicle getVehicle(String id){
		return mapVehicles.get(id);
		
	}
	public List<Junction> getJunctions(){
		return listJunctions;
		
	}
	
	public List<Road>getRoads(){
		return listRoads;
		
	}
	
	public List<Vehicle>getVehicles(){
		return listVehicles;
		
	}
	
}
