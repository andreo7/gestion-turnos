package com.git.gestion_turnos.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MANEJADOR GLOBAL DE EXCEPCIONES
 * Este clase intercepta TODAS las excepciones que se lanzan en la aplicación
 * y las convierte en respuestas HTTP bien formateadas.
 */
@ControllerAdvice  // ← Esto hace que Spring intercepte todas las excepciones
public class GlobalExceptionHandler {

    //Logger para debuggear usando warns
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotificacionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotificacionNotFound(NotificacionNotFoundException ex,
                                                             WebRequest request) {
        log.warn("❌ Notificación no encontrada: {}", ex.getMessage(), ex);

        return new ResponseEntity<>(new ErrorResponse(LocalDateTime.now(),
                404,
                "Not found",
                ex.getMessage(),
                ex.getErrorCode(),
                request.getDescription(false).replace("uri=", "")), HttpStatus.NOT_FOUND);

    }

    // Maneja PersonaNotFoundException
    @ExceptionHandler(PersonaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePersonaNotFound(PersonaNotFoundException ex,
                                                        WebRequest request){
        log.warn("❌ Persona no encontrada: {}", ex.getMessage(), ex);

        return new ResponseEntity<>(new ErrorResponse(LocalDateTime.now(),
                404,
                "Not found",
                ex.getMessage(),
                ex.getErrorCode(),
                request.getDescription(false).replace("uri=", "")), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TurnoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTurnoNotFound(TurnoNotFoundException ex,
                                                             WebRequest request){
        //El ultimo parametro imprime el STACK TRACE.
        log.warn("❌ Turno no encontrado: {}", ex.getMessage(), ex);

        return new ResponseEntity<>(new ErrorResponse(LocalDateTime.now(),
                404,
                "Not found",
                ex.getMessage(),
                ex.getErrorCode(),
                request.getDescription(false).replace("uri=", "")), HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja CUALQUIER otra excepción no prevista
     * Este es el "catch-all" (atrapa todo)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(
            Exception ex,
            WebRequest request) {

        // Loguear con STACK TRACE completo (para debugging)
        log.error("❌ Error inesperado: {}", ex.getMessage(), ex);

        Map<String, Object> body = new LinkedHashMap<>();
                body.put("status", 500);
                body.put("error", "Internal Server Error");
                body.put("message", "Ocurrió un error inesperado. Por favor contacte al administrador.");
                body.put("errorCode", "SYS_001");
                body.put("path", request.getDescription(false).replace("uri", ""));

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
