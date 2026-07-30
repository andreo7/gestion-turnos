import { format, parseISO } from 'date-fns';
import { es } from 'date-fns/locale';

/**
 * Formatea fecha LocalDate del backend
 * @param {string} dateString - "2024-03-15"
 * @returns {string} - "15 de marzo de 2024"
 */
export const formatFecha = (dateString) => {
  if (!dateString) return '-';
  try {
    const date = parseISO(dateString);
    return format(date, "d 'de' MMMM 'de' yyyy", { locale: es });
  } catch {
    return dateString;
  }
};

/**
 * Formatea fecha en formato corto
 * @param {string} dateString
 * @returns {string} - "15/03/2024"
 */
export const formatFechaCorta = (dateString) => {
  if (!dateString) return '-';
  try {
    const date = parseISO(dateString);
    return format(date, 'dd/MM/yyyy');
  } catch {
    return dateString;
  }
};

/**
 * Formatea hora LocalTime del backend
 * @param {string} timeString - "10:30:00"
 * @returns {string} - "10:30"
 */
export const formatHora = (timeString) => {
  if (!timeString) return '-';
  return timeString.substring(0, 5); // "10:30:00" -> "10:30"
};

/**
 * Formatea LocalDateTime del backend
 * @param {string} datetimeString - "2024-03-15T10:30:00"
 * @returns {string} - "15/03/2024 10:30"
 */
export const formatFechaHora = (datetimeString) => {
  if (!datetimeString) return '-';
  try {
    const date = parseISO(datetimeString);
    return format(date, 'dd/MM/yyyy HH:mm');
  } catch {
    return datetimeString;
  }
};