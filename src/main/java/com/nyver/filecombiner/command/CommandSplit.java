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
    @Parameter(description = "<archive>", required = true, arity = 1)
    List<String> files = new ArrayList<String>();

    public String getFile()
    {
        return files.get(0);
    }
}
