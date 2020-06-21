package simulator.model;

public enum VehicleStatus {
	PENDING, TRAVELING, WAITING, ARRIVED;
}

/*
 * Pending 		(todavía no ha entrado a la primera carretera de su itinerario)
 * Traveling 	(circulando sobre una carretera)
 * Waiting 		(esperando en un cruce)
 * Arrived 		(ha completado su itinerario)
 * 
 */
