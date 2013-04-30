package licef;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    /**
     * Compress the content of a directory into a zip archive file.
     * @param sourceDir Directory containing the files to compress.  The directory itself will not be included in the zip file.
     * @param destinationFile Location of the zip file.  Children directory will be created if absent.  File will be overwritten if it exists.
     * @return <code>true</code> if the zip file have been successfully created, <code>false</code> otherwise.
     */
    public static boolean zipFile( String sourceDir, String destinationFile ) {
        File srcDir = new File( sourceDir );
        if( !srcDir.exists() )
            return( false );

        // Delete the file if it exists.
        File destFile = new File( destinationFile );
        if( destFile.exists() )
            destFile.delete();

        // Try to create the required directories and exit if it fails.
        if( !destFile.getParentFile().exists() && !destFile.getParentFile().mkdirs() )
            return( false );

        ZipOutputStream zout = null;
        try {
            zout = new ZipOutputStream( new FileOutputStream( destFile ) );
            zipFileRec( srcDir, "", zout );
        }
        catch( IOException ex ) {
            ex.printStackTrace();
            return( false );
        }
        finally {
            try {
                zout.close();
            }
            catch( IOException ex ) {
                ex.printStackTrace();
                return( false );
            }
        }

        return( true );
    }

    private static boolean zipFileRec( File srcDir, String path, ZipOutputStream zout ) throws IOException {
        File longPath = new File( srcDir, path );
        File[] files = longPath.listFiles();
        for( int i = 0; i < files.length; i++ ) {
            File f = files[ i ];
            if( ".".equals( f.getName() ) || "..".equals( f.getName() ) )
                continue;
            else if( f.isDirectory() )
                zipFileRec( srcDir, path.length() == 0 ? f.getName() : path + "/" + f.getName(), zout );
            else {
                InputStream in = null;
                try {
                    in = new FileInputStream( new File( longPath, f.getName() ) );
                    String entryName = path.length() == 0 ? f.getName() : path + "/" + f.getName();
                    ZipEntry entry = new ZipEntry( entryName );
                    zout.putNextEntry( entry );
                    IOUtil.copy(in, zout);
                    zout.closeEntry();
                }
                finally {
                    in.close();
                }
            }
        }
        return( true );
    }

    /**
     * Uncompress a zip archive file into a directory.
     * @param zipFile Location of the zip file.
     * @param destinationDir Location where the files will be decompressed.  The children directories will be created if needed.
     * @return <code>true</code> if the zip file have been successfully decompressed, <code>false</code> otherwise.
     */
    public static boolean unzipFile( String zipFile, String destinationDir ) {
        // Try to create the required directories and exit if it fails.
        File dstDir = new File( destinationDir );
        if( !dstDir.exists() && !dstDir.mkdirs() ) {
            System.err.println( "Could not create directory: " + destinationDir );
            return( false );
        }

        InputStream in = null;
        ZipInputStream zin = null;
        try {
            in = new BufferedInputStream( new FileInputStream( zipFile ) );
            zin = new ZipInputStream( in );
            ZipEntry e;
            while( ( e = zin.getNextEntry() ) != null ) {
                File outputLocation = new File( destinationDir, e.getName() );
                if( e.isDirectory() )
                    outputLocation.mkdirs();
                else
                    unzip( zin, outputLocation );
            }
        }
        catch( IOException ex ) {
            ex.printStackTrace();
            return( false );
        }
        finally {
            try {
                zin.close();
            }
            catch( IOException ex ) {
                ex.printStackTrace();
                return( false );
            }
        }
        return( true );
    }

    private static void unzip( ZipInputStream zin, File outputFile ) throws IOException {
        // Create required parent directories if needed.
        File parentDir = outputFile.getParentFile();
        if( parentDir != null )
            parentDir.mkdirs();

        FileOutputStream out = new FileOutputStream( outputFile );
        IOUtil.copy(zin, out);
        out.close();
    }
}
