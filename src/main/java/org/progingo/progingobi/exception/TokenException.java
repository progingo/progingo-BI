package org.progingo.progingobi.exception;


public class TokenException extends RuntimeException{

    protected String errorMsg;

    public TokenException() {
        super();
    }

    public TokenException(String errorMsg) {
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
