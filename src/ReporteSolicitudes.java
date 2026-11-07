import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ReporteSolicitudes {
    private static final Map<Estado, String> ETIQUETAS_ESTADO = new EnumMap<>(Estado.class);

    static {
        ETIQUETAS_ESTADO.put(Estado.PENDIENTE, "Pendientes");
        ETIQUETAS_ESTADO.put(Estado.EN_PROCESO, "En proceso");
        ETIQUETAS_ESTADO.put(Estado.RESUELTA, "Resueltas");
    }

    public String generar(List<Solicitud> solicitudes) {
        Map<Estado, Integer> porEstado = contarPorEstado(solicitudes);
        StringBuilder texto = new StringBuilder("REPORTE DE SOLICITUDES\n");
        texto.append("Total: ").append(solicitudes.size()).append('\n');
        for (Estado estado : Estado.values()) {
            String etiqueta = ETIQUETAS_ESTADO.getOrDefault(estado, estado.name());
            texto.append(etiqueta).append(": ").append(porEstado.getOrDefault(estado, 0)).append('\n');
        }
        texto.append("Prioridad alta: ").append(contarPrioridadAlta(solicitudes)).append('\n');
        return texto.toString();
    }

    private static Map<Estado, Integer> contarPorEstado(List<Solicitud> solicitudes) {
        Map<Estado, Integer> conteo = new EnumMap<>(Estado.class);
        for (Solicitud solicitud : solicitudes) {
            conteo.merge(solicitud.getEstado(), 1, Integer::sum);
        }
        return conteo;
    }

    private static long contarPrioridadAlta(List<Solicitud> solicitudes) {
        return solicitudes.stream().filter(s -> s.getPrioridad() == Prioridad.ALTA).count();
    }
}
