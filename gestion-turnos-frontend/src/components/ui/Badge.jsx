import { getEstadoBadgeClass, getEstadoLabel } from '@/utils/constants';

/**
 * Badge para mostrar estado de turno
 */
export const Badge = ({ estado }) => {
  return (
    <span
      className={`
        inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium border
        ${getEstadoBadgeClass(estado)}
      `}
    >
      {getEstadoLabel(estado)}
    </span>
  );
};