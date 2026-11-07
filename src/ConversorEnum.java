public final class ConversorEnum<E extends Enum<E>> {
    private final Class<E> tipo;
    private final String mensajeObligatorio;
    private final String prefijoInvalido;

    public ConversorEnum(Class<E> tipo, String mensajeObligatorio, String prefijoInvalido) {
        this.tipo = tipo;
        this.mensajeObligatorio = mensajeObligatorio;
        this.prefijoInvalido = prefijoInvalido;
    }

    public E requerido(String texto) {
        if (esVacio(texto)) {
            throw new IllegalArgumentException(mensajeObligatorio);
        }
        return convertir(texto);
    }

    public E opcional(String texto) {
        return esVacio(texto) ? null : convertir(texto);
    }

    private E convertir(String texto) {
        try {
            return Enum.valueOf(tipo, texto.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(prefijoInvalido + texto);
        }
    }

    private static boolean esVacio(String texto) {
        return texto == null || texto.isBlank();
    }
}
