import java.util.List;

public class PresentadorSolicitudes {

    public String detalle(Solicitud solicitud) {
        Estudiante estudiante = solicitud.getEstudiante();
        return "Código=" + solicitud.getCodigo()
                + " | Estudiante=" + estudiante.getNombre() + " (" + estudiante.getLegajo() + ")"
                + " | Correo=" + estudiante.getCorreo()
                + " | Tipo=" + solicitud.getTipo().name()
                + " | Prioridad=" + solicitud.getPrioridad().name()
                + " | Estado=" + solicitud.getEstado().name()
                + " | Responsable=" + nombreResponsable(solicitud)
                + " | Fecha=" + solicitud.getFecha()
                + " | Descripción=" + solicitud.getDescripcion()
                + " | Observación=" + solicitud.getObservacion();
    }

    public String listado(List<Solicitud> solicitudes) {
        StringBuilder texto = new StringBuilder("LISTADO DE SOLICITUDES\n");
        if (solicitudes.isEmpty()) {
            return texto.append("Sin solicitudes\n").toString();
        }
        for (Solicitud solicitud : solicitudes) {
            texto.append(detalle(solicitud)).append('\n');
        }
        return texto.toString();
    }

    private static String nombreResponsable(Solicitud solicitud) {
        Responsable responsable = solicitud.getResponsable();
        return responsable == null ? "SIN_ASIGNAR" : responsable.getNombre();
    }
}
