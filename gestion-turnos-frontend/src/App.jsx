import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';

// Pages
import { TurnosPage } from '@/pages/Turnos/TurnosPage';
import { TurnoDetailPage } from '@/pages/Turnos/TurnoDetailPage';
import { ReservarTurnoPage } from '@/pages/Turnos/ReservarTurnoPage';
import { PersonasPage } from '@/pages/Personas/PersonasPage';
import { PersonaDetailPage } from '@/pages/Personas/PersonaDetailPage';
import { DashboardPage } from '@/pages/Dashboard';
import { MetricasPage } from '@/pages/Metricas/MetricasPage';

// Layout
import { Layout } from '@/components/layout/Layout';

// Query Client
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
      staleTime: 1000 * 60 * 5,
    },
  },
});

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <Layout>
          <Routes>
            {/* Dashboard */}
            <Route path="/" element={<DashboardPage />} />

            {/* Turnos */}
            <Route path="/turnos" element={<TurnosPage />} />
            <Route path="/turnos/:id" element={<TurnoDetailPage />} />
            <Route path="/turnos/reservar/:id" element={<ReservarTurnoPage />} />

            {/* Personas */}
            <Route path="/personas" element={<PersonasPage />} />
            <Route path="/personas/:id" element={<PersonaDetailPage />} />

            {/* Métricas */}
            <Route path="/metricas" element={<MetricasPage />} />

            {/* Redirect desconocidos */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </Layout>
      </BrowserRouter>

      {/* DevTools (solo en desarrollo) */}
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  );
}

export default App;