package pairwisetesting.coredomain;

/**
 * Thrown to indicate exceptions related with the logic of engine.
 */
public class EngineException extends Exception {
	
	private static final long serialVersionUID = -2629492456304056136L;

	public EngineException(String msg) {
		super(msg);
	}
	
	public EngineException(Exception e) {
		super(e);
	}
}
