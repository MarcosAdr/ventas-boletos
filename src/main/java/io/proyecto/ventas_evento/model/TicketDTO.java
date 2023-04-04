package io.proyecto.ventas_evento.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TicketDTO {

    private Long id;

    @Size(max = 255)
    private String codigoqr;

    @NotNull
    private LocalDateTime fechaTicket;

    private Double precioTotal;

    private Long idCompra;

    private Long eventoId;

}
