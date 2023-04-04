package io.proyecto.ventas_evento.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UbicacionDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String direccion;

    @Size(max = 50)
    private String provincia;

    @Size(max = 50)
    private String ciudad;

    private Double latitud;

    private Double longitud;

}
