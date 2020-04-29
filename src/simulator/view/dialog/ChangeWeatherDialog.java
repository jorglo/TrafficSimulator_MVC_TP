package simulator.view.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;

import simulator.model.RoadMap;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	
	private RoadMap _map;

	public static final int OK = 0;
	public static final int CANCEL = 1;
	private int res = -1;
	
	private JPanel dialogPanel;
	private JPanel dialogPanelDescription;
	private JPanel dialogPanelSpinners;
	private JPanel dialogPanelButtons;
	
	private JLabel jldescription;
	private JLabel jlRoad;
	private JLabel jlweather;
	private JLabel jlTicks;
	
	private JSpinner spinnerRoad;
	private JSpinner spinnerWeather;
	private JSpinner spinnerTicks;
	
    public ChangeWeatherDialog(RoadMap map) {
		super(new JFrame(), "Change CO2 Class", true);
		_map = map;
		initGUI();
    }

    private void initGUI() {
    	
    	//Instanciamos los paneles
    	dialogPanel = new JPanel();
    	dialogPanelDescription = new JPanel();
    	dialogPanelSpinners = new JPanel();
    	dialogPanelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
    	
    	//añadimos los paneles internos
    	dialogPanel.add(dialogPanelDescription,BorderLayout.NORTH);
    	dialogPanel.add(dialogPanelSpinners,BorderLayout.CENTER);
    	dialogPanel.add(dialogPanelButtons,BorderLayout.SOUTH);
    	
    	//anadimos las caracteristicas
    	dialogPanelDescription.setPreferredSize(new Dimension(420, 55));
    	dialogPanelSpinners.setPreferredSize(new Dimension(390, 40));
    	dialogPanelButtons.setPreferredSize(new Dimension(390, 40));
    	
    	//aï¿½adimos los componentes de los Layauts
    	addDescription();
    	addFeatures();
    	addButtons();
    	
    	//caracteristicas de la caja dialog
        this.setContentPane(dialogPanel);
        setLocation(300, 200);
        setResizable(false);
		pack();
		setVisible(true);
	}

	private void addDescription() {
		jldescription = new JLabel("<html><body> Schedule an event to change the weather of a road after a given"
				+ " <br> number of simulation ticks from now. </body></html>");
		dialogPanelDescription.add(jldescription);
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
		
		dialogPanelSpinners.add(jlRoad);
		dialogPanelSpinners.add(spinnerRoad);
		dialogPanelSpinners.add(jlweather);
		dialogPanelSpinners.add(spinnerWeather);
		dialogPanelSpinners.add(jlTicks);
		dialogPanelSpinners.add(spinnerTicks);
	}
	
	private void addButtons() {
		
		//CANCEL
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
        	res = CANCEL;
            dispose();
        	}
        });
        cancelButton.setActionCommand("CANCEL");
        dialogPanelButtons.add(cancelButton);
        getRootPane().setDefaultButton(cancelButton);
        
        //OK
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	res = OK;
        	dispose();
        	}
        });
        
        okButton.setActionCommand("OK");
        dialogPanelButtons.add(okButton);
	}
	
	public JSpinner getSpinnerRoad() {
		return spinnerRoad;
	}

	public JSpinner getSpinnerWeather() {
		return spinnerWeather;
	}

	public JSpinner getSpinnerTicks() {
		return spinnerTicks;
	}

	public int getRes() {
		return res;
	}

}