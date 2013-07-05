package com.nyver.filecombiner;

import com.beust.jcommander.ParameterException;


/**
 * Main class
 * @author Yuri Novitsky
 */
public class FileCombiner
{

    public static void main(String[] args)
    {
        System.out.println("File combiner");
        System.out.println();

        AppOptions options = new AppOptions();

        try {
            options.parse(args);
            options.validate();

            System.out.println(String.format("Archive name: %s", options.getArchive()));
            System.out.println();

            Combiner combiner = new Combiner(options.getArchive(), options.getFiles());

            if (options.isCombine()) {
                combiner.combine();
            } else {
                combiner.split();
            }

        } catch (ParameterException e) {
            System.out.println(e.getMessage());
            options.printHelp();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


}
