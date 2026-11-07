import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GestorSolicitudes gestor = DatosDemo.crearGestor();
        if (args.length > 0 && args[0].equals("demo")) {
            ejecutarDemo(gestor);
            return;
        }
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\nGESTI\u00d3N DE SOLICITUDES ACAD\u00c9MICAS");
                System.out.println("1 Registrar | 2 Listar | 3 Consultar | 4 Estado | 5 Asignar | 6 Filtrar | 7 Reporte | 0 Salir");
                System.out.print("Opci\u00f3n: ");
                if (!scanner.hasNextLine()) break;
                String opcion = scanner.nextLine().trim();
                try {
                    switch (opcion) {
                        case "1" -> {
                            String codigo = pedir(scanner,"C\u00f3digo");
                            String legajo = pedir(scanner,"Legajo E001/E002");
                            String descripcion = pedir(scanner,"Descripci\u00f3n");
                            String tipo = pedir(scanner,"Tipo CERTIFICADO/EQUIVALENCIA/INSCRIPCION");
                            String prioridad = pedir(scanner,"Prioridad BAJA/MEDIA/ALTA");
                            String fecha = pedir(scanner,"Fecha AAAA-MM-DD");
                            String responsable = pedir(scanner,"Responsable R01/R02 (opcional)");
                            String observacion = pedir(scanner,"Observaci\u00f3n (opcional)");
                            DatosRegistroSolicitud datos = DatosRegistroSolicitud.conCodigo(codigo).legajo(legajo)
                                    .descripcion(descripcion).tipo(tipo).prioridad(prioridad)
                                    .fecha(fecha).responsableId(responsable).observacion(observacion).construir();
                            System.out.println("Registrada: " + gestor.registrarSolicitud(datos).getCodigo());
                        }
                        case "2" -> System.out.print(gestor.listar());
                        case "3" -> System.out.println(gestor.formatearDetalle(gestor.consultar(pedir(scanner,"C\u00f3digo"))));
                        case "4" -> System.out.println("Estado actualizado: " + gestor.cambiarEstado(pedir(scanner,"C\u00f3digo"),pedir(scanner,"Nuevo estado")).getEstado());
                        case "5" -> System.out.println("Responsable asignado: " + gestor.asignarResponsable(pedir(scanner,"C\u00f3digo"),pedir(scanner,"Responsable R01/R02")).getResponsable().getNombre());
                        case "6" -> System.out.println("Coincidencias: " + gestor.filtrar(pedir(scanner,"Tipo (opcional)"),pedir(scanner,"Prioridad (opcional)"),pedir(scanner,"Estado (opcional)")).size());
                        case "7" -> System.out.print(gestor.reporte());
                        case "0" -> { System.out.println("Hasta luego"); return; }
                        default -> System.out.println("Opci\u00f3n inv\u00e1lida");
                    }
                } catch (IllegalArgumentException ex) {
                    System.out.println("ERROR: " + ex.getMessage());
                }
            }
        }
    }
    private static String pedir(Scanner s, String etiqueta) {
        System.out.print(etiqueta + ": ");
        return s.nextLine();
    }
    private static void ejecutarDemo(GestorSolicitudes gestor) {
        System.out.println("=== DEMO DETERMINISTA / VERSI\u00d3N INICIAL ===");
        System.out.println("Registrar S004: " + gestor.registrarSolicitud(DatosRegistroSolicitud.conCodigo("S004").legajo("E002")
                .descripcion("Copia programa").tipo("CERTIFICADO").prioridad("ALTA")
                .fecha("2026-11-04").responsableId("").observacion("Urgente").construir()).getCodigo());
        System.out.println("Consultar S004: " + gestor.consultar("S004").getEstado());
        System.out.println("Asignar S004: " + gestor.asignarResponsable("S004","R01").getResponsable().getId());
        System.out.println("Cambiar estado S004: " + gestor.cambiarEstado("S004","EN_PROCESO").getEstado());
        System.out.println("Filtro prioridad ALTA: " + gestor.filtrar("","ALTA","").size());
        System.out.print(gestor.reporte());
        System.out.println("Listar: " + gestor.getSolicitudes().size() + " registros");
        System.out.println("=== FIN DEMO ===");
    }
}
