package licef;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.net.*;
import javax.activation.MimeType;
import javax.activation.MimetypesFileTypeMap;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypes;

public class IOUtil {

    public static final String UTF8_BOM = "\uFEFF";

    /**
     * Returns true if path is an URL. 
     * @param s path to analyze.
     */
    public static boolean isURL(String s) {
        try {
            new URL(s);
            return !s.startsWith("file:/");
        } catch (MalformedURLException e) {
            return false;
        }
    }

    /**
     * Returns true if path begins with an URI scheme
     * @param s path to analyze.
     */
    public static boolean isURIScheme(String s) {
        if (s == null || "".equals(s) || !s.contains(":"))
            return false;
        String scheme = s.substring(0, s.indexOf(":"));
        return scheme.matches("[a-zA-Z][a-zA-Z0-9\\+\\-\\.]*");
    }

    /** 
     * Get a file specified by an input URL and save it to a specified file. 
     * @param url URL of the file to fetch.
     * @param file File into which the fetched file will be written to.
     * @param isCompressed If <code>true</code>, the file will be decompressed.
     * @throws Exception 
     */
    public static void getFile( String url, File file, boolean isCompressed) throws Exception
    {
        FileOutputStream outputStream = null;
        DataInputStream inputStream = null;

        try {
            URL u = new URL(url);
            outputStream = new FileOutputStream( file );
            if( isCompressed )
                inputStream = new DataInputStream( new GZIPInputStream( u.openStream() ) );
            else
                inputStream = new DataInputStream( u.openStream() );
            copy( inputStream, outputStream );

            inputStream.close();
            outputStream.close();
        }
        catch( Exception e ) {System.out.println(e+"");}
    }

    /** 
     * Get a file specified by an input URL and save it to a specified file. 
     * @param url URL of the file to fetch.
     * @param file File into which the fetched file will be written to.
     * @throws Exception 
     */
    public static void getFile( String url, File file ) throws Exception
    {
        getFile( url, file, false );
    }

    /** 
     * Read a file and return it as an array of bytes.
     * @param file Input file.
     * @param isCompressed <code>true</code> if we want a smaller array of bytes.
     * @return A file as an array of bytes.
     */
    public static byte[] readFileIntoByteArray( final File file, boolean isCompressed )
    {
        try
        {
            byte [] buffer = null;
            FileInputStream fisInput = new FileInputStream( file );
            DataInputStream input = null;
            if ( isCompressed )
                input = new DataInputStream( new GZIPInputStream( fisInput ) );
            else
                input = new DataInputStream( fisInput );

            buffer = new byte [ input.available() ];
            input.readFully( buffer );
            input.close();
            return( buffer );
        }
        catch( FileNotFoundException e ) { e.printStackTrace(); return( null ); }
        catch( IOException e ) { e.printStackTrace(); return( null ); }
    }

    /** 
     * Read a file and return it as an array of bytes.
     * @param file Input file.
     * @return A file as an array of bytes.
     */
    public static byte[] readFileIntoByteArray( final File file )
    {
        return( readFileIntoByteArray( file, false ) );
    }

    /** 
     * Read a file and return it as an array of bytes.
     * @param url URL of the file to convert.
     * @param isCompressed <code>true</code> if we want a smaller array of bytes.
     * @return A file as an array of bytes.
     */
    public static byte[] readFileIntoByteArray( final URL url, boolean isCompressed )
    {
        try
        {
            byte [] buffer = null;
            DataInputStream input = null;
            if( isCompressed )
                input = new DataInputStream( new GZIPInputStream( url.openStream() ) );
            else
                input = new DataInputStream( url.openStream() );
            buffer = new byte [ input.available() ];
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            copy( input, output );
            buffer = output.toByteArray();
            input.close();
            output.close();
            return( buffer );
        }
        catch( FileNotFoundException e ) { e.printStackTrace(); return( null ); }
        catch( IOException e ) { e.printStackTrace(); return( null ); }
    }

    /** 
     * Read a file and return it as an array of bytes.
     * @param url URL of the file to convert.
     * @return A file as an array of bytes.
     */
    public static byte[] readFileIntoByteArray( final URL url )
    {
        return( readFileIntoByteArray( url, false ) );
    }

    /** 
     * Write an array of bytes into a file.
     * @param buffer Array of bytes. 
     * @param file File into which the array of bytes will be written to.
     * @param isCompressed <code>true</code> if the array of bytes is compressed.
     */
    public static void writeByteArrayToFile( byte[] buffer, final File file, boolean isCompressed )
    {
        createDirectory( file.getParent() );
        try
        {
            FileOutputStream foutput = new FileOutputStream( file );
            DataOutputStream output = null;
            if ( isCompressed )
                output = new DataOutputStream ( new GZIPOutputStream( foutput ) );
            else
                output = new DataOutputStream ( foutput );

            output.write( buffer );
            output.flush();
            output.close();
        }
        catch( IOException e ) { e.printStackTrace(); }
    }

    /** 
     * Write an array of bytes into a file.
     * @param buffer Array of bytes. 
     * @param file File into which the array of bytes will be written to.
     */
    public static void writeByteArrayToFile( byte[] buffer, final File file )
    {
        writeByteArrayToFile( buffer, file, false );
    }

    /** 
     * Compress an array of bytes. 
     * @param array Input array of bytes.
     * @return Compressed array of bytes.
     */
    public static byte[] compressByteArray( byte[] array )
    {
        try
        {
            ByteArrayOutputStream bOutput = new ByteArrayOutputStream();
            GZIPOutputStream gOutput = new GZIPOutputStream(bOutput);
            gOutput.write(array);
            gOutput.finish();
            return bOutput.toByteArray();
        }
        catch( IOException e ) { e.printStackTrace(); return null; }
    }

    /** 
     * Decompress an array of bytes. 
     * @param zipArray A compressed array of bytes.
     * @return A decompressed array of bytes.
     */
    public static byte[] decompressByteArray( byte[] zipArray )
    {
        try
        {
            ByteArrayInputStream bInput = new ByteArrayInputStream( zipArray );
            GZIPInputStream gInput = new GZIPInputStream( bInput );
            ByteArrayOutputStream bOutput = new ByteArrayOutputStream();
            byte[] buffer = new byte[ 2048 ];
            while( true ) {
                int bytesRead = gInput.read( buffer );
                if( bytesRead == -1 )
                    break;
                bOutput.write( buffer, 0, bytesRead );
            }
            return( bOutput.toByteArray() );
        }
        catch( IOException e ) { 
            System.out.println( ""+e ); 
            return null;
        }
    }

    /** 
     * Read a serialized object from a file.
     * @param file Input file containing a serialized object.
     * @param isCompressed <code>true</code> if the file contains a compressed serialized object.
     * @return An instance of the serialized object.
     */
    public static Object readObject( final File file, final boolean isCompressed )
    {
        Object object = null;
        try
        {
            FileInputStream fisInput = new FileInputStream( file );
            ObjectInputStream input;
            if( isCompressed )
                input = new ObjectInputStream( new GZIPInputStream( fisInput ) );
            else
                input = new ObjectInputStream( fisInput );

            object = input.readObject();
            input.close();
            return object;
        }
        catch ( IOException e ) { e.printStackTrace(); return( null ); }
        catch ( ClassNotFoundException e ) { e.printStackTrace(); return( null ); }
    }

    /** 
     * Read a serialized object from a file.
     * @param file Input file containing a serialized object.
     * @return An instance of the serialized object.
     */
    public static Object readObject( final File file )
    {
        return( readObject( file, false ) );
    }

    /** 
     * Read a serialized object from an URL.
     * @param url URL pointing to a serialized object.
     * @param isCompressed <code>true</code> if the URL points to a compressed serialized object.
     * @return An instance of the serialized object. 
     */
    public static Object readObject( final URL url, final boolean isCompressed )
        throws ClassNotFoundException, OptionalDataException, StreamCorruptedException, IOException
    {
        Object object = null;
        InputStream isInput = url.openStream();
        ObjectInputStream input =
            new ObjectInputStream( (isCompressed ? new GZIPInputStream( isInput ) : isInput ) );

        object = input.readObject();
        input.close();
        return object;
    }

    /** 
     * Read a serialized object from an URL.
     * @param url URL pointing to a serialized object.
     * @return An instance of the serialized object. 
     */
    public static Object readObject( final URL url )
        throws ClassNotFoundException, OptionalDataException, StreamCorruptedException, IOException
    {
        return( readObject( url, false ) );
    }

    /** 
     * Read a serialized object from an array of bytes.
     * @param buffer Array of bytes containing a serialized object. 
     * @return An instance of serialized object. 
     */
    public static Object readObject( final byte[] buffer )
    {
        Object object = null;
        try
        {
            ObjectInputStream input = new ObjectInputStream( new ByteArrayInputStream( buffer ) );
            object = input.readObject();
            input.close();
            return object;
        }
        catch ( IOException e ) { e.printStackTrace(); return( null ); }
        catch ( ClassNotFoundException e ) { e.printStackTrace(); return( null ); }
    }

    /** 
     * Write a serialized object into a file. 
     * @param file File into which the serialized object will be written to. 
     * @param object Object to serialize.
     * @param isCompressed <code>true</code> if we want to compress the data.
     */
    public static void writeObject( File file, Object object, boolean isCompressed )
    {
        if (file.getParent() != null)
            createDirectory( file.getParent() );
        try
        {
            FileOutputStream fisOutput = new FileOutputStream( file );
            ObjectOutput output;
            if( isCompressed )
                output = new ObjectOutputStream( new GZIPOutputStream( fisOutput ) );
            else
                output = new ObjectOutputStream( fisOutput );

            output.writeObject( object );
            output.flush();
            output.close();
        }
        catch ( Exception e ) { e.printStackTrace(); }
    }

    /** 
     * Write a serialized object into a file. 
     * @param file File into which the serialized object will be written to. 
     * @param object Object to serialize.
     */
    public static void writeObject( File file, Object object )
    {
        writeObject( file, object, false );
    }

    /** 
     * Write a serialized object into an array of bytes.
     * @param object Object to serialize.
     * @return An array of bytes containing the serialized object.
     */
    public static byte[] writeObject( final Object object )
    {
        try
        {
            ByteArrayOutputStream bOutput = new ByteArrayOutputStream();
            ObjectOutputStream oOutput = new ObjectOutputStream(bOutput);
            oOutput.writeObject(object);
            oOutput.flush();
            return bOutput.toByteArray();
        }
        catch ( IOException e ) { e.printStackTrace(); return( null ); }
    }

    /** 
     * Returns a <code>Vector</code> of lines of the file pointed by the input URL.
     * @param url URL of the file to read. 
     * @return <code>Vector</code> of lines of the file pointed by the input URL.
     */
    public static Vector readLines( final URL url )
    {
        try
        {
            return readLines(url.openStream());
        } catch (IOException e) {
             System.out.println(e+"");
            return null;
        }
    }

    /** 
     * Returns a <code>Vector</code> of lines of the specified file.
     * @param file File to read.
     * @return <code>Vector</code> of lines of the specified file.
     */
    public static Vector readLines( File file )
    {
        try {
            return readLines(new FileInputStream( file ));
        } catch (FileNotFoundException e) {
            System.out.println("" + e);
            return null;
        }
    }

    /**
     * Returns a <code>Vector</code> of lines of an inputStream.
     * @param in Stream to read.
     * @return <code>Vector</code> of lines of the specified stream.
     */

    public static Vector readLines( InputStream in )
    {
        Vector v = new Vector();
        boolean firstLine = true;
        String nextLine;
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while((nextLine = br.readLine()) != null) {
                if (firstLine) {
                    if (nextLine.startsWith(UTF8_BOM))
                        nextLine = nextLine.substring(1);
                    firstLine = false;
                }
                v.addElement(nextLine);
            }
            return v;
        }
        catch(IOException e){ System.out.println( e.toString() ); return null;}
        finally {
            try {
                if( in != null )
                    in.close();
            } catch( IOException e2 ) { System.out.println( e2.toString() ); return null; }
        }
    }

    public static String readStringFromFile( File location ) throws IOException {
        return readString( new FileInputStream( location ) );
    }

    public static String readStringFromURL( URL url ) throws IOException {
        return readString( url.openStream() );
    }

    public static String readString( InputStream in ) throws IOException {
        ByteArrayOutputStream ostr = new ByteArrayOutputStream();
        BufferedOutputStream bostr = new BufferedOutputStream( ostr );
        BufferedInputStream bistr = new BufferedInputStream( in );
        try {
            IOUtil.copy( bistr, bostr );
        }
        finally {
            bostr.close();
            bistr.close();
        }
        String str = null;
        try {
            str = ostr.toString( "UTF-8" );
        }
        catch( UnsupportedEncodingException shoulNeverHappen ) {
            str = ostr.toString();
        }
        return( str );
    }
    
    
    public static void writeStringToFile( String str, File location ) throws IOException {
        writeStringToFile( str, location, false );
    }

    public static void writeStringToFile( String str, File location, boolean append ) throws IOException {
        File destDir = location.getParentFile();
        if (!destDir.exists()) {
            if (!destDir.mkdirs())
                throw (new IOException("copyFiles: Could not create directory: " + destDir.getAbsolutePath() + "."));
        }

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter( new FileWriter( location, append ) );
            writer.write( str );
        }
        finally {
            if (writer != null)
                writer.close();
        }
    }

    /** 
     * Create a directory recursively from a string.
     * @param dir String of a directory path. 
     */
    public static void createDirectory( final String dir )
    {
        String[] dirs = StringUtil.split( dir.replace( '\\', '/' ), '/' );
        File f = null;
        for( int i = 0; i < dirs.length; i++ ) {
            if( f == null )
                f = new File( dirs[ i ] );
            else
                f = new File( f, dirs[ i ] );
            if( !f.exists() )
                f.mkdir();
        }
    }

    /** 
     * Create a temporary directory.
     * @return dir A File instance pointing to the temporary directory.
     */
    public static File createTempDirectory() throws IOException {
        final File temp;

        temp = File.createTempFile( "temp", Long.toString( System.nanoTime() ) );

        if( !( temp.delete() ) )
            throw new IOException( "Could not delete temp file: " + temp.getAbsolutePath() );

        if( !( temp.mkdir() ) )
            throw new IOException( "Could not create temp directory: " + temp.getAbsolutePath() );

        return( temp );
    }

    /** 
     * Delete a directory (including all its subdirectories and files).
     * @param dir The directory to be deleted
     * @return <tt>true</tt> if the directory has been successfully deleted, <tt>false</tt> otherwise.
     */
    public static boolean deleteDirectory( File dir ) {
        if( dir.isDirectory() ) {
            String[] children = dir.list();
            for( int i = 0; i < children.length; i++ ) {
                if( children[ i ] == "." || children[ i ] == ".." )
                    continue;
                boolean ok = deleteDirectory( new File( dir, children[ i ] ) );
                if( !ok )
                    return( false );
            }
        }

        return( dir.delete() );
    }

    /** 
     * Copy data from an input stream to an output stream. 
     * @param in Input stream.
     * @param out Output stream.
     * @throws IOException 
     */
    public static void copy(InputStream in, OutputStream out) throws IOException
    {
        // Do not allow other threads to read from the
        // input or write to the output while copying is
        // taking place.
        synchronized (in)
        {
            synchronized (out)
            {
                byte[] buffer = new byte[4096];
                while (true)
                {
                    int bytesRead = in.read(buffer);
                    if (bytesRead == -1) 
                        break;
                    out.write(buffer, 0, bytesRead);
                }
            }
        }
    }

    /**
    * This function will copy files or directories from one location to another.
    * note that the source and the destination must be mutually exclusive. This
    * function can not be used to copy a directory to a sub directory of itself.
    * The function will also have problems if the destination files already exist.
    * @param src -- A File object that represents the source for the copy
    * @param dest -- A File object that represents the destination for the copy.
    * @throws IOException if unable to copy.
    */
    public static void copyFiles( File src, File dest ) throws IOException {
        if( !src.exists() )
            throw( new IOException( "copyFiles: Can not find source: " + src.getAbsolutePath() + "." ) );
        else if( !src.canRead() )
            throw( new IOException( "copyFiles: No right to source: " + src.getAbsolutePath() + "." ) );

        if( src.isDirectory() ) { //folder copy
            if( !dest.exists() ) {
                if (!dest.exists()) {
                    if (!dest.mkdirs())
                        throw (new IOException("copyFiles: Could not create directory: " + dest.getAbsolutePath() + "."));
                }
            }
            String list[] = src.list();
            for( int i = 0; i < list.length; i++ ) {
                File dest1 = new File( dest, list[ i ] );
                File src1 = new File( src, list[ i ] );
                copyFiles( src1 , dest1 );
            }
        } 
        else { //file copy
            FileInputStream fin = null;
            FileOutputStream fout = null;
            File destDir = dest.getParentFile();
            if (!destDir.exists()) {
                if (!destDir.mkdirs())
                    throw (new IOException("copyFiles: Could not create directory: " + destDir.getAbsolutePath() + "."));
            }

            try {
                fin =  new FileInputStream( src );
                fout = new FileOutputStream( dest );
                IOUtil.copy( fin, fout );
            } 
            catch( IOException e ) {
                IOException wrapper = new IOException( "copyFiles: Unable to copy file: " + src.getAbsolutePath() + "to" + dest.getAbsolutePath() + "." );
                wrapper.initCause( e );
                wrapper.setStackTrace( e.getStackTrace() );
                throw( wrapper );
            } finally {
                if( fin != null ) 
                    fin.close();
                if( fout != null )
                    fout.close();
            }
        }
    } 

    /** 
     * Ask a question at the console. 
     * @param question Question to ask.
     * @param defaultAnswer Default answer if enter is pressed.
     * @return Answer.
     */
    public static String ask( String question, String defaultAnswer ) throws IOException {
        StringBuffer formattedQuestion = new StringBuffer( question );
        if( defaultAnswer != null || !"".equals( defaultAnswer ) )
            formattedQuestion.append( " [" ).append( defaultAnswer ).append( "]" );
        formattedQuestion.append( ":" );
        
        BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );
        String answer = null;
        answer = reader.readLine();
        if( StringUtil.isEmpty( answer ) )
            return( defaultAnswer );
        else
            return( answer );
    }

    /** 
     * Ask a question at the console. 
     * @param question Question to ask.
     * @return Answer.
     */
    public static String ask( String question ) throws IOException {
        return( ask( question, null ) );
    }

    public static String enforceAbsoluteUrl( String url ) {
        if (url == null || "".equals(url))
            return null;
        
        if( url.startsWith( "http" ) )
            return( url );
        else 
            return( "http://" + url );
    }

    /**
     * Mimetype and extension
     */

    public static String getMimeType(String val) throws Exception {
        if (val.startsWith("http"))
            return getMimeType(new URL(val));
        else
            return getMimeType(new File(val));
    }

    public static String getMimeType( File file ) throws Exception {
        return (new Tika()).detect(file);
    }

    public static String getMimeType( URL url ) throws Exception {
        return (new Tika()).detect(url);
    }

    public static String getExtension( String mimetype ) throws Exception {
        return MimeTypes.getDefaultMimeTypes().forName(mimetype).getExtension();
    }
}
