package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import simulator.error.ExecutionException;
import simulator.error.ParseException;

public class Vehicle extends SimulatedObject{
	
	private List<Junction> itinerary;		// lista de cruces a recorrer
	private int maxSpeed;					// velocidad maxima para el vehiculo
	private int currentSpeed;				// velocidad actual del vehiculo
	private VehicleStatus status;			// estado del vehiculo [PENDING, TRAVELING, WAITING, ARRIVED]
	private Road road;						// carretera por la circula
	private int location;					// localizacion actual dentro de la carretera
	private int cO2;					    // contaminacion producida por el coche
	private int contaminationWholeWay;      // contaminacion total del coche en el trayecto
	private int totalTravelledDistance;		// distancia tota recorrida por el coche
	private int junctionIndex;				// indice de cruces
	
	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) {
		super(id);
		
		if(maxSpeed < 0)
			throw new ParseException("Tiene que ser un valor positivo");
		this.maxSpeed = maxSpeed;
		
		if(itinerary.size() < 2)
			throw new ParseException("Itinerary tiene que ser mayor a 2");
		this.itinerary = itinerary;
		
		setContaminationClass(contClass);
		
		this.junctionIndex = 0;
		this.location = 0;
		this.status = VehicleStatus.PENDING;

		//lista de solo lectura
		Collections.unmodifiableList(new ArrayList<>(itinerary));
	}

	/**
	 * Metodo para mover el coche a traves de la carretera y dentro del Mapa de Carreteras.
	 */
	@Override
	void advance(int time) {
	    if(this.status == VehicleStatus.TRAVELING) {
		      
	      int preLocation = location;
	      int newLocation = location + currentSpeed;
	      int roadLong = road.getLength();
	      
	      this.location = (newLocation < roadLong)?newLocation:roadLong;
	      
	      int c = ((location-preLocation) * cO2);
	      this.contaminationWholeWay += c;
	      road.addContamination(c);
	      
	            if (this.location == roadLong) {
	                this.status = VehicleStatus.WAITING;
	                this.currentSpeed = 0;
	                this.road.getDestJunc().enter(this);
	                this.totalTravelledDistance += (roadLong-preLocation);
	            }
	            else
	              this.totalTravelledDistance += currentSpeed; //total recorrido del coche
	            
	    }else 
	      this.currentSpeed = 0;
	 }
	
	/**
	 * Devuelve el estado del vehiculo en formato JSONObject.
	 */
	@Override
	public JSONObject report() {
		
		JSONObject jo = new JSONObject();
		
		jo.put("id", _id);
		jo.put("speed", this.currentSpeed);
		jo.put("distance", this.totalTravelledDistance);
		jo.put("co2", this.contaminationWholeWay);
		jo.put("class", this.cO2);
		jo.put("status", this.status);
		
		if(this.status != VehicleStatus.ARRIVED && this.status != VehicleStatus.PENDING) {
			jo.put("road", this.road._id);
			jo.put("location", this.location);
		}
		return jo;
	}
	
	/**
	 * Mueve el vehiculo a la siguiente carretera.
	 */
	void moveToNextRoad() {
		this.location = 0;
		this.currentSpeed = 0;
		
		//si el estado no es PENDING O WAITING
		if (this.status == VehicleStatus.ARRIVED || this.status == VehicleStatus.TRAVELING)
			throw new ExecutionException("El estado no puede ser ARRIVED o TRAVELING ");
		
		//si NO es el ultimo cruce..
		if(junctionIndex < this.itinerary.size()-1) {
			
			if(this.road != null) 
	    		this.road.exit(this);
			
			//.. comprobamos que no se va a andir a la primera carretera y borramos el vehiculo de la lista anterior
			if(junctionIndex != 0) {
				this.road.getVehicleList().remove(this);
				//this.road.getDestJunc().getRoadQueue().remove(this.road);
			}
			
			//.. anadimos los nuevos valores del coche
	        this.road = this.itinerary
	        		.get(junctionIndex)
	                .roadTo(this.itinerary.get(junctionIndex + 1));
	        
	        this.junctionIndex++;
	        
	        this.location = 0;
	        if(this.road != null) {
	        	road.enter(this);
	        	this.status = VehicleStatus.TRAVELING;
	        }
		}
		//si es el ULTIMO cruce..
		else {
			//..eliminamos el coche de la lista de vehiculos de la carretera donde estaba
			this.road.getVehicleList().remove(this); 
			
			//..añadimos los nuevos valores del coche
			this.road = null;
			this.status = VehicleStatus.ARRIVED;
			this.currentSpeed = 0;	
		}
	}
	
	/**
	 * Pone la velocidad actual al valor m�nimo entre s y la velocidad m�xima del vehiculo.
	 * @param s
	 */
	void setSpeed(int s) {
		if(s < 0) throw new ParseException("speed no puede ser negativa");
		this.currentSpeed = (s < this.maxSpeed)? s : this.maxSpeed;
	}
	
	/**
	 * Pone el valor de contaminacion del vehiculo a c.
	 * @param c
	 */
	void setContaminationClass(int c){
		if(c < 0 || c > 10) throw new ParseException("ContaminationClass tiene que ser un valor entre 0 y 10");
		this.cO2 = c;
	}

	// GETERS Y SETERS
	
	public int getCurrentSpeed() {
		return currentSpeed;
	}
	
	public int getMaxSpeed() {
		return maxSpeed;
	}

	public int getLocation() {
		return location;
	}
	
	public int getCO2() {
		return cO2;
	}
	
	public int getContaminationWholeWay() {
		return contaminationWholeWay;
	}

	public VehicleStatus getStatus() {
		return this.status;
	}
	
	public List<Junction> getItinerary() {
		return itinerary; 
	}
	
	public Road getRoad() {
		return this.road;
	}

	public int getTotalTravelledDistance() {
		return totalTravelledDistance;
	}
	
	
	
	
}
