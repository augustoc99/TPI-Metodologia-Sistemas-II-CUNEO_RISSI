# Code review profesional: checklist y acta pendiente

## Alcance y regla de uso

**Versión evaluada actualmente:** `estado-inicial`, para diagnóstico únicamente. La revisión profesional **de la versión refactorizada** corresponde al Camila Rissi y se ejecutará cuando existan cambios. No marcar sin evidencia. Para cada ítem registrar: estado (`OK / OBSERVACIÓN / NO APLICA`), archivo/línea, severidad, recomendación, responsable de corrección y commit de cierre.

| ID | Criterio de revisión | Línea base | Estado de revisión final |
|---|---|---|---|
| RV-01 | Los nombres de métodos, variables y clases expresan el dominio | `x` en filtro (170), nombres mixtos mejorables | PENDIENTE |
| RV-02 | Cada método tiene propósito único y longitud razonable | CS-01, 18–85 | PENDIENTE |
| RV-03 | No hay lógica de búsqueda/parsing duplicada | CS-03, CS-08 | PENDIENTE |
| RV-04 | Responsabilidades distribuidas con cohesión | CS-04, CS-05 | PENDIENTE |
| RV-05 | No se exponen colecciones mutables | CS-06 | PENDIENTE |
| RV-06 | Se manejan nulos y datos inválidos con mensajes previsibles | CP-02 a CP-05, CP-24 | PENDIENTE |
| RV-07 | No existen comparaciones frágiles entre enums y strings | CS-07 | PENDIENTE |
| RV-08 | Lógica de dominio independiente de presentación, si es viable | CS-05 | PENDIENTE |
| RV-09 | Contratos y responsabilidades de los objetos claros | CS-02 / F2 | PENDIENTE |
| RV-10 | Las nuevas abstracciones tienen utilidad (sin sobreingeniería) | DIP propuesto condicionado | PENDIENTE |
| RV-11 | Se conservan las seis operaciones funcionales | 24 CP + demo | PENDIENTE |
| RV-12 | Pruebas de bordes y errores ejecutadas tras cada cambio | Logs previos disponibles | PENDIENTE |
| RV-13 | Snapshot de demostración sin cambios inesperados | `expected_demo.txt` | PENDIENTE |
| RV-14 | No se inventan resultados de herramientas ni benchmarks | Sin benchmark efectuado | PENDIENTE |
| RV-15 | Cada refactoring enlaza antes → commit → pruebas | Plan F1–F6 | PENDIENTE |
| RV-16 | Commits y tags Git representan hitos reales | Solo tags iniciales en etapa 1 | PENDIENTE |
| RV-17 | Documentación y README corresponden al código actual | Documentado para línea base | PENDIENTE |
| RV-18 | No hay datos reales ni secretos | Datos de ejemplo `.invalid` | PENDIENTE |

## Registro de observaciones preliminares (no es revisión final)

- **OBS-01 / Alta / `GestorSolicitudes.java:189`:** colección mutable expuesta. Corrección esperada F4. Rechazar revisión final si persiste sin justificación.
- **OBS-02 / Alta / `GestorSolicitudes.java:18–85`:** método de alta con ocho parámetros y validaciones mezcladas. F1 y F2 obligatorias según la propuesta; revisar errores y orden de validación.
- **OBS-03 / Media / `GestorSolicitudes.java:219–222`:** reporte sujeto a cadenas literales; corregir comparaciones por enum y probar conteos.
- **OBS-04 / Media / `GestorSolicitudes.java:192–203`:** formateo fuera de la capa de presentación; separar con compatibilidad de salidas.

## Acta de revisión de la versión refactorizada — A COMPLETAR CON EVIDENCIA REAL

- Revisor/a efectivo/a: **[integrante que revise]**; fecha: **[fecha real]**.
- Commit evaluado: **[hash real]**; rama: **[rama real]**.
- Cambios evaluados: **[listar commits F1–F...]**.
- Hallazgos remanentes, acciones y commits de corrección: **[registro posterior]**.
- Criterio de aceptación: pruebas base + adicionales sin fallos, comportamiento preservado, cuatro refactorings demostrados, observaciones bloqueantes resueltas.
- Dictamen: **NO EMITIDO (etapa 1)**.
