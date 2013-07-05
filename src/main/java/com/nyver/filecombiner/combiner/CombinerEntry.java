package com.nyver.filecombiner.combiner;

import java.io.File;

/**
 * Combiner entry
 * @author Yuri Novitsky
 */
public class CombinerEntry
{
    private String name;

    private long size;

    private boolean isDirectory;

    private File file;

    public CombinerEntry(File file) {
        this(file, file.getName());
    }

    public CombinerEntry(File file, String name) {
        this.file = file;
        this.name = name;
        this.isDirectory = file.isDirectory();
        this.size = file.length();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public long getSize()
    {
        return size;
    }

    public boolean isDirectory()
    {
        return isDirectory;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
