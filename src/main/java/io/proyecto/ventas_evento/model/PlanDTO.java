package io.proyecto.ventas_evento.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PlanDTO {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String nombre;

    @NotNull
    private Double precio;

    @Size(max = 255)
    private String descripcion;

}
