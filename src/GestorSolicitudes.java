import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class GestorSolicitudes {
    private final List<Estudiante> estudiantes;
    private final List<Responsable> responsables;
    private final ArrayList<Solicitud> solicitudes = new ArrayList<>();

    public GestorSolicitudes(List<Estudiante> estudiantes, List<Responsable> responsables) {
        this.estudiantes = estudiantes;
        this.responsables = responsables;
    }

    public Solicitud registrarSolicitud(String codigo, String legajo, String descripcion,
                                       String tipoTexto, String prioridadTexto, String fechaTexto,
                                       String responsableId, String observacion) {
        validarCodigoNuevo(codigo);
        Estudiante estudiante = buscarEstudiante(legajo);
        exigirTexto(descripcion, "Descripci\u00f3n obligatoria");
        TipoSolicitud tipo = convertirTipoObligatorio(tipoTexto);
        Prioridad prioridad = convertirPrioridadObligatoria(prioridadTexto);
        LocalDate fecha = convertirFecha(fechaTexto);
        Responsable responsable = buscarResponsableOpcional(responsableId);
        Solicitud nueva = new Solicitud(codigo.trim(), estudiante, descripcion.trim(),
                tipo, prioridad, fecha, normalizarObservacion(observacion), responsable);
        solicitudes.add(nueva);
        return nueva;
    }

    private void validarCodigoNuevo(String codigo) {
        exigirTexto(codigo, "C\u00f3digo obligatorio");
        for (Solicitud s : solicitudes) {
            if (s.getCodigo().equalsIgnoreCase(codigo.trim())) {
                throw new IllegalArgumentException("C\u00f3digo duplicado: " + codigo.trim());
            }
        }
    }

    private static void exigirTexto(String valor, String mensajeError) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensajeError);
        }
    }

    private Estudiante buscarEstudiante(String legajo) {
        exigirTexto(legajo, "Legajo obligatorio");
        for (Estudiante e : estudiantes) {
            if (e.getLegajo().equalsIgnoreCase(legajo.trim())) {
                return e;
            }
        }
        throw new IllegalArgumentException("Estudiante inexistente: " + legajo);
    }

    private TipoSolicitud convertirTipoObligatorio(String tipoTexto) {
        exigirTexto(tipoTexto, "Tipo obligatorio");
        return parseTipo(tipoTexto);
    }

    private Prioridad convertirPrioridadObligatoria(String prioridadTexto) {
        exigirTexto(prioridadTexto, "Prioridad obligatoria");
        return parsePrioridad(prioridadTexto);
    }

    private LocalDate convertirFecha(String fechaTexto) {
        if (fechaTexto == null) {
            throw new IllegalArgumentException("Fecha inv\u00e1lida (AAAA-MM-DD)");
        }
        try {
            return LocalDate.parse(fechaTexto);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Fecha inv\u00e1lida (AAAA-MM-DD)");
        }
    }

    private Responsable buscarResponsableOpcional(String responsableId) {
        if (responsableId == null || responsableId.isBlank()) {
            return null;
        }
        for (Responsable r : responsables) {
            if (r.getId().equalsIgnoreCase(responsableId.trim())) {
                return r;
            }
        }
        throw new IllegalArgumentException("Responsable inexistente: " + responsableId);
    }

    private static String normalizarObservacion(String observacion) {
        return observacion == null ? "" : observacion.trim();
    }

    public Solicitud consultar(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("C\u00f3digo obligatorio");
        }
        for (Solicitud s : solicitudes) {
            if (s.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return s;
            }
        }
        throw new IllegalArgumentException("Solicitud inexistente: " + codigo);
    }

    public Solicitud cambiarEstado(String codigo, String estadoTexto) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("C\u00f3digo obligatorio");
        }
        Solicitud encontrada = null;
        for (Solicitud s : solicitudes) {
            if (s.getCodigo().equalsIgnoreCase(codigo.trim())) {
                encontrada = s;
                break;
            }
        }
        if (encontrada == null) {
            throw new IllegalArgumentException("Solicitud inexistente: " + codigo);
        }
        if (estadoTexto == null || estadoTexto.isBlank()) {
            throw new IllegalArgumentException("Estado obligatorio");
        }
        try {
            encontrada.setEstado(Estado.valueOf(estadoTexto.trim().toUpperCase()));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Estado inv\u00e1lido: " + estadoTexto);
        }
        return encontrada;
    }

    public Solicitud asignarResponsable(String codigo, String responsableId) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("C\u00f3digo obligatorio");
        }
        Solicitud encontrada = null;
        for (Solicitud s : solicitudes) {
            if (s.getCodigo().equalsIgnoreCase(codigo.trim())) {
                encontrada = s;
                break;
            }
        }
        if (encontrada == null) {
            throw new IllegalArgumentException("Solicitud inexistente: " + codigo);
        }
        if (responsableId == null || responsableId.isBlank()) {
            throw new IllegalArgumentException("Responsable obligatorio");
        }
        Responsable responsable = null;
        for (Responsable r : responsables) {
            if (r.getId().equalsIgnoreCase(responsableId.trim())) {
                responsable = r;
                break;
            }
        }
        if (responsable == null) {
            throw new IllegalArgumentException("Responsable inexistente: " + responsableId);
        }
        encontrada.setResponsable(responsable);
        return encontrada;
    }

    public List<Solicitud> filtrar(String tipoTexto, String prioridadTexto, String estadoTexto) {
        TipoSolicitud tipo = tipoTexto == null || tipoTexto.isBlank() ? null : parseTipo(tipoTexto);
        Prioridad prioridad = prioridadTexto == null || prioridadTexto.isBlank() ? null : parsePrioridad(prioridadTexto);
        Estado estado;
        if (estadoTexto == null || estadoTexto.isBlank()) {
            estado = null;
        } else {
            try {
                estado = Estado.valueOf(estadoTexto.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Estado inv\u00e1lido: " + estadoTexto);
            }
        }
        List<Solicitud> resultado = new ArrayList<>();
        for (Solicitud x : solicitudes) {
            if ((tipo == null || x.getTipo() == tipo)
                    && (prioridad == null || x.getPrioridad() == prioridad)
                    && (estado == null || x.getEstado() == estado)) {
                resultado.add(x);
            }
        }
        return resultado;
    }

    private TipoSolicitud parseTipo(String valor) {
        try { return TipoSolicitud.valueOf(valor.trim().toUpperCase()); }
        catch (IllegalArgumentException ex) { throw new IllegalArgumentException("Tipo inv\u00e1lido: " + valor); }
    }
    private Prioridad parsePrioridad(String valor) {
        try { return Prioridad.valueOf(valor.trim().toUpperCase()); }
        catch (IllegalArgumentException ex) { throw new IllegalArgumentException("Prioridad inv\u00e1lida: " + valor); }
    }

    public List<Solicitud> getSolicitudes() { return solicitudes; }

    public String formatearDetalle(Solicitud s) {
        return "C\u00f3digo=" + s.getCodigo() + " | Estudiante=" + s.getEstudiante().getNombre()
                + " (" + s.getEstudiante().getLegajo() + ")"
                + " | Correo=" + s.getEstudiante().getCorreo()
                + " | Tipo=" + s.getTipo().name()
                + " | Prioridad=" + s.getPrioridad().name()
                + " | Estado=" + s.getEstado().name()
                + " | Responsable=" + (s.getResponsable() == null ? "SIN_ASIGNAR" : s.getResponsable().getNombre())
                + " | Fecha=" + s.getFecha()
                + " | Descripci\u00f3n=" + s.getDescripcion()
                + " | Observaci\u00f3n=" + s.getObservacion();
    }

    public String listar() {
        StringBuilder sb = new StringBuilder();
        sb.append("LISTADO DE SOLICITUDES\n");
        if (solicitudes.isEmpty()) return sb.append("Sin solicitudes\n").toString();
        for (Solicitud s : solicitudes) {
            sb.append(formatearDetalle(s)).append('\n');
        }
        return sb.toString();
    }

    public String reporte() {
        int pendientes = 0, enProceso = 0, resueltas = 0, altas = 0;
        for (Solicitud s : solicitudes) {
            String estado = s.getEstado().name();
            if (estado.equals("PENDIENTE")) pendientes++;
            else if (estado.equals("EN_PROCESO")) enProceso++;
            else if (estado.equals("RESUELTA")) resueltas++;
            if (s.getPrioridad().name().equals("ALTA")) altas++;
        }
        return "REPORTE DE SOLICITUDES\n"
                + "Total: " + solicitudes.size() + "\n"
                + "Pendientes: " + pendientes + "\n"
                + "En proceso: " + enProceso + "\n"
                + "Resueltas: " + resueltas + "\n"
                + "Prioridad alta: " + altas + "\n";
    }
}
