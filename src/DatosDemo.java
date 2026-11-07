import java.util.List;

public class DatosDemo {
    public static GestorSolicitudes crearGestor() {
        GestorSolicitudes gestor = new GestorSolicitudes(
            List.of(new Estudiante("E001", "Ana Ficticia", "ana@example.invalid"),
                    new Estudiante("E002", "Bruno Ejemplo", "bruno@example.invalid")),
            List.of(new Responsable("R01", "Mesa Acad\u00e9mica", "Alumnos"),
                    new Responsable("R02", "Coordinaci\u00f3n", "Secretar\u00eda")));
        gestor.registrarSolicitud(DatosRegistroSolicitud.conCodigo("S001").legajo("E001")
                .descripcion("Constancia regular").tipo("CERTIFICADO").prioridad("MEDIA")
                .fecha("2026-11-01").responsableId("R01").observacion("").construir());
        gestor.registrarSolicitud(DatosRegistroSolicitud.conCodigo("S002").legajo("E002")
                .descripcion("Reconocer materia").tipo("EQUIVALENCIA").prioridad("ALTA")
                .fecha("2026-11-02").responsableId("").observacion("Revisi\u00f3n documental").construir());
        gestor.registrarSolicitud(DatosRegistroSolicitud.conCodigo("S003").legajo("E001")
                .descripcion("Alta cursada").tipo("INSCRIPCION").prioridad("BAJA")
                .fecha("2026-11-03").responsableId("R02").observacion("").construir());
        return gestor;
    }
}
