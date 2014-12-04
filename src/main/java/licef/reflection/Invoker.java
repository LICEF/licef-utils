package licef.reflection;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: amiara
 * Date: 22-Mar-2012
 */

public class Invoker implements Serializable {
    public Object object;
    public Class classe;
    public String method;
    public Object[] param;

    /**
     * Construit une instance d'un <code>Invoker</code> duquel on pourra
     * lancer l'execution d'une methode pour un objet donne.
     *
     * @param o L'objet a partir duquel sera lance la methode.
     *          <code>null</code> pour une methode statique.
     * @param c Nom complet (i.e.: java.lang.String) de la classe de l'objet.
     * @param m Nom de la methode a invoquer.  Ce nom doit etre unique!!
     * @param p Tableau des parametres passes a la methode.
     */
    public Invoker(Object o, String c, String m, Object[] p) {
        object = o;
        method = m;
        param = p;
        try {
            classe = Class.forName(c);
        } catch (ClassNotFoundException e) {
            System.err.println(e.toString());
        }
    }

    /**
     * Construit partiellement une instance d'un <code>Invoker</code> duquel on pourra
     * lancer l'execution d'une methode pour un objet donne.
     * Il faut completer l'initialisation de l'<code>Invoker</code> en appellant
     * <code>complete( o, c )</code>.
     *
     * @param m Nom de la methode a invoquer.  Ce nom doit etre unique!!
     * @param p Tableau des parametres passes a la methode.
     */
    public Invoker(String m, Object[] p) {
        method = m;
        param = p;
    }

    /**
     * Complete l'initialisation entamee par le constructeur
     * <code>Invoker( m, p )</code>.
     *
     * @param o L'objet a partir duquel sera lance la methode.
     * @param c Nom complet (i.e.: java.lang.String) de la classe de l'objet.
     */
    public void complete(Object o, String c) throws Exception {
        object = o;
        classe = Class.forName(c);
    }

    /**
     * Execute l'appel de la methode de l'<code>Invoker</code>.
     * Si cette methode n'est pas unique, ca ne fonctionnera.
     * Par unicite, on entend qu'une fonction ne doit pas avoir deux signatures
     * differentes avec le meme nom et avec le même nombre de parametres.
     * Ainsi, si on a la methode f( x, y ) et f( y, z ) pour un objet O,
     * l'<code>Invoker</code> ne sera pas capable de differencier O.f( x, y ) de O.f( y, z ).
     */
    public Object invoke() throws Exception {
        Method m = null;
        Method[] tabm = classe.getMethods();
        for (int i = 0; i < tabm.length; i++) {
            if (!tabm[i].getName().equals(method))
                continue;
            Class[] paramTypes = tabm[i].getParameterTypes();
            int paramLength = (param == null)?0:param.length;
            if (paramTypes.length != paramLength)
                continue;

            // Même nom et même nombre de paramètres => méthode trouvée!!
            // On ne va pas jusqu'à tester le type des paramètres pour l'instant.
            // pour les varArgs, passer un tableau vide a l'appel
            m = tabm[i];
            break;
        }
        if (m == null)
            throw new NoSuchMethodException();
        return m.invoke(object, param); //si object == null, method statique
    }

    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append(method).append("( ");
        //str.append( Util.join( param, ',' ) );
        str.append(" ) on ");
        str.append(object);
        str.append(" instance of ").append(classe);
        return (str.toString());
    }
}
