package simulator.view.table;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver{
	
	private static final long serialVersionUID = 1L;

	private List<Event> _events; 
	
	private String[] _columns = new String[] {
			"Time", "Desc." 
	};

	public EventsTableModel(Controller ctrl) {
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
		if(_events != null) {
			if(_events != null){
				return _columns == null ? 0 : _events.size();
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
			s = _events.get(rowIndex).getTime();
			break;
		case 1:
			s = _events.get(rowIndex).toString();
			break;
		}

		return s;
	}
	
	public void update(List<Event> events) {
		_events = events;
		//avisamos al JPanel correspondiente el cambio de los datos.
		fireTableDataChanged();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(events);
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(events);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(events);	
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(events);
	}

	@Override
	public void onError(String err) {
		
	}

}
