package com.nyver.filecombiner.compressors.combiner;

import com.nyver.filecombiner.compressors.AbstractOutputStream;
import com.nyver.filecombiner.compressors.Entry;
import com.nyver.filecombiner.exception.EntryNotClosedException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Combiner output stream
 * @author Yuri Novitsky
 */
public class CombinerOutputStream extends AbstractOutputStream
{
    private long bytesWritten = 0;

    private OutputStream out;

    private boolean isEntryClosed = true;

    private boolean closed = false;

    public CombinerOutputStream(OutputStream out)
    {
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException
    {
        out.write(b);
    }

    @Override
    public void putCompressorEntry(Entry entry) throws EntryNotClosedException, IOException {
        if (!isEntryClosed) {
            throw new EntryNotClosedException("Entry must be closed");
        }

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("name", entry.getName());
        headers.put("dir", String.valueOf(entry.isDirectory()));
        headers.put("size", String.valueOf(entry.getSize()));

        StringWriter writer = new StringWriter();

        for(Map.Entry<String, String> item: headers.entrySet()) {
            String line = item.getKey() + "=" + item.getValue() + "\n";
            writer.write(line);
        }

        byte[] data = writer.toString().getBytes(Charset.forName("UTF-8"));
        write(data);

        isEntryClosed = false;
    }

    @Override
    public void closeCompressorEntry()
    {
        isEntryClosed = true;
    }

    public long getBytesWritten()
    {
        return bytesWritten;
    }

    @Override
    public void close() throws IOException {
        if (!closed) {
            out.close();
            closed = true;
        }
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }
}
