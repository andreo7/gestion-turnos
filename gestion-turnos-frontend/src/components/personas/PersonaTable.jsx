import { Table, TableHeader, TableBody, TableRow, TableHead, TableCell } from '@/components/ui/Table';
import { Badge } from '@/components/ui/Badge';
import { Edit2, Trash2, Eye } from 'lucide-react';

export const PersonaTable = ({ 
  personas = [], 
  onView, 
  onEdit, 
  onDelete 
}) => {
  if (personas.length === 0) {
    return (
      <div className="text-center py-12 bg-white rounded-lg border">
        <p className="text-gray-500">No hay personas registradas</p>
      </div>
    );
  }

  return (
    <div className="bg-white rounded-lg border overflow-hidden">
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>ID</TableHead>
            <TableHead>Nombre Completo</TableHead>
            <TableHead>Teléfono</TableHead>
            <TableHead className="text-center">Confirmaciones</TableHead>
            <TableHead className="text-center">Cancelaciones</TableHead>
            <TableHead className="text-right">Acciones</TableHead>
          </TableRow>
        </TableHeader>

        <TableBody>
          {personas.map((persona) => (
            <TableRow key={persona.id}>
              <TableCell className="font-medium text-gray-900">
                #{persona.id}
              </TableCell>
              
              <TableCell>
                <div>
                  <div className="font-medium text-gray-900">
                    {persona.nombre} {persona.apellido}
                  </div>
                </div>
              </TableCell>

              <TableCell className="text-gray-600">
                {persona.telefono}
              </TableCell>

              <TableCell className="text-center">
                <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                  {persona.confirmaciones || 0}
                </span>
              </TableCell>

              <TableCell className="text-center">
                <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-red-100 text-red-800">
                  {persona.cancelaciones || 0}
                </span>
              </TableCell>

              <TableCell>
                <div className="flex justify-end gap-2">
                  <button
                    onClick={() => onView?.(persona.id)}
                    className="p-1.5 text-blue-600 hover:bg-blue-50 rounded transition-colors"
                    title="Ver detalle"
                  >
                    <Eye className="w-4 h-4" />
                  </button>
                  
                  <button
                    onClick={() => onEdit?.(persona)}
                    className="p-1.5 text-gray-600 hover:bg-gray-50 rounded transition-colors"
                    title="Editar"
                  >
                    <Edit2 className="w-4 h-4" />
                  </button>
                  
                  <button
                    onClick={() => onDelete?.(persona.id)}
                    className="p-1.5 text-red-600 hover:bg-red-50 rounded transition-colors"
                    title="Eliminar"
                  >
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
};