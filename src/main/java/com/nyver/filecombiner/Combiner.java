package com.nyver.filecombiner;

import com.nyver.filecombiner.combiner.CombinerEntry;
import com.nyver.filecombiner.combiner.CombinerInputStream;
import com.nyver.filecombiner.combiner.CombinerOutputStream;
import com.nyver.filecombiner.exception.CombinerException;
import com.nyver.filecombiner.exception.EntryNotClosedException;
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

                System.out.println(String.format("File \"%s\" added", file.getPath()));
            }

            System.out.println();
            System.out.println(String.format("Archive \"%s\" created successfully", archive.getName()));

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
    private void addFiles(CombinerOutputStream out, File file, String dir) throws IOException, EntryNotClosedException {
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

    public void split() throws CombinerException, IOException
    {
        archive = new File(archiveName);
        if (!archive.exists()) {
            throw new CombinerException(String.format("Archive \"%s\" is not exists", archiveName));
        }

        if (!archive.canRead()) {
            throw new CombinerException(String.format("Can not read from file \"%s\"", archiveName));
        }

        CombinerInputStream in = null;

        try {
            in = new CombinerInputStream(
                    new BufferedInputStream(
                            new FileInputStream(archive)
                    )
            );

            CombinerEntry entry = null;

            while((entry = in.getNextEntry()) != null) {
                File file = new File(entry.getName());
                if (entry.isDirectory()) {
                    if (!file.exists()) {
                        if (!file.mkdirs()) {
                            throw new CombinerException(String.format("Couldn't create directory \"%s\"", file.getAbsolutePath()));
                        }
                    }
                } else {
                    OutputStream out = new FileOutputStream(file);
                    //IOUtils.copy(in, out);
                    out.close();
                }

                System.out.println(String.format("%s extracted", file.getAbsolutePath()));
            }

        } finally {
            if (null != in) {
                in.close();
            }
        }

    }
}
