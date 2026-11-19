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

Cada parte trae sus pruebas en `PruebasRedSocial`.

## Qué pide la Fase 2

En la Fase 1 el Grupo 4 modeló la red social: personas como vértices y relaciones como aristas. El lunes 16/11 mostramos la lista de adyacencia con nodos propios. Ahora juntamos las dos cosas: una red social que crece y se reduce en memoria, sin `HashMap`, `HashSet`, `ArrayList` ni `LinkedList`.

Operaciones de la fase:

- Alta y baja de persona.
- Crear y eliminar relación (amistad, que es recíproca, y seguir, que va en un solo sentido).
- Listar vecinos: amigos, a quién sigue y quién lo sigue.

## Cómo está hecho

```
cabeza -> [ana] -> [beto] -> [caro] -> null               lista de personas
            |
            +-> (beto,AMISTAD) -> (caro,SEGUIR) -> null    relaciones de ana
```

Es una multilista: la lista de personas y, colgada de cada una, su lista de relaciones. Aplicada a un grafo, esa estructura es la lista de adyacencia. No es la multilista de adyacencia de nodo compartido (donde cada amistad se guarda una sola vez en un nodo que está en las dos listas): aquí cada amistad se guarda dos veces, una en la lista de cada persona. Cuesta el doble de memoria en aristas, pero insertar y recorrer es más simple.

Es un grafo mixto: la amistad es no dirigida y seguir es dirigida, sobre las mismas personas.

- `Vertice` guarda una `Persona`, el puntero a su primera `Arista` y el puntero a la siguiente persona.
- `Arista` guarda la persona destino, el tipo de relación y la siguiente arista.
- Una amistad se guarda como dos aristas `AMISTAD`, una en cada sentido. Seguir se guarda como una sola arista `SEGUIR`.
- Las personas y las relaciones se insertan al inicio de su lista, igual que en la clase del lunes. Por eso la última persona registrada aparece primero en `mostrarRed`.

```
src/
  enums/    TipoRelacion.java       AMISTAD (no dirigida) y SEGUIR (dirigida)
  modelo/   Persona.java, Vertice.java, Arista.java
  negocio/  RedSocial.java          todas las operaciones
  app/      Main.java               demo guiada
            Menu.java               menu de consola para probar a mano
  pruebas/  PruebasRedSocial.java   65 pruebas sin JUnit
docs/
  Contrato_operaciones.md           que recibe, devuelve y rechaza cada operacion
```

Las capas van en un solo sentido: `app -> negocio -> modelo -> enums`.

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

El detalle de cada operación está en `docs/Contrato_operaciones.md`.

## Ejecución (PowerShell, desde la carpeta del proyecto)

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java src).FullName
java -cp out pruebas.PruebasRedSocial
java -cp out app.Main
java -cp out app.Menu
```

- `PruebasRedSocial` debe terminar en `65/65 pruebas OK`.
- `Main` repite la mini demo del Grupo 4: alta de ana, beto, caro y dani (A, B, C, D), amistades A-B, A-C, B-C y C-D, vecinos y grados (2, 2, 3, 1), quitar C-D y verificar los dos lados. Luego agrega seguidores y da de baja a ana.
- `Menu` sirve para probar a mano durante el pair programming.

## Casos límite cubiertos

Red vacía, persona duplicada (también con mayúsculas o espacios), usuario con espacios, relación con una persona que no existe, lazo, relación repetida, quitar una amistad de los dos lados, eliminar a una persona con amigos y seguidores, y eliminar la primera, la última y a todas.

## Siguiente fase

En la Fase 3 (26/11) la misma red se representa con una matriz de adyacencia para comparar con esta versión dinámica. Aquí la memoria es O(V + A); la matriz ocupa O(V²) aunque la red tenga pocas relaciones, pero responde "¿son amigos?" en O(1).
