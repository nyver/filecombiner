package com.nyver.filecombiner.compressors;

import com.nyver.filecombiner.compressors.combiner.CombinerEntry;

import java.io.IOException;
import java.io.InputStream;

/**
 * Abstract input stream
 * @author Yuri Novitsky
 */
public abstract class AbstractInputStream extends InputStream
{
    /**
     * Get next file entry
     * @return entry
     * @throws IOException
     */
    public abstract Entry getNextEntry() throws IOException;
}
