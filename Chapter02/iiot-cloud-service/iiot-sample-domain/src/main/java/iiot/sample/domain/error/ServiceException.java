package iiot.sample.domain.error;

import lombok.Getter;

public class ServiceException extends RuntimeException {

    @Getter
    private final ErrorCode errorCode;

    public ServiceException(ErrorCode errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    public String getPreparedErrorMessage(){
        return getErrorCode().getDefaultMessage() + ":" + getMessage();
    }

    @Override
    public String toString() {
        return this.errorCode.name() + ":" + getPreparedErrorMessage();
    }
}
