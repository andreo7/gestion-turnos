import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { turnoService } from '@/services/turno.service';

/**
 * Hook para obtener todos los turnos
 */
export const useTurnos = () => {
  return useQuery({
    queryKey: ['turnos'],
    queryFn: turnoService.getAll,
    staleTime: 1000 * 60 * 5, // Cache válido por 5 minutos
  });
};

/**
 * Hook para obtener turnos disponibles
 */
export const useTurnosDisponibles = () => {
  return useQuery({
    queryKey: ['turnos', 'disponibles'],
    queryFn: turnoService.getDisponibles,
    staleTime: 1000 * 60, // Cache 1 minuto (más fresco)
  });
};

/**
 * Hook para obtener turnos ocupados
 */
export const useTurnosOcupados = () => {
  return useQuery({
    queryKey: ['turnos', 'ocupados'],
    queryFn: turnoService.getOcupados,
    staleTime: 1000 * 60 * 2,
  });
};

/**
 * Hook para obtener un turno por ID
 */
export const useTurno = (id) => {
  return useQuery({
    queryKey: ['turnos', id],
    queryFn: () => turnoService.getById(id),
    enabled: !!id, // Solo ejecutar si hay ID
  });
};

/**
 * Hook para reservar un turno
 */
export const useReservarTurno = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: ({ turnoId, personaData }) => 
      turnoService.reservar(turnoId, personaData),
    onSuccess: () => {
      // Invalidar cache para refrescar listas
      queryClient.invalidateQueries(['turnos']);
      queryClient.invalidateQueries(['personas']);
    },
    onError: (error) => {
      console.error('Error reservando turno:', error);
    }
  });
};

/**
 * Hook para cancelar un turno
 */
export const useCancelarTurno = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: (turnoId) => turnoService.cancelar(turnoId),
    onSuccess: () => {
      queryClient.invalidateQueries(['turnos']);
    },
    onError: (error) => {
      console.error('Error cancelando turno:', error);
    }
  });
};

/**
 * Hook para generar turnos de un mes (admin)
 */
export const useCrearTurnosMes = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: ({ anio, mes }) => turnoService.crearTurnosMes(anio, mes),
    onSuccess: () => {
      queryClient.invalidateQueries(['turnos']);
    }
  });
};