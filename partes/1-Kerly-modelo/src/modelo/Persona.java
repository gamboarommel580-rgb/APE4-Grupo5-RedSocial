package modelo;

/** Datos de una persona de la red. El usuario es la clave y no cambia. */
public class Persona {

    private final String usuario;
    private final String nombre;

    public Persona(String usuario, String nombre) {
        this.usuario = usuario;
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return usuario + " (" + nombre + ")";
    }
}
