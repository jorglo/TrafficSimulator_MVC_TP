package simulator.view.table;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

@SuppressWarnings("serial")
public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver{
	
	private RoadMap _map;
	
	private String[] _columns = new String[] {
			"Id", "Green", "Queues"
	};
	
	public JunctionsTableModel(Controller ctrl) {
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
			if(_map.getJunctions() != null){
				return _columns == null ? 0 : _map.getJunctions().size();
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
			s = _map.getJunctions().get(rowIndex).getId();
			break;
		case 1:
			int GreenLightIndex = _map.getJunctions().get(rowIndex).getGreenLightIndex();
			s = (GreenLightIndex != -1) ? GreenLightIndex : "NONE";
			break;
		case 2:
			String text = "";
			String idRoad = "";
			String idVehicle = "";
					
			Iterator<Entry<Road, List<Vehicle>>> it = _map.getJunctions().get(rowIndex).getRoadQueue().entrySet().iterator();
			for (Road r : _map.getRoads()) {
				if(_map.getJunctions().get(rowIndex).getIncomingRoadsList().contains(r)) {
				text += r.getId()+":[";
				while (it.hasNext()) {
					Entry<Road, List<Vehicle>> e = it.next();
					for(Vehicle v : e.getValue())
						idVehicle += v.getId() + ", ";
					text += idVehicle;
				}
				text += "] ";
				}
			}
				
	
			s = text;
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