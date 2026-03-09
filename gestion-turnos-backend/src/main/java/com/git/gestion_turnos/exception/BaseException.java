package com.git.gestion_turnos.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException  {

    // Código de error único (ej: "NOTIF_001")
    private final String errorCode;

    private final HttpStatus httpStatus;

    /**
     * Constructor
     *
     * @param message Mensaje de error legible
     * @param errorCode Código único del error
     */
    public BaseException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);  // Llama al constructor de RuntimeException
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}

