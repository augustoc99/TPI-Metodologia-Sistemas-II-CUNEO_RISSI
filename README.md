# Sistema de Gestión de Solicitudes Académicas

**Metodología de Sistemas II · Trabajo Práctico Integrador**  
**Augusto M. Cúneo** — versión inicial y diagnóstico  
**Camila Rissi** — refactorización y revisión técnica

## Descripción del sistema

Prototipo académico de consola en **Java 17**, sin dependencias externas ni datos personales reales. Simula solicitudes en memoria estructurada mediante `Estudiante`, `Solicitud` y `Responsable`. Implementa seis funciones: registrar, listar/consultar, cambiar estado, asignar responsable, filtrar y generar reporte. La versión inicial contiene problemas de diseño deliberadamente conservados con fines de análisis, sin errores funcionales conocidos en la batería de pruebas.

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

## Evidencias y diagnóstico

- `tests/PruebasBase.java`: **24 casos automatizados** (CP-01 a CP-24).
- `tests/expected_demo.txt`: salida determinista usada en la comparación antes/después.
- `evidencias/pruebas_iniciales.txt` y `evidencias/demo_inicial.txt`: resultados realmente ejecutados antes de la refactorización.
- `docs/Diagnostico_8_Code_Smells.md`: ubicación, evidencia, riesgo, prioridad y alternativas para 8 debilidades reales.
- `docs/Matriz_Calidad_y_Pruebas.md`: diferenciación entre calidad externa/interna y trazabilidad de pruebas.
- `docs/Plan_Refactorings_Camila.md`: cuatro técnicas mínimas y otras propuestas, todavía no aplicadas.
- `docs/Checklist_Code_Review.md`: criterios para revisión de la versión final; la aprobación final no está emitida.

## Versiones Git

La entrega inicial se identifica con `estado-inicial` y el análisis con `etapa-diagnostico`. **Camila Rissi** deberá completar y etiquetar efectivamente `etapa-refactoring`, `etapa-pruebas`, `etapa-docs` y `estadorefactorizado`; esos hitos no existen todavía en esta entrega inicial. Mantener los tags históricos sin alterarlos.

## Uso de inteligencia artificial

Se utilizó ChatGPT (OpenAI) como herramienta de apoyo para la revisión y corroboración del código, consulta de criterios de calidad de software y diseño gráfico de presentación en PowerPoint.

