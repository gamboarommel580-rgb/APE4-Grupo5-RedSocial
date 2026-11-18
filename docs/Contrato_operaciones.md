# Contrato de operaciones · Red social dinámica

Antes de programar acordamos qué recibe cada operación, qué devuelve y qué casos tiene que rechazar. Las pruebas de `PruebasRedSocial` siguen este contrato.

## Modelo

| Elemento del grafo | En la red social | Clase |
|---|---|---|
| Vértice | Persona (usuario y nombre) | `Vertice` guarda una `Persona` |
| Arista | Relación entre dos personas | `Arista` guarda el destino y el `TipoRelacion` |
| Arista no dirigida | Amistad (es recíproca) | dos aristas `AMISTAD`, una en cada sentido |
| Arista dirigida | Seguir (un solo sentido) | una arista `SEGUIR` |

Como hay aristas no dirigidas y dirigidas sobre las mismas personas, es un grafo mixto. También se puede ver como dos grafos (el de amistades y el de seguidores) que comparten los vértices.

Reglas del modelo:

- El usuario es la clave. Se guarda sin espacios alrededor y en minúsculas, así que `Ana` y `ana` son la misma persona.
- El usuario no puede tener espacios ni comas (`ana torres` y `ana,beto` no valen), porque los listados separan con `, `.
- Nadie puede ser amigo de sí mismo ni seguirse a sí mismo (sin lazos).
- No hay relaciones repetidas del mismo tipo entre las mismas dos personas.
- Dos personas pueden ser amigas y además seguirse: son relaciones de tipo distinto.

## Operaciones

| # | Operación | Recibe | Devuelve | Rechaza (devuelve false, null o -1) | Costo |
|---|---|---|---|---|---|
| 1 | `registrarPersona` | usuario, nombre | `true` si la registró | usuario null, vacío, con espacios o comas, o repetido; nombre null o vacío | O(V) |
| 2 | `eliminarPersona` | usuario | `true` si la eliminó | usuario inexistente | O(V + A) |
| 3 | `agregarAmistad` | usuario a, usuario b | `true` si la creó en los dos sentidos | alguno inexistente, a = b, ya eran amigos | O(V + grado(a)) |
| 4 | `eliminarAmistad` | usuario a, usuario b | `true` si la quitó de los dos lados | no eran amigos o alguno no existe | O(V + grado(a) + grado(b)) |
| 5 | `seguir` | quien sigue, a quien | `true` si la creó | alguno inexistente, a = b, ya lo seguía | O(V + grado(a)) |
| 6 | `dejarDeSeguir` | quien sigue, a quien | `true` si la quitó | no lo seguía o alguno no existe | O(V + grado(a)) |
| 7 | `listarAmigos` | usuario | `"beto, caro"`, o `""` si no tiene | usuario inexistente → `null` | O(V + grado) |
| 8 | `listarSeguidos` | usuario | a quiénes sigue | usuario inexistente → `null` | O(V + grado) |
| 9 | `listarSeguidores` | usuario | quiénes lo siguen | usuario inexistente → `null` | O(V + A) |
| 10 | `contarAmigos` | usuario | grado de amistad | usuario inexistente → `-1` | O(V + grado) |
| 11 | `mostrarRed` | nada | una línea por persona con sus amigos y a quién sigue | red vacía → `"(red vacia)"` | O(V + A) |

V es el número de personas y A el número de aristas guardadas; cada amistad cuenta como dos aristas. grado(x) es el tamaño de la lista de x: sus amistades más las personas que sigue.

## Por qué cuestan eso

- Casi todo empieza con buscar a la persona en la lista de vértices, que es O(V).
- `eliminarPersona` tiene que revisar las listas de todas las personas para borrar las relaciones que llegan a la eliminada. Por eso es O(V + A).
- `listarSeguidores` también recorre todo. La arista "beto sigue a ana" está guardada en la lista de beto, no en la de ana.

## Casos límite que se prueban

1. Red vacía: mostrar, buscar, eliminar.
2. Persona duplicada, también con mayúsculas o espacios.
3. Relación con una persona que no existe.
4. Lazo: amistad o seguir consigo mismo.
5. Relación repetida.
6. Quitar una amistad y verificar que desapareció de los dos lados.
7. Eliminar una persona que tiene amigos y seguidores, y verificar que nadie la sigue apuntando.
8. Eliminar la primera persona de la lista, la última y luego a todas.
