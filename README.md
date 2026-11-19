# APE 4 · Fase 2 · Red social con grafo dinámico

Estructura de Datos, 3.er semestre de Software, FISEI-UTA. Docente: Ing. José Rubén Caiza.
Facilitación del Grupo 5, jueves 19/11/2026.

## Integrantes y partes

| Parte | Integrante | Qué hizo |
|---|---|---|
| 0 y 2 | Gamboa Araujo Rommel Fabricio | Repositorio; alta de personas, búsqueda y `mostrarRed` |
| 1 | Espinoza Reyes Kerly Margoth | Modelo (`Persona`, `Vertice`, `Arista`, `TipoRelacion`) y contrato de operaciones |
| 3 | Jijón Viscaino Gabriel Sebastián | Crear y eliminar relaciones (amistad y seguir) |
| 4 | Manobanda Puaquiza Italo Esteban | Baja de persona y consulta de vecinos |
| 5 | Tenorio Ronquillo Jonathan Sebastián | Demo (`Main`), menú de consola (`Menu`) y documentación de ejecución |

Cada parte trae sus propias pruebas en `PruebasRedSocial`.

## Qué pide la Fase 2

En la Fase 1 el Grupo 4 modeló la red social: personas como vértices y relaciones como aristas. El lunes 16/11 mostramos la lista de adyacencia con nodos propios. En esta fase juntamos las dos cosas: una red social que crece y se reduce en memoria.

Operaciones de la fase:

- Alta y baja de persona.
- Crear y eliminar relación: amistad, que es recíproca, y seguir, que va en un solo sentido.
- Listar vecinos: amigos, a quién sigue y quién lo sigue.

Restricción: no se usa `HashMap`, `HashSet`, `ArrayList`, `LinkedList` ni ninguna librería de grafos. Todo se arma con nodos propios.

## Cómo ejecutarlo

Se necesita Java 11 o superior. Desde la carpeta del proyecto, en PowerShell o en la terminal de VS Code:

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java src).FullName
java -cp out pruebas.PruebasRedSocial
java -cp out app.Main
java -cp out app.Menu
```

| Programa | Para qué sirve |
|---|---|
| `pruebas.PruebasRedSocial` | Corre todas las pruebas. Debe terminar en `65/65 pruebas OK` |
| `app.Main` | Demo guiada, sin escribir nada |
| `app.Menu` | Menú para probar a mano: registrar, relacionar, eliminar y consultar |

## Cómo está hecho

```
cabeza -> [ana] -> [beto] -> [caro] -> null               lista de personas
            |
            +-> (beto,AMISTAD) -> (caro,SEGUIR) -> null    relaciones de ana
```

Es una multilista: la lista de personas y, colgada de cada una, su lista de relaciones. Aplicada a un grafo, esa estructura es la lista de adyacencia.

- **Amistad (no dirigida):** se guarda como dos aristas `AMISTAD`, una en la lista de cada persona. Cuesta el doble de memoria en aristas, pero así cada persona ve a sus amigos recorriendo solo su lista.
- **Seguir (dirigida):** se guarda como una sola arista `SEGUIR`, en la lista de quien sigue.
- **Grafo mixto:** en la misma red hay aristas no dirigidas y dirigidas sobre las mismas personas.
- **Inserción al inicio:** las personas y las relaciones se insertan al inicio de su lista, igual que en la clase del lunes. Por eso la última persona registrada aparece primero en `mostrarRed`.

## Clases y capas

Las capas van en un solo sentido: `app -> negocio -> modelo -> enums`.

| Paquete | Clase | Qué hace |
|---|---|---|
| `enums` | `TipoRelacion` | Los dos tipos de relación: `AMISTAD` y `SEGUIR` |
| `modelo` | `Persona` | Datos de la persona: usuario (la clave) y nombre |
| `modelo` | `Vertice` | Nodo de la lista de personas: la persona, su primera arista y la siguiente persona |
| `modelo` | `Arista` | Nodo de la lista de relaciones: persona destino, tipo y siguiente arista |
| `negocio` | `RedSocial` | Todas las operaciones de la red |
| `app` | `Main`, `Menu` | Demo guiada y menú de consola |
| `pruebas` | `PruebasRedSocial` | Pruebas sin JUnit |

```
src/
  enums/    TipoRelacion.java
  modelo/   Persona.java, Vertice.java, Arista.java
  negocio/  RedSocial.java
  app/      Main.java, Menu.java
  pruebas/  PruebasRedSocial.java
docs/
  Contrato_operaciones.md           que recibe, devuelve y rechaza cada operacion
```

## Reglas de validación

- El usuario es la clave. Se guarda sin espacios alrededor y en minúsculas, así que `Ana` y `ana` son la misma persona.
- El usuario no puede tener espacios ni comas.
- No se registra una persona repetida.
- Nadie puede ser amigo de sí mismo ni seguirse a sí mismo (sin lazos).
- No se repite una relación del mismo tipo entre las mismas dos personas.
- Dos personas sí pueden ser amigas y además seguirse, porque son relaciones de tipo distinto.

Cuando una operación no se puede hacer, devuelve `false`, o `null` o `-1` si era una consulta sobre una persona que no existe. No lanza excepciones.

## Operaciones y costo

| Operación | Costo | Por qué |
|---|---|---|
| `registrarPersona` | O(V) | busca que no exista antes de insertar |
| `eliminarPersona` | O(V + A) | revisa la lista de todos para borrar las relaciones que llegan a la persona |
| `agregarAmistad` | O(V + grado(a)) | busca a las dos personas, revisa que no sean amigos e inserta dos aristas al inicio |
| `eliminarAmistad` | O(V + grado(a) + grado(b)) | quita la arista en la lista de cada uno |
| `seguir` / `dejarDeSeguir` / `sigueA` / `sonAmigos` | O(V + grado(a)) | una sola lista |
| `listarAmigos` / `listarSeguidos` / `contarAmigos` | O(V + grado) | busca a la persona y recorre solo su lista |
| `listarSeguidores` | O(V + A) | la arista "beto sigue a ana" está en la lista de beto, así que hay que revisar a todos |
| `mostrarRed` / `totalAmistades` / `totalSeguimientos` | O(V + A) | recorren todo |
| `totalPersonas` | O(V) | cuenta la lista de personas |

V es el número de personas, A el número de aristas guardadas (cada amistad son dos) y grado(x) el tamaño de la lista de x.

El detalle de cada operación (qué recibe, qué devuelve y qué rechaza) está en `docs/Contrato_operaciones.md`.

## La demo (`app.Main`)

Repite la mini demo del Grupo 4 con ana, beto, caro y dani en lugar de A, B, C y D:

1. Alta de las cuatro personas. Registrar a ana otra vez da `false`.
2. Amistades A-B, A-C, B-C y C-D. El lazo y la amistad con alguien que no existe dan `false`.
3. Vecinos y grados: 2, 2, 3 y 1, igual que en el grafo de la Fase 1.
4. Beto y dani siguen a ana. Ana no sigue a dani, porque seguir va en un solo sentido.
5. Se quita la amistad caro - dani y se verifica que desapareció de los dos lados.
6. Baja de ana, que tenía amigos y seguidores. Nadie queda apuntando a ella.

Parte de la salida:

```
ana (Ana) | amigos: beto, caro | sigue a: -
beto (Alberto) | amigos: ana, caro | sigue a: -
caro (Carolina) | amigos: ana, beto, dani | sigue a: -
dani (Daniela) | amigos: caro | sigue a: -

amigos(caro) = ana, beto, dani | grado = 3
seguidores(ana) = beto, dani
caro -> dani: false | dani -> caro: false
```

## El menú (`app.Menu`)

| Opción | Acción |
|---|---|
| 1 | Registrar persona |
| 2 | Eliminar persona (con todas sus relaciones) |
| 3 | Crear amistad |
| 4 | Eliminar amistad |
| 5 | Seguir |
| 6 | Dejar de seguir |
| 7 | Listar vecinos: amigos, a quién sigue y quién lo sigue |
| 8 | Mostrar toda la red con sus totales |
| 0 | Salir |

## Pruebas

`PruebasRedSocial` tiene 65 pruebas, una sección por parte:

| Sección | Pruebas | Qué cubre |
|---|---|---|
| Altas | 20 | red vacía, alta, duplicados (también con mayúsculas o espacios), usuario con espacios o comas, nombre vacío, inserción al inicio |
| Relaciones | 21 | amistad en los dos sentidos, seguir dirigido, lazo, persona inexistente, relación repetida, quitar de los dos lados |
| Bajas y vecinos | 24 | grados 2, 2, 3, 1, amigos, seguidos y seguidores, baja de una persona con amigos y seguidores, baja de la primera, la última y de todas |

Cada prueba imprime `OK` o `FALLA`, y al final el total.
