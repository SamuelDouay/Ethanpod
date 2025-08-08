package fr.github.ethanpod.exception;

public class EthanpodRuntimeException extends RuntimeException {
    private final String errorCode;

    public EthanpodRuntimeException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public EthanpodRuntimeException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public static EthanpodRuntimeException systemError(String message, Throwable cause) {
        return new EthanpodRuntimeException("SYSTEM_ERROR", message, cause);
    }

    public static EthanpodRuntimeException configurationError(String message) {
        return new EthanpodRuntimeException("CONFIG_ERROR", message);
    }

    public String getErrorCode() {
        return errorCode;
    }
}
