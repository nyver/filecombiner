package com.nyver.filecombiner;

import com.nyver.filecombiner.compressors.AbstractInputStream;
import com.nyver.filecombiner.compressors.AbstractOutputStream;
import com.nyver.filecombiner.compressors.CompressorsFactory;
import com.nyver.filecombiner.compressors.Entry;
import com.nyver.filecombiner.compressors.combiner.CombinerEntry;
import com.nyver.filecombiner.compressors.combiner.CombinerInputStream;
import com.nyver.filecombiner.compressors.combiner.CombinerOutputStream;
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

    /**
     * Combine files to one file
     * @throws CombinerException
     * @throws IOException
     */
    public void combine(String compression) throws CombinerException, IOException {
        archive = new File(archiveName);
        if (archive.exists()) {
            throw new CombinerException(String.format("Archive \"%s\" is exists", archiveName));
        }

        archive.createNewFile();

        if (!archive.canWrite()) {
            throw new CombinerException(String.format("Can not write to file \"%s\"", archiveName));
        }

        AbstractOutputStream out = null;

        try {
            out = CompressorsFactory.createCompressorOutputStream(compression, new FileOutputStream(archive));

            for(String fileName: files) {
                File file = new File(fileName);
                if (!file.exists()) {
                    throw new CombinerException(String.format("File %s not exists", file.getPath()));
                }

                if (!file.canRead()) {
                    throw new CombinerException(String.format("File %s is not readable", file.getPath()));
                }

                addFiles(out, file, ".");
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
    private void addFiles(AbstractOutputStream out, File file, String dir) throws IOException, EntryNotClosedException {
        out.putCompressorEntry(new CombinerEntry(file, dir + File.separator + file.getName()));

        if (file.isFile()) {
            System.out.print(String.format("Process file \"%s\"", file.getCanonicalPath()));
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
            IOUtils.copy(in, out);
            out.closeCompressorEntry();
            in.close();
            System.out.print(" - added\n");
        } else if (file.isDirectory()) {
            out.closeCompressorEntry();

            System.out.print(String.format("Process directory \"%s\"\n", file.getCanonicalPath()));
            for(File childFile: file.listFiles()) {
                addFiles(out, childFile, file.getName());
            }
        }

    }

    /**
     * Split file to files
     * @throws CombinerException
     * @throws IOException
     */
    public void split(String path, String compression) throws CombinerException, IOException
    {
        archive = new File(archiveName);
        if (!archive.exists()) {
            throw new CombinerException(String.format("Archive \"%s\" is not exists", archive.getCanonicalPath()));
        }

        if (!archive.canRead()) {
            throw new CombinerException(String.format("Can not read from file \"%s\"", archive.getCanonicalPath()));
        }

        File extractPath = new File(path);

        if (!extractPath.exists()) {
            if (!extractPath.mkdirs()) {
                throw new CombinerException(String.format("Can not create path to extract \"%s\"", extractPath.getCanonicalPath()));
            }
        }

        if (!extractPath.canWrite()) {
            throw new CombinerException(String.format("Can not write to directory \"%s\"", extractPath.getCanonicalPath()));
        }


        AbstractInputStream in = null;

        try {
            in = CompressorsFactory.createCompressorInputStream(compression, new FileInputStream(archive));

            Entry entry = null;

            while((entry = in.getNextEntry()) != null) {
                File file = new File(extractPath.getCanonicalPath() + File.separator + entry.getName());
                if (entry.isDirectory()) {
                    if (!file.exists()) {
                        if (!file.mkdirs()) {
                            throw new CombinerException(String.format("Couldn't create directory \"%s\"", file.getAbsolutePath()));
                        }
                    }
                } else {
                    OutputStream out = new FileOutputStream(file);
                    IOUtils.copy(in, out);
                    out.close();
                }

                System.out.println(String.format("%s extracted", file.getCanonicalPath()));
            }

        } finally {
            if (null != in) {
                in.close();
            }
        }

    }
}
