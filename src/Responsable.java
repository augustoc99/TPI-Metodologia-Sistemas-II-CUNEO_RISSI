public class Responsable {
    private final String id;
    private final String nombre;
    private final String area;

    public Responsable(String id, String nombre, String area) {
        this.id = id;
        this.nombre = nombre;
        this.area = area;
    }
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getArea() { return area; }
}
