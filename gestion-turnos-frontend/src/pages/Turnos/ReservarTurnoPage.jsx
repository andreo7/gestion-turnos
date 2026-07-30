import { useParams, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useTurno, useReservarTurno } from '@/hooks/useTurnos';
import { formatFechaCorta, formatHora } from '@/utils/dateFormatter';
import { Loader2, Calendar, Clock, CheckCircle } from 'lucide-react';
import { useState } from 'react';

// Validación con Zod (como @Valid en Spring)
const schema = z.object({
  nombre: z.string()
    .min(2, 'Mínimo 2 caracteres')
    .max(50, 'Máximo 50 caracteres'),
  apellido: z.string()
    .min(2, 'Mínimo 2 caracteres')
    .max(50, 'Máximo 50 caracteres'),
  telefono: z.string()
    .regex(/^[0-9+ ]{6,20}$/, 'Teléfono inválido')
});

export const ReservarTurnoPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [showSuccess, setShowSuccess] = useState(false);

  // React Query
  const { data: turno, isLoading: loadingTurno } = useTurno(id);
  const reservarMutation = useReservarTurno();

  // React Hook Form
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting }
  } = useForm({
    resolver: zodResolver(schema)
  });

  // Submit Handler
  const onSubmit = async (data) => {
    try {
      await reservarMutation.mutateAsync({
        turnoId: id,
        personaData: data
      });

      setShowSuccess(true);

      // Redirigir después de 2 segundos
      setTimeout(() => {
        navigate('/turnos');
      }, 2000);

    } catch (error) {
      const errorMsg = error.response?.data?.message || 'Error al reservar turno';
      alert(errorMsg);
    }
  };

  if (loadingTurno) {
    return (
      <div className="flex justify-center items-center h-64">
        <Loader2 className="w-8 h-8 animate-spin text-blue-600" />
      </div>
    );
  }

  if (!turno) {
    return <div className="text-center py-12">Turno no encontrado</div>;
  }

  // Success State
  if (showSuccess) {
    return (
      <div className="max-w-md mx-auto mt-12">
        <div className="bg-green-50 border-2 border-green-200 rounded-lg p-8 text-center">
          <CheckCircle className="w-16 h-16 text-green-600 mx-auto mb-4" />
          <h2 className="text-2xl font-bold text-green-900 mb-2">
            ¡Turno Reservado!
          </h2>
          <p className="text-green-700">
            Redirigiendo a la lista de turnos...
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-2xl mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Reservar Turno</h1>

      {/* Información del Turno */}
      <div className="bg-blue-50 border border-blue-200 rounded-lg p-6 mb-8">
        <h2 className="text-lg font-semibold text-blue-900 mb-4">
          Datos del Turno
        </h2>
        <div className="space-y-2">
          <div className="flex items-center text-blue-800">
            <Calendar className="w-5 h-5 mr-3" />
            <span className="font-medium">{formatFechaCorta(turno.fecha)}</span>
          </div>
          <div className="flex items-center text-blue-800">
            <Clock className="w-5 h-5 mr-3" />
            <span className="font-medium">{formatHora(turno.hora)}</span>
          </div>
        </div>
      </div>

      {/* Formulario */}
      <form onSubmit={handleSubmit(onSubmit)} className="bg-white rounded-lg shadow-sm border p-6">
        <h2 className="text-lg font-semibold mb-6">Datos del Cliente</h2>

        {/* Nombre */}
        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Nombre *
          </label>
          <input
            type="text"
            {...register('nombre')}
            className={`
              w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2
              ${errors.nombre
                ? 'border-red-300 focus:ring-red-500'
                : 'border-gray-300 focus:ring-blue-500'
              }
            `}
            placeholder="Juan"
          />
          {errors.nombre && (
            <p className="mt-1 text-sm text-red-600">{errors.nombre.message}</p>
          )}
        </div>

        {/* Apellido */}
        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Apellido *
          </label>
          <input
            type="text"
            {...register('apellido')}
            className={`
              w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2
              ${errors.apellido
                ? 'border-red-300 focus:ring-red-500'
                : 'border-gray-300 focus:ring-blue-500'
              }
            `}
            placeholder="Pérez"
          />
          {errors.apellido && (
            <p className="mt-1 text-sm text-red-600">{errors.apellido.message}</p>
          )}
        </div>

        {/* Teléfono */}
        <div className="mb-6">
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Teléfono *
          </label>
          <input
            type="text"
            {...register('telefono')}
            className={`
              w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2
              ${errors.telefono
                ? 'border-red-300 focus:ring-red-500'
                : 'border-gray-300 focus:ring-blue-500'
              }
            `}
            placeholder="+5491112345678"
          />
          {errors.telefono && (
            <p className="mt-1 text-sm text-red-600">{errors.telefono.message}</p>
          )}
        </div>

        {/* Botones */}
        <div className="flex gap-3">
          <button
            type="button"
            onClick={() => navigate('/turnos')}
            className="flex-1 px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50 transition-colors"
            disabled={isSubmitting}
          >
            Cancelar
          </button>
          <button
            type="submit"
            disabled={isSubmitting}
            className="flex-1 bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center"
          >
            {isSubmitting ? (
              <>
                <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                Reservando...
              </>
            ) : (
              'Confirmar Reserva'
            )}
          </button>
        </div>
      </form>
    </div>
  );
};