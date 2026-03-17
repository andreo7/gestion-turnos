import { AlertCircle, CheckCircle, XCircle, Info } from 'lucide-react';
import { clsx } from 'clsx';

/**
 * Componente Alert para mensajes
 */
export const Alert = ({
  type = 'info',
  title,
  message,
  onClose,
  className = ''
}) => {
  const config = {
    success: {
      icon: CheckCircle,
      bgColor: 'bg-green-50',
      borderColor: 'border-green-200',
      iconColor: 'text-green-600',
      textColor: 'text-green-800',
    },
    error: {
      icon: XCircle,
      bgColor: 'bg-red-50',
      borderColor: 'border-red-200',
      iconColor: 'text-red-600',
      textColor: 'text-red-800',
    },
    warning: {
      icon: AlertCircle,
      bgColor: 'bg-yellow-50',
      borderColor: 'border-yellow-200',
      iconColor: 'text-yellow-600',
      textColor: 'text-yellow-800',
    },
    info: {
      icon: Info,
      bgColor: 'bg-blue-50',
      borderColor: 'border-blue-200',
      iconColor: 'text-blue-600',
      textColor: 'text-blue-800',
    },
  };

  const { icon: Icon, bgColor, borderColor, iconColor, textColor } = config[type];

  return (
    <div
      className={clsx(
        'border rounded-lg p-4',
        bgColor,
        borderColor,
        className
      )}
    >
      <div className="flex items-start">
        <Icon className={clsx('w-5 h-5 mr-3 flex-shrink-0', iconColor)} />
        <div className="flex-1">
          {title && (
            <h3 className={clsx('font-semibold mb-1', textColor)}>
              {title}
            </h3>
          )}
          {message && (
            <p className={clsx('text-sm', textColor)}>
              {message}
            </p>
          )}
        </div>
        {onClose && (
          <button
            onClick={onClose}
            className={clsx('ml-3 flex-shrink-0', textColor)}
          >
            <XCircle className="w-5 h-5" />
          </button>
        )}
      </div>
    </div>
  );
};