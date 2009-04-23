/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * @author kokichi3000
 *
 */
public class MyStateDriver extends BaseStateDriver<CarRpmState,CarControl> {
	
	private BufferedWriter writer;
	/**
	 * 
	 */
	public MyStateDriver() {
		// TODO Auto-generated constructor stub
		File file = new File("results.txt");
		try{
			writer = new BufferedWriter(new FileWriter(file));			
		} catch (Exception e){
			e.printStackTrace();
		}
		try{
			writer.write("OldState\tAction\t\tNewState");
			writer.newLine();
			writer.flush();
		} catch (Exception e){
			e.printStackTrace();			
		}
	}

	/**
	 * @param name
	 */
	public MyStateDriver(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		File file = new File(name+"-results.txt");
		try{
			writer = new BufferedWriter(new FileWriter(file));			
		} catch (Exception e){
			e.printStackTrace();
		}
		try{
			writer.write("OldState\tAction\t\tNewState");
			writer.newLine();
			writer.flush();
		} catch (Exception e){
			e.printStackTrace();			
		}
	}
	
	

	@Override
	public void init() {
		// TODO Auto-generated method stub		
	}

	@Override
	public ObjectList<CarControl> drive(State<CarRpmState, CarControl> state) {
		// TODO Auto-generated method stub
		ObjectList<CarControl> ol = new ObjectArrayList<CarControl>();
		for (int i =0;i<1001;++i){
			CarControl cc = new CarControl(0.001*i,0,0,0,0);
			ol.add(cc);
		}
		return ol;
	}

	@Override
	public CarControl restart() {
		// TODO Auto-generated method stub
		pathToTarget = null;
		current = null;
		action = null;
		return new CarControl(0,0,0,0,1);
	}

	@Override
	public CarControl shutdown() {
		// TODO Auto-generated method stub
		try{
			writer.close();			
			save("onestep.txt");
			ObjectSortedSet<CarRpmState> s = map.keySet();
			for (CarRpmState ss:s){
				System.out.print(ss.getRPM()+"   ");
			}
			System.out.println();
		} catch (Exception e){
			e.printStackTrace();
		}
		return new CarControl(0,0,0,0,2);
	}

	@Override
	public boolean stopCondition(State<CarRpmState, CarControl> state) {
		// TODO Auto-generated method stub
		return (target==null) ? state.num>=1 : state.num>=target.num+1;
	}
	
	public static double round(double v){
		return ((int)(v*10000))/10000.0d;
	}

	@Override
	public void storeSingleAction(CarRpmState input, CarControl action,
			CarRpmState output) {
		// TODO Auto-generated method stub
		try{
			if (input!=null && action!=null && output!=null){
				writer.write(round(input.getRPM())+"\t\t"+round(action.accel)+"\t\t"+round(output.getRPM()));
				writer.newLine();
				writer.flush();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	
}
