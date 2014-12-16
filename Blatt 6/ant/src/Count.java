import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;
import java.io.*;

/** 
 * Java Class Count
 * @version 30.11.2014
 * @author Rasmus Diederichsen (rdiederichse@uos.de)
 */
public class Count extends Task {
   
   private boolean printLineCount;
   private boolean printFileCount;
   private FileSet fileSet;

   /**
    * Default constructor.
    * Sets printing of lines and files to true
    */
   public Count()
   {
      printLineCount = true;
      printFileCount = true;
   }

   /**
    * Setter for countlines.
    * Total lines will be counted and output when set to <tt>true</tt>.
    * @param b Whether or not lines shall be counted.
    */
   public void setCountlines(boolean b)
   {
      printLineCount = b;
   }
   
   /**
    * Setter for countfiles.
    * Number of files will be counted and output when set to <tt>true</tt>.
    * @param b Whether or not files shall be counted.
    */
   public void setCountfiles(boolean b)
   {
      printFileCount = b;
   }

   /**
    * Add a {@code FileSet} as a nested element. 
    * @param fs <tt>FileSet<tt> whose ncluded files will be considered.
    */
   public void addFileset(FileSet fs)
   {
      fileSet = fs;
   }

   /**
    * Execute the task.
    */
   public void execute()
   {
      if (printLineCount || printFileCount)
      {
         DirectoryScanner scanner = fileSet.getDirectoryScanner();
         scanner.scan();
         int count = scanner.getIncludedFilesCount();
         String[] files = scanner.getIncludedFiles();
         int numfiles = 0, numlines = 0;
         for (String s : files) 
         {
            if (printFileCount) numfiles++;
            if (printLineCount) {
               File f = new File(scanner.getBasedir(), s);
               try(LineNumberReader bufin = new LineNumberReader(new FileReader(f));) {
                  while(bufin.readLine() != null);
                  numlines += bufin.getLineNumber();
               } catch (IOException io)
               {
                  System.err.println("IO error occured while reading file "+f.toString());
               }
            }
         }
         if (printLineCount) System.out.println("Line count: " + numlines);
         if (printFileCount) System.out.println("File count: " + numfiles);
      }
   }
}
