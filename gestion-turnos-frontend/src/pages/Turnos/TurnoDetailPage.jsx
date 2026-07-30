import { useParams, useNavigate } from 'react-router-dom';
import { useTurno, useCancelarTurno } from '@/hooks/useTurnos';
import { Card, CardHeader, CardBody } from '@/components/ui/Card';
import { Badge } from '@/components/ui/Badge';
import { Button } from '@/components/ui/Button';
import { Loader2, ArrowLeft, Calendar, Clock, User, Phone } from 'lucide-react';
import { formatFechaCorta, formatHora } from '@/utils/dateFormatter';
import toast, { Toaster } from 'react-hot-toast';

export const TurnoDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const { data: turno, isLoading } = useTurno(id);
  const cancelarMutation = useCancelarTurno();

  const handleCancelar = async () => {
    if (!confirm('¿Estás seguro de cancelar este turno?')) return;

    try {
      await cancelarMutation.mutateAsync(id);
      toast.success('Turno cancelado correctamente');
      setTimeout(() => navigate('/turnos'), 1500);
    } catch (error) {
      toast.error(error.response?.data?.message || 'Error al cancelar');
    }
  };

  const handleVerCliente = () => {
    if (turno?.cliente) {
      navigate(`/personas/${turno.cliente.id}`);
    }
  };

  if (isLoading) {
    return (
      <div className="flex justify-center items-center h-64">
        <Loader2 className="w-8 h-8 animate-spin text-blue-600" />
      </div>
    );
  }

  if (!turno) {
    return (
      <div className="text-center py-12">
        <p className="text-gray-500">Turno no encontrado</p>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto px-4 py-8">
      <Toaster position="top-right" />

      {/* Breadcrumb */}
      <button
        onClick={() => navigate('/turnos')}
        className="flex items-center text-gray-600 hover:text-gray-900 mb-6"
      >
        <ArrowLeft className="w-4 h-4 mr-2" />
        Volver a Turnos
      </button>

      {/* Header */}
      <div className="flex items-center justify-between mb-8">
        <div>
          <h1 className="text-3xl font-bold">Turno #{turno.id}</h1>
          <div className="mt-2">
            <Badge estado={turno.estado} />
          </div>
        </div>

        {/* Acciones */}
        {(turno.estado === 'RESERVADO' || turno.estado === 'CONFIRMADO') && (
          <Button
            variant="danger"
            onClick={handleCancelar}
            loading={cancelarMutation.isLoading}
          >
            Cancelar Turno
          </Button>
        )}
      </div>

      {/* Información del Turno */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Datos del Turno */}
        <Card>
          <CardHeader>
            <h2 className="text-lg font-semibold">Información del Turno</h2>
          </CardHeader>
          <CardBody>
            <div className="space-y-4">
              <div className="flex items-center gap-3">
                <div className="p-2 bg-blue-100 rounded">
                  <Calendar className="w-5 h-5 text-blue-600" />
                </div>
                <div>
                  <p className="text-sm text-gray-600">Fecha</p>
                  <p className="font-medium">{formatFechaCorta(turno.fecha)}</p>
                </div>
              </div>

              <div className="flex items-center gap-3">
                <div className="p-2 bg-purple-100 rounded">
                  <Clock className="w-5 h-5 text-purple-600" />
                </div>
                <div>
                  <p className="text-sm text-gray-600">Hora</p>
                  <p className="font-medium">{formatHora(turno.hora)}</p>
                </div>
              </div>
            </div>
          </CardBody>
        </Card>

        {/* Datos del Cliente */}
        {turno.cliente ? (
          <Card>
            <CardHeader>
              <div className="flex items-center justify-between">
                <h2 className="text-lg font-semibold">Información del Cliente</h2>
                <Button size="sm" onClick={handleVerCliente}>
                  Ver Perfil
                </Button>
              </div>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <div className="flex items-center gap-3">
                  <div className="p-2 bg-green-100 rounded">
                    <User className="w-5 h-5 text-green-600" />
                  </div>
                  <div>
                    <p className="text-sm text-gray-600">Nombre</p>
                    <p className="font-medium">
                      {turno.cliente.nombre} {turno.cliente.apellido}
                    </p>
                  </div>
                </div>

                <div className="flex items-center gap-3">
                  <div className="p-2 bg-orange-100 rounded">
                    <Phone className="w-5 h-5 text-orange-600" />
                  </div>
                  <div>
                    <p className="text-sm text-gray-600">Teléfono</p>
                    <p className="font-medium">{turno.cliente.telefono}</p>
                  </div>
                </div>
              </div>
            </CardBody>
          </Card>
        ) : (
          <Card>
            <CardBody>
              <div className="text-center py-8">
                <p className="text-gray-500">Este turno está disponible</p>
                <Button
                  className="mt-4"
                  onClick={() => navigate(`/turnos/reservar/${turno.id}`)}
                >
                  Reservar Turno
                </Button>
              </div>
            </CardBody>
          </Card>
        )}
      </div>
    </div>
  );
};