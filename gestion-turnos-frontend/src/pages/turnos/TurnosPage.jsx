import { useState } from 'react';
import { useTurnos, useCancelarTurno } from '@/hooks/useTurnos';
import { TurnoCard } from '@/components/turnos/TurnoCard';
import { EstadoTurno } from '@/utils/constants';
import { Loader2, AlertCircle } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

export const TurnosPage = () => {
  const navigate = useNavigate();
  const [filtroEstado, setFiltroEstado] = useState('TODOS');

  // React Query
  const { data: turnos = [], isLoading, error } = useTurnos();
  const cancelarMutation = useCancelarTurno();

  // Filtrar turnos
  const turnosFiltrados = turnos.filter(turno => {
    if (filtroEstado === 'TODOS') return true;
    return turno.estado === filtroEstado;
  });

  // Handlers
  const handleReservar = (turnoId) => {
    navigate(/turnos/reservar/${turnoId});
  };

  const handleCancelar = async (turnoId) => {
    if (!confirm('¿Estás seguro de cancelar este turno?')) return;

    try {
      await cancelarMutation.mutateAsync(turnoId);
      alert('Turno cancelado exitosamente');
    } catch (error) {
      alert('Error al cancelar turno: ' + error.response?.data?.message);
    }
  };

  const handleVerCliente = (personaId) => {
    navigate(/personas/${personaId});
  };

  // Loading state
  if (isLoading) {
    return (
      <div className="flex justify-center items-center h-64">
        <Loader2 className="w-8 h-8 animate-spin text-blue-600" />
        <span className="ml-2 text-gray-600">Cargando turnos...</span>
      </div>
    );
  }

  // Error state
  if (error) {
    return (
      <div className="bg-red-50 border border-red-200 rounded-lg p-4">
        <div className="flex items-center">
          <AlertCircle className="w-5 h-5 text-red-600 mr-2" />
          <span className="text-red-800">Error al cargar turnos: {error.message}</span>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      {/* Header */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-2">Gestión de Turnos</h1>
        <p className="text-gray-600">Administra los turnos disponibles y reservas</p>
      </div>

      {/* Filtros */}
      <div className="bg-white rounded-lg shadow-sm p-4 mb-6">
        <div className="flex flex-wrap gap-2">
          <button
            onClick={() => setFiltroEstado('TODOS')}
            className={`px-4 py-2 rounded-md transition-colors ${
              filtroEstado === 'TODOS'
                ? 'bg-blue-600 text-white'
                : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
            }`}
          >
            Todos ({turnos.length})
          </button>
          {Object.values(EstadoTurno).map(estado => (
            <button
              key={estado}
              onClick={() => setFiltroEstado(estado)}
              className={`px-4 py-2 rounded-md transition-colors ${
                filtroEstado === estado
                  ? 'bg-blue-600 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              }`}
            >
              {estado} ({turnos.filter(t => t.estado === estado).length})
            </button>
          ))}
        </div>
      </div>

      {/* Grid de Turnos */}
      {turnosFiltrados.length === 0 ? (
        <div className="text-center py-12">
          <p className="text-gray-500">No hay turnos para mostrar</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
          {turnosFiltrados.map(turno => (
            <TurnoCard
              key={turno.id}
              turno={turno}
              onReservar={handleReservar}
              onCancelar={handleCancelar}
              onVerCliente={handleVerCliente}
            />
          ))}
        </div>
      )}
    </div>
  );
};