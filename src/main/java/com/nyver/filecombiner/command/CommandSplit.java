package com.nyver.filecombiner.command;

import com.beust.jcommander.Parameters;
import com.beust.jcommander.Parameter;

/**
 * Split command's options
 * @author Yuri Novitsky
 */
@Parameters(commandDescription = "Split file")
public class CommandSplit
{
    @Parameter(description = "<archive>", required = true)
    private String file = "";

    public String getFile()
    {
        return file;
    }
}
