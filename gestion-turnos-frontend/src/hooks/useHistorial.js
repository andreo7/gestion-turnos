import { useQuery } from '@tanstack/react-query';
import { historialService } from '@/services/historial.service';

/**
 * Hook para obtener métricas mensuales
 */
export const useMetricasMensuales = (anio, mes) => {
  return useQuery({
    queryKey: ['metricas', anio, mes],
    queryFn: () => historialService.getMetricasMensuales(anio, mes),
    enabled: !!anio && !!mes,
    staleTime: 1000 * 60 * 10, // Cache 10 minutos (métricas cambian poco)
  });
};