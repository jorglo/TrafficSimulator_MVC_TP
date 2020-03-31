package simulator.factories;

import org.json.JSONObject;

public interface Factory<T> {
	/*
	 * recibe como parámetro una estructura que describe el obj. a crear
	 * devuelve una instancia de la clase correspondiente
	 * 
	 */
	public T createInstance(JSONObject info);
}
