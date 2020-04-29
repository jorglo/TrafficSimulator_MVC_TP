package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;
import simulator.model.Weather;

public class MapByRoadComponent extends JPanel implements TrafficSimObserver{
	
	private static final long serialVersionUID = 1L;
	
	private static final int _JRADIUS = 10;
	
	private static final Color _String_ROAD_COLOR = Color.BLACK;
	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;

	private RoadMap _map;
	private Image _car;
	
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
			drawMap(g);
		}
	}
	
	private void drawMap(Graphics g) {
		drawRoads_and_Junctions(g);
	}
	
	private void drawRoads_and_Junctions(Graphics g) {
		int i = 0;
		
		for (Road r : _map.getRoads()) {

			// the road goes from (x1,y1) to (x2,y2)
			int x1 = 50;
			int y1 = (i+1)*50;
			int x2 = getWidth()-100;
			int y2 = (i+1)*50;
			
			// draw a circle with center at (x,y) with radius _JRADIUS
			g.setColor(_JUNCTION_COLOR);
			g.fillOval(x1 - _JRADIUS / 2, y1 - _JRADIUS / 2, _JRADIUS, _JRADIUS);
			
			// draw the junction's identifier at (x2,y2)
			int idx = r.getDestJunc().getGreenLightIndex();
			if (idx != -1 && r.equals(r.getDestJunc().getInRoads().get(idx)))
				g.setColor(_GREEN_LIGHT_COLOR);
			else
				g.setColor(_RED_LIGHT_COLOR);
			
			// draw a circle with center at (x,y) with radius _JRADIUS
			g.fillOval(x2 - _JRADIUS / 2, y2 - _JRADIUS / 2, _JRADIUS, _JRADIUS);

			// choose a color for the road depending on the total contamination, the darker
			// the more contaminated (wrt its co2 limit)
			int roadColorValue = 200 - (int) (200.0 * Math.min(1.0, (double) r.getTotalCO2() / (1.0 + (double) r.getCO2Limit())));
			Color roadColor = new Color(roadColorValue, roadColorValue, roadColorValue);
			g.setColor(roadColor);
	
			//draw line road
			g.drawLine(x1, y1, x2, y2);
			
			//draw road id
			g.setColor(_String_ROAD_COLOR);
			g.drawString(r.getId(), x1-30, y1);
			
			//draw Weather
			drawWeather(g,x2+40, y1, r.getWeatherConditions());
			
			//draw CO2
			drawCO2(g,x2+60, y2, r);
			
			drawVehicles(g, r, x1, y1, x2, y2);
			
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
	
	private void drawVehicles(Graphics g, Road r2, int x1, int y1, int x2, int y2) {
		for (Vehicle v : _map.getVehicles()) {
			if (v.getStatus() != VehicleStatus.ARRIVED) {

				// The calculation below compute the coordinate (vX,vY) of the vehicle on the
				// corresponding road. It is calculated relativly to the length of the road, and
				// the location on the vehicle.
				Road r = v.getRoad();
				if(r == r2) {
					double roadLength = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
					double alpha = Math.atan(((double) Math.abs(x1 - x2)) / ((double) Math.abs(y1 - y2)));
					double relLoc = roadLength * ((double) v.getLocation()) / ((double) r.getLength());
					//double x = Math.sin(alpha) * relLoc;
					double x = x1 + ( int ) ((x2 - x1) * (( double ) relLoc / ( double ) roadLength));
					double y = Math.cos(alpha) * relLoc;
					int xDir = x1 < x2 ? 1 : -1;
					int yDir = y1 < y2 ? 1 : -1;

					int vX = x1 + xDir * ((int) x);
					int vY = y1 + yDir * ((int) y);

					// Choose a color for the vehcile's label and background, depending on its
					// contamination class
					int vLabelColor = (int) (25.0 * (10.0 - (double) v.getCO2()));
					g.setColor(new Color(0, vLabelColor, 0));

					// draw an image of a car (with circle as background) and it identifier
					g.drawImage(_car, vX - 60, vY-11, 16, 16, this);
					g.drawString(v.getId(), vX - 60, vY-11);
					
				}
			}
		}
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
