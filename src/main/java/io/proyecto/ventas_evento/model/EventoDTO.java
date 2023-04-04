package io.proyecto.ventas_evento.model;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EventoDTO {

    private Long id;

    @Size(max = 255)
    private String nombre;

    @Size(max = 255)
    private String descripcion;

    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFin;

    @Size(max = 255)
    private String mediaUrl;

    private Long idTipoEvento;

    private Long idCategoria;

    private Long idUbicacion;

    private Long idUsuario;

}
