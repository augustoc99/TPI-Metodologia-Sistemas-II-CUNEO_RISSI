import java.util.Scanner;

public class ConsolaSolicitudes {
    private final GestorSolicitudes gestor;
    private final PresentadorSolicitudes presentador;
    private final ReporteSolicitudes reporte;
    private final Scanner entrada;

    public ConsolaSolicitudes(GestorSolicitudes gestor, PresentadorSolicitudes presentador,
                              ReporteSolicitudes reporte, Scanner entrada) {
        this.gestor = gestor;
        this.presentador = presentador;
        this.reporte = reporte;
        this.entrada = entrada;
    }

    public void ejecutar() {
        while (true) {
            mostrarMenu();
            if (!entrada.hasNextLine()) {
                return;
            }
            String opcion = entrada.nextLine().trim();
            try {
                if (!procesar(opcion)) {
                    return;
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("\nGESTIÓN DE SOLICITUDES ACADÉMICAS");
        System.out.println("1 Registrar | 2 Listar | 3 Consultar | 4 Estado | 5 Asignar | 6 Filtrar | 7 Reporte | 0 Salir");
        System.out.print("Opción: ");
    }

    private boolean procesar(String opcion) {
        switch (opcion) {
            case "1" -> registrar();
            case "2" -> System.out.print(presentador.listado(gestor.getSolicitudes()));
            case "3" -> consultar();
            case "4" -> cambiarEstado();
            case "5" -> asignarResponsable();
            case "6" -> filtrar();
            case "7" -> System.out.print(reporte.generar(gestor.getSolicitudes()));
            case "0" -> {
                System.out.println("Hasta luego");
                return false;
            }
            default -> System.out.println("Opción inválida");
        }
        return true;
    }

    private void registrar() {
        String codigo = pedir("Código");
        String legajo = pedir("Legajo E001/E002");
        String descripcion = pedir("Descripción");
        String tipo = pedir("Tipo CERTIFICADO/EQUIVALENCIA/INSCRIPCION");
        String prioridad = pedir("Prioridad BAJA/MEDIA/ALTA");
        String fecha = pedir("Fecha AAAA-MM-DD");
        String responsable = pedir("Responsable R01/R02 (opcional)");
        String observacion = pedir("Observación (opcional)");
        DatosRegistroSolicitud datos = DatosRegistroSolicitud.conCodigo(codigo).legajo(legajo)
                .descripcion(descripcion).tipo(tipo).prioridad(prioridad).fecha(fecha)
                .responsableId(responsable).observacion(observacion).construir();
        System.out.println("Registrada: " + gestor.registrarSolicitud(datos).getCodigo());
    }

    private void consultar() {
        String codigo = pedir("Código");
        System.out.println(presentador.detalle(gestor.consultar(codigo)));
    }

    private void cambiarEstado() {
        String codigo = pedir("Código");
        String estado = pedir("Nuevo estado");
        System.out.println("Estado actualizado: " + gestor.cambiarEstado(codigo, estado).getEstado());
    }

    private void asignarResponsable() {
        String codigo = pedir("Código");
        String responsable = pedir("Responsable R01/R02");
        System.out.println("Responsable asignado: "
                + gestor.asignarResponsable(codigo, responsable).getResponsable().getNombre());
    }

    private void filtrar() {
        String tipo = pedir("Tipo (opcional)");
        String prioridad = pedir("Prioridad (opcional)");
        String estado = pedir("Estado (opcional)");
        System.out.println("Coincidencias: " + gestor.filtrar(tipo, prioridad, estado).size());
    }

    private String pedir(String etiqueta) {
        System.out.print(etiqueta + ": ");
        return entrada.nextLine();
    }
}
