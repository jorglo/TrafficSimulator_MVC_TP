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

import simulator.control.Controller;
import simulator.view.table.EventsTableModel;
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
		
		//Definimos su tipo y organizacion dentro de la ventana
		viewsPanel.setLayout(new BoxLayout(viewsPanel, BoxLayout.X_AXIS));
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
		mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
			
		//Creamos los componentes
		JPanel eventsView = createViewPanel(new JTable(new EventsTableModel(_ctrl)), "Events");
		JPanel vehiclesView = createViewPanel(new JTable(new VehiclesTableModel(_ctrl)), "Vehicles");
		JPanel roadsView = createViewPanel(new JTable(new RoadsTableModel(_ctrl)), "Roads");
		JPanel junctionsView = createViewPanel(new JTable(new JunctionsTableModel(_ctrl)), "Junctions");
		JPanel mapView = createViewPanel(new MapComponent(_ctrl), "Map");
		JPanel mapByRoadsView = createViewPanel(new MapByRoadComponent(_ctrl), "Map By Roads");
		
		//Definimos sus dimensiones dentro de la ventana
		eventsView.setPreferredSize(new Dimension(500, 200));
		vehiclesView.setPreferredSize(new Dimension(500, 200));
		junctionsView.setPreferredSize(new Dimension(500, 200));
		roadsView.setPreferredSize(new Dimension(500, 200));
		mapView.setPreferredSize(new Dimension(500, 400));
		mapByRoadsView.setPreferredSize(new Dimension(500, 400));
		
		//anadimos los paneles
		this.setContentPane(mainPanel);
		mainPanel.add(new ControlPanel(_ctrl), BorderLayout.PAGE_START);
		mainPanel.add(viewsPanel, BorderLayout.CENTER);
		mainPanel.add(new StatusBar(_ctrl), BorderLayout.PAGE_END);
		viewsPanel.add(tablesPanel);
		viewsPanel.add(mapsPanel);
		//*tablas
		tablesPanel.add(eventsView);
		tablesPanel.add(vehiclesView);
		tablesPanel.add(roadsView);
		tablesPanel.add(junctionsView);
		//*mapas
		mapsPanel.add(mapView);
		mapsPanel.add(mapByRoadsView);
		
		//Opciones de la ventana
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.pack();
		this.setBounds(300, 100, 1200, 700);
		this.setVisible(true);
	}
	
	private JPanel createViewPanel(JComponent c, String title) {
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(new TitledBorder(title));
		p.add(new JScrollPane(c));
		return p;
	}
}
