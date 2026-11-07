import java.util.List;

/** Pruebas sin frameworks ni dependencias externas. Cada CP es independiente. */
public class PruebasBase {
    private static int aprobadas = 0;
    private static int fallidas = 0;

    public static void main(String[] args) {
        caso("CP-01", "Registrar válida", () -> {
            GestorSolicitudes g = DatosDemo.crearGestor();
            eq("S004", g.registrarSolicitud("S004","E001","Solicitud","CERTIFICADO","MEDIA","2026-11-04","","nota").getCodigo());
            eq(4, g.getSolicitudes().size());
        });
        caso("CP-02", "Rechazar código duplicado", () -> error(() -> DatosDemo.crearGestor().registrarSolicitud("S001","E001","Solicitud","CERTIFICADO","MEDIA","2026-11-04","",""), "Código duplicado"));
        caso("CP-03", "Rechazar estudiante inexistente", () -> error(() -> DatosDemo.crearGestor().registrarSolicitud("S004","NO","Solicitud","CERTIFICADO","MEDIA","2026-11-04","",""), "Estudiante inexistente"));
        caso("CP-04", "Rechazar fecha inválida", () -> error(() -> DatosDemo.crearGestor().registrarSolicitud("S004","E001","Solicitud","CERTIFICADO","MEDIA","2026-13-40","",""), "Fecha inválida"));
        caso("CP-05", "Rechazar tipo inválido", () -> error(() -> DatosDemo.crearGestor().registrarSolicitud("S004","E001","Solicitud","OTRO","MEDIA","2026-11-04","",""), "Tipo inválido"));
        caso("CP-06", "Listar tres registros", () -> {
            String listado = DatosDemo.crearGestor().listar();
            check(listado.contains("S001") && listado.contains("S002") && listado.contains("S003"), "faltan códigos");
        });
        caso("CP-07", "Listar vacío", () -> {
            GestorSolicitudes g = new GestorSolicitudes(List.of(), List.of());
            check(g.listar().contains("Sin solicitudes"), "no informa vacío");
        });
        caso("CP-08", "Consultar existente", () -> eq("E001", DatosDemo.crearGestor().consultar("s001").getEstudiante().getLegajo()));
        caso("CP-09", "Consultar inexistente", () -> error(() -> DatosDemo.crearGestor().consultar("S999"), "Solicitud inexistente"));
        caso("CP-10", "Consultar código vacío", () -> error(() -> DatosDemo.crearGestor().consultar(" "), "Código obligatorio"));
        caso("CP-11", "Cambiar estado", () -> eq(Estado.RESUELTA, DatosDemo.crearGestor().cambiarEstado("S001","resuelta").getEstado()));
        caso("CP-12", "Rechazar estado inválido", () -> error(() -> DatosDemo.crearGestor().cambiarEstado("S001","PAUSADA"), "Estado inválido"));
        caso("CP-13", "No mutar estado ante error", () -> {
            GestorSolicitudes g = DatosDemo.crearGestor();
            error(() -> g.cambiarEstado("S001","PAUSADA"), "Estado inválido");
            eq(Estado.PENDIENTE, g.consultar("S001").getEstado());
        });
        caso("CP-14", "Asignar responsable", () -> eq("R02", DatosDemo.crearGestor().asignarResponsable("S002","r02").getResponsable().getId()));
        caso("CP-15", "Responsable inexistente", () -> error(() -> DatosDemo.crearGestor().asignarResponsable("S001","R99"), "Responsable inexistente"));
        caso("CP-16", "Asignación no muta al fallar", () -> {
            GestorSolicitudes g = DatosDemo.crearGestor();
            error(() -> g.asignarResponsable("S001","R99"), "Responsable inexistente");
            eq("R01", g.consultar("S001").getResponsable().getId());
        });
        caso("CP-17", "Filtrar prioridad ALTA", () -> eq(1, DatosDemo.crearGestor().filtrar("","ALTA","").size()));
        caso("CP-18", "Filtrar combinación de 3 criterios", () -> eq(1, DatosDemo.crearGestor().filtrar("CERTIFICADO","MEDIA","PENDIENTE").size()));
        caso("CP-19", "Filtro sin coincidencias", () -> eq(0, DatosDemo.crearGestor().filtrar("EQUIVALENCIA","BAJA","").size()));
        caso("CP-20", "Filtro inválido", () -> error(() -> DatosDemo.crearGestor().filtrar("OTRO","", ""), "Tipo inválido"));
        caso("CP-21", "Reporte inicial consistente", () -> {
            String r = DatosDemo.crearGestor().reporte();
            check(r.contains("Total: 3") && r.contains("Pendientes: 3") && r.contains("Prioridad alta: 1"), "conteos erróneos");
        });
        caso("CP-22", "Reporte luego de cambio estado", () -> {
            GestorSolicitudes g = DatosDemo.crearGestor();
            g.cambiarEstado("S001", "RESUELTA");
            check(g.reporte().contains("Pendientes: 2") && g.reporte().contains("Resueltas: 1"), "conteos erróneos");
        });
        caso("CP-23", "Reporte sin solicitudes", () -> check(new GestorSolicitudes(List.of(),List.of()).reporte().contains("Total: 0"), "sin cero"));
        caso("CP-24", "Proteger registro de responsable inválido", () -> {
            GestorSolicitudes g = DatosDemo.crearGestor();
            error(() -> g.registrarSolicitud("S004","E001","Solicitud","CERTIFICADO","MEDIA","2026-11-04","NO",""), "Responsable inexistente");
            eq(3, g.getSolicitudes().size());
        });
        System.out.printf("TOTAL: %d aprobadas, %d fallidas%n", aprobadas, fallidas);
        if (fallidas != 0) System.exit(1);
    }
    private static void caso(String id, String nombre, Runnable accion) {
        try { accion.run(); aprobadas++; System.out.println("[OK] " + id + " " + nombre); }
        catch (Throwable t) { fallidas++; System.out.println("[FAIL] " + id + " " + nombre + " -> " + t); }
    }
    private static void eq(Object esperado, Object real) {
        if (!esperado.equals(real)) throw new AssertionError("esperado=" + esperado + " real=" + real);
    }
    private static void check(boolean condicion, String mensaje) {
        if (!condicion) throw new AssertionError(mensaje);
    }
    private static void error(Runnable r, String texto) {
        try { r.run(); } catch (IllegalArgumentException ex) {
            if (ex.getMessage() != null && ex.getMessage().contains(texto)) return;
            throw new AssertionError("excepción no coincide: " + ex.getMessage());
        }
        throw new AssertionError("se esperaba error: " + texto);
    }
}
