/**
 * Validadores custom para formularios
 */

/**
 * Valida formato de teléfono argentino
 */
export const validarTelefonoArgentino = (telefono) => {
  const regex = /^\+54\d{10}$/;
  return regex.test(telefono);
};

/**
 * Valida que la fecha no sea pasada
 */
export const validarFechaFutura = (fecha) => {
  const hoy = new Date();
  hoy.setHours(0, 0, 0, 0);
  const fechaSeleccionada = new Date(fecha);
  return fechaSeleccionada >= hoy;
};

/**
 * Valida formato de hora (HH:mm)
 */
export const validarFormatoHora = (hora) => {
  const regex = /^([01]\d|2[0-3]):([0-5]\d)$/;
  return regex.test(hora);
};

/**
 * Valida que un string no esté vacío después de trim
 */
export const validarNoVacio = (valor) => {
  return valor && valor.trim().length > 0;
};

/**
 * Sanitiza input de teléfono (elimina espacios y caracteres especiales excepto +)
 */
export const sanitizarTelefono = (telefono) => {
  return telefono.replace(/[^\d+]/g, '');
};