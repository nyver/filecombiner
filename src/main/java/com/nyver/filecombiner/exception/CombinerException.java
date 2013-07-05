package com.nyver.filecombiner.exception;

/**
 * Combiner main exception
 * @author Yuri Novitsky
 */
public class CombinerException extends Exception
{

    public CombinerException(String message) {
        super(message);
    }

    public CombinerException(Exception e) {
        super(e);
    }

    public CombinerException(String message, Throwable cause) {
        super(message, cause);
    }
}
