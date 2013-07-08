package com.nyver.filecombiner.command;

import com.beust.jcommander.Parameters;
import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Split command's options
 * @author Yuri Novitsky
 */
@Parameters(commandDescription = "Split file")
public class CommandSplit
{
    @Parameter(names = "-compression", description = "Compression algorithms: dummy", required = false)
    private String compression = "";

    @Parameter(description = "<archive> <path to extract>", required = true, arity = 2)
    List<String> files = new ArrayList<String>();

    public String getFile()
    {
        return files.get(0);
    }

    public String getPath()
    {
        return files.get(1);
    }

    public String getCompression()
    {
        return compression;
    }
}
