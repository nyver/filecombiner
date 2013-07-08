package com.nyver.filecombiner.compressors.dummy;

import com.nyver.filecombiner.compressors.combiner.CombinerInputStream;

import java.io.InputStream;

/**
 * Dummy input stream
 * @author Yuri Novitsky
 */
public class DummyInputStream extends CombinerInputStream
{
    public DummyInputStream(InputStream in) {
        super(in);
    }
}
