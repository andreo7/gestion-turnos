import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { usePersona, usePersonaHistorial, useActualizarPersona } from '@/hooks/usePersonas';
import { Card, CardHeader, CardBody } from '@/components/ui/Card';
import { Badge } from '@/components/ui/Badge';
import { Button } from '@/components/ui/Button';
import { Modal } from '@/components/ui/Modal';
import { PersonaForm } from '@/components/personas/PersonaForm';
import { Table, TableHeader, TableBody, TableRow, TableHead, TableCell } from '@/components/ui/Table';
import { Pagination } from '@/components/ui/Pagination';
import { Loader2, Edit, ArrowLeft, Phone, User, Award, XCircle } from 'lucide-react';
import { formatFechaCorta, formatHora } from '@/utils/dateFormatter';
import toast, { Toaster } from 'react-hot-toast';

export const PersonaDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [showEditModal, setShowEditModal] = useState(false);
  const [historialPage, setHistorialPage] = useState(0);
  const [filtroEstado, setFiltroEstado] = useState(null);

  const { data: persona, isLoading } = usePersona(id);
  const { data: historialData } = usePersonaHistorial(id, {
    page: historialPage,
    size: 10,
    estadoTurno: filtroEstado
  });
  const actualizarMutation = useActualizarPersona();

  const historial = historialData?.content || [];
  const totalPages = historialData?.totalPages || 0;
  const totalElements = historialData?.totalElements || 0;

  const handleUpdate = async (data) => {
    try {
      await actualizarMutation.mutateAsync({ id, data });
      toast.success('Persona actualizada correctamente');
      setShowEditModal(false);
    } catch (error) {
      toast.error(error.response?.data?.message || 'Error al actualizar');
    }
  };

  if (isLoading) {
    return (
      <div className="flex justify-center items-center h-64">
        <Loader2 className="w-8 h-8 animate-spin text-blue-600" />
      </div>
    );
  }

  if (!persona) {
    return (
      <div className="text-center py-12">
        <p className="text-gray-500">Persona no encontrada</p>
      </div>
    );
  }

  const tasaAsistencia = persona.confirmaciones + persona.cancelaciones > 0
    ? (persona.confirmaciones / (persona.confirmaciones + persona.cancelaciones) * 100).toFixed(1)
    : 0;

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <Toaster position="top-right" />

      {/* Breadcrumb */}
      <button
        onClick={() => navigate('/personas')}
        className="flex items-center text-gray-600 hover:text-gray-900 mb-6"
      >
        <ArrowLeft className="w-4 h-4 mr-2" />
        Volver a Personas
      </button>

      {/* Header */}
      <div className="flex items-center justify-between mb-8">
        <div>
          <h1 className="text-3xl font-bold">
            {persona.nombre} {persona.apellido}
          </h1>
          <p className="text-gray-600 mt-1">ID: #{persona.id}</p>
        </div>
        <Button onClick={() => setShowEditModal(true)}>
          <Edit className="w-4 h-4 mr-2" />
          Editar
        </Button>
      </div>

      {/* Info Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        <Card>
          <CardBody>
            <div className="flex items-center gap-3">
              <div className="p-2 bg-blue-100 rounded">
                <Phone className="w-5 h-5 text-blue-600" />
              </div>
              <div>
                <p className="text-sm text-gray-600">Teléfono</p>
                <p className="font-medium">{persona.telefono}</p>
              </div>
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardBody>
            <div className="flex items-center gap-3">
              <div className="p-2 bg-green-100 rounded">
                <Award className="w-5 h-5 text-green-600" />
              </div>
              <div>
                <p className="text-sm text-gray-600">Confirmaciones</p>
                <p className="font-medium text-2xl text-green-600">
                  {persona.confirmaciones}
                </p>
              </div>
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardBody>
            <div className="flex items-center gap-3">
              <div className="p-2 bg-red-100 rounded">
                <XCircle className="w-5 h-5 text-red-600" />
              </div>
              <div>
                <p className="text-sm text-gray-600">Cancelaciones</p>
                <p className="font-medium text-2xl text-red-600">
                  {persona.cancelaciones}
                </p>
              </div>
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardBody>
            <div className="flex items-center gap-3">
              <div className="p-2 bg-purple-100 rounded">
                <User className="w-5 h-5 text-purple-600" />
              </div>
              <div>
                <p className="text-sm text-gray-600">Tasa Asistencia</p>
                <p className="font-medium text-2xl text-purple-600">
                  {tasaAsistencia}%
                </p>
              </div>
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Historial */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <h2 className="text-xl font-semibold">Historial de Turnos</h2>

            {/* Filtros */}
            <div className="flex gap-2">
              <button
                onClick={() => setFiltroEstado(null)}
                className={`px-3 py-1 text-sm rounded ${
                  filtroEstado === null
                    ? 'bg-blue-600 text-white'
                    : 'bg-gray-100 text-gray-700'
                }`}
              >
                Todos
              </button>
              <button
                onClick={() => setFiltroEstado('CONFIRMADO')}
                className={`px-3 py-1 text-sm rounded ${
                  filtroEstado === 'CONFIRMADO'
                    ? 'bg-blue-600 text-white'
                    : 'bg-gray-100 text-gray-700'
                }`}
              >
                Confirmados
              </button>
              <button
                onClick={() => setFiltroEstado('CANCELADO')}
                className={`px-3 py-1 text-sm rounded ${
                  filtroEstado === 'CANCELADO'
                    ? 'bg-blue-600 text-white'
                    : 'bg-gray-100 text-gray-700'
                }`}
              >
                Cancelados
              </button>
            </div>
          </div>
        </CardHeader>

        <CardBody className="p-0">
          {historial.length === 0 ? (
            <div className="text-center py-12">
              <p className="text-gray-500">No hay historial de turnos</p>
            </div>
          ) : (
            <>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>ID</TableHead>
                    <TableHead>Fecha</TableHead>
                    <TableHead>Hora</TableHead>
                    <TableHead>Estado</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {historial.map((item) => (
                    <TableRow key={item.id}>
                      <TableCell className="font-medium">
                        #{item.turno?.id}
                      </TableCell>
                      <TableCell>
                        {formatFechaCorta(item.turno?.fecha)}
                      </TableCell>
                      <TableCell>
                        {formatHora(item.turno?.hora)}
                      </TableCell>
                      <TableCell>
                        <Badge estado={item.estadoTurnoActual} />
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>

              {totalPages > 1 && (
                <div className="border-t">
                  <Pagination
                    currentPage={historialPage}
                    totalPages={totalPages}
                    totalElements={totalElements}
                    onPageChange={setHistorialPage}
                  />
                </div>
              )}
            </>
          )}
        </CardBody>
      </Card>

      {/* Modal Editar */}
      <Modal
        isOpen={showEditModal}
        onClose={() => setShowEditModal(false)}
        title="Editar Persona"
      >
        <PersonaForm
          initialData={persona}
          onSubmit={handleUpdate}
          onCancel={() => setShowEditModal(false)}
          isSubmitting={actualizarMutation.isLoading}
        />
      </Modal>
    </div>
  );
};