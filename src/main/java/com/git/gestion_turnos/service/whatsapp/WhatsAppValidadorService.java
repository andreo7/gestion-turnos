package com.git.gestion_turnos.service.whatsapp;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Servicio para validar que los webhooks vienen realmente de Twilio.
 *
 * Twilio firma cada request con HMAC-SHA1 usando tu Auth Token.
 * Esto previene ataques donde alguien intenta enviar requests falsos
 * a tu webhook haciéndose pasar por Twilio.
 */
@Service
public class WhatsAppValidadorService {

    private static final Logger log = LoggerFactory.getLogger(WhatsAppValidadorService.class);

    @Value("${whatsapp.auth.token:}")
    private String authToken;

    @Value("${whatsapp.validate.signature:true}")
    private boolean validateSignature;

    @Value("${whatsapp.webhook.url:}")
    private String webhookUrl;

    /**
     * Valida que el request viene de Twilio verificando la firma.
     *
     * @param request HttpServletRequest con headers y parámetros
     * @return true si la firma es válida o la validación está deshabilitada
     */
    public boolean validarFirma(HttpServletRequest request) {

        // En desarrollo, permitir desactivar validación
        if (!validateSignature) {
            log.warn("🧪 Validación de firma deshabilitada (solo desarrollo)");
            return true;
        }

        try {
            // 1. Obtener firma del header
            String twilioSignature = request.getHeader("X-Twilio-Signature");
            if (twilioSignature == null || twilioSignature.isEmpty()) {
                log.warn("❌ No se encontró header X-Twilio-Signature");
                return false;
            }

            // 2. Construir URL completa del webhook
            String url = construirURLCompleta(request);

            // 3. Obtener parámetros del request
            Map<String, String> parametros = obtenerParametros(request);

            // 4. Calcular firma esperada
            String firmaCalculada = calcularFirma(url, parametros, authToken);

            // 5. Comparar firmas
            boolean esValida = firmaCalculada.equals(twilioSignature);

            if (!esValida) {
                log.warn("❌ Firma inválida. Esperada: {}, Recibida: {}",
                        firmaCalculada, twilioSignature);
            }

            return esValida;

        } catch (Exception e) {
            log.error("❌ Error validando firma de Twilio", e);
            return false;
        }
    }

    /**
     * Calcula la firma HMAC-SHA1 según el algoritmo de Twilio.
     *
     * Algoritmo:
     * 1. Concatenar URL + parámetros ordenados alfabéticamente
     * 2. Calcular HMAC-SHA1 usando Auth Token como clave
     * 3. Codificar en Base64
     */
    private String calcularFirma(String url, Map<String, String> parametros, String authToken)
            throws NoSuchAlgorithmException, InvalidKeyException {

        // 1. Construir string para firmar
        StringBuilder builder = new StringBuilder(url);

        // Ordenar parámetros alfabéticamente
        List<String> keys = new ArrayList<>(parametros.keySet());
        Collections.sort(keys);

        for (String key : keys) {
            builder.append(key).append(parametros.get(key));
        }

        String dataToSign = builder.toString();

        // 2. Calcular HMAC-SHA1
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secretKey = new SecretKeySpec(
                authToken.getBytes(StandardCharsets.UTF_8),
                "HmacSHA1"
        );
        mac.init(secretKey);

        byte[] hash = mac.doFinal(dataToSign.getBytes(StandardCharsets.UTF_8));

        // 3. Codificar en Base64
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Construye la URL completa del webhook.
     *
     * Si está configurado webhookUrl, usa ese.
     * Sino, intenta construir desde el request (puede fallar con proxies).
     */
    private String construirURLCompleta(HttpServletRequest request) {
        if (webhookUrl != null && !webhookUrl.isEmpty()) {
            return webhookUrl;
        }

        // Construir desde request (funciona solo sin proxies)
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();

        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);

        if ((scheme.equals("http") && serverPort != 80) ||
                (scheme.equals("https") && serverPort != 443)) {
            url.append(":").append(serverPort);
        }

        url.append(contextPath).append(servletPath);

        return url.toString();
    }

    /**
     * Obtiene todos los parámetros del request.
     */
    private Map<String, String> obtenerParametros(HttpServletRequest request) {
        Map<String, String> parametros = new HashMap<>();

        Enumeration<String> nombres = request.getParameterNames();
        while (nombres.hasMoreElements()) {
            String nombre = nombres.nextElement();
            String valor = request.getParameter(nombre);
            parametros.put(nombre, valor);
        }

        return parametros;
    }

    /**
     * Valida manualmente una firma (útil para testing).
     */
    public boolean validarFirmaManual(
            String url,
            Map<String, String> parametros,
            String firmaRecibida) {
        try {
            String firmaCalculada = calcularFirma(url, parametros, authToken);
            return firmaCalculada.equals(firmaRecibida);
        } catch (Exception e) {
            log.error("Error validando firma manual", e);
            return false;
        }
    }
}