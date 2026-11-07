public record DatosRegistroSolicitud(String codigo, String legajo, String descripcion,
                                     String tipo, String prioridad, String fecha,
                                     String responsableId, String observacion) {

    public static Builder conCodigo(String codigo) {
        return new Builder(codigo);
    }

    public static final class Builder {
        private final String codigo;
        private String legajo;
        private String descripcion;
        private String tipo;
        private String prioridad;
        private String fecha;
        private String responsableId;
        private String observacion;

        private Builder(String codigo) {
            this.codigo = codigo;
        }

        public Builder legajo(String legajo) {
            this.legajo = legajo;
            return this;
        }

        public Builder descripcion(String descripcion) {
            this.descripcion = descripcion;
            return this;
        }

        public Builder tipo(String tipo) {
            this.tipo = tipo;
            return this;
        }

        public Builder prioridad(String prioridad) {
            this.prioridad = prioridad;
            return this;
        }

        public Builder fecha(String fecha) {
            this.fecha = fecha;
            return this;
        }

        public Builder responsableId(String responsableId) {
            this.responsableId = responsableId;
            return this;
        }

        public Builder observacion(String observacion) {
            this.observacion = observacion;
            return this;
        }

        public DatosRegistroSolicitud construir() {
            return new DatosRegistroSolicitud(codigo, legajo, descripcion, tipo, prioridad,
                    fecha, responsableId, observacion);
        }
    }
}
