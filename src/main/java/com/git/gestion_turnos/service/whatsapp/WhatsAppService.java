package com.git.gestion_turnos.service.whatsapp;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppService {
    private static final Logger log = LoggerFactory.getLogger(WhatsAppService.class);

    @Value("${whatsapp.api.url:https://api.twilio.com/2010-04-01/Accounts}")
    private String apiUrl;

    @Value("${whatsapp.account.sid}")
    private String accountSid;

    @Value("${whatsapp.auth.token}")
    private String authToken;

    @Value("${whatsapp.from.number}") // Sandbox Twilio
    private String fromNumber;

    @Value("${whatsapp.enabled:true}")
    private boolean whatsappEnabled;

    /**
     * @PostConstruct = Este método se ejecuta UNA VEZ cuando Spring crea el servicio
     * Es como el "setup" o "inicialización"
     */
    @PostConstruct
    public void init() {
        // Inicializar Twilio con tus credenciales
        // Es como "iniciar sesión" en Twilio
        Twilio.init(accountSid, authToken);
        log.info("✅ Twilio WhatsApp inicializado correctamente");
    }

    /**
     * Envía un mensaje de WhatsApp
     *
     * @param telefono Número del destinatario (ej: "+5493512345678")
     * @param mensaje El texto a enviar
     * @return El SID del mensaje (ID único que Twilio le da al mensaje)
     */
    public String enviarMensaje(String telefono, String mensaje) {
        // Validaciones
        if (telefono == null || telefono.isBlank()) {
            throw new RuntimeException("Número de teléfono inválido");
        }

        if (mensaje == null || mensaje.isBlank()) {
            throw new RuntimeException("Mensaje vacío");
        }

        // Modo desarrollo: simula envío sin consumir API
        if (!whatsappEnabled) {
            log.warn("🧪 [MODO DEV] Simulando envío de WhatsApp a {}: {}",
                    telefono, mensaje.substring(0, Math.min(50, mensaje.length())));
            return "Mensajee dev";
        }

        try {
            // 1. Formatear el teléfono (asegurar que tenga el formato correcto)
            String telefonoFormateado = formatearNumero(telefono);

            log.info("📤 Enviando WhatsApp a {}: '{}'", telefono, mensaje);


            // 2. Crear el mensaje usando la API de Twilio
            Message message = Message.creator(
                    // A: (destinatario) → Debe empezar con "whatsapp:"
                    new PhoneNumber(telefonoFormateado),

                    // De: (remitente) → Tu número de WhatsApp de Twilio
                    new PhoneNumber(fromNumber),

                    // Cuerpo: El texto del mensaje
                    mensaje

            ).create();  // .create() = ENVIAR AHORA

            // 3. Twilio responde con un SID (ID del mensaje)
            log.info("✅ Mensaje enviado exitosamente. SID: {}", message.getSid());

            // 4. Retornar el SID por si lo necesitas guardar
            return message.getSid();
        } catch(Exception e) {
            // Si algo falla, loguear el error
            log.error("❌ Error enviando WhatsApp a {}: {}", telefono, e.getMessage(), e);
            throw new RuntimeException("Error enviando WhatsApp", e);
        }
    }


    private String formatearNumero(String numero) {

        // Eliminar todo lo que no sea número
        String limpio = numero.replaceAll("[^0-9]", "");

        // Si empieza con 0 (ej: 0358...), quitarlo
        if (limpio.startsWith("0")) {
            limpio = limpio.substring(1);
        }

        // Si no empieza con código país, agregar Argentina móvil (+549)
        if (!limpio.startsWith("54")) {
            limpio = "549" + limpio;
        }

        return "whatsapp:+" + limpio;
    }
}
