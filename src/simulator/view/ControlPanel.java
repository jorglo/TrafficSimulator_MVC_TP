package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;
import simulator.model.NewSetWeatherEvent;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Weather;
import simulator.view.dialog.ChangeCO2ClassDialog;
import simulator.view.dialog.ChangeWeatherDialog;

public class ControlPanel extends JPanel implements TrafficSimObserver, ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	private Controller _ctrl;
	private boolean _stopped;
	private RoadMap _map;
	private int _ticks;
	private int _time;
	
	private JPanel jpToolBar;
	private JPanel jpToolBarQuit;
	
	//JTOOLBAR
	private JToolBar toolBar1; 
	private JToolBar toolBar2;
	private JFileChooser fc; 
	private JButton openButton;
	private JButton co2classButton; 
	private JButton weatherButton; 
	private JButton runButton; 
	private JButton stopButton; 
	private JButton quitButton; 
	private JSpinner ticksSpinner;
	
	//ACTIONS LISTENERS
	private final String OPEN = "open";
	private final String CO2CLASS = "co2class";
	private final String WEATHER = "weather";
	private final String RUN = "run";
	private final String STOP = "stop";
	private final String QUIT = "quit";
	
	public ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		_ctrl.addObserver(this);
		fc = new JFileChooser();
		initGUI();
	}
	
	public void initGUI() {
		
		//instanciamos el panel
		jpToolBar = new JPanel(); // Open, CO2class, Weather, Run, Stop y Ticks
		jpToolBarQuit = new JPanel(); // Quit
		
		//declaramos su organizacion dentro del panel contenedor
		jpToolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		jpToolBarQuit.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		//anadimos las caracteristicas
		jpToolBar.setPreferredSize(new Dimension(1100, 50));
		
		// anadimos los componentes
		addToolBar();
		
		// anadimos paneles
		this.add(jpToolBar);
		this.add(jpToolBarQuit);
		this.setVisible(true);
	}

	public void addToolBar() {
		toolBar1 = new JToolBar();
		toolBar1.setFloatable(true);
		toolBar2 = new JToolBar();
		toolBar2.setFloatable(true);

		// Creamos los botones con sus imagenes y caracteristicas
		openButton = new JButton(new ImageIcon("icons/open.png"));
		openButton.setActionCommand(OPEN);
		openButton.setToolTipText("Open a file");
		openButton.addActionListener(this);
		
		co2classButton = new JButton(new ImageIcon("icons/co2class.png"));
		co2classButton.setActionCommand(CO2CLASS);
		co2classButton.setToolTipText("CO2class");
		co2classButton.addActionListener(this);
		
		weatherButton = new JButton(new ImageIcon("icons/weather.png"));
		weatherButton.setActionCommand(WEATHER);
		weatherButton.setToolTipText("Weather");
		weatherButton.addActionListener(this);
		
		runButton = new JButton(new ImageIcon("icons/run.png"));
		runButton.setActionCommand(RUN);
		runButton.setToolTipText("Run");
		runButton.addActionListener(this);
		
		stopButton = new JButton(new ImageIcon("icons/stop.png"));
		stopButton.setActionCommand(STOP);
		stopButton.setToolTipText("Stop");
		stopButton.addActionListener(this);
		
		ticksSpinner = new JSpinner(new SpinnerNumberModel(10, 10, 1000,1));
		JLabel stepsLabel = new JLabel("Ticks: ");
		ticksSpinner.setToolTipText("Ticks");
		ticksSpinner.setPreferredSize(new Dimension(60, 30));
		_ticks = (int) ticksSpinner.getValue();
		
		quitButton = new JButton(new ImageIcon("icons/exit.png"));
		quitButton.setActionCommand(QUIT);
		quitButton.setToolTipText("Quit");
		quitButton.addActionListener(this);
		
		// a�adimos los botones al panel
		toolBar1.add(openButton);
		toolBar1.add(co2classButton);
		toolBar1.add(weatherButton);
		toolBar1.add(runButton);
		toolBar1.add(stopButton);
		toolBar1.add(stepsLabel);
		toolBar1.add(ticksSpinner);
		toolBar2.add(quitButton);
	
		jpToolBar.add(toolBar1);
		jpToolBarQuit.add(toolBar2);

		// ------------------------------------------------- //
		// *SU FUNCIONALIDAD DE DEFINE EN actionPerformed!!  //
		// ------------------------------------------------- //
	}
	

	//LISTA DE ACCIONES
	@Override
	public void actionPerformed(ActionEvent e) {
		if (OPEN.equals(e.getActionCommand()))
			OpenFile();
		else if (CO2CLASS.equals(e.getActionCommand()))
			co2class();
		else if (WEATHER.equals(e.getActionCommand()))
			weather();
		else if (RUN.equals(e.getActionCommand())) {
			_ticks = (int) ticksSpinner.getValue();
			run_sim(_ticks); 
			_stopped = false;
		}
		else if (STOP.equals(e.getActionCommand()))
			stop();
		else if (QUIT.equals(e.getActionCommand()))
			quit();
	}
	
	/** Medoto para CARGAR FICHERO*/
	private void OpenFile() {
		InputStream inputStream = null;
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
				inputStream = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			_ctrl.reset();
			_ctrl.loadEvents(inputStream);
		}
	}
	
	/** Metodo para cambiar en CO2*/
	private void co2class() {
		if(_map.getVehicles().size()!=0) {
			
			ChangeCO2ClassDialog dialogCO2 = new ChangeCO2ClassDialog(_map);
			
			if(dialogCO2.getRes() == 0) {
				List<Pair<String, Integer>> contClass = new ArrayList<Pair<String,Integer>>();
    			
	    		String first = dialogCO2.getSpinnerVehicle().getValue().toString();
	    		Integer	second = (int) dialogCO2.getSpinnerCO2Class().getValue();
	    			
	    		Pair<String, Integer> coche = new Pair<String, Integer>(first, second);
	 
	    		contClass.add(coche);
	    			
	    		int newTime = _time + (int) dialogCO2.getSpinnerTicks().getValue();
	    		
	        	_ctrl.addEvent(new NewSetContClassEvent(newTime, contClass));
			}
   
		}
	}
	
	/** Metodo para cabiar las CONDICIONES METEOROLOGICAS*/
	private void weather() {
		if(_map.getVehicles().size()!=0) {
			ChangeWeatherDialog dialogWeather = new ChangeWeatherDialog(_map);
			
			List<Pair<String, Weather>> contClass = new ArrayList<Pair<String,Weather>>();
    		
    		String first = dialogWeather.getSpinnerRoad().getValue().toString();
    		Weather second = (Weather) dialogWeather.getSpinnerWeather().getValue();
    			
    		Pair<String, Weather> coche = new Pair<String, Weather>(first, second);
 
			contClass.add(coche);
			
			int newTime = _time + (int)dialogWeather.getSpinnerTicks().getValue();
    		
        	_ctrl.addEvent(new NewSetWeatherEvent(newTime, contClass));
		}
	}
	
	/** Metodo para EJECUTAR SIMULACION*/
	private void run_sim( int n ) {
		if ( n > 0 && ! _stopped ) {
			try {
				_ctrl.run(1);
				enableToolBar( false );
			} catch (Exception e ) {
				_stopped = true ;
				return ;
			}
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					run_sim( n - 1);
				}
			});
		} else {
			enableToolBar( true );
			_stopped = true ;
		}
	}
		
	// APAGADO Y ENCENDIDO DE LA VISTA
	private void enableToolBar(boolean enable) {
		openButton.setEnabled(enable);
		co2classButton.setEnabled(enable);
		weatherButton.setEnabled(enable);
		runButton.setEnabled(enable);
		quitButton.setEnabled(enable);
	}

	/** Metodo para DETENER LA SIMULACION*/
	private void stop() {
		_stopped = true ;
	}
	
	/** Metodo para CERRAR LA APLICACION*/
	private void quit() {
		int opt = JOptionPane.showConfirmDialog(this, "Estas seguro que quieres salir?", "salir", JOptionPane.YES_NO_OPTION);
		if(opt == 0) 
			System.exit(0);
	}
	
	public void update(RoadMap map, int time) {
		_map = map;
		_time = time;
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		update(map, time);
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map, time);
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map, time);
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map, time);
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map, time);
		
	}

	@Override
	public void onError(String err) {
		
	}

}



