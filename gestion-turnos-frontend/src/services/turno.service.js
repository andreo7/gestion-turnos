import api from '@/api/axios.config';


export const turnoService = {
  /**
   * Obtiene todos los turnos
   * @returns {Promise<TurnoDTO[]>}
   */
  getAll: async () => {
    const response = await api.get('/turnos');
    return response.data;
  },

  /**
   * Obtiene un turno por ID
   * @param {number} id
   * @returns {Promise<TurnoDTO>}
   */
  getById: async (id) => {
    const response = await api.get(`/turnos/${id}`);
    return response.data;
  },

  /**
   * Obtiene turnos disponibles
   * @returns {Promise<TurnoDTO[]>}
   */
  getDisponibles: async () => {
    const response = await api.get('/turnos/disponibles');
    return response.data;
  },

  /**
   * Obtiene turnos ocupados (RESERVADO o CONFIRMADO)
   * @returns {Promise<TurnoDTO[]>}
   */
  getOcupados: async () => {
    const response = await api.get('/turnos/ocupados');
    return response.data;
  },

  /**
   * Reserva un turno
   * @param {number} turnoId
   * @param {PersonaDTO} personaData
   * @returns {Promise<TurnoDTO>}
   */
  reservar: async (turnoId, personaData) => {
    const response = await api.patch(`/turnos/${turnoId}/reservar`, personaData);
    return response.data;
  },

  /**
   * Cancela un turno
   * @param {number} turnoId
   * @returns {Promise<TurnoDTO>}
   */
  cancelar: async (turnoId) => {
    const response = await api.patch(`/turnos/${turnoId}/cancelar`);
    return response.data;
  },

  /**
   * Genera turnos para un mes (admin)
   * @param {number} anio
   * @param {number} mes
   * @returns {Promise<void>}
   */
  crearTurnosMes: async (anio, mes) => {
    await api.post('/turnos/crear', null, {
      params: { anio, mes }
    });
  }
};