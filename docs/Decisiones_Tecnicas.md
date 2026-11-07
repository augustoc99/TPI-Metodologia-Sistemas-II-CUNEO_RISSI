# Decisiones tecnicas: SOLID y Clean Code aplicados

## Responsabilidades despues del refactoring

| Clase | Responsabilidad unica |
|---|---|
| `GestorSolicitudes` | Coordinar los casos de uso (registrar, consultar, cambiar estado, asignar, filtrar) y sus validaciones |
| `RepositorioSolicitudes` y `RepositorioSolicitudesEnMemoria` | Guardar y buscar solicitudes |
| `DatosRegistroSolicitud` | Transportar los datos de entrada del alta con nombres |
| `ConversorEnum` | Convertir texto en enum con los mensajes de error del campo |
| `Mensajes` | Textos de error en un solo lugar |
| `PresentadorSolicitudes` | Dar formato de texto a una solicitud y al listado |
| `ReporteSolicitudes` | Contar y formatear el reporte |
| `ConsolaSolicitudes` y `Main` | Entrada y salida por consola |

## Principios

- SRP: aplicado (tabla anterior). Antes, `GestorSolicitudes` cambiaba por reglas de alta, busquedas, formato, estadistica y almacenamiento (CS-04).
- OCP: aplicado de forma acotada. `ReporteSolicitudes` incluye cualquier `Estado` nuevo sin modificar la logica de conteo; `ConversorEnum` sirve para cualquier enum. No se afirma OCP en el resto del sistema.
- LSP: no se declara violacion previa ni mejora general. Solo hay una abstraccion nueva (`RepositorioSolicitudes`); CP-27 la sustituye por otra implementacion y el gestor se comporta igual, que es la unica evidencia disponible.
- ISP: la interfaz del repositorio tiene tres operaciones que el gestor usa todas. No hay interfaces grandes que dividir.
- DIP: el gestor depende de `RepositorioSolicitudes` y la implementacion se inyecta por constructor. Se aplica porque el almacenamiento es el punto de variacion mas probable (hoy en memoria, mañana archivo o base de datos).

## Decisiones y alternativas descartadas

- No se crearon interfaces para el presentador ni para el reporte: hoy no hay una segunda variante y la interfaz seria una clase de mas sin uso.
- No se creo un repositorio para estudiantes y responsables: son listas de consulta que llegan por constructor; quedan como observacion OBS-R02.
- Se uso un constructor fluido en `DatosRegistroSolicitud` porque un record con ocho `String` posicionales conserva el riesgo de invertir argumentos que motivo CS-02.
- Los literales con tildes del codigo fuente se escriben como escapes unicode (`ó`). Asi el codigo fuente queda en ASCII, compila igual con cualquier codificacion y la salida es identica a la del estado inicial. Costo: menor legibilidad de esos textos (OBS-R07).
- Se eliminaron los comentarios y javadocs: los nombres de metodos y clases describen la intencion. Excepcion: los documentos de `docs`.

## Adaptaciones de las pruebas

Se cambio la forma de llamar al sistema, no lo que se verifica. Las 24 pruebas originales conservan identificador, nombre y aserciones.

| Antes | Despues | Pruebas afectadas |
|---|---|---|
| `g.registrarSolicitud("S004","E001",...,"nota")` | `g.registrarSolicitud(altaValida("S004").observacion("nota").construir())` | CP-01 a CP-05, CP-24 |
| `g.listar()` | `new PresentadorSolicitudes().listado(g.getSolicitudes())` | CP-06, CP-07 |
| `g.reporte()` | `new ReporteSolicitudes().generar(g.getSolicitudes())` | CP-21 a CP-23 |

Pruebas agregadas: CP-25 (coleccion no mutable), CP-26 (reporte con estados mezclados), CP-27 (repositorio inyectado y alta invalida que no llega al repositorio).

Como las pruebas unitarias cambiaron de llamada, la equivalencia con la version inicial se respalda ademas con `scripts/comparar_con_inicial.sh`: compila `estado-inicial` y la version actual, les envia la misma entrada (`tests/entrada_comparacion.txt`: todas las operaciones del menu y 25 errores de validacion) y compara la salida completa. Se comprobo que la comparacion detecta cambios alterando a proposito un texto del reporte: el diff lo mostro.
