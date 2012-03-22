package licef.reflection;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: amiara
 * Date: 22-Mar-2012
 */

public class ThreadInvoker extends Thread {
    Invoker invoker;
    public Object resultat = new Invoker(null, null); //resultat bidon pour l'init. doit être différent de null
    public Exception exception;

    /**
     * Construit l'instance du <code>ThreadInvoker</code>.
     *
     * @param invk Instance d'un <code>Invoker</code> duquel la methode sera lancee.
     */
    public ThreadInvoker(Invoker invk) {
        invoker = invk;
    }

    /**
     * Execute l'<code>Invoker</code> associee au <code>ThreadInvoker</code>.
     */
    public void run() {
        try {
            resultat = invoker.invoke();
        } catch (InvocationTargetException ex1) {
//            System.out.println( ex1 + " via " + invoker.method +
//                " de la classe " + invoker.classe.getName() );
//            Throwable throwable = ex1.getTargetException();
//            System.out.println( throwable );
//            throwable.printStackTrace();
            exception = ex1;
        } catch (Exception e) {
            System.out.println(e + " via " + invoker.method +
                    " de la classe " + invoker.classe.getName());
            e.printStackTrace();
            exception = e;
        }
    }
}
