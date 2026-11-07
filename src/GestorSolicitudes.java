import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/** Versión didáctica deliberadamente monolítica: comportamiento correcto, estructura mejorable. */
public class GestorSolicitudes {
    private final List<Estudiante> estudiantes;
    private final List<Responsable> responsables;
    private final ArrayList<Solicitud> solicitudes = new ArrayList<>();

    public GestorSolicitudes(List<Estudiante> estudiantes, List<Responsable> responsables) {
        this.estudiantes = estudiantes;
        this.responsables = responsables;
    }

    // Registro concentrado: búsqueda, validación, conversiones, regla y almacenamiento.
    public Solicitud registrarSolicitud(String codigo, String legajo, String descripcion,
                                       String tipoTexto, String prioridadTexto, String fechaTexto,
                                       String responsableId, String observacion) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código obligatorio");
        }
        for (Solicitud s : solicitudes) {
            if (s.getCodigo().equalsIgnoreCase(codigo.trim())) {
                throw new IllegalArgumentException("Código duplicado: " + codigo.trim());
            }
        }
        if (legajo == null || legajo.isBlank()) {
            throw new IllegalArgumentException("Legajo obligatorio");
        }
        Estudiante encontrado = null;
        for (Estudiante e : estudiantes) {
            if (e.getLegajo().equalsIgnoreCase(legajo.trim())) {
                encontrado = e;
                break;
            }
        }
        if (encontrado == null) {
            throw new IllegalArgumentException("Estudiante inexistente: " + legajo);
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("Descripción obligatoria");
        }
        if (tipoTexto == null || tipoTexto.isBlank()) {
            throw new IllegalArgumentException("Tipo obligatorio");
        }
        TipoSolicitud tipo;
        try {
            tipo = TipoSolicitud.valueOf(tipoTexto.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Tipo inválido: " + tipoTexto);
        }
        if (prioridadTexto == null || prioridadTexto.isBlank()) {
            throw new IllegalArgumentException("Prioridad obligatoria");
        }
        Prioridad prioridad;
        try {
            prioridad = Prioridad.valueOf(prioridadTexto.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Prioridad inválida: " + prioridadTexto);
        }
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaTexto);
        } catch (DateTimeParseException | NullPointerException ex) {
            throw new IllegalArgumentException("Fecha inválida (AAAA-MM-DD)");
        }
        Responsable responsable = null;
        if (responsableId != null && !responsableId.isBlank()) {
            for (Responsable r : responsables) {
                if (r.getId().equalsIgnoreCase(responsableId.trim())) {
                    responsable = r;
                    break;
                }
            }
            if (responsable == null) {
                throw new IllegalArgumentException("Responsable inexistente: " + responsableId);
            }
        }
        Solicitud nueva = new Solicitud(codigo.trim(), encontrado, descripcion.trim(),
                tipo, prioridad, fecha, observacion == null ? "" : observacion.trim(), responsable);
        solicitudes.add(nueva);
        return nueva;
    }

    public Solicitud consultar(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código obligatorio");
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
            throw new IllegalArgumentException("Código obligatorio");
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
            throw new IllegalArgumentException("Estado inválido: " + estadoTexto);
        }
        return encontrada;
    }

    public Solicitud asignarResponsable(String codigo, String responsableId) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código obligatorio");
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
                throw new IllegalArgumentException("Estado inválido: " + estadoTexto);
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
        catch (IllegalArgumentException ex) { throw new IllegalArgumentException("Tipo inválido: " + valor); }
    }
    private Prioridad parsePrioridad(String valor) {
        try { return Prioridad.valueOf(valor.trim().toUpperCase()); }
        catch (IllegalArgumentException ex) { throw new IllegalArgumentException("Prioridad inválida: " + valor); }
    }

    // Devolución directa de la colección mutable: defecto de encapsulamiento a corregir.
    public List<Solicitud> getSolicitudes() { return solicitudes; }

    // Presentación colocada en el gestor en vez de un formatter especializado.
    public String formatearDetalle(Solicitud s) {
        return "Código=" + s.getCodigo() + " | Estudiante=" + s.getEstudiante().getNombre()
                + " (" + s.getEstudiante().getLegajo() + ")"
                + " | Correo=" + s.getEstudiante().getCorreo()
                + " | Tipo=" + s.getTipo().name()
                + " | Prioridad=" + s.getPrioridad().name()
                + " | Estado=" + s.getEstado().name()
                + " | Responsable=" + (s.getResponsable() == null ? "SIN_ASIGNAR" : s.getResponsable().getNombre())
                + " | Fecha=" + s.getFecha()
                + " | Descripción=" + s.getDescripcion()
                + " | Observación=" + s.getObservacion();
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
