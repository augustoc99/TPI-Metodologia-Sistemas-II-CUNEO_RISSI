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
                System.out.println("\nGESTIÓN DE SOLICITUDES ACADÉMICAS");
                System.out.println("1 Registrar | 2 Listar | 3 Consultar | 4 Estado | 5 Asignar | 6 Filtrar | 7 Reporte | 0 Salir");
                System.out.print("Opción: ");
                if (!scanner.hasNextLine()) break;
                String opcion = scanner.nextLine().trim();
                try {
                    switch (opcion) {
                        case "1" -> {
                            String codigo = pedir(scanner,"Código");
                            String legajo = pedir(scanner,"Legajo E001/E002");
                            String descripcion = pedir(scanner,"Descripción");
                            String tipo = pedir(scanner,"Tipo CERTIFICADO/EQUIVALENCIA/INSCRIPCION");
                            String prioridad = pedir(scanner,"Prioridad BAJA/MEDIA/ALTA");
                            String fecha = pedir(scanner,"Fecha AAAA-MM-DD");
                            String responsable = pedir(scanner,"Responsable R01/R02 (opcional)");
                            String observacion = pedir(scanner,"Observación (opcional)");
                            System.out.println("Registrada: " + gestor.registrarSolicitud(codigo,legajo,descripcion,tipo,prioridad,fecha,responsable,observacion).getCodigo());
                        }
                        case "2" -> System.out.print(gestor.listar());
                        case "3" -> System.out.println(gestor.formatearDetalle(gestor.consultar(pedir(scanner,"Código"))));
                        case "4" -> System.out.println("Estado actualizado: " + gestor.cambiarEstado(pedir(scanner,"Código"),pedir(scanner,"Nuevo estado")).getEstado());
                        case "5" -> System.out.println("Responsable asignado: " + gestor.asignarResponsable(pedir(scanner,"Código"),pedir(scanner,"Responsable R01/R02")).getResponsable().getNombre());
                        case "6" -> System.out.println("Coincidencias: " + gestor.filtrar(pedir(scanner,"Tipo (opcional)"),pedir(scanner,"Prioridad (opcional)"),pedir(scanner,"Estado (opcional)")).size());
                        case "7" -> System.out.print(gestor.reporte());
                        case "0" -> { System.out.println("Hasta luego"); return; }
                        default -> System.out.println("Opción inválida");
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
        System.out.println("=== DEMO DETERMINISTA / VERSIÓN INICIAL ===");
        System.out.println("Registrar S004: " + gestor.registrarSolicitud("S004","E002","Copia programa","CERTIFICADO","ALTA","2026-11-04","","Urgente").getCodigo());
        System.out.println("Consultar S004: " + gestor.consultar("S004").getEstado());
        System.out.println("Asignar S004: " + gestor.asignarResponsable("S004","R01").getResponsable().getId());
        System.out.println("Cambiar estado S004: " + gestor.cambiarEstado("S004","EN_PROCESO").getEstado());
        System.out.println("Filtro prioridad ALTA: " + gestor.filtrar("","ALTA","").size());
        System.out.print(gestor.reporte());
        System.out.println("Listar: " + gestor.getSolicitudes().size() + " registros");
        System.out.println("=== FIN DEMO ===");
    }
}
