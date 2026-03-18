/**
 * Modelo de Persona
 * Refleja la estructura del DTO del backend
 */
export class Persona {
  constructor(data = {}) {
    this.id = data.id || null;
    this.nombre = data.nombre || '';
    this.apellido = data.apellido || '';
    this.telefono = data.telefono || '';
    this.confirmaciones = data.confirmaciones || 0;
    this.cancelaciones = data.cancelaciones || 0;
  }

  /**
   * Obtiene el nombre completo
   */
  getNombreCompleto() {
    return `${this.nombre} ${this.apellido}`;
  }

  /**
   * Obtiene las iniciales
   */
  getIniciales() {
    return `${this.nombre.charAt(0)}${this.apellido.charAt(0)}`.toUpperCase();
  }

  /**
   * Calcula la tasa de asistencia
   */
  getTasaAsistencia() {
    const total = this.confirmaciones + this.cancelaciones;
    if (total === 0) return 0;
    return (this.confirmaciones / total) * 100;
  }

  /**
   * Verifica si es un cliente frecuente (>= 5 confirmaciones)
   */
  isClienteFrecuente() {
    return this.confirmaciones >= 5;
  }

  /**
   * Verifica si tiene muchas cancelaciones (>= 3)
   */
  tieneMuchasCancelaciones() {
    return this.cancelaciones >= 3;
  }
}