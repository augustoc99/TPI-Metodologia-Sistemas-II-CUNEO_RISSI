#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
mkdir -p build
javac --release 17 -encoding UTF-8 -d build src/*.java tests/*.java
java -cp build PruebasBase
java -cp build Main demo
