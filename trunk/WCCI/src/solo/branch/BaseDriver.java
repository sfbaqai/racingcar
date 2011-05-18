/**
 * 
 */
package solo;

/**
 * @author kokichi3000
 *
 */
public abstract class BaseDriver {
	public abstract int drive(int[] sensors,int len);
	public void onShutdown(){};
	public void onRestart(){};

}
