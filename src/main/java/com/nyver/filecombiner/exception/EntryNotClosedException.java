package com.nyver.filecombiner.exception;

/**
 * @author Yuri Novitsky
 */
public class EntryNotClosedException extends CombinerException
{

    public EntryNotClosedException(String message) {
        super(message);
    }

    public EntryNotClosedException(Exception e) {
        super(e);
    }

    public EntryNotClosedException(String message, Throwable cause) {
        super(message, cause);
    }
}
