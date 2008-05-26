package raceclient;

/**
 * Created by IntelliJ IDEA.
 * User: jtogel
 * Date: 12-Oct-2006
 * Time: 19:06:37
 */
public interface Evolvable extends Controller {

    public void mutate ();

    public Evolvable copy ();

}
