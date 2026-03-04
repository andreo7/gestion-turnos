package com.git.gestion_turnos.controller;

import com.git.gestion_turnos.dto.whatsapp.WhatsAppWebhookDTO;
import com.git.gestion_turnos.service.whatsapp.WhatsAppRespuestaService;
import com.git.gestion_turnos.service.whatsapp.WhatsAppValidadorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Controller para recibir webhooks de Twilio WhatsApp.
 *
 * Este endpoint es llamado por Twilio cuando:
 * - Un usuario responde a un mensaje
 * - Un mensaje es entregado/leído
 * - Un mensaje falla
 *
 * IMPORTANTE: Este endpoint debe ser público (sin autenticación)
 * ya que Twilio lo llama desde fuera. La seguridad se maneja
 * validando la firma de Twilio.
 *
 * Configuración en Twilio Console:
 * 1. Ir a: Messaging > Settings > WhatsApp Sandbox
 * 2. "When a message comes in": https://tu-dominio.com/webhook/whatsapp
 * 3. Método: POST
 */
@RestController
@RequestMapping("/webhook/whatsapp")
public class WhatsAppWebhookController {

    private static final Logger log = LoggerFactory.getLogger(WhatsAppWebhookController.class);

    private final WhatsAppRespuestaService respuestaService;
    private final WhatsAppValidadorService validadorService;

    public WhatsAppWebhookController(
            WhatsAppRespuestaService respuestaService,
            WhatsAppValidadorService validadorService) {
        this.respuestaService = respuestaService;
        this.validadorService = validadorService;
    }

    /**
     * Endpoint principal para recibir mensajes de WhatsApp.
     *
     * Twilio envía datos como application/x-www-form-urlencoded
     * y espera una respuesta TwiML (XML).
     *
     * @param webhook Datos parseados del request
     * @param request Request completo (para validar firma)
     * @return TwiML response
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity<String> recibirMensaje(
            WhatsAppWebhookDTO webhook,
            HttpServletRequest request) {

        log.info("📥 Webhook recibido de {}: {}",
                webhook.getFromNumber(), webhook.getBody());

        try {
            // 1. Validar firma de Twilio (seguridad)
            if (!validadorService.validarFirma(request)) {
                log.warn("⚠️ Firma de Twilio inválida - posible ataque");
                return ResponseEntity.status(403)
                        .body(generarTwiML("Error de autenticación"));
            }

            // 2. Procesar respuesta
            String mensajeRespuesta = respuestaService.procesarRespuesta(webhook);

            // 3. Retornar respuesta en formato TwiML
            String twiml = generarTwiML(mensajeRespuesta);

            log.info("✅ Webhook procesado correctamente");
            return ResponseEntity.ok(twiml);

        } catch (Exception e) {
            log.error("❌ Error procesando webhook", e);
            String twimlError = generarTwiML(
                    "Ocurrió un error procesando su mensaje. Intente nuevamente."
            );
            return ResponseEntity.status(500).body(twimlError);
        }
    }

    /**
     * Endpoint para verificar estado (Twilio hace GET primero).
     *
     * Cuando configuras el webhook en Twilio, hace un GET
     * para verificar que el endpoint existe.
     */
    @GetMapping
    public ResponseEntity<String> verificarEstado() {
        log.info("🔍 Verificación GET del webhook");
        return ResponseEntity.ok("Webhook activo");
    }

    /**
     * Endpoint de prueba para desarrollo (solo dev).
     *
     * Simula un mensaje de WhatsApp sin necesitar Twilio.
     *
     * Uso:
     * curl -X POST http://localhost:8080/webhook/whatsapp/test \
     *   -H "Content-Type: application/json" \
     *   -d '{"telefono": "+5491112345678", "mensaje": "SI"}'
     */
    @PostMapping("/test")
    public ResponseEntity<Map<String, String>> testMensaje(
            @RequestBody Map<String, String> payload) {

        log.info("🧪 TEST: Simulando mensaje de {}: {}",
                payload.get("telefono"), payload.get("mensaje"));

        WhatsAppWebhookDTO webhook = new WhatsAppWebhookDTO();
        webhook.setFrom("whatsapp:" + payload.get("telefono"));
        webhook.setBody(payload.get("mensaje"));
        webhook.setMessageSid("TEST_" + System.currentTimeMillis());

        String respuesta = respuestaService.procesarRespuesta(webhook);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "respuesta", respuesta
        ));
    }

    /**
     * Genera respuesta TwiML para Twilio.
     *
     * TwiML es el formato XML que Twilio espera para enviar
     * mensajes de vuelta al usuario.
     *
     * Ejemplo:
     * <?xml version="1.0" encoding="UTF-8"?>
     * <Response>
     *   <Message>Su turno ha sido confirmado</Message>
     * </Response>
     */
    private String generarTwiML(String mensaje) {
        return String.format(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<Response>\n" +
                        "  <Message>%s</Message>\n" +
                        "</Response>",
                escaparXML(mensaje)
        );
    }

    /**
     * Escapa caracteres especiales para XML.
     */
    private String escaparXML(String texto) {
        if (texto == null) return "";
        return texto
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}


