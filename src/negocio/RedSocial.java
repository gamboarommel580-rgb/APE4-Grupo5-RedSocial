package negocio;

import enums.TipoRelacion;
import modelo.Arista;
import modelo.Persona;
import modelo.Vertice;

/**
 * Red social como grafo dinamico. Es una multilista: la lista de personas y, colgada de
 * cada una, su lista de relaciones (en un grafo eso se llama lista de adyacencia):
 *
 *   cabeza -> [ana] -> [beto] -> [caro] -> null              lista de personas
 *               |
 *               +-> (beto,AMISTAD) -> (caro,SEGUIR) -> null   relaciones de ana
 *
 * Es un grafo mixto: la amistad es no dirigida y seguir es dirigida, sobre las mismas personas.
 * Una amistad es reciproca y se guarda como dos aristas (ana -> beto y beto -> ana).
 * Seguir va en un solo sentido y se guarda como una arista.
 * Sin HashMap, HashSet, ArrayList ni LinkedList: todo son nodos enlazados.
 * Las personas y las relaciones se insertan AL INICIO de su lista.
 */
public class RedSocial {

    private Vertice cabeza;

    // ===================== BUSQUEDA Y ALTAS =====================

    // El usuario se compara sin espacios alrededor y en minusculas: "Ana" y "ana" son el mismo.
    private String normalizar(String usuario) {
        return usuario == null ? null : usuario.trim().toLowerCase();
    }

    private Vertice buscar(String usuario) {
        String clave = normalizar(usuario);
        if (clave == null) {
            return null;
        }
        for (Vertice v = cabeza; v != null; v = v.getSig()) {
            if (v.getPersona().getUsuario().equals(clave)) {
                return v;
            }
        }
        return null;
    }

    private boolean usuarioValido(String usuario) {
        if (usuario == null || usuario.isBlank()) {
            return false;
        }
        String u = usuario.trim();
        for (int i = 0; i < u.length(); i++) {
            char c = u.charAt(i);
            if (Character.isWhitespace(c) || Character.isSpaceChar(c) || c == ',') {
                return false;   // "ana torres" o "ana,beto" no: los listados separan con ", "
            }
        }
        return true;
    }

    /** true si la persona existe. O(V) */
    public boolean existePersona(String usuario) {
        return buscar(usuario) != null;
    }

    /**
     * Alta de persona. O(V) por la busqueda de duplicados.
     * false si el usuario es null, vacio, tiene espacios, ya existe, o si el nombre esta vacio.
     */
    public boolean registrarPersona(String usuario, String nombre) {
        if (!usuarioValido(usuario) || nombre == null || nombre.isBlank() || buscar(usuario) != null) {
            return false;
        }
        Vertice v = new Vertice(new Persona(normalizar(usuario), nombre.trim()));
        v.setSig(cabeza);   //PASO 1: el nuevo apunta a la cabeza actual
        cabeza = v;         //PASO 2: el nuevo pasa a ser la cabeza
        return true;
    }

    /** Nombre de la persona, o null si no existe. */
    public String nombreDe(String usuario) {
        Vertice v = buscar(usuario);
        return v == null ? null : v.getPersona().getNombre();
    }

    public int totalPersonas() {
        int n = 0;
        for (Vertice v = cabeza; v != null; v = v.getSig()) {
            n++;
        }
        return n;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    /** Usuarios de la lista de v con ese tipo de relacion, separados por coma: "beto, caro". */
    private String listar(Vertice v, TipoRelacion tipo) {
        StringBuilder sb = new StringBuilder();
        for (Arista a = v.getAristas(); a != null; a = a.getSig()) {
            if (a.getTipo() == tipo) {
                sb.append(sb.length() == 0 ? "" : ", ").append(a.getDestino().getPersona().getUsuario());
            }
        }
        return sb.toString();
    }

    /** Toda la red, una linea por persona: "ana (Ana Torres) | amigos: beto | sigue a: -". */
    public String mostrarRed() {
        if (cabeza == null) {
            return "(red vacia)";
        }
        StringBuilder sb = new StringBuilder();
        for (Vertice v = cabeza; v != null; v = v.getSig()) {
            String amigos = listar(v, TipoRelacion.AMISTAD);
            String sigue = listar(v, TipoRelacion.SEGUIR);
            sb.append(v.getPersona())
              .append(" | amigos: ").append(amigos.isEmpty() ? "-" : amigos)
              .append(" | sigue a: ").append(sigue.isEmpty() ? "-" : sigue);
            if (v.getSig() != null) {
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }
}
