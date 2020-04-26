package simulator.view.dialog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetWeatherEvent;
import simulator.model.RoadMap;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	
	private Controller _ctrl;
	private RoadMap _map;
	private int _ticks;
	private int _time;

	private JPanel dialogPanel;
	private JPanel dialogPanelDescription;
	private JPanel dialogPanelFeatures;
	private JPanel dialogPanelButtons;
	
	private JLabel jldescription;
	private JLabel jldescription2;
	private JLabel jlRoad;
	private JLabel jlweather;
	private JLabel jlTicks;
	
	private JSpinner spinnerRoad;
	private JSpinner spinnerWeather;
	private JSpinner spinnerTicks;
	
    public ChangeWeatherDialog(Controller ctrl, RoadMap map, List<Event> events, int time) {
    	_ctrl = ctrl;
    	_map = map;
    	_time = time;
    	initGUI();     
    }

    private void initGUI() {
    	
    	//Instanciamos los paneles
    	dialogPanel = new JPanel();
    	dialogPanelDescription = new JPanel();
    	dialogPanelFeatures = new JPanel();
    	dialogPanelButtons = new JPanel();
    	
    	//declaramos su organizacion dentro de la ventana
    	dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
    	dialogPanelDescription.setLayout(new FlowLayout(FlowLayout.LEFT));
    	dialogPanelFeatures.setLayout(new FlowLayout(FlowLayout.LEFT));
    	dialogPanelButtons.setLayout(new FlowLayout(FlowLayout.CENTER));
    	
    	//a�adimos las caracteristicas
    	dialogPanelDescription.setPreferredSize(new Dimension(390, 55));
    	dialogPanelFeatures.setPreferredSize(new Dimension(390, 40));
    	dialogPanelButtons.setPreferredSize(new Dimension(390, 40));
    	
    	//a�adimos los paneles 
    	this.setContentPane(dialogPanel);
    	dialogPanel.add(dialogPanelDescription);
    	dialogPanel.add(dialogPanelFeatures);
    	dialogPanel.add(dialogPanelButtons);
    	
    	//a�adimos los componentes de los Layauts
    	addDescription();
    	addFeatures();
    	addButtons();
    	
    	//caracteristicas de la caja dialog
        setTitle("Change Weather Class");
        setLocation(300, 200);
        setResizable(false);
		pack();
		setVisible(true);
	}

	private void addDescription() {
		String text1 = "Schedule an event to change the weather of a road after a given number";
		String text2 = "of simulation ticks from now.";
		jldescription = new JLabel(text1);
		jldescription2 = new JLabel(text2);
		dialogPanelDescription.add(jldescription);
		dialogPanelDescription.add(jldescription2);
	}

	private void addFeatures() {
		
		jlRoad = new JLabel("Road: ");
		spinnerRoad = new JSpinner(new SpinnerListModel(_map.getRoads()));
		spinnerRoad.setToolTipText("Road");
		spinnerRoad.setPreferredSize(new Dimension(50, 20));

		jlweather = new JLabel("Weather: ");
		spinnerWeather = new JSpinner(new SpinnerListModel(Weather.values()));
		spinnerWeather.setToolTipText("Weather");
		spinnerWeather.setPreferredSize(new Dimension(70, 20));
		
		jlTicks = new JLabel("Ticks: ");
		spinnerTicks= new JSpinner(new SpinnerNumberModel(1, 1, 100,1));
		spinnerTicks.setToolTipText("Ticks");
		spinnerTicks.setPreferredSize(new Dimension(50, 20));
		
		dialogPanelFeatures.add(jlRoad);
		dialogPanelFeatures.add(spinnerRoad);
		dialogPanelFeatures.add(jlweather);
		dialogPanelFeatures.add(spinnerWeather);
		dialogPanelFeatures.add(jlTicks);
		dialogPanelFeatures.add(spinnerTicks);
	}
	
	private void addButtons() {
		
		//CANCEL
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
            dispose();
        	}
        });
        cancelButton.setActionCommand("CANCEL");
        dialogPanelButtons.add(cancelButton);
		
        //OK
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	
        	List<Pair<String, Weather>> contClass = new ArrayList<Pair<String,Weather>>();
    		String first;
    		Weather second;
    		Pair<String, Weather> coche = null;
    			
    			first = spinnerRoad.getValue().toString();
    			second = (Weather) spinnerWeather.getValue();
    			
    			coche = new Pair<String, Weather>(first, second);
 
    			contClass.add(coche);
    			
    			_ticks = (int)spinnerTicks.getValue();
    			int newTime = _time + _ticks;
    			
        	_ctrl.addEvent(new NewSetWeatherEvent(newTime, contClass));
        	dispose();
        	}
        });
        
        okButton.setActionCommand("OK");
        dialogPanelButtons.add(okButton);
        getRootPane().setDefaultButton(okButton);

	}

}