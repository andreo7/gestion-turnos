import api from '@/api/axios.config';

export const historialTurnoService = {

    /**
   * Obtiene métricas mensuales
   * @param {number} anio
   * @param {number} mes
   * @returns {Promise<HistorialTurnoMensualDTO>}
   */
    getMetricasMensuales: async (anio, mes) => {
        const response = await api.get('/historial-turnos', {
            params: {anio, mes}
        });
        return response.data; 
    } 
};