package com.nyver.filecombiner.command;

import com.beust.jcommander.Parameters;
import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Combine command's options
 * @author Yuri Novitsky
 */
@Parameters(commandDescription = "Combine files")
public class CommandCombine
{
    @Parameter(description = "<archive> <file or directory> <file or directory> ...")
    private List<String> files = new ArrayList<String>();

    public List<String> getFiles()
    {
        return files;
    }
}
