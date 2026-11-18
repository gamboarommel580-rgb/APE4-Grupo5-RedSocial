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
}
