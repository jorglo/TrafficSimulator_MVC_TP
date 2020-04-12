package simulator.model;

public class InterCityRoad extends Road {

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		if(this.getWeatherConditions() == Weather.SUNNY) {
			this.setTotalContamination((int) (((100.0 - 2) / 100.0 ) * this.getTotalCO2()));
		}
		else if(this.getWeatherConditions() == Weather.CLOUDY) {
			this.setTotalContamination((int) (((100.0 - 3) / 100.0 ) * this.getTotalCO2()));
		}
		else if(this.getWeatherConditions() == Weather.RAINY) {
			this.setTotalContamination((int) (((100.0 - 10) / 100.0 ) * this.getTotalCO2()));
		}
		else if(this.getWeatherConditions() == Weather.WINDY) {
			this.setTotalContamination((int) (((100.0 - 15) / 100.0 ) * this.getTotalCO2()));
		}
		else{
			this.setTotalContamination((int) (((100.0 - 20) / 100.0 ) * this.getTotalCO2()));
		}
	}

	@Override
	void updateSpeedLimit() {
		if(this.getTotalCO2() > this.getCO2Limit()) 
			this.setCurrentSpeedLimit((int) (this.getMaxSpeed() * 0.5));
		else 
			this.setCurrentSpeedLimit(this.getMaxSpeed());
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		return (this.getWeatherConditions() == Weather.STORM)?
				(int) (this.getCurrentSpeedLimit()*0.8) : this.getCurrentSpeedLimit();
	}

}
