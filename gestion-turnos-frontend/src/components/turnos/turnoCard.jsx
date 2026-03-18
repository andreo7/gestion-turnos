import { formatFechaCorta, formatHora } from '@/utils/dateFormatter';
import { Badge } from '@/components/ui/Badge';
import { Calendar, Clock, User } from 'lucide-react';
import { EstadoTurno } from '@/utils/constants';

/**
 * Card para mostrar un turno
 */
export const TurnoCard = ({ turno, onReservar, onCancelar, onVerCliente }) => {
  const { id, fecha, hora, estado, cliente } = turno;

  const handleAction = () => {
    if (estado === EstadoTurno.DISPONIBLE) {
      onReservar?.(id);
    } else if (estado === EstadoTurno.RESERVADO || estado === EstadoTurno.CONFIRMADO) {
      // Podría abrir modal con opciones
      onVerCliente?.(cliente.id);
    }
  };

  return (
    <div className="bg-white border rounded-lg p-4 hover:shadow-md transition-shadow">
      {/* Header */}
      <div className="flex justify-between items-start mb-3">
        <Badge estado={estado} />
        <span className="text-sm text-gray-500">#{id}</span>
      </div>

      {/* Fecha y Hora */}
      <div className="space-y-2 mb-4">
        <div className="flex items-center text-gray-700">
          <Calendar className="w-4 h-4 mr-2" />
          <span className="text-sm font-medium">{formatFechaCorta(fecha)}</span>
        </div>
        <div className="flex items-center text-gray-700">
          <Clock className="w-4 h-4 mr-2" />
          <span className="text-sm font-medium">{formatHora(hora)}</span>
        </div>
      </div>

      {/* Cliente */}
      {cliente && (
        <div className="flex items-center text-gray-600 mb-4 pb-4 border-b">
          <User className="w-4 h-4 mr-2" />
          <span className="text-sm">
            {cliente.nombre} {cliente.apellido}
          </span>
        </div>
      )}

      {/* Acciones */}
      <div className="flex gap-2">
        {estado === EstadoTurno.DISPONIBLE && (
          <button
            onClick={handleAction}
            className="flex-1 bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition-colors text-sm font-medium"
          >
            Reservar
          </button>
        )}

        {(estado === EstadoTurno.RESERVADO || estado === EstadoTurno.CONFIRMADO) && (
          <>
            <button
              onClick={() => onVerCliente?.(cliente.id)}
              className="flex-1 bg-gray-100 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-200 transition-colors text-sm font-medium"
            >
              Ver Cliente
            </button>
            <button
              onClick={() => onCancelar?.(id)}
              className="flex-1 bg-red-100 text-red-700 px-4 py-2 rounded-md hover:bg-red-200 transition-colors text-sm font-medium"
            >
              Cancelar
            </button>
          </>
        )}
      </div>
    </div>
  );
};