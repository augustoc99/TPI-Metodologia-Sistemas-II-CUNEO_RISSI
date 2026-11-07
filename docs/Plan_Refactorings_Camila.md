# Plan técnico de intervención — reservado para Camila Rissi

**Estado:** todas las intervenciones siguientes son **propuestas no implementadas**. Se diseñaron después de ejecutar la versión inicial y sus 24 pruebas. No modificar el tag `estado-inicial`.

| Orden | Técnica requerida / adicional | Smells y código origen | Transformación propuesta | Riesgo a controlar | Pruebas y aceptación |
|---|---|---|---|---|---|
| F1 | **Extract Method** | CS-01, `registrarSolicitud` (18–85) | Extraer `validarCodigoUnico`, `resolverEstudiante`, `resolverResponsable`, `convertirFecha` y construcción, manteniendo orden de errores | Mensajes distintos ante entradas ambiguas; registro parcial | CP-01 a CP-05, CP-24; no insertar si falla validación |
| F2 | **Introduce Parameter Object** | CS-02, firma de ocho argumentos (18–20) | Crear `DatosRegistroSolicitud` con campos del dominio. Adaptar CLI/fixture y preservar salida | Cambiar posición/semántica de argumentos; compatibilidad del contrato | CP-01 a CP-05, CP-24; snapshot demo exacto |
| F3 | **Move Method / reubicación de responsabilidad** | CS-05, `formatearDetalle` (192–203), CS-04 | Mover a `PresentadorSolicitudes.formatearDetalle(Solicitud)`; delegar desde la UI y retirar formato del gestor o mantener delegador transitorio | Variar espacios, etiquetas, nulos `SIN_ASIGNAR` | CP-06–10; snapshot exacto y prueba de salida completa |
| F4 | **Encapsulate Collection** | CS-06, `getSolicitudes` (189) | Retornar `List.copyOf(solicitudes)` o vista inmutable; crear `contarSolicitudes` para cliente; no entregar colección modificable | Cambiar referencias compartidas, afectar tests y demo | CP-01, CP-06, CP-07, CP-24 y nueva prueba `UnsupportedOperationException` |
| F5 | **Extract Class + Replace Magic Strings** | CS-04, CS-07, `reporte` (215–230) | Servicio `GeneradorReportes` y comparaciones enum (`Estado.PENDIENTE`) o `EnumMap`; conservar formato | Modificar conteos/orden de líneas | CP-21 a CP-23, snapshot demo |
| F6 | **Consolidate Duplicate Conditional / Extract Lookup** | CS-03, CS-08 (87–185) | Reutilizar `consultar` y conversiones `parseTipo/parsePrioridad`; eliminar bucles y parses redundantes | Diferencias en precedencia/errores para IDs vacíos | CP-08–20; pruebas de mayúsculas/minúsculas |

## Aplicación justificada de SOLID, sin afirmaciones retroactivas

- **SRP:** objetivo concreto de F1, F3 y F5: separar lógica de registro, formato y reporte. La línea base **no** cumple plenamente esta separación.
- **OCP:** una vez aislado el cómputo del reporte o repositorio, futuras variantes pueden añadirse con menor edición del gestor. No es necesario crear una jerarquía artificial para demostrarlo.
- **DIP:** si se introduce repositorio, que el gestor dependa de `RepositorioSolicitudes` y que memoria sea una implementación. Justificar si añade valor: con datos mínimos podría ser sobreingeniería.
- **LSP e ISP:** no hay violaciones demostradas de estos principios en la línea base; explicar su no aplicabilidad a jerarquías ausentes es mejor que afirmar los cinco por obligación.
- **Clean Code:** funciones acotadas, nombres de negocio, manejo de errores explícito, menos duplicación y menores acoplamientos.

## Secuencia de trabajo seguro

1. Crear rama `refactorizacion`, verificar `git status` limpio y conservar `estado-inicial`.
2. Repetir pruebas al comienzo, guardar log, aplicar F1 y F2 en commits pequeños; volver a probar.
3. Aplicar F3 y F4, agregar prueba de encapsulación y reejecutar comparaciones.
4. Aplicar F5 y F6, documentar resultados y capturas reales de código antes/después.
5. Ejecutar y registrar checklist de code review; corregir observaciones graves antes del tag final.
6. Crear hitos `etapa-refactoring`, `etapa-pruebas`, `etapa-docs` y finalmente `estadorefactorizado`, con commits claros. Evitar tags que apunten a trabajo no ejecutado.

## Evidencia mínima de cada refactoring

Antes: cita al código inicial, línea y síntoma; después: commit/diff real con ubicación; criterio de aceptación: resultado repetido de la suite, snapshot y observaciones del revisor. `docs/Checklist_Code_Review.md` es el formulario de revisión pendiente: no marcar «aprobado» sin revisión humana efectiva.
