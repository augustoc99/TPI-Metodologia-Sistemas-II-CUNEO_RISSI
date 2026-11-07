import java.util.List;
import java.util.Optional;

public interface RepositorioSolicitudes {
    void agregar(Solicitud solicitud);

    Optional<Solicitud> buscarPorCodigo(String codigo);

    List<Solicitud> todas();
}
