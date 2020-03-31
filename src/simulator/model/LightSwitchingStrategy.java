package simulator.model;

import java.util.List;

public interface LightSwitchingStrategy {
	
	/**
	 * Interfaz estrategia de cambio de semaforo.
	 * Implementada por MostCrowdedStrategy y RoundRobinStrategy
	 * 
	 * @param road				--Lista de carreteras entrantes
	 * @param qs 				--Lista de colas de vehiculos por carretera entrante
	 * @param currGreen 		--Indice de la carretera con el semaforo en verde (-1 todos en rojo)
	 * @param lastSwitchingTime --Paso de simulación en el que la carretera i, cambió de rojo a verde.
	 * @param currTime 			--Paso de simulacion actual
	 * 
	 * @return int (0 = rojo o 1 = verde)
	 */
	int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, 
			int currGreen, int lastSwitchingTime, int currTime);
}
