package licef;

import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.tree.*;

public class SwingsUtil {

    /** 
     * Expand (or collapse) all the nodes of a JTree.
     * @param tree Tree of which we want to expand all nodes.
     * @param expand <code>true</code> to expand all the nodes.  <code>false</code> to collapse all the nodes. 
     */
    public static void expandAllNodes(JTree tree, boolean expand) {
        Object root = tree.getModel().getRoot();
        expandAllNodes( tree, new TreePath( root ), expand );
    }
    
    /** 
     * Expand (or collapse) all the children nodes of specified node of a JTree.
     * @param tree Tree of which we want to expand the nodes.
     * @param parent Parent node.
     * @param expand <code>true</code> to expand all the nodes.  <code>false</code> to collapse all the nodes. 
     */
    public static void expandAllNodes( JTree tree, TreePath parent, boolean expand ) {
        if( parent == null )
            return;

        Object node = parent.getLastPathComponent();
        if( node != null ) {
            for( int i = 0; i < tree.getModel().getChildCount( node ); i++ ) {
                Object child = tree.getModel().getChild( node, i );
                TreePath path = parent.pathByAddingChild( child );
                expandAllNodes( tree, path, expand );
            }
        }
    
        if( expand ) 
            tree.expandPath( parent );
        else
            tree.collapsePath( parent );
    }

    /** 
     * Expand (or collapse) the children nodes of specified node of a JTree for a certain depth.
     * @param tree Tree of which we want to expand the nodes.
     * @param parent Parent node.
     * @param expand <code>true</code> to expand the nodes.  <code>false</code> to collapse the nodes. 
     * @param depth Depth of children to expand or collapse.
     */
    public static void expandNodes( JTree tree, TreePath parent, boolean expand, int depth ) {
        if( parent == null || depth == 0 )
            return;

        Object node = parent.getLastPathComponent();
        if( node != null ) {
            for( int i = 0; i < tree.getModel().getChildCount( node ); i++ ) {
                Object child = tree.getModel().getChild( node, i );
                TreePath path = parent.pathByAddingChild( child );
                expandNodes( tree, path, expand, depth - 1 );
            }
        }
    
        if( expand ) 
            tree.expandPath( parent );
        else
            tree.collapsePath( parent );
    }

    /** 
     * Clear the content of a JTextPane.
     * @param textPane Instance of JTextPane to clear. 
     */
    public static void clear( JTextPane textPane ) {
        Document doc = textPane.getDocument();
        try {
            doc.remove( 0, doc.getLength() ); // Erase all the previous text.
        } 
        catch( BadLocationException ignore ) {
            ignore.printStackTrace();
        }
    }

    /** 
     * Affect a stylised text into an instance of JTextPane. 
     * @param textPane Instance of JTextPane to affect.
     * @param textArray Array of strings.
     * @param styleArray Array of styles.  Must match the textArray.
     */
    public static void setText( JTextPane textPane, String[] textArray, String[] styleArray ) {
        StyledDocument doc = textPane.getStyledDocument();
        try {
            doc.remove( 0, doc.getLength() ); // Erase all the previous text.
            for (int i=0; i < textArray.length; i++) {
                int offset = doc.getLength();
                javax.swing.text.Style style = textPane.getStyle( styleArray[ i ] );
                doc.insertString( offset, textArray[i], style );
                doc.setParagraphAttributes( offset, textArray[ i ].length(), style, true );
            }
            textPane.setCaretPosition( 0 );
        } catch( BadLocationException ignore ) {
            ignore.printStackTrace();
        }
    }

}
