package com.git.gestion_turnos.exception;

public abstract class BaseException extends RuntimeException  {

    // Código de error único (ej: "NOTIF_001")
    private final String errorCode;

    /**
     * Constructor
     *
     * @param message Mensaje de error legible
     * @param errorCode Código único del error
     */
    public BaseException(String message, String errorCode) {
        super(message);  // Llama al constructor de RuntimeException
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

