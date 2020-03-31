package simulator.model;

import simulator.error.SimulatorError;

public class CityRoad extends Road{

	public CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {

		if(this.getWeatherConditions() == Weather.WINDY || this.getWeatherConditions() == Weather.STORM)
			this.setTotalContamination(this.getTotalContamination() - 10);
		else 
			this.setTotalContamination(this.getTotalContamination() - 2);
		
		if(this.getTotalContamination() < 0)
			throw new SimulatorError("TotalContamination no puede ser negativo");	
	}

	@Override
	void updateSpeedLimit() {
		this.setCurrentSpeedLimit(this.getMaxSpeed());
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		int newSpeed = (int) (((11.0-v.getContaminacionCoche())/11.0)*this.getMaxSpeed());
		return newSpeed;
	}

}
