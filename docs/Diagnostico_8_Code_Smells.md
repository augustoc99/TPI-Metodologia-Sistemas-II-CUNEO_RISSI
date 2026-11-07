# Diagnóstico verificable de la versión inicial — 8 problemas de diseño

**Objeto de examen:** commit/tag `estado-inicial`, archivo `src/GestorSolicitudes.java`, líneas numeradas mediante `nl -ba`. **Alcance:** debilidades de calidad interna, no afirmaciones de fallos funcionales. El prototipo fue desarrollado para este TPI y su deuda técnica se conservó deliberadamente como objeto de estudio. La prioridad es orientativa (P1 = alta, P2 = media, P3 = baja); el riesgo es cualitativo, no una medición estadística.

| ID | Smell / debilidad | Ubicación exacta | Evidencia literal verificable | Impacto y riesgo de mantenimiento | Prioridad | Mejora sugerida |
|---|---|---|---|---|---|---|
| CS-01 | **Long Method**: registro monolítico | `GestorSolicitudes.registrarSolicitud`, líneas 18–85 | `for (Solicitud s : solicitudes)`, `TipoSolicitud.valueOf(...)`, `LocalDate.parse(...)`, `solicitudes.add(nueva)` en el mismo método | Una operación concentra duplicidad, búsqueda, validación, parseo y persistencia; agregar reglas puede producir regresiones en entradas válidas. **Riesgo alto**. | P1 | `Extract Method` para validación, resolución y construcción |
| CS-02 | **Long Parameter List / data clump** | `GestorSolicitudes.registrarSolicitud`, líneas 18–20 | `String codigo, String legajo, String descripcion, String tipoTexto, String prioridadTexto, String fechaTexto, String responsableId, String observacion` | Ocho valores posicionales del mismo tipo favorecen inversión accidental de argumentos y llamadas extensas. **Riesgo medio-alto**. | P1 | `Introduce Parameter Object` (`DatosRegistroSolicitud`) |
| CS-03 | **Duplicated Code: búsqueda por código** | `GestorSolicitudes.consultar` 91–96; `cambiarEstado` 103–112; `asignarResponsable` 128–137 | Se repite `for (Solicitud s : solicitudes)` y `s.getCodigo().equalsIgnoreCase(codigo.trim())` con búsqueda de coincidencia | Cambiar normalización o estrategia de identificación exige editar tres lugares; riesgo de comportamiento divergente. **Riesgo alto**. | P1 | Reutilizar `consultar`/extraer `buscarPorCodigo` |
| CS-04 | **Divergent Change / SRP cuestionable** | `GestorSolicitudes`, líneas 18–230 (`registrarSolicitud`, `filtrar`, `formatearDetalle`, `reporte`) | La misma clase cambia por reglas de ingreso, consultas, presentación y formato del reporte | Un cambio de la vista obliga a recompilar el componente que almacena y aplica reglas. **Riesgo alto**. | P1 | Separar `ServicioSolicitudes`, `Repositorio` y `Presentador/Reportes` conforme necesidad |
| CS-05 | **Feature Envy / formato lejos de su responsabilidad** | `GestorSolicitudes.formatearDetalle`, líneas 192–203 | Múltiples llamadas a `s.get...()` y `s.getEstudiante().get...()` para componer texto | El gestor conoce detalles de dos entidades y del formato de consola; ajustes de presentación aumentan acoplamiento. **Riesgo medio**. | P2 | `Move Method` / reubicar formato a `PresentadorSolicitudes` |
| CS-06 | **Encapsulación rota: exposición de colección mutable** | `GestorSolicitudes.getSolicitudes`, líneas 188–189 | `public List<Solicitud> getSolicitudes() { return solicitudes; }` | Un cliente podría ejecutar `clear()` o `add()` sin validaciones: riesgo de invalidar invariantes sin pasar por operaciones del gestor. **Riesgo alto** (potencial, no ejecutado en demo). | P1 | `Encapsulate Collection`: vista inmodificable/copia; API específica de lectura |
| CS-07 | **Magic Strings / lógica frágil a nombres de enums** | `GestorSolicitudes.reporte`, líneas 215–223 | `estado.equals("PENDIENTE")`, `estado.equals("EN_PROCESO")`, `s.getPrioridad().name().equals("ALTA")` | El reporte convierte enums a cadenas y compara textos; renombrar un valor puede dejar conteos incorrectos sin advertencia de compilación. **Riesgo medio-alto**. | P1 | Comparar enums tipados / `EnumMap`, extraer `ReporteSolicitudes` |
| CS-08 | **Duplicación de conversión y manejo de errores** | `registrarSolicitud` 45–62 y `filtrar` 155–167, `parseTipo` 179–185 | `TipoSolicitud.valueOf(tipoTexto.trim().toUpperCase())` aparece en registro y `parseTipo`; idem prioridad | La política de validación debe actualizarse en múltiples puntos; mensajes y normalización pueden divergir. **Riesgo medio**. | P2 | Centralizar parsers validados y reutilizarlos en ambos flujos |

## Diferenciación entre hallazgos relacionados

- **CS-01** afecta el tamaño y secuencia de responsabilidades dentro de *una operación*; **CS-04** afecta la cohesión de *la clase completa*.
- **CS-03** duplica la búsqueda de solicitudes; **CS-08** duplica conversiones a enums: son fragmentos, cambios y riesgos distintos.
- **CS-05** localiza una responsabilidad de presentación mal ubicada; **CS-04** registra el acoplamiento arquitectónico mayor. Se puede solucionar parcialmente uno sin resolver el otro.
- **CS-02** se refiere a contrato de entrada; **CS-06** a fuga del estado interno en contrato de salida.

## Riesgo, deuda y costo del cambio

El sistema satisface los casos de prueba de la línea base, pero cambiar tipos, estados, formato o almacenamiento obliga a revisar varios sectores. Es deuda técnica **deliberada para fines docentes**, no evidencia de negligencia en un sistema de producción. El mayor beneficio esperado proviene de reducir zonas de modificación simultánea y reforzar invariantes.

## Evidencia reproducible

`nl -ba src/GestorSolicitudes.java | sed -n '1,235p'` sobre **`estado-inicial`**. Las líneas no deben desplazarse retroactivamente: al editar código en la etapa 2, comparar con `git show estado-inicial:src/GestorSolicitudes.java` y registrar nuevas líneas por separado.

**Fuentes conceptuales del material adjunto:** `SEMANA_1_CALIDAD.pdf` (calidad, deuda, smells), `SEMANA_II_SOLID.pdf` (responsabilidad, extensibilidad, dependencias), `SEMANA_III_CLEAN_CODE.pdf` (nombres, funciones y errores), `Trabajo Práctico I - Calidad de Diseño y Construcción.pdf` (ejemplos de duplicación y responsabilidades) y `Trabajo práctico II - SOLID.pdf` (distinción entre principios aplicables/no aplicables). No se presume que LSP o ISP estén violados: la línea base no tiene jerarquías ni interfaces suficientes para sostener esa afirmación.
