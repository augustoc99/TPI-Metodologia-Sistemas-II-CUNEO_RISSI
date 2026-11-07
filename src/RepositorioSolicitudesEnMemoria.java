import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RepositorioSolicitudesEnMemoria implements RepositorioSolicitudes {
    private final List<Solicitud> solicitudes = new ArrayList<>();

    @Override
    public void agregar(Solicitud solicitud) {
        solicitudes.add(solicitud);
    }

    @Override
    public Optional<Solicitud> buscarPorCodigo(String codigo) {
        String buscado = codigo.trim();
        for (Solicitud solicitud : solicitudes) {
            if (solicitud.getCodigo().equalsIgnoreCase(buscado)) {
                return Optional.of(solicitud);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Solicitud> todas() {
        return Collections.unmodifiableList(solicitudes);
    }
}
