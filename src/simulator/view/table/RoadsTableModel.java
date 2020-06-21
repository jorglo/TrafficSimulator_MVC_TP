package simulator.view.table;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

@SuppressWarnings("serial")
public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver{
	
	// atributos
	private RoadMap _map;
	String[] _columns = new String[] {
			"Id", "Length", "weather", "Max. Speed", "Speed Limit", "Total CO2", "CO2 Limit"
	};
	
	//constructor
	public RoadsTableModel(Controller ctrl) {
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
			if(_map.getRoads() != null){
				return _columns == null ? 0 : _map.getRoads().size();
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
			s = _map.getRoads().get(rowIndex).getId();
			break;
		case 1:
			s = _map.getRoads().get(rowIndex).getLength();
			break;
		case 2:
			s = _map.getRoads().get(rowIndex).getWeatherConditions();
			break;
		case 3:
			s = _map.getRoads().get(rowIndex).getMaxSpeed();
			break;
		case 4:
			s = _map.getRoads().get(rowIndex).getCurrentSpeedLimit();
			break;
		case 5:
			s = _map.getRoads().get(rowIndex).getTotalCO2();
			break;
		case 6:
			s = _map.getRoads().get(rowIndex).getCO2Limit();
			break;
		}
		return s;
	}
	
	public void update(RoadMap map) {
		_map = map;
		fireTableDataChanged(); // avisamos al JPanel correspondiente el cambio de los datos.
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