# Code review profesional: checklist y acta

## Alcance

Version evaluada: codigo en el commit `9bce3a3` (rama `main`), comparado con el tag `estado-inicial`. Fecha de la revision: 2026-11-07. Criterios: 18 puntos RV-01 a RV-18 (definidos en la etapa 1). Estados: `OK`, `OBSERVACION`, `NO APLICA`.

Esta revision la preparo Claude (IA) por pedido de Camila Rissi, leyendo el codigo completo y ejecutando las pruebas. El dictamen final tiene que ser confirmado por Camila Rissi despues de leer el codigo (seccion Acta).

## Resultado por criterio

| ID | Criterio | Estado | Evidencia (archivo) | Detalle |
|---|---|---|---|---|
| RV-01 | Nombres expresan el dominio | OK | `GestorSolicitudes.java` | La variable `x` del filtro paso a `solicitud`. Se corrigio un nombre engañoso (OBS-R01) |
| RV-02 | Un proposito y longitud razonable por metodo | OK | `evidencias/metricas_antes_despues.txt` | Metodo mas largo: 18 lineas (`ConsolaSolicitudes.procesar`, un switch). Antes: 68 |
| RV-03 | Sin busqueda ni parsing duplicado | OK | `RepositorioSolicitudesEnMemoria.java`, `ConversorEnum.java` | Una busqueda por codigo y una conversion por enum |
| RV-04 | Responsabilidades con cohesion | OBSERVACION | `GestorSolicitudes.java` | Mejora clara (formato, reporte, almacenamiento y consola afuera). El gestor aun busca estudiantes y responsables (OBS-R02) |
| RV-05 | Sin colecciones mutables expuestas | OK | `RepositorioSolicitudesEnMemoria.todas`, CP-25 | Vista inmodificable |
| RV-06 | Nulos y datos invalidos con mensajes previsibles | OBSERVACION | `ConversorEnum.java` | 25 errores de la caja negra con mensajes identicos. `toUpperCase()` depende de la configuracion regional (OBS-R03) |
| RV-07 | Sin comparaciones enum contra texto | OK | `ReporteSolicitudes.java` | `EnumMap` y `==` |
| RV-08 | Dominio independiente de la presentacion | OK | `GestorSolicitudes.java` | El gestor no imprime ni formatea |
| RV-09 | Contratos claros | OBSERVACION | `DatosRegistroSolicitud.java`, `Solicitud.java` | El alta recibe un objeto. El constructor de `Solicitud` mantiene 8 parametros (OBS-R04) |
| RV-10 | Abstracciones con utilidad | OK | `RepositorioSolicitudes.java` | Una sola interfaz, con segunda implementacion en CP-27. Sin interfaces para presentador ni reporte |
| RV-11 | Se conservan las seis operaciones | OK | `evidencias/comparacion_caja_negra/` | Salida de 185 lineas identica a `estado-inicial` |
| RV-12 | Pruebas tras cada cambio | OK | mensajes de commit F1 a F8 | Cada commit registra el resultado de las pruebas |
| RV-13 | Snapshot de la demo sin cambios | OK | `evidencias/demo_refactorizado.txt` | Igual a `tests/expected_demo.txt` |
| RV-14 | No se inventan resultados | OK | `evidencias/` | Logs generados con los scripts del repositorio. Las metricas son conteos simples, no analisis estatico |
| RV-15 | Antes, commit y pruebas enlazados | OK | `docs/Refactorings_Antes_Despues.md` | Tabla con commit y resultado por refactoring |
| RV-16 | Commits y tags son hitos reales | OK | `git tag` | `etapa-refactoring`, `etapa-pruebas`, `etapa-docs` y `estadorefactorizado` (ver README) |
| RV-17 | Documentacion corresponde al codigo | OK | `README.md` | Actualizado; se corrigieron dos enlaces a archivos inexistentes (OBS-R05) |
| RV-18 | Sin datos reales ni secretos | OK | `DatosDemo.java` | Datos ficticios con dominio `example.invalid` |

## Observaciones y acciones

| ID | Severidad | Ubicacion | Observacion | Accion | Estado |
|---|---|---|---|---|---|
| OBS-01 (etapa 1) | Alta | `getSolicitudes` | Coleccion mutable expuesta | F4, prueba CP-25 | Cerrada (`80ae61e`) |
| OBS-02 (etapa 1) | Alta | `registrarSolicitud` | Metodo largo y 8 parametros | F1 y F2 | Cerrada (`a6df0a2`, `38b9872`) |
| OBS-03 (etapa 1) | Media | `reporte` | Comparacion por texto | F5, prueba CP-26 | Cerrada (`154318f`) |
| OBS-04 (etapa 1) | Media | `formatearDetalle` | Formato en el gestor | F3 | Cerrada (`7335014`) |
| OBS-R01 | Baja | `GestorSolicitudes.asignarResponsable` | Llamaba a `buscarResponsableOpcional` siendo el dato obligatorio | Rename Method y rama de vacio movida al registro | Cerrada (`9bce3a3`) |
| OBS-R02 | Baja | `GestorSolicitudes.buscarEstudiante`, `buscarResponsable` | Busquedas lineales de estudiantes y responsables dentro del gestor | Evaluar catalogos con la misma forma que el repositorio si crecen las entidades | Abierta, mejora futura |
| OBS-R03 | Baja | `ConversorEnum.convertir` | `toUpperCase()` sin `Locale.ROOT` (en configuracion regional turca la letra i cambia). Viene del codigo inicial | No se cambia en un refactoring porque altera comportamiento; usar `Locale.ROOT` en una etapa posterior | Abierta |
| OBS-R04 | Baja | `Solicitud` (constructor) | 8 parametros en el constructor de la entidad | Se construye en un solo lugar; un builder seria sobreingenieria hoy | Abierta, aceptada |
| OBS-R05 | Baja | `README.md` | Referenciaba `docs/Plan_Refactorings_Camila.md` y `docs/Checklist_Code_Review.md`, que no existen con esos nombres | Corregido en el README | Cerrada (commit de documentacion) |
| OBS-R06 | Informativa | `scripts/*.sh` | Los scripts de regresion y de comparacion requieren bash; en Windows se usan con Git Bash o WSL | Documentado en el README | Abierta, informativa |
| OBS-R07 | Informativa | `src/*.java` | Los literales con tildes estan como escapes unicode, menos legibles | Decision documentada en `docs/Decisiones_Tecnicas.md` | Aceptada |

## Criterios de aceptacion tecnica

1. Pruebas base y adicionales sin fallos: 27 aprobadas, 0 fallidas (`evidencias/pruebas_refactorizado.txt`). Cumplido.
2. Comportamiento externo preservado: demo igual al snapshot y salida de caja negra identica a `estado-inicial`. Cumplido.
3. Al menos cuatro refactorings demostrados, con Extract Method, Move Method e Introduce Parameter Object: ocho aplicados (F1 a F8). Cumplido.
4. Observaciones bloqueantes resueltas: ninguna observacion alta o media queda abierta. Cumplido.

## Acta

- Revision preparada por: Claude (IA), con ejecucion real de pruebas; fecha 2026-11-07.
- Commit evaluado: `9bce3a3`; rama `main`.
- Cambios evaluados: commits `a5fef43` a `9bce3a3` (F1 a F8, evidencias y correccion OBS-R01).
- Hallazgos remanentes: OBS-R02, OBS-R03, OBS-R04, OBS-R06 y OBS-R07 (ninguna bloqueante).
- Dictamen propuesto: aceptada con observaciones menores.
- Confirmacion del dictamen por la integrante que defiende el trabajo: Camila Rissi, [pendiente: completar fecha y firma despues de leer el codigo].
