package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;


//DUDA: StatusBar NO FUNCIONA!! NO ACTUALIZA!!
public class StatusBar extends JPanel implements TrafficSimObserver{
	
	private static final long serialVersionUID = 1L;
	
	private RoadMap _map;
	private List<Event> _events; 
	private int _time;
	
	private JPanel sbPanel;
	private JPanel timePanel;
	private JPanel eventsPanel;
	
	//private JLabel jlTime;
	private JLabel jlEvents;

	public StatusBar(Controller ctrl) {
		ctrl.addObserver(this);
		iniGui();
	}

	private void iniGui() {
		
		//instanciamos los paneles
		this.sbPanel = new JPanel();
		this.timePanel = new JPanel();
		this.eventsPanel = new JPanel();
		
		//declaramos su organizacion dentro del panel contenedor
		this.sbPanel.setLayout(new BoxLayout(this.sbPanel, BoxLayout.X_AXIS));
		this.timePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.eventsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		//añadimos las caracteristicas
    	this.timePanel.setPreferredSize(new Dimension(390, 40));
    	this.eventsPanel.setPreferredSize(new Dimension(390, 40));
		
    	//añadimos los paneles 
    	this.sbPanel.add(timePanel);
    	this.sbPanel.add(eventsPanel);
    	
		//añadimos los componentes de los Layauts
    	addStatusBarTime();
    	addStatusBarEvents();
    	
    	//caracteristicas de la caja dialog
    	this.add(sbPanel);
		this.setVisible(true);
	}

	private void addStatusBarTime() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JLabel jlTime = new JLabel();
				jlTime.setText("Time: " + _time);
				timePanel.add(jlTime);	
			}
		});
	}

	private void addStatusBarEvents() {
		this.jlEvents = new JLabel();
		this.jlEvents.setText("Event added("+_events.toString()+")");
		this.eventsPanel.add(jlEvents);
	}

	public void update(RoadMap map, List<Event> events, int time) {
		_map = map;
		_events = events;
		_time += time;
		if(this.timePanel != null)
			addStatusBarTime();
		if(this.eventsPanel != null)
			addStatusBarEvents();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		update(map, events, time);
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map, events, time); 
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map, events, time);
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map, events, time);
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map, events, time);
		
	}

	@Override
	public void onError(String err) {
		// TODO onError en StatusBar
		
	}

}
