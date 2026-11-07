# Registro de uso de inteligencia artificial

## Etapa 1 (Augusto M. Cuneo)

Herramienta: ChatGPT (OpenAI). Finalidad: revision y corroboracion del codigo, consulta de criterios de calidad y diseño grafico de la presentacion.

## Etapa 2 (Camila Rissi)

Herramienta: Claude (Anthropic), modelo Claude Sonnet 5.5, en una sesion de Claude Code.

Finalidad: aplicar los refactorings sobre el repositorio, ejecutar las pruebas, redactar la documentacion tecnica de la etapa, el checklist de code review y los borradores de las secciones 6 a 10 del informe y de las diapositivas 9 a 14.

Grado de intervencion: la IA escribio el codigo refactorizado y los commits (cada commit lleva el trailer `Co-Authored-By` con la herramienta). La integrante dirigio el trabajo, tiene que revisar y comprender todo el resultado y es quien lo defiende en el video. La revision de codigo la propuso la IA; el dictamen final lo confirma la integrante (ver `docs/Checklist_CodeReview.md`).

Prompts utilizados (textuales):

1. Mensaje inicial con la consigna, el reparto de tareas y el material del compañero: "Con mi comppañero nos repartimos las actividades te paso lo q le toco a el y lo q me toco a mi [...] Lo que te quedaría hacer es aplicar los refactorings al código (incluido Introduce Parameter Object), volver a ejecutar las pruebas, completar el checklist de revisión que está en docs y subir tus cambios a GitHub con los tags que faltan. Después completás tu parte del Word, las diapositivas y grabás tu segmento del video. [...] yo seria el integrante 2". Se adjuntaron la consigna (PDF) y un ZIP con el informe parcial y la presentacion.
2. "podrias revisar ahora el tpi que me paso mi comañero ya estoy conectada y hacer lo q le seria dado al integrante 2 q seria yo". Se adjunto una captura del repositorio.

Verificaciones hechas sobre lo que produjo la IA: ejecucion de las pruebas tras cada commit, comparacion de caja negra contra `estado-inicial` y prueba de que esa comparacion detecta cambios (ver `evidencias/`).
