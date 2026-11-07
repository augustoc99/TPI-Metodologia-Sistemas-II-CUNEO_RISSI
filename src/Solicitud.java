import java.time.LocalDate;

public class Solicitud {
    private final String codigo;
    private final Estudiante estudiante;
    private final String descripcion;
    private final TipoSolicitud tipo;
    private final Prioridad prioridad;
    private final LocalDate fecha;
    private final String observacion;
    private Estado estado;
    private Responsable responsable;

    public Solicitud(String codigo, Estudiante estudiante, String descripcion,
                     TipoSolicitud tipo, Prioridad prioridad, LocalDate fecha,
                     String observacion, Responsable responsable) {
        this.codigo = codigo;
        this.estudiante = estudiante;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.fecha = fecha;
        this.observacion = observacion;
        this.responsable = responsable;
        this.estado = Estado.PENDIENTE;
    }
    public String getCodigo() { return codigo; }
    public Estudiante getEstudiante() { return estudiante; }
    public String getDescripcion() { return descripcion; }
    public TipoSolicitud getTipo() { return tipo; }
    public Prioridad getPrioridad() { return prioridad; }
    public LocalDate getFecha() { return fecha; }
    public String getObservacion() { return observacion; }
    public Estado getEstado() { return estado; }
    public Responsable getResponsable() { return responsable; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public void setResponsable(Responsable responsable) { this.responsable = responsable; }
}
