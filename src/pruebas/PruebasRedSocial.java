package pruebas;

import negocio.RedSocial;

/**
 * Pruebas sin JUnit: cada caso imprime OK o FALLA y al final el total.
 * Ejecutar: java -cp out pruebas.PruebasRedSocial
 */
public class PruebasRedSocial {

    private static int ok = 0;
    private static int total = 0;

    private static void verificar(String nombre, boolean condicion) {
        total++;
        if (condicion) {
            ok++;
            System.out.println("OK    " + nombre);
        } else {
            System.out.println("FALLA " + nombre);
        }
    }

    // Las cuatro personas de la demo del Grupo 4 (sin relaciones).
    // Se registran al reves porque se inserta al inicio: la lista queda ana, beto, caro, dani.
    private static RedSocial redEjemplo() {
        RedSocial r = new RedSocial();
        r.registrarPersona("dani", "Daniela");
        r.registrarPersona("caro", "Carolina");
        r.registrarPersona("beto", "Alberto");
        r.registrarPersona("ana", "Ana");
        return r;
    }

    public static void main(String[] args) {
        probarAltas();
        probarRelaciones();
        probarBajasYVecinos();

        System.out.println();
        System.out.println(ok + "/" + total + " pruebas OK");
        if (ok != total) {
            System.exit(1);
        }
    }

    // ===================== ALTAS =====================

    private static void probarAltas() {
        RedSocial vacia = new RedSocial();
        verificar("red vacia: estaVacia", vacia.estaVacia());
        verificar("red vacia: 0 personas", vacia.totalPersonas() == 0);
        verificar("red vacia: mostrarRed", vacia.mostrarRed().equals("(red vacia)"));
        verificar("red vacia: nombreDe inexistente es null", vacia.nombreDe("ana") == null);

        RedSocial r = new RedSocial();
        verificar("registrar ana", r.registrarPersona("ana", "Ana Torres"));
        verificar("duplicado rechazado", !r.registrarPersona("ana", "Otra Ana"));
        verificar("duplicado con mayusculas rechazado", !r.registrarPersona("ANA", "Otra Ana"));
        verificar("duplicado con espacios rechazado", !r.registrarPersona("  ana ", "Otra Ana"));
        verificar("usuario null rechazado", !r.registrarPersona(null, "X"));
        verificar("usuario vacio rechazado", !r.registrarPersona("   ", "X"));
        verificar("usuario con espacio interno rechazado", !r.registrarPersona("ana torres", "X"));
        verificar("usuario con coma rechazado", !r.registrarPersona("ana,beto", "X"));
        verificar("nombre vacio rechazado", !r.registrarPersona("beto", " "));
        verificar("nombre null rechazado", !r.registrarPersona("beto", null));
        verificar("solo quedo 1 persona", r.totalPersonas() == 1);
        verificar("existePersona sin importar mayusculas", r.existePersona("Ana"));
        verificar("el duplicado no cambio el nombre", "Ana Torres".equals(r.nombreDe("ana")));
        verificar("mostrarRed de una persona", r.mostrarRed().equals("ana (Ana Torres) | amigos: - | sigue a: -"));

        r.registrarPersona("Beto", "Alberto");
        verificar("usuario se guarda en minusculas", r.mostrarRed().startsWith("beto (Alberto)"));
        verificar("insercion al inicio: beto primero", r.mostrarRed().split("\\R")[0].startsWith("beto"));
    }

    // ===================== RELACIONES =====================

    private static void probarRelaciones() {
        RedSocial r = redEjemplo();
        verificar("amistad ana - beto", r.agregarAmistad("ana", "beto"));
        verificar("amistad en los dos sentidos", r.sonAmigos("ana", "beto") && r.sonAmigos("beto", "ana"));
        verificar("amistad repetida rechazada", !r.agregarAmistad("beto", "ana"));
        verificar("lazo rechazado", !r.agregarAmistad("ana", "ana"));
        verificar("amistad con inexistente rechazada", !r.agregarAmistad("ana", "zoe"));
        verificar("amistad desde inexistente rechazada", !r.agregarAmistad("zoe", "ana"));
        verificar("amistad con null rechazada", !r.agregarAmistad(null, "ana"));

        verificar("ana sigue a caro", r.seguir("ana", "caro"));
        verificar("seguir es dirigido", r.sigueA("ana", "caro") && !r.sigueA("caro", "ana"));
        verificar("seguir repetido rechazado", !r.seguir("ana", "caro"));
        verificar("seguirse a si mismo rechazado", !r.seguir("ana", "ana"));
        verificar("seguir a inexistente rechazado", !r.seguir("ana", "zoe"));
        verificar("amigo y seguidor a la vez", r.seguir("ana", "beto") && r.sonAmigos("ana", "beto"));

        verificar("mostrarRed linea de ana",
                r.mostrarRed().contains("ana (Ana) | amigos: beto | sigue a: beto, caro"));

        verificar("eliminar amistad ana - beto", r.eliminarAmistad("beto", "ana"));
        verificar("se quito de los dos lados", !r.sonAmigos("ana", "beto") && !r.sonAmigos("beto", "ana"));
        verificar("quitar amistad no toca el seguir", r.sigueA("ana", "beto"));
        verificar("eliminar amistad inexistente", !r.eliminarAmistad("ana", "beto"));
        verificar("eliminar amistad con inexistente", !r.eliminarAmistad("ana", "zoe"));
        verificar("dejar de seguir", r.dejarDeSeguir("ana", "caro") && !r.sigueA("ana", "caro"));
        verificar("dejar de seguir otra vez", !r.dejarDeSeguir("ana", "caro"));
    }

    // ===================== BAJAS Y VECINOS =====================

    private static void probarBajasYVecinos() {
        RedSocial r = redEjemplo();
        r.agregarAmistad("caro", "dani");
        r.agregarAmistad("beto", "caro");
        r.agregarAmistad("ana", "caro");
        r.agregarAmistad("ana", "beto");
        verificar("amigos(ana) = beto, caro", "beto, caro".equals(r.listarAmigos("ana")));
        verificar("amigos(caro) = ana, beto, dani", "ana, beto, dani".equals(r.listarAmigos("caro")));
        verificar("grados 2, 2, 3, 1", r.contarAmigos("ana") == 2 && r.contarAmigos("beto") == 2
                && r.contarAmigos("caro") == 3 && r.contarAmigos("dani") == 1);
        verificar("4 amistades", r.totalAmistades() == 4);
        verificar("amigos de inexistente = null", r.listarAmigos("zoe") == null);
        verificar("contarAmigos de inexistente = -1", r.contarAmigos("zoe") == -1);

        r.seguir("dani", "ana");
        r.seguir("beto", "ana");
        r.seguir("ana", "dani");
        verificar("seguidos(dani) = ana", "ana".equals(r.listarSeguidos("dani")));
        verificar("seguidores(ana) = beto, dani", "beto, dani".equals(r.listarSeguidores("ana")));
        verificar("seguidores sin nadie = vacio", "".equals(r.listarSeguidores("caro")));
        verificar("seguidores de inexistente = null", r.listarSeguidores("zoe") == null);
        verificar("3 seguimientos", r.totalSeguimientos() == 3);

        verificar("quitar caro - dani", r.eliminarAmistad("caro", "dani"));
        verificar("dani sin amigos", "".equals(r.listarAmigos("dani")) && r.contarAmigos("dani") == 0);

        verificar("baja de ana (tiene amigos y seguidores)", r.eliminarPersona("ana"));
        verificar("ana ya no existe", !r.existePersona("ana"));
        verificar("beto ya no tiene a ana", "caro".equals(r.listarAmigos("beto")));
        verificar("dani ya no sigue a ana", "".equals(r.listarSeguidos("dani")));
        verificar("quedan 3 personas", r.totalPersonas() == 3);
        verificar("queda 1 amistad y 0 seguimientos", r.totalAmistades() == 1 && r.totalSeguimientos() == 0);
        verificar("baja de inexistente", !r.eliminarPersona("ana"));
        verificar("ana se puede registrar de nuevo limpia",
                r.registrarPersona("ana", "Ana") && "".equals(r.listarAmigos("ana")));

        verificar("baja de la ultima de la lista (dani)", r.eliminarPersona("dani"));
        verificar("baja de la cabeza (ana)", r.eliminarPersona("ana"));
        verificar("baja de todos deja la red vacia",
                r.eliminarPersona("beto") && r.eliminarPersona("caro") && r.estaVacia());
    }
}
