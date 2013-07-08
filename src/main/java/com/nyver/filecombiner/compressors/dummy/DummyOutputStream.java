package com.nyver.filecombiner.compressors.dummy;

import com.nyver.filecombiner.compressors.combiner.CombinerOutputStream;

import java.io.OutputStream;

/**
 * Dummy output stream
 * @author Yuri Novitsky
 */
public class DummyOutputStream extends CombinerOutputStream
{

    public DummyOutputStream(OutputStream out) {
        super(out);
    }
}
