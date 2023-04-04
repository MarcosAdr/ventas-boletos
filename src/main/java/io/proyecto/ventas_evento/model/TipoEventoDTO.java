package io.proyecto.ventas_evento.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TipoEventoDTO {

    private Long id;

    @Size(max = 50)
    private String nombre;

    @Size(max = 255)
    private String descripcion;

}
