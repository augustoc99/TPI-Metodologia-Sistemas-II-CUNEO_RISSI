#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
OPCIONES="-Dstdout.encoding=UTF-8 -Dfile.encoding=UTF-8"
TMP="$(mktemp -d)"
trap 'rm -rf "$TMP"' EXIT
mkdir -p "$TMP/inicial" build
git archive estado-inicial src | tar -x -C "$TMP/inicial"
mkdir -p "$TMP/inicial/build"
javac --release 17 -encoding UTF-8 -d "$TMP/inicial/build" "$TMP"/inicial/src/*.java
javac --release 17 -encoding UTF-8 -d build src/*.java tests/*.java
java $OPCIONES -cp "$TMP/inicial/build" Main < tests/entrada_comparacion.txt > "$TMP/salida_inicial.txt"
java $OPCIONES -cp build Main < tests/entrada_comparacion.txt > "$TMP/salida_actual.txt"
if [ -n "${1:-}" ]; then
  mkdir -p "$1"
  cp "$TMP/salida_inicial.txt" "$1/salida_estado_inicial.txt"
  cp "$TMP/salida_actual.txt" "$1/salida_version_actual.txt"
fi
LINEAS="$(wc -l < "$TMP/salida_inicial.txt")"
diff -u "$TMP/salida_inicial.txt" "$TMP/salida_actual.txt"
echo "SALIDA IDENTICA A ESTADO-INICIAL ($LINEAS lineas comparadas)"
