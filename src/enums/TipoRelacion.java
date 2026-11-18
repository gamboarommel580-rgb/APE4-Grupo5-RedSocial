package enums;

/**
 * Tipos de relacion de la red social.
 * AMISTAD es reciproca (no dirigida): se guarda como dos aristas, una en cada sentido.
 * SEGUIR va en un solo sentido (dirigida): se guarda como una sola arista.
 */
public enum TipoRelacion {
    AMISTAD,
    SEGUIR
}
