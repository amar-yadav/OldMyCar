package com.example.amar.mycar.exceptions;

/**
 * Thrown when there is a "NO DATA" message.
 *
 * @author pires
 * @version $Id: $Id
 */
public class NoDataException extends ResponseException {

    /**
     * <p>Constructor for NoDataException.</p>
     */
    public NoDataException() {
        super("NO DATA");
    }

}
