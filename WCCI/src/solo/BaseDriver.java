/**
 * 
 */
package solo;

/**
 * @author kokichi3000
 *
 */
public abstract class BaseDriver {
	public abstract String drive(String sensors);
	public void onShutdown(){};
	public void onRestart(){};

}
