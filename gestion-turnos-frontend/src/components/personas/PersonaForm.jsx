import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Input } from '@/components/ui/Input';
import { Button } from '@/components/ui/Button';

const schema = z.object({
  nombre: z.string()
    .min(2, 'Mínimo 2 caracteres')
    .max(50, 'Máximo 50 caracteres'),
  apellido: z.string()
    .min(2, 'Mínimo 2 caracteres')
    .max(50, 'Máximo 50 caracteres'),
  telefono: z.string()
    .regex(/^[0-9+ ]{6,20}$/, 'Formato inválido. Ej: +5491112345678')
});

export const PersonaForm = ({ 
  initialData = null, 
  onSubmit, 
  onCancel,
  isSubmitting = false 
}) => {
  const {
    register,
    handleSubmit,
    formState: { errors }
  } = useForm({
    resolver: zodResolver(schema),
    defaultValues: initialData || {
      nombre: '',
      apellido: '',
      telefono: ''
    }
  });

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <Input
        label="Nombre"
        {...register('nombre')}
        error={errors.nombre?.message}
        placeholder="Juan"
        required
      />

      <Input
        label="Apellido"
        {...register('apellido')}
        error={errors.apellido?.message}
        placeholder="Pérez"
        required
      />

      <Input
        label="Teléfono"
        {...register('telefono')}
        error={errors.telefono?.message}
        placeholder="+5491112345678"
        helperText="Incluir código de país"
        required
      />

      <div className="flex gap-3 pt-4">
        {onCancel && (
          <Button
            type="button"
            variant="outline"
            onClick={onCancel}
            className="flex-1"
          >
            Cancelar
          </Button>
        )}
        
        <Button
          type="submit"
          variant="primary"
          loading={isSubmitting}
          className="flex-1"
        >
          {initialData ? 'Actualizar' : 'Crear'}
        </Button>
      </div>
    </form>
  );
};