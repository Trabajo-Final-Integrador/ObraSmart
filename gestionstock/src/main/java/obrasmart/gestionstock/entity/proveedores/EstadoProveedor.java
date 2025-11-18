package obrasmart.gestionstock.entity.proveedores;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EstadoProveedor {
    @JsonProperty("Activo")
    ACTIVO,
    @JsonProperty("Inactivo")
    INACTIVO,
    @JsonProperty("Suspendido")
    SUSPENDIDO
}