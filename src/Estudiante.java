/** Estudiante ficticio que presenta solicitudes académicas. */
public class Estudiante {
    private final String legajo;
    private final String nombre;
    private final String correo;

    public Estudiante(String legajo, String nombre, String correo) {
        this.legajo = legajo;
        this.nombre = nombre;
        this.correo = correo;
    }
    public String getLegajo() { return legajo; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
}
