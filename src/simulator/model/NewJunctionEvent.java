package simulator.model;

public class NewJunctionEvent extends Event{
	
	private LightSwitchingStrategy _lsStrategy;
	private DequeuingStrategy _dqStrategy;
	private int _xCoor;
	private int _yCoor;

	public NewJunctionEvent(int time, String id, LightSwitchingStrategy
			lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
			super(time);
			_id = id;
			_lsStrategy = lsStrategy;
			_dqStrategy = dqStrategy;
			_xCoor = xCoor;
			_yCoor = yCoor;
	}

	@Override
	void execute(RoadMap map) {
		map.addJunction(new Junction(_id, _lsStrategy, _dqStrategy, _xCoor, _yCoor));
	}
	
	@Override
	public String toString() {
	return "New Junction '" + _id + "'" ;
	}
}
