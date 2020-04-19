package simulator.view;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver{
	
	private static final long serialVersionUID = 1L;
	
	private RoadMap _map;
	private List<Event> _events; 
	private int _time;
	
	JLabel statusBarText;

	public StatusBar(Controller _ctrl) {
		iniGui();
	}

	private void iniGui() {
		addStatusBar();
	}

	private void addStatusBar() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				statusBarText = new JLabel("Welcome to the simulator!");
			}
		});
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO onAdvanceStart en StatusBar
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO onAdvanceEnd en StatusBar
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO onEventAdded en StatusBar
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO onReset en StatusBar
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO onRegister en StatusBar
		
	}

	@Override
	public void onError(String err) {
		// TODO onError en StatusBar
		
	}

}
