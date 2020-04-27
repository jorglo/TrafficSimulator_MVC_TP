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

public class ChangeCO2ClassDialog extends JDialog{

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
	private JLabel jlVeicle;
	private JLabel jlCO2Class;
	private JLabel jlTicks;
	
	private JSpinner spinnerVehicle;
	private JSpinner spinnerCO2Class;
	private JSpinner spinnerTicks;

    public ChangeCO2ClassDialog(RoadMap map) {
		super(new JFrame(), "Change CO2 Class", true);
		_map = map;
		initGUI();
	}
    
    private void initGUI() {
    	
    	//Instanciamos los paneles
    	dialogPanel =new JPanel(new BorderLayout());
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
    	
    	//añadimos los componentes de los Layauts
    	addDescription();
    	addFeatures();
    	addButtons();
    	
    	//caracteristicas de la caja dialog
    	this.setContentPane(dialogPanel);
        this.setLocation(300, 200);
        this.setResizable(false);
		this.pack();
		this.setVisible(true);
 
	}

	private void addDescription() {
		jldescription = new JLabel("<html><body> Schedule an event to change the CO2 class of a vehicle after a given"
				+ " <br> numer of simulation ticks from now. </body></html>");
		dialogPanelDescription.add(jldescription);
	}

	private void addFeatures() {
		
		jlVeicle = new JLabel("Vehicle: ");
		spinnerVehicle = new JSpinner(new SpinnerListModel(_map.getVehicles()));		
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
		
		dialogPanelSpinners.add(jlVeicle);
		dialogPanelSpinners.add(spinnerVehicle);
		dialogPanelSpinners.add(jlCO2Class);
		dialogPanelSpinners.add(spinnerCO2Class);
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

	public JSpinner getSpinnerVehicle() {
		return spinnerVehicle;
	}

	public JSpinner getSpinnerCO2Class() {
		return spinnerCO2Class;
	}

	public JSpinner getSpinnerTicks() {
		return spinnerTicks;
	}

	public int getRes() {
		return res;
	}
	
	
	
}






