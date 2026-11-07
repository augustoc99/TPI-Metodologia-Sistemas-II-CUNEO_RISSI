import glob
import re
import subprocess


def medir_metodos(texto):
    resultado = []
    patron = re.compile(
        r'^\s+(?:public|private|protected)?\s*(?:static\s+)?(?:final\s+)?[\w<>\[\], ?]+\s+(\w+)\(([^)]*)\)\s*(?:throws [\w, ]+)?\{',
        re.M)
    for m in patron.finditer(texto):
        nombre = m.group(1)
        if nombre in ("if", "for", "while", "switch", "catch"):
            continue
        i = m.end()
        profundidad = 1
        while profundidad and i < len(texto):
            if texto[i] == "{":
                profundidad += 1
            elif texto[i] == "}":
                profundidad -= 1
            i += 1
        lineas = texto[m.start():i].count("\n") + 1
        params = m.group(2)
        cantidad = 0 if not params.strip() else len(re.split(r',(?![^<]*>)', params))
        resultado.append((nombre, lineas, cantidad))
    return resultado


def analizar(archivos):
    total = 0
    metodos = []
    for nombre, texto in archivos:
        total += texto.count("\n")
        for n, l, p in medir_metodos(texto):
            metodos.append((nombre, n, l, p))
    return total, metodos


def leer_estado_inicial():
    archivos = []
    listado = subprocess.check_output(["git", "ls-tree", "--name-only", "estado-inicial", "src/"], text=True).split()
    for ruta in listado:
        texto = subprocess.check_output(["git", "show", "estado-inicial:" + ruta], text=True)
        archivos.append((ruta.split("/")[-1], texto))
    return archivos


def leer_actual():
    return [(ruta.split("/")[-1], open(ruta, encoding="utf-8").read()) for ruta in sorted(glob.glob("src/*.java"))]


def main():
    for etiqueta, archivos in (("estado-inicial", leer_estado_inicial()), ("version actual", leer_actual())):
        total, metodos = analizar(archivos)
        mayor = max(metodos, key=lambda x: x[2])
        registrar = [x for x in metodos if x[0] == "GestorSolicitudes.java" and x[1] == "registrarSolicitud"][0]
        print("Version:", etiqueta)
        print("  Archivos fuente en src:", len(archivos))
        print("  Lineas totales en src:", total)
        print("  Lineas de GestorSolicitudes:", dict(archivos)["GestorSolicitudes.java"].count("\n"))
        print("  Metodo mas largo: %s.%s con %d lineas" % (mayor[0][:-5], mayor[1], mayor[2]))
        print("  GestorSolicitudes.registrarSolicitud: %d lineas, %d parametros" % (registrar[2], registrar[3]))
        print()
    print("Medicion simple por conteo de lineas y llaves sobre src/*.java; no es una herramienta de analisis estatico.")


main()
