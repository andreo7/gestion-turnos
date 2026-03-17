export const EstadoTurno = {
  DISPONIBLE: 'DISPONIBLE',
  RESERVADO: 'RESERVADO',
  CONFIRMADO: 'CONFIRMADO',
  CANCELADO: 'CANCELADO',
};

export const TipoNotificacion = {
  TURNO_CONFIRMADO: 'TURNO_CONFIRMADO',
  TURNO_CANCELADO: 'TURNO_CANCELADO',
  RECORDATORIO: 'RECORDATORIO',
};

/**
 * Funcion que define un objeto.
 * Obtiene clases CSS para el estado del turno
 */
export const getEstadoBadgeClass = (estado) => {
  const classes = {
    DISPONIBLE: 'bg-green-100 text-green-800 border-green-300',
    RESERVADO: 'bg-yellow-100 text-yellow-800 border-yellow-300',
    CONFIRMADO: 'bg-blue-100 text-blue-800 border-blue-300',
    CANCELADO: 'bg-red-100 text-red-800 border-red-300',
  };

  return classes[estado] || 'bg-gray-100 text-gray-800'; //Devuelve el color segun el estado.
};


export const getEstadoLabel = (estado) => {
  const labels = {
    DISPONIBLE: 'Disponible',
    RESERVADO: 'Reservado',
    CONFIRMADO: 'Confirmado',
    CANCELADO: 'Cancelado',
  };
  
  return labels[estado] || estado;
};