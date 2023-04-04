package io.proyecto.ventas_evento.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ValoracionDTO {

    private Long id;

    @Size(max = 255)
    private String comentario;

    private Integer calificacion;

    private Long idEvento;

    private Long idUsuario;

}
