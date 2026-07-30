import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { usePersonas, useEliminarPersona, useCrearPersona } from '@/hooks/usePersonas';
import { PersonaTable } from '@/components/personas/PersonaTable';
import { PersonaForm } from '@/components/personas/PersonaForm';
import { Modal } from '@/components/ui/Modal';
import { Button } from '@/components/ui/Button';
import { Pagination } from '@/components/ui/Pagination';
import { Input } from '@/components/ui/Input';
import { Loader2, Plus, Search } from 'lucide-react';
import toast, { Toaster } from 'react-hot-toast';

export const PersonasPage = () => {
  const navigate = useNavigate();
  const [page, setPage] = useState(0);
  const [searchTerm, setSearchTerm] = useState('');
  const [showCreateModal, setShowCreateModal] = useState(false);

  const { data, isLoading } = usePersonas(page, 10);
  const eliminarMutation = useEliminarPersona();
  const crearMutation = useCrearPersona();

  const personas = data?.content || [];
  const totalPages = data?.totalPages || 0;
  const totalElements = data?.totalElements || 0;

  // Filtrado local
  const personasFiltradas = personas.filter(persona => {
    const searchLower = searchTerm.toLowerCase();
    return (
      persona.nombre.toLowerCase().includes(searchLower) ||
      persona.apellido.toLowerCase().includes(searchLower) ||
      persona.telefono.includes(searchTerm)
    );
  });

  const handleView = (id) => {
    navigate(`/personas/${id}`);
  };

  const handleEdit = (persona) => {
    navigate(`/personas/${persona.id}`);
  };

  const handleDelete = async (id) => {
    if (!confirm('¿Estás seguro de eliminar esta persona?')) return;

    try {
      await eliminarMutation.mutateAsync(id);
      toast.success('Persona eliminada correctamente');
    } catch (error) {
      toast.error(error.response?.data?.message || 'Error al eliminar');
    }
  };

  const handleCreate = async (data) => {
    try {
      await crearMutation.mutateAsync(data);
      toast.success('Persona creada correctamente');
      setShowCreateModal(false);
    } catch (error) {
      toast.error(error.response?.data?.message || 'Error al crear');
    }
  };

  if (isLoading) {
    return (
      <div className="flex justify-center items-center h-64">
        <Loader2 className="w-8 h-8 animate-spin text-blue-600" />
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <Toaster position="top-right" />

      {/* Header */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold mb-2">Gestión de Personas</h1>
        <p className="text-gray-600">Administra los clientes del sistema</p>
      </div>

      {/* Barra de acciones */}
      <div className="flex flex-col md:flex-row gap-4 mb-6">
        {/* Búsqueda */}
        <div className="flex-1">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
            <input
              type="text"
              placeholder="Buscar por nombre, apellido o teléfono..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
        </div>

        {/* Botón crear */}
        <Button onClick={() => setShowCreateModal(true)}>
          <Plus className="w-4 h-4 mr-2" />
          Nueva Persona
        </Button>
      </div>

      {/* Tabla */}
      <PersonaTable
        personas={personasFiltradas}
        onView={handleView}
        onEdit={handleEdit}
        onDelete={handleDelete}
      />

      {/* Paginación */}
      {totalPages > 1 && (
        <div className="mt-6">
          <Pagination
            currentPage={page}
            totalPages={totalPages}
            totalElements={totalElements}
            onPageChange={setPage}
          />
        </div>
      )}

      {/* Modal Crear */}
      <Modal
        isOpen={showCreateModal}
        onClose={() => setShowCreateModal(false)}
        title="Nueva Persona"
      >
        <PersonaForm
          onSubmit={handleCreate}
          onCancel={() => setShowCreateModal(false)}
          isSubmitting={crearMutation.isLoading}
        />
      </Modal>
    </div>
  );
};