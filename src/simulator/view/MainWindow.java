package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import simulator.view.table.EventsTableModel;
import simulator.control.Controller;
import simulator.view.table.JunctionsTableModel;
import simulator.view.table.RoadsTableModel;
import simulator.view.table.VehiclesTableModel;

public class MainWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private Controller _ctrl;

	public MainWindow(Controller ctrl) {
		super("Traffic Simulator");
		_ctrl = ctrl;
		initGUI();
	}

	private void initGUI() {
		
		//Instanciamos los paneles contenedores
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel viewsPanel = new JPanel();
		JPanel tablesPanel = new JPanel();
		JPanel mapsPanel = new JPanel();
		
		//Creamos los componentes
		JPanel eventsView = createViewPanel(new JTable(new EventsTableModel(_ctrl)), "Events");
		JPanel vehiclesView = createViewPanel(new JTable(new VehiclesTableModel(_ctrl)), "Vehicles");
		JPanel roadsView = createViewPanel(new JTable(new RoadsTableModel(_ctrl)), "Roads");
		JPanel junctionsView = createViewPanel(new JTable(new JunctionsTableModel(_ctrl)), "Junctions");
		JPanel mapView = createViewPanel(new MapComponent(_ctrl), "Map");
		JPanel mapByRoadsView = createViewPanel(new MapComponent(_ctrl), "Map By Roads");
		
		//Definimos su tipo y organizacion dentro de la ventana
		viewsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
		mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
		
		//Definimos sus dimensiones dentro de la ventana
		eventsView.setPreferredSize(new Dimension(500, 200));
		vehiclesView.setPreferredSize(new Dimension(500, 200));
		junctionsView.setPreferredSize(new Dimension(500, 200));
		roadsView.setPreferredSize(new Dimension(500, 200));
		mapView.setPreferredSize(new Dimension(500, 400));
		mapByRoadsView.setPreferredSize(new Dimension(500, 400));
		
		//a�adimos los paneles
		this.setContentPane(mainPanel);
		mainPanel.add(new ControlPanel(_ctrl), BorderLayout.PAGE_START);
		mainPanel.add(viewsPanel, BorderLayout.CENTER);
		mainPanel.add(new StatusBar(_ctrl), BorderLayout.PAGE_END);
		viewsPanel.add(tablesPanel);
		viewsPanel.add(mapsPanel);
		
		tablesPanel.add(eventsView);
		tablesPanel.add(vehiclesView);
		tablesPanel.add(junctionsView);
		tablesPanel.add(roadsView);
		
		mapsPanel.add(mapView);
		mapsPanel.add(mapByRoadsView);
		
		//nos permite mostrar f�cilmente una ventana para la selecci�n de un fichero
		
		
		//Opciones de la ventana
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		
		//ventana flotante para la confirmacion de cerrar la ventana
		
		
	}
	
	private JPanel createViewPanel(JComponent c, String title) {
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(new TitledBorder(title));
		p.add(new JScrollPane(c));
		return p;
	}
}
