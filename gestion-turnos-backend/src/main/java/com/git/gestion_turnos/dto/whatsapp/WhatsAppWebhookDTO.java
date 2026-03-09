package com.git.gestion_turnos.dto.whatsapp;

/**
 * DTO para parsear el webhook de Twilio WhatsApp.
 *
 * Twilio envía mensajes en formato application/x-www-form-urlencoded
 * con estos campos principales.
 */
public class WhatsAppWebhookDTO {

    // Identificador único del mensaje
    private String MessageSid;

    // Número del remitente (formato: whatsapp:+5491112345678)
    private String From;

    // Tu número de WhatsApp Business (formato: whatsapp:+14155238886)
    private String To;

    // Contenido del mensaje
    private String Body;

    // Estado del mensaje: sent, delivered, read, failed, undelivered
    private String SmsStatus;

    // Número de mensajes en la conversación
    private String NumMedia;

    // ID de la cuenta Twilio
    private String AccountSid;

    // Timestamp del mensaje
    private String MessageTimestamp;

    // Constructores
    public WhatsAppWebhookDTO() {}

    // Getters y Setters
    public String getMessageSid() {
        return MessageSid;
    }

    public void setMessageSid(String messageSid) {
        MessageSid = messageSid;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getSmsStatus() {
        return SmsStatus;
    }

    public void setSmsStatus(String smsStatus) {
        SmsStatus = smsStatus;
    }

    public String getNumMedia() {
        return NumMedia;
    }

    public void setNumMedia(String numMedia) {
        NumMedia = numMedia;
    }

    public String getAccountSid() {
        return AccountSid;
    }

    public void setAccountSid(String accountSid) {
        AccountSid = accountSid;
    }

    public String getMessageTimestamp() {
        return MessageTimestamp;
    }

    public void setMessageTimestamp(String messageTimestamp) {
        MessageTimestamp = messageTimestamp;
    }

    /**
     * Extrae el número de teléfono sin el prefijo "whatsapp:".
     * whatsapp:+5491112345678 → +5491112345678
     */
    public String getFromNumber() {
        if (From != null && From.startsWith("whatsapp:")) {
            return From.substring(9); // Elimina "whatsapp:"
        }
        return From;
    }

    /**
     * Normaliza el mensaje recibido:
     * - Elimina espacios al inicio/final
     * - Convierte a mayúsculas
     */
    public String getBodyNormalized() {
        if (Body == null) return "";
        return Body.trim().toUpperCase();
    }

    @Override
    public String toString() {
        return "WhatsAppWebhookDTO{" +
                "MessageSid='" + MessageSid + '\'' +
                ", From='" + From + '\'' +
                ", Body='" + Body + '\'' +
                ", SmsStatus='" + SmsStatus + '\'' +
                '}';
    }
}