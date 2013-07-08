package com.nyver.filecombiner.compressors.combiner;

import com.nyver.filecombiner.compressors.AbstractInputStream;
import com.nyver.filecombiner.compressors.Entry;

import java.io.*;

/**
 * Combiner Input Stream
 * @author Yuri Novitsky
 */
public class CombinerInputStream extends AbstractInputStream
{
    private static final int ENTRY_READ_LIMIT = 10000;
    private static final String CHARSET = "UTF-8";

    private InputStream in;

    private CombinerEntry currentEntry = null;

    private long entryBytesReaded = 0;

    public CombinerInputStream(InputStream in)
    {
        this.in = in;
    }

    @Override
    public int read() throws IOException
    {
        if (null == currentEntry) return -1;
        if (entryBytesReaded >= currentEntry.getSize()) return -1;
        entryBytesReaded++;
        return in.read();
    }

    /**
     * Method for read the entry header
     * @return
     * @throws IOException
     */
    public int readByte() throws IOException
    {
        return in.read();
    }

    /**
     * Get next file entry
     * @return
     * @throws IOException
     */
    @Override
    public Entry getNextEntry() throws IOException
    {
        CombinerEntry entry = new CombinerEntry();

        String line;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int count = 0;

        int ch;
        long readed = 0;

        while((ch = readByte()) != -1) {
            readed++;

            if (readed > ENTRY_READ_LIMIT) {
                break;
            }

            if (ch == '\n') {
                count++;

                line = os.toString(CHARSET);

                String[] parts = line.split("=");

                if (parts.length != 2) {
                    entry = null;
                    break;
                }

                switch(count) {
                    case 1:
                        Boolean isDirectory = Boolean.parseBoolean(parts[1]);
                        if (null == isDirectory) {
                            break;
                        }
                        entry.setDirectory(isDirectory.booleanValue());
                        break;
                    case 2:
                        entry.setName(parts[1]);
                        break;
                    case 3:
                        Long size = Long.parseLong(parts[1]);
                        if (null == size) {
                            break;
                        }
                        entry.setSize(size.longValue());
                        break;
                }


                if (count >= 3) {
                    break;
                }

                os = new ByteArrayOutputStream();
                continue;
            }
            os.write((byte) ch);
        }

        if (count < 3) {
            entry = null;
        }

        currentEntry = entry;
        entryBytesReaded = 0;
        return entry;
    }
}
