import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { personaService } from '@/services/persona.service';

/**
 * Hook para obtener lista paginada de personas
 */
export const usePersonas = (page = 0, size = 10) => {
  return useQuery({
    queryKey: ['personas', page, size],
    queryFn: () => personaService.getAll(page, size),
    keepPreviousData: true, // Mantener datos anteriores durante paginación
    staleTime: 1000 * 60 * 5,
  });
};

/**
 * Hook para obtener detalle de una persona
 */
export const usePersona = (id) => {
  return useQuery({
    queryKey: ['personas', id],
    queryFn: () => personaService.getById(id),
    enabled: !!id,
    staleTime: 1000 * 60 * 5,
  });
};

/**
 * Hook para obtener historial de una persona
 */
export const usePersonaHistorial = (personaId, params = {}) => {
  return useQuery({
    queryKey: ['personas', personaId, 'historial', params],
    queryFn: () => personaService.getHistorial(personaId, params),
    enabled: !!personaId,
    keepPreviousData: true,
  });
};

/**
 * Hook para crear persona
 */
export const useCrearPersona = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (personaData) => personaService.create(personaData),
    onSuccess: () => {
      queryClient.invalidateQueries(['personas']);
    },
  });
};

/**
 * Hook para actualizar persona
 */
export const useActualizarPersona = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, data }) => personaService.update(id, data),
    onSuccess: (data, variables) => {
      queryClient.invalidateQueries(['personas']);
      queryClient.invalidateQueries(['personas', variables.id]);
    },
  });
};

/**
 * Hook para eliminar persona
 */
export const useEliminarPersona = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id) => personaService.delete(id),
    onSuccess: () => {
      queryClient.invalidateQueries(['personas']);
    },
  });
};