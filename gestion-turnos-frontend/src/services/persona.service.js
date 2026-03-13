import api from '@/api/axios.config';

export const personaService = {
  /**
   * Obtiene lista paginada de personas
   * @param {number} page - Número de página (default 0)
   * @param {number} size - Tamaño de página (default 10)
   * @returns {Promise<Page<PersonaDTO>>}
   */
  getAll: async (page = 0, size = 10) => {
    const response = await api.get('/personas', {
      params: { page, size }
    });
    return response.data;
  },

  /**
   * Obtiene detalle de una persona
   * @param {number} id
   * @returns {Promise<PersonaDetalleDTO>}
   */
  getById: async (id) => {
    const response = await api.get(`/personas/${id}`);
    return response.data;
  },

  /**
   * Crea una nueva persona
   * @param {PersonaDTO} personaData
   * @returns {Promise<PersonaDTO>}
   */
  create: async (personaData) => {
    const response = await api.post('/personas', personaData);
    return response.data;
  },

  /**
   * Actualiza una persona
   * @param {number} id
   * @param {PersonaDTO} personaData
   * @returns {Promise<PersonaDTO>}
   */
  update: async (id, personaData) => {
    const response = await api.put(`/personas/${id}`, personaData);
    return response.data;
  },

  /**
   * Elimina una persona
   * @param {number} id
   * @returns {Promise<void>}
   */
  delete: async (id) => {
    await api.delete(`/personas/${id}`);
  },

  /**
   * Obtiene historial de turnos de una persona
   * @param {number} personaId
   * @param {Object} params - { estadoTurno?, page, size, sort }
   * @returns {Promise<Page<HistorialDetalleDTO>>}
   */
  getHistorial: async (personaId, params = {}) => {
    const response = await api.get(`/personas/${personaId}/historial`, {
      params: {
        page: params.page || 0,
        size: params.size || 10,
        sort: params.sort || 'fechaHoraActualizacion,desc',
        estadoTurno: params.estadoTurno
      }
    });
    return response.data;
  }
};