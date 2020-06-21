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

	/**
	 * Metodo para crear la vista.
	 */
	private void initGUI() {
		
		//Instanciamos los paneles contenedores
		JPanel mainPanel = new JPanel(new BorderLayout()); 	// panel principal que contiene a viewsPanel
		JPanel viewsPanel = new JPanel();					// panel de vista que contiene a tablesPanel y mapsPanel	
		JPanel tablesPanel = new JPanel();					// panel de tablas 
		JPanel mapsPanel = new JPanel();					// panel de mapas
		
		//Definimos el tipo de layaut y organizacion que tendran los componentes dentro de cada panel
		viewsPanel.setLayout(new BoxLayout(viewsPanel, BoxLayout.X_AXIS));	// organizacion de izq - der
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));// organizacion de arriba - abajo
		mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));	// organizacion de arriba - abajo
			
		//Creamos los componentes
		//*tablas
		JPanel eventsView = createViewPanel(new JTable(new EventsTableModel(_ctrl)), "Events");
		JPanel vehiclesView = createViewPanel(new JTable(new VehiclesTableModel(_ctrl)), "Vehicles");
		JPanel roadsView = createViewPanel(new JTable(new RoadsTableModel(_ctrl)), "Roads");
		JPanel junctionsView = createViewPanel(new JTable(new JunctionsTableModel(_ctrl)), "Junctions");
		//*mapas
		JPanel mapView = createViewPanel(new MapComponent(_ctrl), "Map");
		JPanel mapByRoadsView = createViewPanel(new MapByRoadComponent(_ctrl), "Map By Roads");
		
		//Definimos las dimensiones de los componentes
		eventsView.setPreferredSize(new Dimension(500, 200));
		vehiclesView.setPreferredSize(new Dimension(500, 200));
		junctionsView.setPreferredSize(new Dimension(500, 200));
		roadsView.setPreferredSize(new Dimension(500, 200));
		mapView.setPreferredSize(new Dimension(500, 400));
		mapByRoadsView.setPreferredSize(new Dimension(500, 400));
		
		//anadimos los paneles
		this.setContentPane(mainPanel);
		//*panel principal
		mainPanel.add(new ControlPanel(_ctrl), BorderLayout.PAGE_START);
		mainPanel.add(viewsPanel, BorderLayout.CENTER);
		mainPanel.add(new StatusBar(_ctrl), BorderLayout.PAGE_END);
		//*vista
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
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); //para que no se cirre la ventana con la x_roja
		this.pack();										//compacta la ventana al menor tamano posible
		this.setBounds(300, 100, 1200, 700);				//dimension por defecto
		this.setVisible(true);								//visibilidad de la pantalla
	}
	
	/**
	 * Metodo para crear los componentes.
	 * @param c
	 * @param title
	 * @return
	 */
	private JPanel createViewPanel(JComponent c, String title) {
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(new TitledBorder(title));
		p.add(new JScrollPane(c));
		return p;
	}
}
