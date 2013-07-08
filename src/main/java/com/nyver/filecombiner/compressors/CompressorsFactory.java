package com.nyver.filecombiner.compressors;

import com.nyver.filecombiner.compressors.combiner.CombinerInputStream;
import com.nyver.filecombiner.compressors.combiner.CombinerOutputStream;
import com.nyver.filecombiner.compressors.dummy.DummyInputStream;
import com.nyver.filecombiner.compressors.dummy.DummyOutputStream;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Compress format factory class
 * @author Yuri Novitsky
 */
public class CompressorsFactory
{
    public static final String DUMMY = "dummy";

    private static List<String> formats = Arrays.asList(DUMMY);

    public static boolean isFormatAvailable(String format)
    {
        return formats.contains(format.toLowerCase());
    }

    public static AbstractInputStream createCompressorInputStream(String compression, InputStream in)
    {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(in);

        if (compression.equalsIgnoreCase(DUMMY)) {
            return new DummyInputStream(bufferedInputStream);
        }

        return new CombinerInputStream(bufferedInputStream);
    }

    public static AbstractOutputStream createCompressorOutputStream(String compression, OutputStream out)
    {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(out);

        if (compression.equalsIgnoreCase(DUMMY)) {
            return new DummyOutputStream(bufferedOutputStream);
        }

        return new CombinerOutputStream(bufferedOutputStream);
    }
}
