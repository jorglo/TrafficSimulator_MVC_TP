package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.view.dialog.ChangeCO2ClassDialog;
import simulator.view.dialog.ChangeWeatherDialog;

public class ControlPanel extends JPanel implements TrafficSimObserver, ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	private Controller _ctrl;
	private boolean _stopped;
	private int _steps;
	
	//JTOOLBAR
	private JToolBar toolBar; 
	private JFileChooser fc; 
	private JButton openButton;
	private JButton co2classButton; 
	private JButton weatherButton; 
	private JButton runButton; 
	private JButton stopButton; 
	private JButton quitButton; 
	private JTextArea eventsEditor; // editor de eventos 
	private JSpinner stepsSpinner;
	private JSpinner delaySpinner;
	private JTextField timeViewer;
	
	//ACTIONS LISTENERS
	private final String OPEN = "open";
	private final String CO2CLASS = "co2class";
	private final String WEATHER = "weather";
	private final String RUN = "run";
	private final String STOP = "stop";
	private final String QUIT = "quit";
	private final String REDIRECT_OUTPUT = "redirect_output";
	private final String GENERATE = "generate";
	private final String CLEAR_REPORTS = "clear_reports";
	private final String TIME_SIM = "time_sim";
	
	public ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
	}
	
	//BOTONERA
	private void addToolBar() {
		toolBar = new JToolBar();
		toolBar.setFloatable(false);

		// Creamos los botones con sus imagenes y caracteristicas
		openButton = new JButton(new ImageIcon("icons/open.png"));
		openButton.setActionCommand(OPEN);
		openButton.setToolTipText("Open a file");
		openButton.addActionListener(this);
		
		co2classButton = new JButton(new ImageIcon("icons/co2class.png"));
		co2classButton.setActionCommand(CO2CLASS);
		co2classButton.setToolTipText("co2class");
		co2classButton.addActionListener(this);
		
		weatherButton = new JButton(new ImageIcon("icons/weather.png"));
		weatherButton.setActionCommand(WEATHER);
		weatherButton.setToolTipText("weather");
		weatherButton.addActionListener(this);
		
		runButton = new JButton(new ImageIcon("icons/run.png"));
		runButton.setActionCommand(RUN);
		runButton.setToolTipText("Run");
		runButton.addActionListener(this);
		
		stopButton = new JButton(new ImageIcon("icons/stop.png"));
		stopButton.setActionCommand(STOP);
		stopButton.setToolTipText("Stop");
		stopButton.addActionListener(this);
		
		delaySpinner = new JSpinner(new SpinnerNumberModel(1000, 1, 1000000,1));
		JLabel delayLabel = new JLabel("Delay: ");
		delaySpinner.setToolTipText("Delay");
		delaySpinner.setPreferredSize(new Dimension(60, 30));
		
		stepsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100,1));
		JLabel stepsLabel = new JLabel("Steps: ");
		stepsSpinner.setToolTipText("Steps");
		stepsSpinner.setPreferredSize(new Dimension(60, 30));
		_steps = (int) stepsSpinner.getValue();
		
		timeViewer = new JTextField(5);
		JLabel timeLabel = new JLabel("Time: ");
		timeViewer.setToolTipText("Time");
		timeViewer.setPreferredSize(new Dimension(100, 30));
		timeViewer.setEditable(false);
		timeViewer.addActionListener(this);
		
		quitButton = new JButton(new ImageIcon("icons/exit.png"));
		quitButton.setActionCommand(QUIT);
		quitButton.setToolTipText("Quit");
		quitButton.addActionListener(this);
		
		// añadimos los botones al panel
		toolBar.add(openButton);
		toolBar.add(co2classButton);
		toolBar.add(weatherButton);
		toolBar.add(runButton);
		toolBar.add(stopButton);
		toolBar.add(delayLabel);
		toolBar.add(delaySpinner);
		toolBar.add(stepsLabel);
		toolBar.add(stepsSpinner);
		toolBar.add(timeLabel);
		toolBar.add(timeViewer);
		toolBar.add(quitButton);
		
		// le indicamos la posicion
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));

		// -------------------------------------------------
		// *SU FUNCIONALIDAD DE DEFINE EN actionPerformed!!
		// -------------------------------------------------
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
		else if (RUN.equals(e.getActionCommand()))
			run_sim(_steps);
		else if (STOP.equals(e.getActionCommand()))
			stop();
		else if (QUIT.equals(e.getActionCommand()))
			System.out.println("cerrar ventana");
	}
	
	/**CARGAR FICHERO*/
	private void OpenFile() {
		InputStream inputStream = null;
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
				inputStream = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			_ctrl.reset();
			_ctrl.loadEvents(inputStream);
		}
	}
	
	/**CONTAMINACION*/
	private void co2class() {
		try {
        	ChangeCO2ClassDialog dialog = new ChangeCO2ClassDialog();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	/**CONDICIONES METEOROLOGICAS*/
	private void weather() {
		try {
			ChangeWeatherDialog dialog = new ChangeWeatherDialog();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	/**EJECUTAR SIMULACION*/
	private void run_sim( int n ) {
		if ( n > 0 && ! _stopped ) {
			try {
				_ctrl.run(1);
			} catch (Exception e ) {
				// TODO show error message
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
		
	private void enableToolBar(boolean enable) {
		
		
	}

	/**PARAR LA SIMULACION*/
	private void stop() {
		_stopped = true ;
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
