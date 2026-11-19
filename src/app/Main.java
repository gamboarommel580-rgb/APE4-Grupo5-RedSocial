package app;

import negocio.RedSocial;

/**
 * Demo de la Fase 2: la misma mini demostracion del Grupo 4 (alta A/B/C/D, AB, AC, BC, CD,
 * vecinos, quitar C-D y verificar los dos lados), ahora con la red dinamica.
 * Ejecutar: java -cp out app.Main
 */
public class Main {

    public static void main(String[] args) {
        RedSocial red = new RedSocial();

        //PASO 1: alta de personas. Se registran al reves (D, C, B, A) porque se inserta
        // al inicio; asi la lista queda ana, beto, caro, dani
        System.out.println("=== 1. Alta de personas ===");
        red.registrarPersona("dani", "Daniela");
        red.registrarPersona("caro", "Carolina");
        red.registrarPersona("beto", "Alberto");
        red.registrarPersona("ana", "Ana");
        System.out.println("Personas: " + red.totalPersonas());
        System.out.println("Registrar ana otra vez: " + red.registrarPersona("ana", "Ana"));

        //PASO 2: crear amistades (cada una queda en los dos sentidos). Tambien van al reves
        // para que los amigos de cada uno salgan en orden alfabetico
        System.out.println();
        System.out.println("=== 2. Crear amistades A-B, A-C, B-C, C-D ===");
        red.agregarAmistad("caro", "dani");
        red.agregarAmistad("beto", "caro");
        red.agregarAmistad("ana", "caro");
        red.agregarAmistad("ana", "beto");
        System.out.println("Lazo ana - ana: " + red.agregarAmistad("ana", "ana"));
        System.out.println("Amistad con usuario inexistente: " + red.agregarAmistad("ana", "zoe"));
        System.out.println(red.mostrarRed());

        //PASO 3: listar vecinos y grados
        System.out.println();
        System.out.println("=== 3. Vecinos y grados ===");
        for (String u : new String[]{"ana", "beto", "caro", "dani"}) {
            System.out.println("amigos(" + u + ") = " + red.listarAmigos(u) + " | grado = " + red.contarAmigos(u));
        }
        System.out.println("Total de amistades: " + red.totalAmistades());

        //PASO 4: seguir (relacion dirigida)
        System.out.println();
        System.out.println("=== 4. Seguir (un solo sentido) ===");
        red.seguir("dani", "ana");
        red.seguir("beto", "ana");
        System.out.println("seguidores(ana) = " + red.listarSeguidores("ana"));
        System.out.println("ana sigue a dani: " + red.sigueA("ana", "dani"));

        //PASO 5: quitar C-D y verificar los dos lados
        System.out.println();
        System.out.println("=== 5. Quitar la amistad caro - dani ===");
        System.out.println("Quitar: " + red.eliminarAmistad("caro", "dani"));
        System.out.println("caro -> dani: " + red.sonAmigos("caro", "dani")
                + " | dani -> caro: " + red.sonAmigos("dani", "caro"));
        System.out.println("amigos(dani) = '" + red.listarAmigos("dani") + "'");

        //PASO 6: baja de persona limpiando sus referencias
        System.out.println();
        System.out.println("=== 6. Baja de ana (tiene amigos y seguidores) ===");
        System.out.println("Eliminar ana: " + red.eliminarPersona("ana"));
        System.out.println(red.mostrarRed());
        System.out.println("Eliminar ana otra vez: " + red.eliminarPersona("ana"));
    }
}
