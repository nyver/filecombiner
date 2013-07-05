package com.nyver.filecombiner.combiner;

import com.nyver.filecombiner.exception.CombinerException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Combiner output stream
 * @author Yuri Novitsky
 */
public class CombinerOutputStream extends OutputStream
{
    private OutputStream out;

    private boolean isEntryClosed = true;

    public CombinerOutputStream(OutputStream out)
    {
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void putCombinerEntry(CombinerEntry entry) throws CombinerException {
        if (!isEntryClosed) {
            throw new CombinerException("Entry must be closed");
        }

        isEntryClosed = false;
    }

    public void closeCombinerEntry()
    {
        isEntryClosed = true;
    }
}
