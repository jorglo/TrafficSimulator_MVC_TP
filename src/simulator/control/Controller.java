package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {
	
	private TrafficSimulator _trafficSim;
	private Factory<Event> _eventsFactory;

	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) throws IllegalArgumentException{
		if(sim == null) 
			throw new IllegalArgumentException("sim toma un valor null");
		if(eventsFactory == null) 
			throw new IllegalArgumentException("eventsFactory toma un valor null");
		
		_trafficSim = sim;
		_eventsFactory = eventsFactory;
	}
	
	/**
	 * Cargar eventos
	 * @param in
	 */
	public void loadEvents(InputStream in) {
		
		// creamos un objeto de tipo JSONObject al que le pasamos el fichero de entrada
		JSONObject jo = null;
		
		try {
			jo = new JSONObject(new JSONTokener(in));
		}catch (IllegalArgumentException e) {
			System.out.println(e.toString());
		}
		
		// creamos un array para extraer cada elemento de jo			
		JSONArray arrayEventos = jo.getJSONArray("events");
		JSONObject evento = null;
			
		for (int i = 0; i < arrayEventos.length(); i++) {					
			evento = arrayEventos.getJSONObject(i); //obtenemos el evento del array				
			_trafficSim.addEvent(_eventsFactory.createInstance(evento)); //convertimos el evento de JSON a Event y lo aÃ±adimos a la lista				
		}
						
	}
	
	//JORGE: metodo run creado con un solo argumento por que lo pedía en la vista
	/**
	 * ejecutar la aplicacion por la interfaz grafica
	 * @param n
	 */
	public void run(int n) {
		for (int i = 0; i < n; i++) {
			_trafficSim.advance();
		} 
	}
	
	/**
	 * ejecutar la aplicacion con fichero de salida
	 * @param n
	 * @param out
	 */
	public void run(int n, OutputStream out) {
		
		JSONObject jo = new JSONObject();
		JSONArray aux = new JSONArray();
		JSONObject states = new JSONObject();		
	
		PrintWriter pw = new PrintWriter(out);
		
		for (int i = 0; i < n; i++) {
			_trafficSim.advance();
			jo = _trafficSim.report();
			aux.put(jo);
		} 
		
		states.put("states", aux);
		
		//String report = states.toString(); 
		String report = states.toString(3); 
		
		pw.print(report); 	
		pw.close();
	}
	
	/**
	 * reset de los valores de la aplicacion
	 */
	public void reset() {
		_trafficSim.reset();
	}
	
	/**
	 * anadir eventos
	 * @param e
	 */
	public void addEvent(Event e) {
		_trafficSim.addEvent(e);
	}
	
	// INTEFAZ OBSERVABLE
	/**
	 * anadir un observador
	 * @param o
	 */
	public void addObserver(TrafficSimObserver o){
		_trafficSim.addObserver(o);
	}
	
	/**
	 * eliminar un observador
	 * @param o
	 */
	public void removeObserver(TrafficSimObserver o) {
		_trafficSim.removeObserver(o);
	}
	
	
	// METODO RUN PARA PRUEBAS CON FICHERO
	
//	public void run(int n, OutputStream out) {
//		
//		JSONObject jo = new JSONObject();
//		JSONArray aux = new JSONArray();
//	
//		PrintWriter pw = new PrintWriter(out);
//		
//		pw.println("{");
//		pw.println("  \"states\": [ ");
//		
//		for (int i = 0; i < n; i++) {
//			_trafficSim.advance();
//			jo = _trafficSim.report();
//			pw.println(jo + ",");
//		} 
//		
//		pw.println("]");
//		pw.println("}");	
//		pw.close();
//	
//	}
}
