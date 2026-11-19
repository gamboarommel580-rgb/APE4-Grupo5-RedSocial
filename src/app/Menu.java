package app;

import java.util.Scanner;
import negocio.RedSocial;

/**
 * Menu de consola para probar la red a mano (sirve para el pair programming).
 * Scanner solo lee el teclado; no es una coleccion.
 * Ejecutar: java -cp out app.Menu
 */
public class Menu {

    private static final Scanner TECLADO = new Scanner(System.in);

    public static void main(String[] args) {
        RedSocial red = new RedSocial();
        boolean seguirEnMenu = true;
        while (seguirEnMenu) {
            System.out.println();
            System.out.println("===== RED SOCIAL - Grupo 5 - APE 4 Fase 2 =====");
            System.out.println("1. Registrar persona");
            System.out.println("2. Eliminar persona");
            System.out.println("3. Crear amistad");
            System.out.println("4. Eliminar amistad");
            System.out.println("5. Seguir");
            System.out.println("6. Dejar de seguir");
            System.out.println("7. Listar vecinos de una persona");
            System.out.println("8. Mostrar toda la red");
            System.out.println("0. Salir");
            String opcion = leer("Opcion: ");
            if (opcion == null) {
                break;   // se acabo la entrada
            }
            switch (opcion) {
                case "1":
                    informar(red.registrarPersona(leer("Usuario (sin espacios): "), leer("Nombre: ")),
                            "Persona registrada", "No se registro: usuario vacio, con espacios o repetido, o nombre vacio");
                    break;
                case "2":
                    informar(red.eliminarPersona(leer("Usuario: ")),
                            "Persona eliminada con todas sus relaciones", "Ese usuario no existe");
                    break;
                case "3":
                    informar(red.agregarAmistad(leer("Usuario 1: "), leer("Usuario 2: ")),
                            "Amistad creada", "No se creo: usuario inexistente, la misma persona o ya son amigos");
                    break;
                case "4":
                    informar(red.eliminarAmistad(leer("Usuario 1: "), leer("Usuario 2: ")),
                            "Amistad eliminada de los dos lados", "No eran amigos o algun usuario no existe");
                    break;
                case "5":
                    informar(red.seguir(leer("Quien sigue: "), leer("A quien sigue: ")),
                            "Ahora lo sigue", "No se pudo: usuario inexistente, la misma persona o ya lo seguia");
                    break;
                case "6":
                    informar(red.dejarDeSeguir(leer("Quien sigue: "), leer("A quien deja de seguir: ")),
                            "Lo dejo de seguir", "No lo seguia o algun usuario no existe");
                    break;
                case "7":
                    listarVecinos(red, leer("Usuario: "));
                    break;
                case "8":
                    System.out.println(red.mostrarRed());
                    System.out.println("Personas: " + red.totalPersonas() + " | amistades: "
                            + red.totalAmistades() + " | seguimientos: " + red.totalSeguimientos());
                    break;
                case "0":
                    seguirEnMenu = false;
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        }
        System.out.println("Fin.");
    }

    private static void listarVecinos(RedSocial red, String usuario) {
        if (!red.existePersona(usuario)) {
            System.out.println("Ese usuario no existe");
            return;
        }
        System.out.println("Amigos    : " + vacioComoGuion(red.listarAmigos(usuario))
                + " (" + red.contarAmigos(usuario) + ")");
        System.out.println("Sigue a   : " + vacioComoGuion(red.listarSeguidos(usuario)));
        System.out.println("Seguidores: " + vacioComoGuion(red.listarSeguidores(usuario)));
    }

    private static String vacioComoGuion(String texto) {
        return texto == null || texto.isEmpty() ? "-" : texto;
    }

    private static void informar(boolean exito, String siOk, String siFalla) {
        System.out.println(exito ? siOk : siFalla);
    }

    // Devuelve null si ya no hay mas entrada (por ejemplo, al terminar un archivo redirigido).
    private static String leer(String mensaje) {
        System.out.print(mensaje);
        return TECLADO.hasNextLine() ? TECLADO.nextLine().trim() : null;
    }
}
