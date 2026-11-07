# Sistema de Gestión de Solicitudes Académicas

**Metodología de Sistemas II · Trabajo Práctico Integrador**  
**Augusto M. Cúneo** — versión inicial y diagnóstico  
**Camila Rissi** — refactorización y revisión técnica

## Descripción del sistema

Prototipo académico de consola en **Java 17**, sin dependencias externas ni datos personales reales. Simula solicitudes en memoria estructurada mediante `Estudiante`, `Solicitud` y `Responsable`. Implementa seis funciones: registrar, listar/consultar, cambiar estado, asignar responsable, filtrar y generar reporte. La versión inicial (`estado-inicial`) contenía problemas de diseño deliberadamente conservados con fines de análisis; la versión final (`estadorefactorizado`) los corrige sin cambiar el comportamiento observable.

## Requisitos y ejecución

Se necesita **JDK 17 o posterior**. Desde PowerShell en la raíz de este repositorio:

```powershell
.\scripts\probar.ps1
```

O, si se desea ejecutar por separado:

```powershell
New-Item -ItemType Directory -Force build | Out-Null
javac --release 17 -encoding UTF-8 -d build (Get-ChildItem src,tests -Filter *.java).FullName
java -cp build PruebasBase
java -cp build Main demo
java -cp build Main
```

En Linux/macOS: `bash scripts/probar.sh`. Para comparar el comportamiento observado con la salida de referencia: `bash scripts/verificar_regresion.sh`.

## Estructura del codigo (version refactorizada)

- `GestorSolicitudes`: casos de uso y validaciones.
- `RepositorioSolicitudes` y `RepositorioSolicitudesEnMemoria`: almacenamiento.
- `DatosRegistroSolicitud`: objeto parametro del alta (constructor fluido).
- `ConversorEnum` y `Mensajes`: conversion de texto a enum y textos de error.
- `PresentadorSolicitudes` y `ReporteSolicitudes`: formato del detalle, listado y reporte.
- `ConsolaSolicitudes` y `Main`: menu de consola y demo.
- `Solicitud`, `Estudiante`, `Responsable`, `Estado`, `Prioridad`, `TipoSolicitud`, `DatosDemo`: modelo y datos de ejemplo.

## Verificacion

- `bash scripts/verificar_regresion.sh`: compila, ejecuta las 27 pruebas (`tests/PruebasBase.java`) y compara la demo con `tests/expected_demo.txt`.
- `bash scripts/comparar_con_inicial.sh`: compila `estado-inicial` y la version actual, les envia la misma entrada (`tests/entrada_comparacion.txt`) y compara la salida completa. Requiere bash (en Windows, Git Bash o WSL) y el tag `estado-inicial` en el repositorio.
- `python3 scripts/medir_metricas.py`: lineas y tamaño de metodos antes y despues.
- Los scripts usan `-Dstdout.encoding=UTF-8` solo en la comparacion; si la consola muestra mal las tildes, ejecutar con `JAVA_TOOL_OPTIONS="-Dstdout.encoding=UTF-8 -Dfile.encoding=UTF-8"`.

## Evidencias y documentacion

- `tests/PruebasBase.java`: 27 casos automatizados (CP-01 a CP-27).
- `tests/expected_demo.txt` y `tests/entrada_comparacion.txt`: referencia de la demo y entrada de la comparacion de caja negra.
- `evidencias/pruebas_iniciales.txt` y `evidencias/demo_inicial.txt`: ejecuciones antes de refactorizar.
- `evidencias/pruebas_refactorizado.txt`, `evidencias/demo_refactorizado.txt`, `evidencias/comparacion_caja_negra/`, `evidencias/defecto_CP25_en_estado_inicial.txt` y `evidencias/metricas_antes_despues.txt`: ejecuciones posteriores.
- `docs/Diagnostico_8_Code_Smells.md`: ubicacion, evidencia, riesgo y prioridad de las 8 debilidades.
- `docs/Matriz_Calidad_y_Pruebas.md`: calidad externa e interna y trazabilidad de pruebas, con los resultados posteriores.
- `docs/Refactorings_Antes_Despues.md`: ocho refactorings con antes, despues, commit y pruebas.
- `docs/Decisiones_Tecnicas.md`: SOLID, Clean Code, decisiones y adaptaciones de las pruebas.
- `docs/Checklist_CodeReview.md`: checklist aplicado, observaciones, correcciones y acta.
- `docs/Registro_Uso_IA.md`: uso de inteligencia artificial en cada etapa.

## Versiones Git

| Tag | Contenido |
|---|---|
| `estado-inicial` | Sistema base con 24 pruebas |
| `etapa-diagnostico` | Diagnostico de los 8 code smells |
| `etapa-refactoring` | Refactorings F1 a F8 aplicados |
| `etapa-pruebas` | Evidencias de ejecucion posteriores (27 pruebas, caja negra) |
| `etapa-docs` | Documentacion de refactorings, decisiones y code review |
| `estadorefactorizado` | Estado final entregable |

Los tags historicos no se modifican. Comparar versiones: `git diff estado-inicial..estadorefactorizado -- src/`.

## Uso de inteligencia artificial

Etapa 1: ChatGPT (OpenAI), para revision del codigo, criterios de calidad y diseño de la presentacion. Etapa 2: Claude (Anthropic) para refactorings, pruebas, documentacion y revision de codigo. Detalle, finalidad, grado de intervencion y prompts en `docs/Registro_Uso_IA.md`.
