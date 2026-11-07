# Refactorings aplicados: antes y despues

Referencia del antes: tag `estado-inicial` (archivo `src/GestorSolicitudes.java`, 231 lineas). Referencia del despues: tag `estadorefactorizado`. Cada refactoring tiene su propio commit; despues de cada uno se ejecutaron las 24 pruebas originales (mas las nuevas), la demo contra `tests/expected_demo.txt` y la comparacion de caja negra (`scripts/comparar_con_inicial.sh`).

## Resumen

| ID | Tecnica | Smell | Commit | Pruebas tras el cambio |
|---|---|---|---|---|
| F1 | Extract Method | CS-01 | `a6df0a2` | 24/24, demo y caja negra iguales |
| F2 | Introduce Parameter Object | CS-02 | `38b9872` | 24/24, demo y caja negra iguales |
| F3 | Move Method (formato al presentador) | CS-05, CS-04 | `7335014` | 24/24, demo y caja negra iguales |
| F4 | Encapsulate Collection | CS-06 | `80ae61e` | 25/25 (nueva CP-25), demo y caja negra iguales |
| F5 | Extract Class (reporte) con comparacion tipada | CS-07, CS-04 | `154318f` | 26/26 (nueva CP-26), demo y caja negra iguales |
| F6 | Consolidate Duplicate Code (conversiones, mensajes, busquedas) | CS-08, CS-03 | `e9fd435` | 26/26, demo y caja negra iguales |
| F7 | Extract Interface / Extract Class (repositorio, DIP) | CS-03, CS-04 | `8f46232` | 27/27 (nueva CP-27), demo y caja negra iguales |
| F8 | Extract Class + Extract Method en la consola | Clean Code en `Main` | `c47a8f6` | 27/27, demo y caja negra iguales |

Correccion del code review: `9bce3a3` (Rename Method, observacion OBS-R01). Los requisitos minimos de la consigna (Extract Method, Move Method e Introduce Parameter Object) corresponden a F1, F3 y F2.

## F1. Extract Method en registrarSolicitud (CS-01)

Antes (`estado-inicial`, lineas 18 a 85): un metodo de 68 lineas con busqueda de duplicados, busqueda de estudiante, tres conversiones, busqueda de responsable y alta.

```java
public Solicitud registrarSolicitud(String codigo, String legajo, String descripcion,
                                   String tipoTexto, String prioridadTexto, String fechaTexto,
                                   String responsableId, String observacion) {
    if (codigo == null || codigo.isBlank()) { throw ... }
    for (Solicitud s : solicitudes) { ... }
    ...
    TipoSolicitud tipo;
    try { tipo = TipoSolicitud.valueOf(tipoTexto.trim().toUpperCase()); }
    catch (IllegalArgumentException ex) { throw ... }
    ...
}
```

Despues (14 lineas, cada paso con nombre):

```java
public Solicitud registrarSolicitud(DatosRegistroSolicitud datos) {
    validarCodigoNuevo(datos.codigo());
    Estudiante estudiante = buscarEstudiante(datos.legajo());
    exigirTexto(datos.descripcion(), Mensajes.DESCRIPCION_OBLIGATORIA);
    TipoSolicitud tipo = TIPOS.requerido(datos.tipo());
    Prioridad prioridad = PRIORIDADES.requerido(datos.prioridad());
    LocalDate fecha = convertirFecha(datos.fecha());
    Responsable responsable = esVacio(datos.responsableId()) ? null : buscarResponsable(datos.responsableId());
    Solicitud nueva = new Solicitud(...);
    repositorio.agregar(nueva);
    return nueva;
}
```

Por que: una operacion con un solo motivo de cambio por paso; el orden de validacion (que determina que error ve el usuario cuando hay varios) quedo identico y lo verifica la comparacion de caja negra con 25 errores.

## F2. Introduce Parameter Object (CS-02)

Antes: 8 parametros `String` posicionales. Una llamada como `registrarSolicitud("S004","E002","Copia programa","CERTIFICADO","ALTA","2026-11-04","","Urgente")` compila aunque se inviertan dos argumentos.

Despues: un `DatosRegistroSolicitud` (record) con constructor fluido; cada dato se nombra en el punto de llamada:

```java
DatosRegistroSolicitud.conCodigo("S004").legajo("E002")
        .descripcion("Copia programa").tipo("CERTIFICADO").prioridad("ALTA")
        .fecha("2026-11-04").responsableId("").observacion("Urgente").construir()
```

Adaptacion de API: cambia la firma publica, por eso se migraron `Main`, `DatosDemo` y las pruebas CP-01 a CP-05 y CP-24 sin cambiar sus aserciones (equivalencia en `docs/Decisiones_Tecnicas.md`). Limite: el constructor interno de `Solicitud` sigue teniendo 8 parametros (observacion OBS-R04).

## F3. Move Method (CS-05)

Antes: `GestorSolicitudes.formatearDetalle` (lineas 192 a 203) y `listar` armaban texto de consola usando getters de `Solicitud`, `Estudiante` y `Responsable`.

Despues: ambos metodos viven en `PresentadorSolicitudes` (`detalle` y `listado`). El gestor ya no conoce el formato; `Main` y las pruebas CP-06 y CP-07 usan el presentador. El texto producido es el mismo (la caja negra compara las opciones 2 y 3 del menu).

## F4. Encapsulate Collection (CS-06)

Antes: `public List<Solicitud> getSolicitudes() { return solicitudes; }` (linea 189). Evidencia: `evidencias/defecto_CP25_en_estado_inicial.txt` (un `clear()` externo deja 0 solicitudes).

Despues: el gestor devuelve una vista inmodificable (hoy a traves del repositorio) y CP-25 comprueba que `clear()` y `add()` lanzan `UnsupportedOperationException` y que el total sigue en 3.

## F5. Extract Class con comparacion tipada (CS-07)

Antes (lineas 215 a 223): `estado.equals("PENDIENTE")`, `s.getPrioridad().name().equals("ALTA")`.

Despues: `ReporteSolicitudes` cuenta con `EnumMap<Estado, Integer>` y compara `s.getPrioridad() == Prioridad.ALTA`. Si se renombra una constante, el codigo deja de compilar en lugar de contar mal; si se agrega un `Estado`, el reporte lo incluye con su nombre. CP-26 verifica un reporte con estados mezclados.

## F6. Consolidate Duplicate Code (CS-08 y parte de CS-03)

- `ConversorEnum<E>` reemplaza los `try/catch` con `valueOf(...trim().toUpperCase())` de registro, filtro y cambio de estado (tres conversiones del mismo tipo de dato en lugares distintos).
- `Mensajes` concentra los textos de error; "Codigo obligatorio" estaba escrito cuatro veces.
- `cambiarEstado` y `asignarResponsable` reutilizan `consultar` en lugar de copiar el recorrido de la lista.

`GestorSolicitudes` pasa de 231 a 131 lineas en este commit.

## F7. Extract Interface y repositorio (CS-03, CS-04, DIP)

Antes: el gestor guardaba y recorria un `ArrayList` propio en varios metodos (control de duplicados, consulta, filtro y lectura).

Despues: `RepositorioSolicitudes` (`agregar`, `buscarPorCodigo`, `todas`) con `RepositorioSolicitudesEnMemoria`. La busqueda por codigo existe una sola vez. El gestor recibe el repositorio por constructor (hay un constructor de conveniencia que usa el de memoria). CP-27 inyecta un repositorio de prueba y verifica que un alta invalida no llega al repositorio.

## F8. Consola (Clean Code)

Antes: `Main.main` de 41 lineas con un `switch` que mezclaba lectura de datos, llamadas y formato. Despues: `ConsolaSolicitudes` con un metodo por opcion; `Main` queda como punto de entrada y demo.

## Metricas

Ver `evidencias/metricas_antes_despues.txt` (generadas con `scripts/medir_metricas.py`, conteo simple de lineas y llaves, no analisis estatico).

| Medida | estado-inicial | version refactorizada |
|---|---|---|
| Archivos en `src` | 9 | 17 |
| Lineas totales en `src` | 379 | 581 |
| Lineas de `GestorSolicitudes` | 231 | 131 |
| Metodo mas largo | `registrarSolicitud`, 68 lineas | `ConsolaSolicitudes.procesar`, 18 lineas |
| `registrarSolicitud` | 68 lineas, 8 parametros | 14 lineas, 1 parametro |

El codigo total crecio: hay mas clases y mas lineas de estructura (por ejemplo el objeto parametro y el repositorio). La ganancia que se afirma es de localizacion del cambio y de tamano de cada pieza, no de menos codigo.
