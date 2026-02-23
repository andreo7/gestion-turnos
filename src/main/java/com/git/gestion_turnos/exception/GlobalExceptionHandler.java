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
 *
 * Este clase intercepta TODAS las excepciones que se lanzan en tu aplicación
 * y las convierte en respuestas HTTP bien formateadas.
 */
@ControllerAdvice  // ← Esto hace que Spring intercepte todas las excepciones
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(NotificacionNotFoundException.class)
    public ResponseEntity<Object> handleNotificacionNotFound(
            NotificacionNotFoundException ex,
            WebRequest request) {
        // Loguear el error (importante para debugging)
        log.warn("❌ Notificación no encontrada: {}", ex.getMessage());

        // Crear respuesta estructurada
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 404);
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        body.put("errorCode", ex.getErrorCode());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        // Retornar con código HTTP 404
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // Maneja PersonaNotFoundException
    @ExceptionHandler(PersonaNotFoundException.class)
    public ResponseEntity<Object> handlePersonaNotFound(
            PersonaNotFoundException ex,
            WebRequest request){

        log.warn("❌ Persona no encontrada: {}", ex.getMessage());

        Map<String, Object> body = buildErrorBody(
                404,
                "Not Found",
                ex.getMessage(),
                ex.getErrorCode(),
                request
        );

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja CUALQUIER otra excepción no prevista
     *
     * Este es el "catch-all" (atrapa todo)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(
            Exception ex,
            WebRequest request) {

        // Loguear con STACK TRACE completo (para debugging)
        log.error("❌ Error inesperado: {}", ex.getMessage(), ex);

        Map<String, Object> body = buildErrorBody(
                500,
                "Internal Server Error",
                "Ocurrió un error inesperado. Por favor contacte al administrador.",
                "SYS_001",
                request
        );

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TurnoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTurnoNotFound(TurnoNotFoundException ex,
                                                             WebRequest request){
        log.warn("❌ Turno no encontrado: {}", ex.getMessage());

        return new ResponseEntity<>(new ErrorResponse(LocalDateTime.now(),
                404,
                "Not found",
                ex.getMessage(),
                ex.getErrorCode(),
                request.getContextPath()), HttpStatus.NOT_FOUND);
    }

    //METODO AUXILIAR PARA NO REPETIR CODIGOS EN LOS HANDLER
    private Map<String, Object> buildErrorBody(
            int status,
            String error,
            String message,
            String errorCode,
            WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status);
        body.put("error", error);
        body.put("message", message);
        body.put("errorCode", errorCode);
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return body;
    }
}
