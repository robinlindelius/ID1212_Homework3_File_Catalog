package se.kth.id1212.server.model;

/**
 * Created by Robin on 2017-12-02.
 */
public class UserException extends Exception {
    /**
     * Create a new instance thrown because of the specified reason.
     *
     * @param reason Why the exception was thrown.
     */
    public UserException(String reason) {
        super(reason);
    }

    /**
     * Create a new instance thrown because of the specified reason and exception.
     *
     * @param reason    Why the exception was thrown.
     * @param rootCause The exception that caused this exception to be thrown.
     */
    public UserException(String reason, Throwable rootCause) {
        super(reason, rootCause);
    }
}
