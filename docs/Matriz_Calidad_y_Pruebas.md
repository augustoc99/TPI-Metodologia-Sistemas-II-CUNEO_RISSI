# Matriz de calidad externa e interna y pruebas de línea base

## Diferenciación desde dos perspectivas

| Dimensión | Evidencia en el prototipo | Diagnóstico acotado |
|---|---|---|
| Calidad externa: eficacia | Se registran, consultan, asignan y filtran solicitudes; la demo imprime un reporte con cuatro registros. | En los escenarios CP-01 a CP-24 se obtuvieron resultados esperados. No equivale a pruebas exhaustivas. |
| Calidad externa: fiabilidad | Ante códigos duplicados, fechas inválidas y entidades inexistentes se informan errores controlados. | No se observaron cierres inesperados en los casos probados. Sin estrés ni concurrencia evaluados. |
| Calidad externa: usabilidad | Menú numerado de consola y mensajes de error específicos. | Adecuado para prototipo académico; requiere conocer IDs y tipos válidos. Sin investigación con usuarios. |
| Calidad externa: desempeño | Datos en memoria; demo determinista. | No hay benchmark ni medición de latencias: no atribuir rapidez cuantificada. Búsqueda secuencial escala linealmente. |
| Calidad interna: mantenibilidad | Ocho hallazgos específicos CS-01 a CS-08 con líneas de fuente. | Estructura todavía no refactorizada, deuda consciente y priorizada. |
| Calidad interna: legibilidad | Enum de dominio y entidades explícitas; método de registro extenso y formatos mezclados. | Mezcla de aciertos de modelado y problemas de claridad local. |
| Calidad interna: cohesión/acoplamiento | Gestor abarca validación, almacén, búsqueda, formato, estadísticas. | Varias razones para modificar la misma clase; CS-04, CS-05. |
| Calidad interna: testabilidad | Suite propia sin dependencias, fixture nuevo por caso. | Posibilidad de prueba automatizada, pero hay acoplamiento y estado mutable; CS-06. |

## Matriz de casos (ejecutados antes de refactorizar)

La suite `tests/PruebasBase.java` verifica entradas, comportamiento esperado y errores. Cada caso reconstruye su gestor, evita contaminación entre pruebas y arroja una línea `[OK]` o `[FAIL]`.

| Caso | Operación / entrada representativa | Resultado esperado | Resultado observado en la ejecución inicial |
|---|---|---|---|
| CP-01 | Registrar `S004`, estudiante `E001`, tipo CERTIFICADO, fecha válida | Código `S004` y total 4 | OK |
| CP-02 | Registrar código repetido `S001` | Excepción `Código duplicado` | OK |
| CP-03 | Registrar con legajo `NO` | Excepción `Estudiante inexistente` | OK |
| CP-04 | Registrar fecha `2026-13-40` | Excepción `Fecha inválida` | OK |
| CP-05 | Registrar tipo `OTRO` | Excepción `Tipo inválido` | OK |
| CP-06 | Listar datos precargados | Aparecen `S001`, `S002`, `S003` | OK |
| CP-07 | Listar gestor sin registros | Texto `Sin solicitudes` | OK |
| CP-08 | Consultar `s001` | Legajo `E001` | OK |
| CP-09 | Consultar `S999` | Excepción `Solicitud inexistente` | OK |
| CP-10 | Consultar código vacío | Excepción `Código obligatorio` | OK |
| CP-11 | Cambiar estado a `resuelta` | Estado `RESUELTA` | OK |
| CP-12 | Cambiar estado a `PAUSADA` | Excepción `Estado inválido` | OK |
| CP-13 | Cambio de estado inválido | Estado previo `PENDIENTE` se mantiene | OK |
| CP-14 | Asignar `r02` | Responsable `R02` | OK |
| CP-15 | Asignar `R99` | Excepción `Responsable inexistente` | OK |
| CP-16 | Asignación inválida | Responsable anterior `R01` se mantiene | OK |
| CP-17 | Filtrar prioridad ALTA | 1 coincidencia | OK |
| CP-18 | Filtrar CERTIFICADO+MEDIA+PENDIENTE | 1 coincidencia | OK |
| CP-19 | Filtrar combinación sin registros | 0 coincidencias | OK |
| CP-20 | Filtrar tipo OTRO | Excepción `Tipo inválido` | OK |
| CP-21 | Reporte inicial | Total 3, pendientes 3, altas 1 | OK |
| CP-22 | Reporte tras resolver `S001` | Pendientes 2, resueltas 1 | OK |
| CP-23 | Reporte de gestor vacío | Total 0 | OK |
| CP-24 | Alta con responsable inexistente | No se agrega solicitud: total 3 | OK |

**Resultado real:** 24 OK, 0 FAIL; ver `evidencias/pruebas_iniciales.txt`. La demo `Main demo` ejecutó el circuito de 6 funciones y la salida quedó fijada en `tests/expected_demo.txt`. El script `scripts/verificar_regresion.sh` contrasta la salida exacta mediante `diff -u`.

## Riesgos fuera del alcance de la suite

La suite no somete a carga elevada, no valida persistencia real, seguridad de datos, concurrencia, interfaz gráfica ni navegación accesible; no debe afirmarse que cubre esos aspectos. Para CS-06 se propone agregar en etapa 2 un caso que pruebe que un consumidor ya no puede mutar directamente la colección expuesta.

## Verificación posterior requerida

1. Ejecutar `bash scripts/verificar_regresion.sh` en la rama refactorizada y guardar nuevo log.
2. Contrastar `git diff estado-inicial..estadorefactorizado -- src/` y dejar evidencia de cada transformación.
3. Si la API pública cambia por `Introduce Parameter Object` o encapsulamiento, preservar mediante adaptador temporal la batería base o migrar pruebas documentando la equivalencia del comportamiento observable.
4. Repetir errores y bordes de CP-01 a CP-24; añadir pruebas específicas para invariantes, inyección de dependencias y nuevo objeto parámetro.
5. No considerar «seguro» un refactoring exclusivamente por compilar: también revisar salida y reglas del dominio.

## Resultados posteriores al refactoring (etapa 2)

Version evaluada: commit `9bce3a3`. Resultado real: 27 aprobadas, 0 fallidas (`evidencias/pruebas_refactorizado.txt`). Los casos CP-01 a CP-24 conservan identificador, nombre y aserciones; solo cambio la forma de llamar al sistema (ver `docs/Decisiones_Tecnicas.md`).

| Caso | Operacion / entrada representativa | Resultado esperado | Resultado observado |
|---|---|---|---|
| CP-25 | Intentar `clear()` y `add()` sobre `getSolicitudes()` | `UnsupportedOperationException` y total 3. En `estado-inicial` la lista quedaba vacia | OK |
| CP-26 | Reporte tras resolver S001 y poner S002 en proceso | Total 3, pendientes 1, en proceso 1, resueltas 1, altas 1 | OK |
| CP-27 | Gestor con repositorio inyectado: alta invalida, alta valida, duplicado | El repositorio no recibe el alta invalida, recibe una valida y el duplicado se rechaza | OK |

Comparacion de caja negra (`scripts/comparar_con_inicial.sh`): misma entrada (`tests/entrada_comparacion.txt`) sobre `estado-inicial` y la version actual, 185 lineas de salida sin diferencias, con 25 errores controlados. La comparacion detecta cambios: alterando un texto del reporte el diff lo mostro.
