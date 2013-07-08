package com.nyver.filecombiner.compressors;

import com.nyver.filecombiner.exception.EntryNotClosedException;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Abstract output stream
 * @author Yuri Novitsky
 */
public abstract class AbstractOutputStream extends OutputStream
{
    /**
     * Put entry to output stream
     * @param entry
     * @throws EntryNotClosedException
     * @throws IOException
     */
    public abstract void putCompressorEntry(Entry entry) throws EntryNotClosedException, IOException;

    /**
     * Close entry in output stream
     */
    public abstract void closeCompressorEntry();
}
