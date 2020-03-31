package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {
	
	private int timeSlot;
	
	public MostCrowdedStrategy(int timeSlot) {
		this.timeSlot = timeSlot;
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		
		int size = 0;
		int roadIndex = 0;
		if(roads.isEmpty())
			return -1;
		else if(currGreen == -1){
			for(int i = 0; i < qs.size(); i++) {
				if(qs.get(i).size() > size) {
					size = qs.get(i).size();
					roadIndex = i;
				}
			}
			return roadIndex;			
		}else if(currTime - lastSwitchingTime < this.timeSlot)
			return currGreen;
		else {
			for(int i = (currGreen + 1) % roads.size(); i < qs.size(); i++) {
				if(qs.get(i).size() > size) {
					size = qs.get(i).size();
					roadIndex = i;
				}
			}
			return roadIndex;
		}	
	}
}
