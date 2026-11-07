import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class GestorSolicitudes {
    private static final ConversorEnum<TipoSolicitud> TIPOS =
            new ConversorEnum<>(TipoSolicitud.class, Mensajes.TIPO_OBLIGATORIO, Mensajes.TIPO_INVALIDO);
    private static final ConversorEnum<Prioridad> PRIORIDADES =
            new ConversorEnum<>(Prioridad.class, Mensajes.PRIORIDAD_OBLIGATORIA, Mensajes.PRIORIDAD_INVALIDA);
    private static final ConversorEnum<Estado> ESTADOS =
            new ConversorEnum<>(Estado.class, Mensajes.ESTADO_OBLIGATORIO, Mensajes.ESTADO_INVALIDO);

    private final List<Estudiante> estudiantes;
    private final List<Responsable> responsables;
    private final RepositorioSolicitudes repositorio;

    public GestorSolicitudes(List<Estudiante> estudiantes, List<Responsable> responsables) {
        this(estudiantes, responsables, new RepositorioSolicitudesEnMemoria());
    }

    public GestorSolicitudes(List<Estudiante> estudiantes, List<Responsable> responsables,
                             RepositorioSolicitudes repositorio) {
        this.estudiantes = estudiantes;
        this.responsables = responsables;
        this.repositorio = repositorio;
    }

    public Solicitud registrarSolicitud(DatosRegistroSolicitud datos) {
        validarCodigoNuevo(datos.codigo());
        Estudiante estudiante = buscarEstudiante(datos.legajo());
        exigirTexto(datos.descripcion(), Mensajes.DESCRIPCION_OBLIGATORIA);
        TipoSolicitud tipo = TIPOS.requerido(datos.tipo());
        Prioridad prioridad = PRIORIDADES.requerido(datos.prioridad());
        LocalDate fecha = convertirFecha(datos.fecha());
        Responsable responsable = buscarResponsableOpcional(datos.responsableId());
        Solicitud nueva = new Solicitud(datos.codigo().trim(), estudiante, datos.descripcion().trim(),
                tipo, prioridad, fecha, normalizarObservacion(datos.observacion()), responsable);
        repositorio.agregar(nueva);
        return nueva;
    }

    public Solicitud consultar(String codigo) {
        exigirTexto(codigo, Mensajes.CODIGO_OBLIGATORIO);
        return repositorio.buscarPorCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException(Mensajes.SOLICITUD_INEXISTENTE + codigo));
    }

    public Solicitud cambiarEstado(String codigo, String estadoTexto) {
        Solicitud solicitud = consultar(codigo);
        solicitud.setEstado(ESTADOS.requerido(estadoTexto));
        return solicitud;
    }

    public Solicitud asignarResponsable(String codigo, String responsableId) {
        Solicitud solicitud = consultar(codigo);
        exigirTexto(responsableId, Mensajes.RESPONSABLE_OBLIGATORIO);
        solicitud.setResponsable(buscarResponsableOpcional(responsableId));
        return solicitud;
    }

    public List<Solicitud> filtrar(String tipoTexto, String prioridadTexto, String estadoTexto) {
        TipoSolicitud tipo = TIPOS.opcional(tipoTexto);
        Prioridad prioridad = PRIORIDADES.opcional(prioridadTexto);
        Estado estado = ESTADOS.opcional(estadoTexto);
        List<Solicitud> resultado = new ArrayList<>();
        for (Solicitud solicitud : repositorio.todas()) {
            if ((tipo == null || solicitud.getTipo() == tipo)
                    && (prioridad == null || solicitud.getPrioridad() == prioridad)
                    && (estado == null || solicitud.getEstado() == estado)) {
                resultado.add(solicitud);
            }
        }
        return resultado;
    }

    public List<Solicitud> getSolicitudes() {
        return repositorio.todas();
    }

    private void validarCodigoNuevo(String codigo) {
        exigirTexto(codigo, Mensajes.CODIGO_OBLIGATORIO);
        if (repositorio.buscarPorCodigo(codigo).isPresent()) {
            throw new IllegalArgumentException(Mensajes.CODIGO_DUPLICADO + codigo.trim());
        }
    }

    private static void exigirTexto(String valor, String mensajeError) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensajeError);
        }
    }

    private Estudiante buscarEstudiante(String legajo) {
        exigirTexto(legajo, Mensajes.LEGAJO_OBLIGATORIO);
        for (Estudiante e : estudiantes) {
            if (e.getLegajo().equalsIgnoreCase(legajo.trim())) {
                return e;
            }
        }
        throw new IllegalArgumentException(Mensajes.ESTUDIANTE_INEXISTENTE + legajo);
    }

    private LocalDate convertirFecha(String fechaTexto) {
        if (fechaTexto == null) {
            throw new IllegalArgumentException(Mensajes.FECHA_INVALIDA);
        }
        try {
            return LocalDate.parse(fechaTexto);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(Mensajes.FECHA_INVALIDA);
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
        throw new IllegalArgumentException(Mensajes.RESPONSABLE_INEXISTENTE + responsableId);
    }

    private static String normalizarObservacion(String observacion) {
        return observacion == null ? "" : observacion.trim();
    }
}
