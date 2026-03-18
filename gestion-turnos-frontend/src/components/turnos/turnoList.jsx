import { TurnoCard } from './turnoCard';
import { Loader2 } from 'lucide-react';

/**
 * Lista de turnos con grid responsive
 */
export const TurnoList = ({ 
  turnos = [], 
  loading = false,
  onReservar,
  onCancelar,
  onVerCliente 
}) => {
  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <Loader2 className="w-8 h-8 animate-spin text-blue-600" />
        <span className="ml-2 text-gray-600">Cargando turnos...</span>
      </div>
    );
  }

  if (turnos.length === 0) {
    return (
      <div className="text-center py-12 bg-white rounded-lg border">
        <p className="text-gray-500">No hay turnos disponibles</p>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
      {turnos.map((turno) => (
        <TurnoCard
          key={turno.id}
          turno={turno}
          onReservar={onReservar}
          onCancelar={onCancelar}
          onVerCliente={onVerCliente}
        />
      ))}
    </div>
  );
};