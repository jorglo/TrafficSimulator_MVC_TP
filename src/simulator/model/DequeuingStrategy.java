package simulator.model;

import java.util.List;

public interface DequeuingStrategy {
	
	/**
	 * @param q -- Lista de vehiculos entrantes.
	 * 
	 * @return List<Vehicle> lista de vehiculos
	 */
	List<Vehicle> dequeue(List<Vehicle> q);
	
}
