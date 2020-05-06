package simulator.view.table;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

@SuppressWarnings("serial")
public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver{
		
	private RoadMap _map;
	
	String[] _columns = new String[] {
			"Id", "Location", "Itinerary", "CO2 Class", "Max.Speed", "Speed", "Total CO2", "Distance"
	};
	
	public VehiclesTableModel(Controller ctrl) {
		ctrl.addObserver(this);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public String getColumnName(int col) {
		return _columns[col];
	}

	@Override
	public int getColumnCount() {
		return _columns.length;
	}

	@Override
	public int getRowCount() {
		if(_map != null) {
			if(_map.getVehicles() != null){
				return _columns == null ? 0 : _map.getVehicles().size();
			}
		}
		return 0;
		
	}

	// asi es como se va a cargar la tabla desde el ArrayList
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = _map.getVehicles().get(rowIndex).getId();
			break;
		case 1:
			if(_map.getVehicles().get(rowIndex).getRoad() != null) {
				String idRoad = _map.getVehicles().get(rowIndex).getRoad().getId();
				int location = _map.getVehicles().get(rowIndex).getLocation();
				s = idRoad + ":" + location;
			}
			break;
		case 2:
			String text = "";
			List<Junction> itinerary = _map.getVehicles().get(rowIndex).getItinerary();
			for(Junction j : itinerary) {
				text +=j.getId()+", ";
			}
			s = "["+text+"]";
			break;
		case 3:
			s = _map.getVehicles().get(rowIndex).getCO2();
			break;
		case 4:
			s = _map.getVehicles().get(rowIndex).getMaxSpeed();
			break;
		case 5:
			s = _map.getVehicles().get(rowIndex).getCurrentSpeed();
			break;
		case 6:
			s = _map.getVehicles().get(rowIndex).getContaminationWholeWay();
			break;
		case 7:
			s = _map.getVehicles().get(rowIndex).getTotalTravelledDistance();
			break;
		}
		return s;
	}
	
	public void update(RoadMap map) {
		_map = map;
		//avisamos al JPanel correspondiente el cambio de los datos.
		fireTableDataChanged();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
		
	}

	@Override
	public void onError(String err) {
		
	}
}