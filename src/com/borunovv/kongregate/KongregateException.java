package com.borunovv.kongregate;

/**
 * @author borunovv
 */
public class KongregateException extends Exception {

    public KongregateException() {
    }

    public KongregateException(String message) {
        super(message);
    }

    public KongregateException(String message, Throwable cause) {
        super(message, cause);
    }

    public KongregateException(Throwable cause) {
        super(cause);
    }

    public KongregateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
