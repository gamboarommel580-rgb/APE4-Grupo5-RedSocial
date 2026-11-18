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

    // ===================== RELACIONES =====================

    /** Arista o -> d de ese tipo, o null. O(grado de o) */
    private Arista buscarArista(Vertice o, Vertice d, TipoRelacion tipo) {
        for (Arista a = o.getAristas(); a != null; a = a.getSig()) {
            if (a.getDestino() == d && a.getTipo() == tipo) {
                return a;
            }
        }
        return null;
    }

    private void insertar(Vertice o, Vertice d, TipoRelacion tipo) {
        Arista a = new Arista(d, tipo);
        a.setSig(o.getAristas());   //PASO 1: la nueva apunta a la primera relacion de o
        o.setAristas(a);            //PASO 2: la nueva pasa a ser la primera
    }

    /** Quita la arista o -> d de ese tipo usando un puntero previo. */
    private boolean quitar(Vertice o, Vertice d, TipoRelacion tipo) {
        Arista prev = null;
        for (Arista a = o.getAristas(); a != null; prev = a, a = a.getSig()) {
            if (a.getDestino() == d && a.getTipo() == tipo) {
                if (prev == null) {
                    o.setAristas(a.getSig());   //PASO 1: era la primera, la cabeza pasa a la siguiente
                } else {
                    prev.setSig(a.getSig());    //PASO 2: si no, la anterior se la salta
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Crea la amistad a - b en los dos sentidos. O(V + grado)
     * false si falta alguna persona, si es la misma persona (lazo) o si ya son amigos.
     */
    public boolean agregarAmistad(String a, String b) {
        Vertice va = buscar(a);
        Vertice vb = buscar(b);
        if (va == null || vb == null || va == vb || buscarArista(va, vb, TipoRelacion.AMISTAD) != null) {
            return false;
        }
        insertar(va, vb, TipoRelacion.AMISTAD);   //PASO 1: a -> b
        insertar(vb, va, TipoRelacion.AMISTAD);   //PASO 2: b -> a (la amistad es reciproca)
        return true;
    }

    /** Quita la amistad de los dos lados. false si no eran amigos. O(V + grado) */
    public boolean eliminarAmistad(String a, String b) {
        Vertice va = buscar(a);
        Vertice vb = buscar(b);
        if (va == null || vb == null || buscarArista(va, vb, TipoRelacion.AMISTAD) == null) {
            return false;
        }
        quitar(va, vb, TipoRelacion.AMISTAD);   //PASO 1: se quita a -> b
        quitar(vb, va, TipoRelacion.AMISTAD);   //PASO 2: y tambien b -> a
        return true;
    }

    /** a empieza a seguir a b (un solo sentido). Mismas validaciones que la amistad. */
    public boolean seguir(String a, String b) {
        Vertice va = buscar(a);
        Vertice vb = buscar(b);
        if (va == null || vb == null || va == vb || buscarArista(va, vb, TipoRelacion.SEGUIR) != null) {
            return false;
        }
        insertar(va, vb, TipoRelacion.SEGUIR);
        return true;
    }

    public boolean dejarDeSeguir(String a, String b) {
        Vertice va = buscar(a);
        Vertice vb = buscar(b);
        return va != null && vb != null && quitar(va, vb, TipoRelacion.SEGUIR);
    }

    public boolean sonAmigos(String a, String b) {
        Vertice va = buscar(a);
        Vertice vb = buscar(b);
        return va != null && vb != null && buscarArista(va, vb, TipoRelacion.AMISTAD) != null;
    }

    public boolean sigueA(String a, String b) {
        Vertice va = buscar(a);
        Vertice vb = buscar(b);
        return va != null && vb != null && buscarArista(va, vb, TipoRelacion.SEGUIR) != null;
    }

    // ===================== BAJAS Y VECINOS =====================

    /** Quita de la lista de w TODAS las aristas que van hacia v (amistad y seguir). */
    private void quitarHacia(Vertice w, Vertice v) {
        Arista prev = null;
        Arista a = w.getAristas();
        while (a != null) {
            if (a.getDestino() == v) {
                //PASO 1: se desengancha la arista; prev no avanza porque la siguiente
                // tambien puede ir hacia v (amistad y seguir a la vez)
                if (prev == null) {
                    w.setAristas(a.getSig());
                } else {
                    prev.setSig(a.getSig());
                }
            } else {
                prev = a;   // solo avanza prev si no se borro
            }
            a = a.getSig();
        }
    }

    /**
     * Baja de persona: borra a la persona y todas las relaciones que LLEGAN a ella. O(V + A)
     * Las relaciones que SALEN de ella desaparecen junto con su nodo.
     */
    public boolean eliminarPersona(String usuario) {
        Vertice v = buscar(usuario);
        if (v == null) {
            return false;
        }
        //PASO 1: cualquier persona puede tener una relacion hacia v; se limpian todas
        for (Vertice w = cabeza; w != null; w = w.getSig()) {
            quitarHacia(w, v);
        }
        //PASO 2: desenlazar v de la lista de personas con un puntero previo
        Vertice prev = null;
        for (Vertice w = cabeza; w != null; prev = w, w = w.getSig()) {
            if (w == v) {
                if (prev == null) {
                    cabeza = w.getSig();
                } else {
                    prev.setSig(w.getSig());
                }
                break;
            }
        }
        v.setSig(null);
        v.setAristas(null);
        return true;
    }

    /** Amigos de la persona: "beto, caro". "" si no tiene, null si no existe. O(V + grado) */
    public String listarAmigos(String usuario) {
        Vertice v = buscar(usuario);
        return v == null ? null : listar(v, TipoRelacion.AMISTAD);
    }

    /** A quienes sigue la persona. "" si a nadie, null si no existe. O(V + grado) */
    public String listarSeguidos(String usuario) {
        Vertice v = buscar(usuario);
        return v == null ? null : listar(v, TipoRelacion.SEGUIR);
    }

    /**
     * Quienes siguen a la persona. Hay que revisar las listas de todos porque
     * la arista esta guardada en quien sigue, no en quien es seguido. O(V + A)
     */
    public String listarSeguidores(String usuario) {
        Vertice v = buscar(usuario);
        if (v == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        //PASO 1: se revisa la lista de cada persona buscando una arista SEGUIR hacia v
        for (Vertice w = cabeza; w != null; w = w.getSig()) {
            if (buscarArista(w, v, TipoRelacion.SEGUIR) != null) {
                sb.append(sb.length() == 0 ? "" : ", ").append(w.getPersona().getUsuario());
            }
        }
        return sb.toString();
    }

    private int contar(Vertice v, TipoRelacion tipo) {
        int n = 0;
        for (Arista a = v.getAristas(); a != null; a = a.getSig()) {
            if (a.getTipo() == tipo) {
                n++;
            }
        }
        return n;
    }

    /** Grado de amistad: cuantos amigos tiene. -1 si no existe. */
    public int contarAmigos(String usuario) {
        Vertice v = buscar(usuario);
        return v == null ? -1 : contar(v, TipoRelacion.AMISTAD);
    }

    /** Cuantas amistades hay en la red (cada una esta guardada dos veces). */
    public int totalAmistades() {
        int n = 0;
        for (Vertice v = cabeza; v != null; v = v.getSig()) {
            n += contar(v, TipoRelacion.AMISTAD);
        }
        return n / 2;
    }

    public int totalSeguimientos() {
        int n = 0;
        for (Vertice v = cabeza; v != null; v = v.getSig()) {
            n += contar(v, TipoRelacion.SEGUIR);
        }
        return n;
    }
}
