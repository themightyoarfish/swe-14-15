package de.uos.inf.swe;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * Counts number of files and/or lines
 *
 * @goal count
 * 
 * @phase process-sources
 */
public class CountMojo extends AbstractMojo
{
    /**
     * Location of the file.
     * @parameter expression="${project.basedir}"
     */
    private String folderPath;

    /**
     * Determines whether number of files are printed
     * @parameter expression=true
     */
    private boolean countFiles;

    /**
     * Determines whether LOC are printed
     * @parameter expression=true
     */
    private boolean countLines;

    private int numberFiles;
    private int numberLines;

    public void execute() throws MojoExecutionException
    {
        numberFiles = 0;
        numberLines = 0;

        try {
            listDir(folderPath);
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        if (countFiles) {
            getLog().info( "Number of files in project: " + countFiles );
        }

        if (countLines) {
            getLog().info( "Number of lines in project: " + numberLines );
        }
    }

    private void listDir(String path) throws IOException {
        File file = new File(path);

        if (file.isFile()) {
            numberFiles++;

            if (countLines) {

                FileReader fr = new FileReader(file);
                LineNumberReader lnr = new LineNumberReader(fr);

                int linenumber = 0;
                String line;

                while ((line = lnr.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        numberLines++;
                    }
                }

                lnr.close();
            }
        } else {
            File[] children = file.listFiles();
            for (File child : children) {
                listDir(child.getAbsolutePath());
            }
        }
    }
}