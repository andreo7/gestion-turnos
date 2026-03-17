import { FileQuestion } from 'lucide-react';

/**
 * Estado vacío para cuando no hay datos
 */
export const EmptyState = ({
  icon: Icon = FileQuestion,
  title = 'No hay datos',
  description,
  action
}) => {
  return (
    <div className="text-center py-12">
      <div className="flex justify-center mb-4">
        <div className="p-4 bg-gray-100 rounded-full">
          <Icon className="w-12 h-12 text-gray-400" />
        </div>
      </div>
      <h3 className="text-lg font-medium text-gray-900 mb-2">{title}</h3>
      {description && (
        <p className="text-sm text-gray-500 mb-4">{description}</p>
      )}
      {action}
    </div>
  );
};