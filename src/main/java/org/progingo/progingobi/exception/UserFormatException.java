package org.progingo.progingobi.exception;

public class UserFormatException extends RuntimeException{
    protected String errorMsg;

    public UserFormatException() {
        super();
    }

    public UserFormatException(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    @Override
    public String getMessage() {
        return errorMsg;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }
}
