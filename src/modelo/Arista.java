package modelo;

import enums.TipoRelacion;

/** Nodo de la lista de relaciones de un vertice: hacia quien va, de que tipo y la siguiente. */
public class Arista {

    private final Vertice destino;
    private final TipoRelacion tipo;
    private Arista sig;   // null = es la ultima relacion de la lista

    public Arista(Vertice destino, TipoRelacion tipo) {
        this.destino = destino;
        this.tipo = tipo;
    }

    public Vertice getDestino() {
        return destino;
    }

    public TipoRelacion getTipo() {
        return tipo;
    }

    public Arista getSig() {
        return sig;
    }

    public void setSig(Arista sig) {
        this.sig = sig;
    }
}
