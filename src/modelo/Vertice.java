package modelo;

/**
 * Nodo de la lista de personas:
 *   la persona, la primera arista de su lista de relaciones y el siguiente vertice.
 */
public class Vertice {

    private final Persona persona;
    private Arista aristas;   // null = no tiene relaciones
    private Vertice sig;      // null = es el ultimo de la lista de personas

    public Vertice(Persona persona) {
        this.persona = persona;
    }

    public Persona getPersona() {
        return persona;
    }

    public Arista getAristas() {
        return aristas;
    }

    public void setAristas(Arista aristas) {
        this.aristas = aristas;
    }

    public Vertice getSig() {
        return sig;
    }

    public void setSig(Vertice sig) {
        this.sig = sig;
    }
}
