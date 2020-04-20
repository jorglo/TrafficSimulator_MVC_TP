package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;
import simulator.model.Weather;

public class MapByRoadComponent extends JPanel implements TrafficSimObserver{
	
	private static final long serialVersionUID = 1L;
	
	private static final int _JRADIUS = 10;
	
	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;

	private RoadMap _map;
	private Image _car;
	private List<Junction> j_src = new ArrayList<Junction>();
	
	MapByRoadComponent(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}
	
	private void initGUI() {
		_car = loadImage("car.png");
		setPreferredSize (new Dimension (300, 200));
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			//updatePrefferedSize();
			drawMap(g);
		}
	}
	
	private void drawMap(Graphics g) {
		drawRoads(g);
		drawJunctions(g);
		drawVehicles(g);
	}
	
	private void drawRoads(Graphics g) {
		int i = 0;
		
		for (Road r : _map.getRoads()) {

			// the road goes from (x1,y1) to (x2,y2)
			int x1 = 50;
			int y1 = (i+1)*50;
			int x2 = getWidth()-100;
			int y2 = (i+1)*50;
			
			// set de new position in the map by road.
			r.getSrcJunc().set_xCoorBR(x1);
			r.getSrcJunc().set_yCoorBR(y1);
			r.getDestJunc().set_xCoorBR(x2);
			r.getDestJunc().set_yCoorBR(y2);
			
			//save the SrcJunc in the j_src List
			j_src.add(r.getSrcJunc());
			
			// choose a color for the arrow depending on the traffic light of the road
			Color arrowColor = _RED_LIGHT_COLOR;
			int idx = r.getDestJunc().getGreenLightIndex();
			if (idx != -1 && r.equals(r.getDestJunc().getInRoads().get(idx))) {
				arrowColor = _GREEN_LIGHT_COLOR;
			}

			// choose a color for the road depending on the total contamination, the darker
			// the
			// more contaminated (wrt its co2 limit)
			int roadColorValue = 200 - (int) (200.0 * Math.min(1.0, (double) r.getTotalCO2() / (1.0 + (double) r.getCO2Limit())));
			Color roadColor = new Color(roadColorValue, roadColorValue, roadColorValue);

			// draw line from (x1,y1) to (x2,y2) with arrow of color arrowColor and line of
			// color roadColor. The size of the arrow is 15px length and 5 px width
			drawLineWithArrow(g, x1, y1, x2, y2, 15, 5, roadColor, arrowColor);
			//g.drawLine(x1, y1, x2, y2);
			
			//draw road id
			g.drawString(r.getId(), x1-30, y1);
			
			//draw Weather
			drawWeather(g,x2+40, y1, r.getWeatherConditions());
			
			//draw CO2
			drawCO2(g,x2+60, y2, r);
			
			i++;
		}

	}
	
	private void drawWeather(Graphics g, int x, int y, Weather weather) {
		Image imgWeather = null;
		
		switch (weather) {
		case SUNNY:
			imgWeather = loadImage("sun.png");
			break;
		case CLOUDY:
			imgWeather = loadImage("cloud.png");
			break;
		case RAINY:
			imgWeather = loadImage("rain.png");
			break;
		case WINDY:
			imgWeather = loadImage("wind.png");
			break;
		case STORM:
			imgWeather = loadImage("storm.png");
			break;
		}
		
		g.drawImage(imgWeather, x-30, y-15, 32, 32, this);
		
	}
	
	private void drawCO2(Graphics g, int x, int y, Road r) {
		Image imgC02 = null;
		int A = r.getTotalCO2();
		int B = r.getCO2Limit();
		int C = 0;

		C = ( int ) Math.floor(Math.min(( double ) A/(1.0 + ( double ) B),1.0) / 0.19);
		
		switch (C) {
		case 0:
			imgC02 = loadImage("cont_0.png");
			break;
		case 1:
			imgC02 = loadImage("cont_1.png");
			break;
		case 2:
			imgC02 = loadImage("cont_2.png");
			break;
		case 3:
			imgC02 = loadImage("cont_3.png");
			break;
		case 4:
			imgC02 = loadImage("cont_4.png");
			break;
		case 5:
			imgC02 = loadImage("cont_5.png");
			break;
		}
		
		g.drawImage(imgC02, x-10, y-15, 32, 32, this);
		
	}

	private void drawJunctions(Graphics g) {
		
		for (Junction j : _map.getJunctions()) {

			// (x,y) are the coordinates of the junction
			int x = j.get_xCoorBR();
			int y = j.get_yCoorBR();
			
			if(j_src.contains(j)) {
				// draw a circle with center at (x,y) with radius _JRADIUS
				g.setColor(_JUNCTION_COLOR);
				g.fillOval(x - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
			}
			else {
				// draw a circle with center at (x,y) with radius _JRADIUS
				g.fillOval(x - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);

				// draw the junction's identifier at (x,y)
				if(j.getGreenLightIndex() == 0)
					g.setColor(_GREEN_LIGHT_COLOR);
				else
					g.setColor(_RED_LIGHT_COLOR);
			}
			
			g.drawString(j.getId(), x, y);
		}
	}
	
	//DUDA: no hace el movimiento del coche bien.
	private void drawVehicles(Graphics g) {
		for (Vehicle v : _map.getVehicles()) {
			if (v.getStatus() != VehicleStatus.ARRIVED) {

				// The calculation below compute the coordinate (vX,vY) of the vehicle on the
				// corresponding road. It is calculated relativly to the length of the road, and
				// the location on the vehicle.
				Road r = v.getRoad();
				int x1 = r.getSrcJunc().get_xCoorBR();
				int y1 = r.getSrcJunc().get_yCoorBR();
				int x2 = r.getDestJunc().get_xCoorBR();
				int y2 = r.getDestJunc().get_yCoorBR();
				double roadLength = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
				double alpha = Math.atan(((double) Math.abs(x1 - x2)) / ((double) Math.abs(y1 - y2)));
				double relLoc = roadLength * ((double) v.getLocation()) / ((double) r.getLength());
				//double x = Math.sin(alpha) * relLoc;
				double x = x1 + ( int ) ((x2 - x1) * (( double ) relLoc / ( double ) roadLength));
				double y = Math.cos(alpha) * relLoc;
				int xDir = x1 <= x2 ? 1 : -1;
				int yDir = y1 < y2 ? 1 : -1;

				int vX = x1 + xDir * ((int) x);
				int vY = y1 + yDir * ((int) y);

				// Choose a color for the vehcile's label and background, depending on its
				// contamination class
				int vLabelColor = (int) (25.0 * (10.0 - (double) v.getCO2()));
				g.setColor(new Color(0, vLabelColor, 0));

				// draw an image of a car (with circle as background) and it identifier
				g.drawImage(_car, vX, vY-20, 16, 16, this);
				g.drawString(v.getId(), vX, vY-20);
				
			}
		}
	}
	
//	private void drawVehicles(Graphics g) {
//		for (Vehicle v : _map.getVehicles()) {
//			if (v.getStatus() != VehicleStatus.ARRIVED) {
//
//				// The calculation below compute the coordinate (vX,vY) of the vehicle on the
//				// corresponding road. It is calculated relativly to the length of the road, and
//				// the location on the vehicle.
//				Road r = v.getRoad();
//				int x1 = r.getSrcJunc().get_xCoorBR();
//				int y1 = r.getSrcJunc().get_yCoorBR();
//				int x2 = r.getDestJunc().get_xCoorBR();
//				int y2 = r.getDestJunc().get_yCoorBR();
//				double roadLength = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
//				double alpha = Math.atan(((double) Math.abs(x2 - x1)) / ((double) Math.abs(y2 - y1)));
//				double relLoc = roadLength * ((double) v.getLocation()) / ((double) r.getLength());
//				//double x = Math.sin(alpha) * relLoc;
//				double y = Math.cos(alpha) * relLoc;
//				int xDir = x1 < x2 ? 1 : -1;
//				int yDir = y1 < y2 ? 1 : -1;
//				
//				double A = v.getLocation();
//				double B = roadLength;
//
//				int x = x1 + ( int ) ((x2 - x1) * (( double ) A / ( double ) B));
//				int vX = x1 + xDir * ((int) x);
//				int vY = y1 + yDir * ((int) y);
//
//				// Choose a color for the vehcile's label and background, depending on its
//				// contamination class
//				int vLabelColor = (int) (25.0 * (10.0 - (double) v.getCO2()));
//				g.setColor(new Color(0, vLabelColor, 0));
//
//				// draw an image of a car and it identifier
//				g.drawImage(_car, vX+60, vY, 16, 16, this);
//				g.drawString(v.getId(), vX+60, vY);
//			}
//		}
//	}
	
	// This method draws a line from (x1,y1) to (x2,y2) with an arrow.
	// The arrow is of height h and width w.
	// The last two arguments are the colors of the arrow and the line
	private void drawLineWithArrow(//
			Graphics g, //
			int x1, int y1, //
			int x2, int y2, //
			int w, int h, //
			Color lineColor, Color arrowColor) {

		int dx = x2 - x1, dy = y2 - y1;
		double D = Math.sqrt(dx * dx + dy * dy);
		double xm = D - w, xn = xm, ym = h, yn = -h, x;
		double sin = dy / D, cos = dx / D;

		x = xm * cos - ym * sin + x1;
		ym = xm * sin + ym * cos + y1;
		xm = x;

		x = xn * cos - yn * sin + x1;
		yn = xn * sin + yn * cos + y1;
		xn = x;

		int[] xpoints = { x2, (int) xm, (int) xn };
		int[] ypoints = { y2, (int) ym, (int) yn };

		g.setColor(lineColor);
		g.drawLine(x1, y1, x2, y2);
		g.setColor(arrowColor);
		g.fillPolygon(xpoints, ypoints, 3);
	}
	
	// loads an image from a file
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}

	public void update(RoadMap map) {
		_map = map;
		repaint();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {
	}
	
}
