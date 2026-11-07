import java.util.List;

public class PruebasBase {
    private static int aprobadas = 0;
    private static int fallidas = 0;

    public static void main(String[] args) {
        caso("CP-01", "Registrar v\u00e1lida", () -> {
            GestorSolicitudes g = DatosDemo.crearGestor();
            eq("S004", g.registrarSolicitud(altaValida("S004").observacion("nota").construir()).getCodigo());
            eq(4, g.getSolicitudes().size());
        });
        caso("CP-02", "Rechazar c\u00f3digo duplicado", () -> error(() -> DatosDemo.crearGestor().registrarSolicitud(altaValida("S001").construir()), "C\u00f3digo duplicado"));
        caso("CP-03", "Rechazar estudiante inexistente", () -> error(() -> DatosDemo.crearGestor().registrarSolicitud(altaValida("S004").legajo("NO").construir()), "Estudiante inexistente"));
        caso("CP-04", "Rechazar fecha inv\u00e1lida", () -> error(() -> DatosDemo.crearGestor().registrarSolicitud(altaValida("S004").fecha("2026-13-40").construir()), "Fecha inv\u00e1lida"));
        caso("CP-05", "Rechazar tipo inv\u00e1lido", () -> error(() -> DatosDemo.crearGestor().registrarSolicitud(altaValida("S004").tipo("OTRO").construir()), "Tipo inv\u00e1lido"));
        caso("CP-06", "Listar tres registros", () -> {
            String listado = new PresentadorSolicitudes().listado(DatosDemo.crearGestor().getSolicitudes());
            check(listado.contains("S001") && listado.contains("S002") && listado.contains("S003"), "faltan c\u00f3digos");
        });
        caso("CP-07", "Listar vac\u00edo", () -> {
            GestorSolicitudes g = new GestorSolicitudes(List.of(), List.of());
            check(new PresentadorSolicitudes().listado(g.getSolicitudes()).contains("Sin solicitudes"), "no informa vac\u00edo");
        });
        caso("CP-08", "Consultar existente", () -> eq("E001", DatosDemo.crearGestor().consultar("s001").getEstudiante().getLegajo()));
        caso("CP-09", "Consultar inexistente", () -> error(() -> DatosDemo.crearGestor().consultar("S999"), "Solicitud inexistente"));
        caso("CP-10", "Consultar c\u00f3digo vac\u00edo", () -> error(() -> DatosDemo.crearGestor().consultar(" "), "C\u00f3digo obligatorio"));
        caso("CP-11", "Cambiar estado", () -> eq(Estado.RESUELTA, DatosDemo.crearGestor().cambiarEstado("S001","resuelta").getEstado()));
        caso("CP-12", "Rechazar estado inv\u00e1lido", () -> error(() -> DatosDemo.crearGestor().cambiarEstado("S001","PAUSADA"), "Estado inv\u00e1lido"));
        caso("CP-13", "No mutar estado ante error", () -> {
            GestorSolicitudes g = DatosDemo.crearGestor();
            error(() -> g.cambiarEstado("S001","PAUSADA"), "Estado inv\u00e1lido");
            eq(Estado.PENDIENTE, g.consultar("S001").getEstado());
        });
        caso("CP-14", "Asignar responsable", () -> eq("R02", DatosDemo.crearGestor().asignarResponsable("S002","r02").getResponsable().getId()));
        caso("CP-15", "Responsable inexistente", () -> error(() -> DatosDemo.crearGestor().asignarResponsable("S001","R99"), "Responsable inexistente"));
        caso("CP-16", "Asignaci\u00f3n no muta al fallar", () -> {
            GestorSolicitudes g = DatosDemo.crearGestor();
            error(() -> g.asignarResponsable("S001","R99"), "Responsable inexistente");
            eq("R01", g.consultar("S001").getResponsable().getId());
        });
        caso("CP-17", "Filtrar prioridad ALTA", () -> eq(1, DatosDemo.crearGestor().filtrar("","ALTA","").size()));
        caso("CP-18", "Filtrar combinaci\u00f3n de 3 criterios", () -> eq(1, DatosDemo.crearGestor().filtrar("CERTIFICADO","MEDIA","PENDIENTE").size()));
        caso("CP-19", "Filtro sin coincidencias", () -> eq(0, DatosDemo.crearGestor().filtrar("EQUIVALENCIA","BAJA","").size()));
        caso("CP-20", "Filtro inv\u00e1lido", () -> error(() -> DatosDemo.crearGestor().filtrar("OTRO","", ""), "Tipo inv\u00e1lido"));
        caso("CP-21", "Reporte inicial consistente", () -> {
            String r = DatosDemo.crearGestor().reporte();
            check(r.contains("Total: 3") && r.contains("Pendientes: 3") && r.contains("Prioridad alta: 1"), "conteos err\u00f3neos");
        });
        caso("CP-22", "Reporte luego de cambio estado", () -> {
            GestorSolicitudes g = DatosDemo.crearGestor();
            g.cambiarEstado("S001", "RESUELTA");
            check(g.reporte().contains("Pendientes: 2") && g.reporte().contains("Resueltas: 1"), "conteos err\u00f3neos");
        });
        caso("CP-23", "Reporte sin solicitudes", () -> check(new GestorSolicitudes(List.of(),List.of()).reporte().contains("Total: 0"), "sin cero"));
        caso("CP-24", "Proteger registro de responsable inv\u00e1lido", () -> {
            GestorSolicitudes g = DatosDemo.crearGestor();
            error(() -> g.registrarSolicitud(altaValida("S004").responsableId("NO").construir()), "Responsable inexistente");
            eq(3, g.getSolicitudes().size());
        });
        System.out.printf("TOTAL: %d aprobadas, %d fallidas%n", aprobadas, fallidas);
        if (fallidas != 0) System.exit(1);
    }
    private static DatosRegistroSolicitud.Builder altaValida(String codigo) {
        return DatosRegistroSolicitud.conCodigo(codigo).legajo("E001").descripcion("Solicitud")
                .tipo("CERTIFICADO").prioridad("MEDIA").fecha("2026-11-04")
                .responsableId("").observacion("");
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
            throw new AssertionError("excepci\u00f3n no coincide: " + ex.getMessage());
        }
        throw new AssertionError("se esperaba error: " + texto);
    }
}
