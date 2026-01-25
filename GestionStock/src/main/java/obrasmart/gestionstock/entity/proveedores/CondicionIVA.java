package obrasmart.gestionstock.entity.proveedores;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CondicionIVA {
    @JsonProperty("Responsable Inscripto")
    RESPONSABLE_INSCRIPTO,

    @JsonProperty("Monotributista")
    MONOTRIBUTISTA,

    @JsonProperty("Exento")
    EXENTO,

    @JsonProperty("Consumidor Final")
    CONSUMIDOR_FINAL
}
