/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

/**
 * @author kokichi3000
 *
 */
public class TestStateDriver extends BaseStateDriver<Integer,Integer> {

	/**
	 * 
	 */
	public TestStateDriver() {
		// TODO Auto-generated constructor stub
		super();
		targetAction = null;
		target = null;
	}

	/**
	 * @param name
	 */
	
	public TestStateDriver(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#drive(solo.State)
	 */
	@Override
	public ObjectList<Integer> drive(State<Integer,Integer> state) {
		// TODO Auto-generated method stub
		ObjectList<Integer> rs = new ObjectArrayList<Integer>();
		rs.add(new Integer(1));
		rs.add(new Integer(2));
		rs.add(new Integer(3));
		return rs;
	}

	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void storeSingleAction(Integer input, Integer action, Integer output) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#restart()
	 */
	@Override
	public Integer restart() {
		// TODO Auto-generated method stub
		System.out.println("Restart");
		//current = new State<Integer, Integer>(0,new Integer(0),null,null);		
		return new Integer(0);
	}

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#shutdown()
	 */
	@Override
	public Integer shutdown() {
		// TODO Auto-generated method stub
		System.out.println("Shutdown");
		return new Integer(-1);
	}

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#stopCondition(solo.State)
	 */
	@Override
	public boolean stopCondition(State<Integer,Integer> state) {
		// TODO Auto-generated method stub
		return (state.num>=1);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestStateDriver driver = new TestStateDriver();
		Integer c = new Integer(0);
		Integer action = new Integer(0);
		while (action!=null && action!=-1){
			System.out.print(c+"\t");
			action = driver.wDrive(c);
			System.out.print(action+"\t");
			c = (action==0 || action==-1) ? 0 : new Integer(c.intValue()+action.intValue());
			System.out.println(c);
		};
		
		System.out.println(driver);
	}

}
