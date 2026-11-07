$ErrorActionPreference = "Stop"
Set-Location (Join-Path $PSScriptRoot "..")
New-Item -ItemType Directory -Force build | Out-Null
$fuentes = (Get-ChildItem src,tests -Filter *.java).FullName
javac --release 17 -encoding UTF-8 -d build $fuentes
if ($LASTEXITCODE -ne 0) { throw "Compilacion fallida" }
Write-Host "=== PRUEBAS ==="
java -cp build PruebasBase
if ($LASTEXITCODE -ne 0) { throw "Pruebas fallidas" }
Write-Host "=== DEMOSTRACION ==="
java -cp build Main demo
if ($LASTEXITCODE -ne 0) { throw "Demostracion fallida" }
