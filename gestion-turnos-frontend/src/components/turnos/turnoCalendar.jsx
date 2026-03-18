import { useState } from 'react';
import { ChevronLeft, ChevronRight } from 'lucide-react';
import { format, startOfMonth, endOfMonth, eachDayOfInterval, isSameMonth, isSameDay, addMonths, subMonths } from 'date-fns';
import { es } from 'date-fns/locale';
import { getEstadoBadgeClass } from '@/utils/constants';

/**
 * Calendario mensual de turnos
 */
export const TurnoCalendar = ({ turnos = [], onDayClick }) => {
  const [currentMonth, setCurrentMonth] = useState(new Date());

  const monthStart = startOfMonth(currentMonth);
  const monthEnd = endOfMonth(currentMonth);
  const daysInMonth = eachDayOfInterval({ start: monthStart, end: monthEnd });

  // Obtener turnos de un día específico
  const getTurnosDelDia = (day) => {
    const dayStr = format(day, 'yyyy-MM-dd');
    return turnos.filter(turno => turno.fecha === dayStr);
  };

  // Navegación
  const previousMonth = () => setCurrentMonth(subMonths(currentMonth, 1));
  const nextMonth = () => setCurrentMonth(addMonths(currentMonth, 1));

  return (
    <div className="bg-white rounded-lg border p-6">
      {/* Header */}
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-xl font-semibold capitalize">
          {format(currentMonth, 'MMMM yyyy', { locale: es })}
        </h2>
        <div className="flex gap-2">
          <button
            onClick={previousMonth}
            className="p-2 hover:bg-gray-100 rounded transition-colors"
          >
            <ChevronLeft className="w-5 h-5" />
          </button>
          <button
            onClick={nextMonth}
            className="p-2 hover:bg-gray-100 rounded transition-colors"
          >
            <ChevronRight className="w-5 h-5" />
          </button>
        </div>
      </div>

      {/* Días de la semana */}
      <div className="grid grid-cols-7 gap-2 mb-2">
        {['Dom', 'Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb'].map((day) => (
          <div
            key={day}
            className="text-center text-sm font-medium text-gray-600 py-2"
          >
            {day}
          </div>
        ))}
      </div>

      {/* Días del mes */}
      <div className="grid grid-cols-7 gap-2">
        {daysInMonth.map((day) => {
          const turnosDelDia = getTurnosDelDia(day);
          const isToday = isSameDay(day, new Date());

          return (
            <button
              key={day.toString()}
              onClick={() => onDayClick?.(day, turnosDelDia)}
              className={`
                min-h-[80px] p-2 border rounded-lg text-left transition-colors
                ${isToday ? 'border-blue-500 bg-blue-50' : 'hover:bg-gray-50'}
                ${turnosDelDia.length > 0 ? 'cursor-pointer' : 'cursor-default'}
              `}
            >
              <div className="font-medium text-sm mb-1">
                {format(day, 'd')}
              </div>
              
              {/* Badges de turnos */}
              <div className="space-y-1">
                {turnosDelDia.slice(0, 3).map((turno, idx) => (
                  <div
                    key={idx}
                    className={`text-xs px-1.5 py-0.5 rounded ${getEstadoBadgeClass(turno.estado)}`}
                  >
                    {turno.hora.substring(0, 5)}
                  </div>
                ))}
                {turnosDelDia.length > 3 && (
                  <div className="text-xs text-gray-500">
                    +{turnosDelDia.length - 3} más
                  </div>
                )}
              </div>
            </button>
          );
        })}
      </div>
    </div>
  );
};