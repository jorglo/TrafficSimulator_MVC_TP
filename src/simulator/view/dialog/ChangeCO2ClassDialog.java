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

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.factories.SetContClassEventBuilder;
import simulator.factories.SetWeatherEventBuilder;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;
import simulator.model.RoadMap;

public class ChangeCO2ClassDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	
	private Controller _ctrl;
	private RoadMap _map;
	private List<Event> _events;
	private int _ticks;
	private int _time;

	private JPanel dialogPanel;
	private JPanel dialogPanelDescription;
	private JPanel dialogPanelFeatures;
	private JPanel dialogPanelButtons;
	
	private JLabel jldescription;
	private JLabel jldescription2;
	private JLabel jlVeicle;
	private JLabel jlCO2Class;
	private JLabel jlTicks;
	
	private JSpinner spinnerVehicle;
	private JSpinner spinnerCO2Class;
	private JSpinner spinnerTicks;
	
    public ChangeCO2ClassDialog(Controller ctrl, RoadMap map, List<Event> events, int time) {
    	_ctrl = ctrl;
    	_map = map;
    	_events = events;
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
    	
    	//añadimos las caracteristicas
    	dialogPanelDescription.setPreferredSize(new Dimension(390, 55));
    	dialogPanelFeatures.setPreferredSize(new Dimension(390, 40));
    	dialogPanelButtons.setPreferredSize(new Dimension(390, 40));
    	
    	//añadimos los paneles 
    	this.setContentPane(dialogPanel);
    	dialogPanel.add(dialogPanelDescription);
    	dialogPanel.add(dialogPanelFeatures);
    	dialogPanel.add(dialogPanelButtons);
    	
    	//añadimos los componentes de los Layauts
    	addDescription();
    	addFeatures();
    	addButtons();
    	
    	//caracteristicas de la caja dialog
        setTitle("Change CO2 Class");
        setLocation(300, 200);
        setResizable(false);
		pack();
		setVisible(true);
 
	}

	private void addDescription() {
		String text1 = "Schedule an event to change the CO2 class of a vehicle after a given";
		String text2 = "numer of simulation ticks from now.";
		jldescription = new JLabel(text1);
		jldescription2 = new JLabel(text2);
		dialogPanelDescription.add(jldescription);
		dialogPanelDescription.add(jldescription2);
	}

	private void addFeatures() {
		
		jlVeicle = new JLabel("Vehicle: ");
				
		spinnerVehicle = (_map.getVehicles().size()==0) 
				? new JSpinner() 
				: new JSpinner(new SpinnerListModel(_map.getVehicles()));
		spinnerVehicle.setToolTipText("Vehicle");
		spinnerVehicle.setPreferredSize(new Dimension(80, 20));

		jlCO2Class = new JLabel("CO2 Class: ");
		spinnerCO2Class= new JSpinner(new SpinnerNumberModel(0, 0, 10,1));
		spinnerCO2Class.setToolTipText("CO2");
		spinnerCO2Class.setPreferredSize(new Dimension(60, 20));
		
		jlTicks = new JLabel("Ticks: ");
		spinnerTicks= new JSpinner(new SpinnerNumberModel(1, 1, 1000,1));
		spinnerTicks.setToolTipText("Ticks");
		spinnerTicks.setPreferredSize(new Dimension(60, 20));
		
		dialogPanelFeatures.add(jlVeicle);
		dialogPanelFeatures.add(spinnerVehicle);
		dialogPanelFeatures.add(jlCO2Class);
		dialogPanelFeatures.add(spinnerCO2Class);
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
        	//DUDA: evento de ChangeCO2ClassDialog
    
        	List<Pair<String, Integer>> contClass = new ArrayList<Pair<String,Integer>>();
    		String first;
    		Integer second;
    		Pair<String, Integer> coche = null;
    			
    			first = spinnerVehicle.getValue().toString();
    			second = (int) spinnerCO2Class.getValue();
    			
    			coche = new Pair<String, Integer>(first, second);
 
    			contClass.add(coche);
    			
    			_ticks = (int)spinnerTicks.getValue();
    			int newTime = _time + _ticks;
    			
        	_ctrl.addEvent(new NewSetContClassEvent(newTime, contClass));
        	}
        });
        okButton.setActionCommand("OK");
        dialogPanelButtons.add(okButton);
        getRootPane().setDefaultButton(okButton);

	}

	/* 
    * PARA PROBAR EL DIALOGO SIN PROBAR TODA LA APLICACION COMPLETA 
    */
//	public static void main(String[] args) {
//        try {
//        	ChangeCO2ClassDialog dialog = new ChangeCO2ClassDialog();
//            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//            dialog.setVisible(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}






