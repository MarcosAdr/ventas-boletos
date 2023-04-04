package io.proyecto.ventas_evento.model;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CompraDTO {

    private Long id;

    @NotNull
    private Integer cantidadEntradas;

    @NotNull
    private LocalDateTime fechaCompra;

    private Long idEvento;

    private Long idUsuario;

}
