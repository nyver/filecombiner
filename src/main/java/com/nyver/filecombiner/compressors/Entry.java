package com.nyver.filecombiner.compressors;

/**
 * Abstract entry
 * @author Yuri Novitsky
 */
public interface Entry
{
    public String getName();

    public long getSize();

    public boolean isDirectory();
}
