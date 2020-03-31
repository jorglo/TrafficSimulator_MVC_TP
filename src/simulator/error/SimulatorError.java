package simulator.error;

public class SimulatorError extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SimulatorError(String msg) {
		super(msg);
	}
	
	@Override
	public String toString() {
		return getMessage();
	}
}
