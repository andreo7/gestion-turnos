import { useTurnosDisponibles, useTurnosOcupados } from '@/hooks/useTurnos';
import { useMetricasMensuales } from '@/hooks/useHistorial';
import { Card, CardHeader, CardBody } from '@/components/ui/Card';
import { Calendar, CheckCircle, XCircle, Clock, TrendingUp } from 'lucide-react';
import { formatFechaCorta, formatHora } from '@/utils/dateFormatter';
import { Link } from 'react-router-dom';

export const DashboardPage = () => {
  const today = new Date();
  const currentYear = today.getFullYear();
  const currentMonth = today.getMonth() + 1;

  const { data: disponibles = [] } = useTurnosDisponibles();
  const { data: ocupados = [] } = useTurnosOcupados();
  const { data: metricas } = useMetricasMensuales(currentYear, currentMonth);

  // Turnos de hoy
  const hoy = today.toISOString().split('T')[0];
  const turnosHoy = [...disponibles, ...ocupados].filter(
    t => t.fecha === hoy
  );

  // Próximos turnos confirmados
  const proximosTurnos = ocupados
    .filter(t => t.estado === 'CONFIRMADO' && new Date(t.fecha) >= today)
    .sort((a, b) => new Date(a.fecha) - new Date(b.fecha))
    .slice(0, 5);

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Dashboard</h1>

      {/* Métricas Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        {/* Disponibles Hoy */}
        <Card className="hover:shadow-md transition-shadow">
          <CardBody>
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600 mb-1">Disponibles Hoy</p>
                <p className="text-3xl font-bold text-green-600">
                  {turnosHoy.filter(t => t.estado === 'DISPONIBLE').length}
                </p>
              </div>
              <div className="p-3 bg-green-100 rounded-full">
                <Calendar className="w-6 h-6 text-green-600" />
              </div>
            </div>
          </CardBody>
        </Card>

        {/* Confirmados Hoy */}
        <Card className="hover:shadow-md transition-shadow">
          <CardBody>
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600 mb-1">Confirmados Hoy</p>
                <p className="text-3xl font-bold text-blue-600">
                  {turnosHoy.filter(t => t.estado === 'CONFIRMADO').length}
                </p>
              </div>
              <div className="p-3 bg-blue-100 rounded-full">
                <CheckCircle className="w-6 h-6 text-blue-600" />
              </div>
            </div>
          </CardBody>
        </Card>

        {/* Métricas del Mes */}
        {metricas && (
          <>
            <Card className="hover:shadow-md transition-shadow">
              <CardBody>
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm text-gray-600 mb-1">% Asistencia Mes</p>
                    <p className="text-3xl font-bold text-purple-600">
                      {metricas.porcentajeAsistencia.toFixed(1)}%
                    </p>
                  </div>
                  <div className="p-3 bg-purple-100 rounded-full">
                    <TrendingUp className="w-6 h-6 text-purple-600" />
                  </div>
                </div>
              </CardBody>
            </Card>

            <Card className="hover:shadow-md transition-shadow">
              <CardBody>
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm text-gray-600 mb-1">Cancelaciones Mes</p>
                    <p className="text-3xl font-bold text-red-600">
                      {metricas.cancelados}
                    </p>
                  </div>
                  <div className="p-3 bg-red-100 rounded-full">
                    <XCircle className="w-6 h-6 text-red-600" />
                  </div>
                </div>
              </CardBody>
            </Card>
          </>
        )}
      </div>

      {/* Próximos Turnos */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <h2 className="text-xl font-semibold">Próximos Turnos Confirmados</h2>
            <Link
              to="/turnos"
              className="text-sm text-blue-600 hover:text-blue-700"
            >
              Ver todos →
            </Link>
          </div>
        </CardHeader>
        <CardBody>
          {proximosTurnos.length === 0 ? (
            <p className="text-gray-500 text-center py-8">
              No hay turnos confirmados próximamente
            </p>
          ) : (
            <div className="space-y-3">
              {proximosTurnos.map((turno) => (
                <div
                  key={turno.id}
                  className="flex items-center justify-between p-4 border rounded-lg hover:bg-gray-50 transition-colors"
                >
                  <div className="flex items-center gap-4">
                    <div className="p-2 bg-blue-100 rounded">
                      <Clock className="w-5 h-5 text-blue-600" />
                    </div>
                    <div>
                      <p className="font-medium">
                        {turno.cliente?.nombre} {turno.cliente?.apellido}
                      </p>
                      <p className="text-sm text-gray-600">
                        {turno.cliente?.telefono}
                      </p>
                    </div>
                  </div>
                  <div className="text-right">
                    <p className="font-medium">{formatFechaCorta(turno.fecha)}</p>
                    <p className="text-sm text-gray-600">{formatHora(turno.hora)}</p>
                  </div>
                </div>
              ))}
            </div>
          )}
        </CardBody>
      </Card>
    </div>
  );
};