package licef;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import java.util.*;

public class AWTUtil {

    /** 
     * Add an instance of a KeyListener to a component and all its subcomponents recursively.
     * @param k KeyListener to add.
     * @param c Component that will receive the listener.
     */
    public static void addKeyListenerToAllComponents( KeyListener k, Component c ) {
        if( c != null ) {
            c.addKeyListener( k );
            if( c instanceof Container ) {
                Component[] components = ((Container)c).getComponents();
                for( int i = 0; i < components.length; i++ )
                    addKeyListenerToAllComponents( k, components[ i ] );    
            }
        }
    }
    
    /** 
     * Add a listener to a component and all its subcomponents recursively.
     * @param listenerName Name of the listener class (e.g.: KeyListener).
     * @param listener Instance of listener.
     * @param c Component to which the listener will be added.
     */
    public static void addListenerToAllComponents( String listenerName, EventListener listener, Component c ) {
        if( c == null )
            return;
        try {
            Class[] classes = listener.getClass().getInterfaces();
            if( classes.length > 0 ) {
                Method addListenerMethod = c.getClass().getMethod( "add" + listenerName, new Class[] { classes[ 0 ] } );
                addListenerMethod.invoke( c, new Object[] { listener } );
            }
        }
        catch( Exception ignore ) {
            ignore.printStackTrace();
        }
        if( c instanceof Container ) {
            Component[] components = ((Container)c).getComponents();
            for( int i = 0; i < components.length; i++ )
                addListenerToAllComponents( listenerName, listener, components[ i ] );    
        }
    }

    /** 
     * Returns the top frame of a component (its highest parent in the hierarchy).
     * Convenient to show modal dialogs.
     * @param c Component
     * @return The top frame of the component.
     */
    public static Frame getTopFrame(Container c)
    {
        if (c instanceof Frame) 
            return (Frame)c;

        Container theFrame = c;
        do {
            theFrame = theFrame.getParent();
        } while ((theFrame != null) && !(theFrame instanceof Frame));
        
        if (theFrame == null) 
            theFrame = new Frame();

        return (Frame)theFrame;
    }

}
