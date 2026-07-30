import { useState } from 'react';
import { useMetricasMensuales } from '@/hooks/useHistorial';
import { Card, CardHeader, CardBody } from '@/components/ui/Card';
import { Loader2, TrendingUp, TrendingDown, Calendar } from 'lucide-react';

export const MetricasPage = () => {
  const today = new Date();
  const [anio, setAnio] = useState(today.getFullYear());
  const [mes, setMes] = useState(today.getMonth() + 1);

  const { data: metricas, isLoading } = useMetricasMensuales(anio, mes);

  const meses = [
    { value: 1, label: 'Enero' },
    { value: 2, label: 'Febrero' },
    { value: 3, label: 'Marzo' },
    { value: 4, label: 'Abril' },
    { value: 5, label: 'Mayo' },
    { value: 6, label: 'Junio' },
    { value: 7, label: 'Julio' },
    { value: 8, label: 'Agosto' },
    { value: 9, label: 'Septiembre' },
    { value: 10, label: 'Octubre' },
    { value: 11, label: 'Noviembre' },
    { value: 12, label: 'Diciembre' },
  ];

  const anios = Array.from({ length: 5 }, (_, i) => today.getFullYear() - i);

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Métricas Mensuales</h1>

      {/* Selectores */}
      <div className="flex gap-4 mb-8">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Mes
          </label>
          <select
            value={mes}
            onChange={(e) => setMes(Number(e.target.value))}
            className="px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            {meses.map((m) => (
              <option key={m.value} value={m.value}>
                {m.label}
              </option>
            ))}
          </select>
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Año
          </label>
          <select
            value={anio}
            onChange={(e) => setAnio(Number(e.target.value))}
            className="px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            {anios.map((a) => (
              <option key={a} value={a}>
                {a}
              </option>
            ))}
          </select>
        </div>
      </div>

      {/* Métricas */}
      {isLoading ? (
        <div className="flex justify-center items-center h-64">
          <Loader2 className="w-8 h-8 animate-spin text-blue-600" />
        </div>
      ) : metricas ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          {/* Total */}
          <Card>
            <CardBody>
              <div className="flex items-center gap-3">
                <div className="p-3 bg-blue-100 rounded-full">
                  <Calendar className="w-6 h-6 text-blue-600" />
                </div>
                <div>
                  <p className="text-sm text-gray-600">Total Turnos</p>
                  <p className="text-3xl font-bold text-blue-600">
                    {metricas.total}
                  </p>
                </div>
              </div>
            </CardBody>
          </Card>

          {/* Confirmados */}
          <Card>
            <CardBody>
              <div className="flex items-center gap-3">
                <div className="p-3 bg-green-100 rounded-full">
                  <TrendingUp className="w-6 h-6 text-green-600" />
                </div>
                <div>
                  <p className="text-sm text-gray-600">Confirmados</p>
                  <p className="text-3xl font-bold text-green-600">
                    {metricas.confirmados}
                  </p>
                </div>
              </div>
            </CardBody>
          </Card>

          {/* Cancelados */}
          <Card>
            <CardBody>
              <div className="flex items-center gap-3">
                <div className="p-3 bg-red-100 rounded-full">
                  <TrendingDown className="w-6 h-6 text-red-600" />
                </div>
                <div>
                  <p className="text-sm text-gray-600">Cancelados</p>
                  <p className="text-3xl font-bold text-red-600">
                    {metricas.cancelados}
                  </p>
                </div>
              </div>
            </CardBody>
          </Card>

          {/* Porcentaje */}
          <Card>
            <CardBody>
              <div className="flex items-center gap-3">
                <div className="p-3 bg-purple-100 rounded-full">
                  <TrendingUp className="w-6 h-6 text-purple-600" />
                </div>
                <div>
                  <p className="text-sm text-gray-600">% Asistencia</p>
                  <p className="text-3xl font-bold text-purple-600">
                    {metricas.porcentajeAsistencia.toFixed(1)}%
                  </p>
                </div>
              </div>
            </CardBody>
          </Card>
        </div>
      ) : (
        <div className="text-center py-12">
          <p className="text-gray-500">No hay datos para este período</p>
        </div>
      )}
    </div>
  );
};