package simulator.model;

import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.error.ParseException;
import simulator.error.SimulatorError;
import simulator.misc.SortedArrayList;

public abstract class Road extends SimulatedObject{

	private Junction srcJunc; 				// cruce origen de la carretera
	private Junction destJunc;				// cruce destino de la carretera
	private int length; 					// longitud de la carretera
	private int maxSpeed; 					// limite máximo de velocidad
	private int currentSpeedLimit;			// limite de velocidad actual									
	private int contLimit;					// limmite de contaminacion
	private Weather weatherConditions;		// condiciones atmosfericas de la carretera
	private int totalContamination;			// contaminacion que existe en la carretera
	private List<Vehicle> vehicles; 		// lista de vehiculos que circulan por la carretera
	private Com comp;						// comparador
	
	public Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id);
		
		if(maxSpeed < 0) throw new ParseException("maxSpeed no puede ser negativa");
		this.maxSpeed = maxSpeed;
		
		if(contLimit < 0) throw new ParseException("contLimit no puede ser negativo");
		this.contLimit = contLimit;
		
		if(length < 0) throw new ParseException("length no puede ser negativo");
		this.length = length;
		
		if(srcJunc==null) throw new ParseException("srcJunc no puede ser null");
		this.srcJunc = srcJunc;
		
		if(destJunc==null) throw new ParseException("destJunc no puede ser null");
		this.destJunc = destJunc;
		
		setWeather(weather);
		this.totalContamination = 0;		
		this.comp = new Com();
		this.vehicles = new SortedArrayList<Vehicle>(comp);
	}

	/**
	 * Metodo para actualizar el estado de las carreteras dentro del Mapa de Carreteras.
	 */
	@Override
	void advance(int time) {
		
		if(this.totalContamination > 0)
			this.reduceTotalContamination();
		
		this.updateSpeedLimit();
		
		for(Vehicle coches: vehicles) {
			coches.setSpeed(this.calculateVehicleSpeed(coches));
			coches.advance(time);
		}
		vehicles.sort(this.comp);
	}
	
	/**
	 * Clase comparador
	 * @author Jorge y Alvaro
	 *
	 */
	public static class Com implements Comparator<Vehicle>{
		@Override
		public int compare(Vehicle o1, Vehicle o2) {
			if(o1.getLocation()<o2.getLocation())
				return 1;
			else if(o1.getLocation()==o2.getLocation())
				return 0;
		   return  -1;
		}
	}

	/**
	 * Devuelve el estado del vehiculo en formato JSONObject.
	 */
	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		
		jo.put("id", _id);
		jo.put("speedlimit", this.getCurrentSpeedLimit());
		jo.put("weather", this.weatherConditions);
		jo.put("co2", this.totalContamination);
		
		JSONArray idVehicles = new JSONArray();
		for(Vehicle v: vehicles) {
			idVehicles.put(v._id);
		}
		
		jo.put("vehicles", idVehicles);
		
		return jo;
	}
	
	/**
	 * se utiliza por los vehiculo para entrar en la siguiente carretera.
	 * @param v
	 */
	void enter(Vehicle v) {
		if(v.getCurrentSpeed()!=0 || v.getLocation()!=0)
			throw new SimulatorError("La velocidad actual y la localizacion deben tomar el valor 0");
			vehicles.add(v);
	}
	
	/**
	 * salir de la carretera
	 * @param v
	 */
	void exit(Vehicle v) {
		vehicles.remove(v);
	}
	
	/**
	 * poner las condiciones meteorologicas
	 * @param w
	 */
	void setWeather(Weather w) {
		if(w == null)
			throw new SimulatorError("wearher no puedes ser null");
		this.setWeatherConditions(w);
	}
	
	/**
	 * anadir contaminacion a la carretera
	 * @param c
	 */
	void addContamination(int c) {
		if(c < 0) 
			throw new SimulatorError("El valor de la contaminacion no puede ser negativa");
		setTotalContamination(getTotalCO2() + c);
	}
	
	// GETERS Y SETERS
	
	public int getLength() {
		return length;
	}

	public List<Vehicle> getVehicleList(){
		return this.vehicles;
	}
	
	public Weather getWeatherConditions() {
		return weatherConditions;
	}

	public int getTotalCO2() {
		return totalContamination;
	}

	public void setTotalContamination(int totalContamination) {
		this.totalContamination = totalContamination;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public int getCO2Limit() {
		return contLimit;
	}

	public void setCurrentSpeedLimit(int currentSpeedLimit) {
		this.currentSpeedLimit = currentSpeedLimit;
	}

	public int getCurrentSpeedLimit() {
		return currentSpeedLimit;
	}
	
	public Junction getDestJunc() {
		return destJunc;
	}
	
	public Junction getSrcJunc() {
		return srcJunc;
	}

	public void setWeatherConditions(Weather weatherConditions) {
		this.weatherConditions = weatherConditions;
	}
	
	// METODOS ABSTRACTOS
	
	abstract void reduceTotalContamination();
	abstract void updateSpeedLimit();
	abstract int calculateVehicleSpeed(Vehicle v);
	
}


