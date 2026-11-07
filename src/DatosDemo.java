import java.util.List;

/** Datos sintéticos: no representan identidades de estudiantes reales. */
public class DatosDemo {
    public static GestorSolicitudes crearGestor() {
        GestorSolicitudes gestor = new GestorSolicitudes(
            List.of(new Estudiante("E001", "Ana Ficticia", "ana@example.invalid"),
                    new Estudiante("E002", "Bruno Ejemplo", "bruno@example.invalid")),
            List.of(new Responsable("R01", "Mesa Académica", "Alumnos"),
                    new Responsable("R02", "Coordinación", "Secretaría")));
        gestor.registrarSolicitud("S001", "E001", "Constancia regular", "CERTIFICADO", "MEDIA", "2026-11-01", "R01", "");
        gestor.registrarSolicitud("S002", "E002", "Reconocer materia", "EQUIVALENCIA", "ALTA", "2026-11-02", "", "Revisión documental");
        gestor.registrarSolicitud("S003", "E001", "Alta cursada", "INSCRIPCION", "BAJA", "2026-11-03", "R02", "");
        return gestor;
    }
}
