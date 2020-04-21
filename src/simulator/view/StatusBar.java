package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;


//DUDA: Bien implementada??
public class StatusBar extends JPanel implements TrafficSimObserver{
	
	private static final long serialVersionUID = 1L;
	
	private JPanel sbPanel;
	private JPanel timePanel;
	private JPanel eventsPanel;
	
	private JLabel jlTime;
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
		this.sbPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.timePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.eventsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		//añadimos las caracteristicas
		this.sbPanel.setPreferredSize(new Dimension(1180, 30));
    	this.timePanel.setPreferredSize(new Dimension(200, 25));
    	this.eventsPanel.setPreferredSize(new Dimension(390, 25));
		
    	//añadimos los paneles 
    	this.sbPanel.add(timePanel);
    	this.sbPanel.add(eventsPanel);
    	
		//añadimos los componentes de los Layauts
    	addStatusBarTime();
    	addStatusBarEvents();
    	
    	//caracteristicas de la caja dialog
    	this.add(sbPanel);
    	//sbPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    	//sbPanel.setBackground(Color.green);
    	//etBackground(Color.blue);
		this.setVisible(true);
	}

	private void addStatusBarTime() {
		this.jlTime = new JLabel();
		timePanel.add(jlTime);	
		timePanel.setAlignmentX(LEFT_ALIGNMENT);
		//timePanel.setBackground(Color.gray);
	}

	private void addStatusBarEvents() {
		this.jlEvents = new JLabel();
		this.eventsPanel.add(jlEvents);
		//eventsPanel.setBackground(Color.white);
	}

	public void update(List<Event> events, int time) {
		if(this.jlTime != null && this.jlEvents != null) {
			this.jlTime.setText("Time: " + time);
			for (Event event : events) {
				if(event.getTime() == time) {
					this.jlEvents.setText("Event added("+event.toString()+")");
				}
			}
		}	
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		update(events, time);
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(events, time); 

	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(events, time);
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(events, time);
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(events, time);
		
	}

	@Override
	public void onError(String err) {
		// TODO onError en StatusBar
		
	}

}
