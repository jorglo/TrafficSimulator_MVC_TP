package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import simulator.view.MainWindow;
import simulator.control.Controller;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.MostCrowdedStrategyBuilder;
import simulator.factories.MoveAllStrategyBuilder;
import simulator.factories.MoveFirstStrategyBuilder;
import simulator.factories.NewCityRoadEventBuilder;
import simulator.factories.NewInterCityRoadEventBuilder;
import simulator.factories.NewJunctionEventBuilder;
import simulator.factories.NewVehicleEventBuilder;
import simulator.factories.RoundRobinStrategyBuilder;
import simulator.factories.SetContClassEventBuilder;
import simulator.factories.SetWeatherEventBuilder;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;


/* -- TAREAS GENERALES --*/

public class Main {

	private final static Integer _timeLimitDefaultValue = 10;	//limite de pasos por defecto
	private static Integer _timeLimit = null; 					//limite de pasos
	private static String _inFile = null;						//fichero de entrada
	private static String _outFile = null;						//fichero de salida
	private static Factory<Event> _eventsFactory = null;		//factoria de eventos
	private static boolean _guiMode; 							//modo de juego

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseSimulationMode(line);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseTicksOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg().desc("Ticks").build());
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Mode").build());

		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {		
//		if (_inFile == null) {
//			throw new ParseException("An events file is missing");
//		}
		_inFile = line.getOptionValue("i");
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}
	
	private static void parseTicksOption(CommandLine line) throws ParseException {
		if (!line.hasOption("t"))
			_timeLimit = _timeLimitDefaultValue;
		else
		_timeLimit =Integer.parseInt(line.getOptionValue("t"));
	}
	
	private static void parseSimulationMode(CommandLine line) throws ParseException {
		String modeArg = line.getOptionValue("m");
		//si hay opciones... 
		if(modeArg != null) {
			try {
				//determina que una de las dos condiciones debe ser cierta
				assert (modeArg.equalsIgnoreCase("console") || modeArg.equalsIgnoreCase("gui"));
				//si es guiMode...
				if(modeArg.equalsIgnoreCase("gui")) _guiMode = true;
			}catch(Exception e) {
				throw new ParseException("Invalid argument for mode");
			}
		}
		//por defecto...
		else
			_guiMode = true;
	}

	private static void initFactories() {
		
		List<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add(new RoundRobinStrategyBuilder());
		lsbs.add(new MostCrowdedStrategyBuilder());
		Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory<>(lsbs);
		
		List<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
		dqbs.add(new MoveFirstStrategyBuilder());
		dqbs.add(new MoveAllStrategyBuilder());
		Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(dqbs);
		
		List<Builder<Event>> ebs = new ArrayList<>();
		ebs.add(new NewJunctionEventBuilder(lssFactory,dqsFactory));
		ebs.add(new NewCityRoadEventBuilder());
		ebs.add(new NewVehicleEventBuilder());
		ebs.add(new NewInterCityRoadEventBuilder());
		ebs.add(new SetWeatherEventBuilder());
		ebs.add(new SetContClassEventBuilder());
		_eventsFactory= new BuilderBasedFactory<>(ebs);
		
	}

	private static void startBatchMode() throws IOException {
		
		//si la ruta del fichero es nula... ERROR!
		if (_inFile == null) {
			System.err.println("An events file is missing");
			System.exit(-1);
		}
		
		//instanciamos el fichero de entrada
		InputStream inputStream = new FileInputStream(new File(_inFile));
		//instanciamos el fichero de salida
		OutputStream outputStream = (_outFile != null)? new FileOutputStream(_outFile) : System.out;
		//instanciamos el simulador
		TrafficSimulator simulator = new TrafficSimulator();
		//tomamos el numero de paso de simulacion
		int ticks = (_timeLimit != null)? _timeLimit : _timeLimitDefaultValue;
		//instanciamos el controlador
		Controller controller = new Controller(simulator, _eventsFactory);
		//cargamos los eventos
		controller.loadEvents(inputStream);
		//corremos la simulacion
		controller.run(ticks, outputStream);
		//cerramos el fichero de ENTRADA
		inputStream.close();
		//cerramos el fichero de SALIDA
		outputStream.close();
	}
	
	private static void startGUIMode() throws IOException {
		//instanciamos el simulador
		TrafficSimulator simulator = new TrafficSimulator();
		//instanciamos el controlador al que le pasamos el simulador y los pasos de simulacion
		Controller controller = new Controller(simulator, _eventsFactory);
		
		if(_inFile != null) {
			//instanciamos el fichero de entrada
			InputStream inputStream = new FileInputStream(new File(_inFile));
			//cargamos los eventos
			controller.loadEvents(inputStream);
		}
		//crea una apariencia especificando el nombre de la clase.
		try { 
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
			} 
		catch (Exception e) { e.printStackTrace(); }
		
		//crea la ventana principal
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				new MainWindow(controller);
			}
		});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}

	private static void start(String[] args) throws IOException {
		initFactories();
		parseArgs(args);
		if(_guiMode) startGUIMode();
		else startBatchMode();
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
