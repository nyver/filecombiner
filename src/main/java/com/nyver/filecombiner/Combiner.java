package com.nyver.filecombiner;

import com.nyver.filecombiner.combiner.CombinerEntry;
import com.nyver.filecombiner.combiner.CombinerOutputStream;
import com.nyver.filecombiner.exception.CombinerException;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;

/**
 * Combiner
 * @author Yuri Novitsky
 */
public class Combiner
{
    private String archiveName;

    private List<String> files;

    private File archive;

    public Combiner(String archiveName, List<String> files)
    {
        this.archiveName = archiveName;
        this.files = files;
    }

    public void combine() throws CombinerException, IOException {
        archive = new File(archiveName);
        if (archive.exists()) {
            throw new CombinerException(String.format("Archive \"%s\" is exists", archiveName));
        }

        archive.createNewFile();

        if (!archive.canWrite()) {
            throw new CombinerException(String.format("Can not write to file \"%s\"", archiveName));
        }

        CombinerOutputStream out = null;

        try {
            out = new CombinerOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(archive)
                    )
            );

            for(String fileName: files) {
                File file = new File(fileName);
                if (!file.exists()) {
                    throw new CombinerException(String.format("File %s not exists", file.getPath()));
                }

                if (!file.canRead()) {
                    throw new CombinerException(String.format("File %s is not readable", file.getPath()));
                }

                addFiles(out, file, ".");

                System.out.println(String.format("File \"%s\" processed", file.getPath()));
            }
        } finally {
            if (null != out) {
                out.close();
            }
        }
    }

    /**
     * Add files to archive
     * @param out
     * @param file
     * @param dir
     * @throws IOException
     */
    private void addFiles(CombinerOutputStream out, File file, String dir) throws IOException
    {
        out.putCombinerEntry(new CombinerEntry(file, dir + File.separator + file.getName()));

        if (file.isFile()) {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
            IOUtils.copy(in, out);
            out.closeCombinerEntry();
            in.close();
        } else if (file.isDirectory()) {
            out.closeCombinerEntry();

            for(File childFile: file.listFiles()) {
                addFiles(out, childFile, file.getName());
            }
        }

    }

    public void split()
    {

    }
}
