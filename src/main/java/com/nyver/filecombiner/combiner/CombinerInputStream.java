package com.nyver.filecombiner.combiner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Combiner Input Stream
 * @author Yuri Novitsky
 */
public class CombinerInputStream extends InputStream
{
    private InputStream in;

    private CombinerEntry currentEntry = null;

    public CombinerInputStream(InputStream in)
    {
        this.in = in;
    }

    @Override
    public int read() throws IOException
    {
        return in.read();
    }

    public CombinerEntry getNextEntry() throws IOException {
        CombinerEntry entry = new CombinerEntry();

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        int count = 0;
        while((line = reader.readLine()) != null) {
            String[] parts = line.split("=");

            if (parts.length != 2) {
                entry = null;
                break;
            }

            switch(count) {
                case 0:
                    Boolean isDirectory = Boolean.parseBoolean(parts[1]);
                    entry.setDirectory(isDirectory.booleanValue());
                    break;
                case 1:
                    entry.setName(parts[1]);
                    break;
                case 2:
                    Long size = Long.getLong(parts[1]);
                    entry.setSize(size.longValue());
                    break;
            }

            count++;
            if (count >= 2) {
                break;
            }
        }

        if (count < 2) {
            entry = null;
        }

        return entry;
    }
}
