import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GestorSolicitudes gestor = DatosDemo.crearGestor();
        PresentadorSolicitudes presentador = new PresentadorSolicitudes();
        ReporteSolicitudes reporte = new ReporteSolicitudes();
        if (args.length > 0 && args[0].equals("demo")) {
            ejecutarDemo(gestor, reporte);
            return;
        }
        try (Scanner scanner = new Scanner(System.in)) {
            new ConsolaSolicitudes(gestor, presentador, reporte, scanner).ejecutar();
        }
    }

    private static void ejecutarDemo(GestorSolicitudes gestor, ReporteSolicitudes reporte) {
        DatosRegistroSolicitud nueva = DatosRegistroSolicitud.conCodigo("S004").legajo("E002")
                .descripcion("Copia programa").tipo("CERTIFICADO").prioridad("ALTA")
                .fecha("2026-11-04").responsableId("").observacion("Urgente").construir();
        System.out.println("=== DEMO DETERMINISTA / VERSIÓN INICIAL ===");
        System.out.println("Registrar S004: " + gestor.registrarSolicitud(nueva).getCodigo());
        System.out.println("Consultar S004: " + gestor.consultar("S004").getEstado());
        System.out.println("Asignar S004: " + gestor.asignarResponsable("S004", "R01").getResponsable().getId());
        System.out.println("Cambiar estado S004: " + gestor.cambiarEstado("S004", "EN_PROCESO").getEstado());
        System.out.println("Filtro prioridad ALTA: " + gestor.filtrar("", "ALTA", "").size());
        System.out.print(reporte.generar(gestor.getSolicitudes()));
        System.out.println("Listar: " + gestor.getSolicitudes().size() + " registros");
        System.out.println("=== FIN DEMO ===");
    }
}
