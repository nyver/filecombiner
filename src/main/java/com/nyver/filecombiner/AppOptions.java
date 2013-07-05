package com.nyver.filecombiner;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.nyver.filecombiner.command.CommandCombine;
import com.nyver.filecombiner.command.CommandSplit;

import java.util.ArrayList;
import java.util.List;

/**
 * Application options
 * @author Yuri Novitsky
 */
public class AppOptions
{
    public static final String COMMAND_COMBINE = "combine";
    public static final String COMMAND_SPLIT = "split";

    private JCommander commander = new JCommander();
    private CommandCombine commandCombine = new CommandCombine();
    private CommandSplit commandSplit = new CommandSplit();

    private String command = "";

    private String archive;
    private List<String> files = new ArrayList<String>();

    public AppOptions()
    {
        commander.addCommand(COMMAND_COMBINE, commandCombine);
        commander.addCommand(COMMAND_SPLIT, commandSplit);
    }

    /**
     * Parse arguments
     * @param args
     */
    public void parse(String[] args)
    {

        commander.parse(args);

        command = null == commander.getParsedCommand() ? "" : commander.getParsedCommand();
    }

    /**
     * Validate options
     * @return
     */
    public boolean validate()
    {
        if (!command.equals(COMMAND_COMBINE) && !command.equals(COMMAND_SPLIT)) {
            throw new ParameterException(String.format("Command \"%s\" or \"%s\" is not exists", COMMAND_COMBINE, COMMAND_SPLIT));
        }

        if (command.equals(COMMAND_COMBINE)) {
            if (commandCombine.getFiles().size() < 2) {
                throw new ParameterException("Expected file or directory");
            }

            archive = commandCombine.getFiles().get(0);
            files = commandCombine.getFiles().subList(1, commandCombine.getFiles().size());
        } else {
            if (commandSplit.getFile().isEmpty()) {
                throw new ParameterException("Expected archive");
            }

            archive = commandSplit.getFile();
        }

        return true;
    }

    /**
     * Print help information
     */
    public void printHelp()
    {
        if (null != commander) {
            commander.usage();
        }
    }

    /**
     * Get true if command is combine
     * @return
     */
    public boolean isCombine()
    {
        return command.equals(COMMAND_COMBINE);
    }

    /**
     * Get archive name
     * @return
     */
    public String getArchive()
    {
        return archive;
    }

    /**
     * Get files to add
     * @return
     */
    public List<String> getFiles()
    {
        return files;
    }
}
