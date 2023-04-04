package io.proyecto.ventas_evento.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UsuarioDTO {

    private Long id;

    @Size(max = 100)
    private String nombre;

    @Size(max = 100)
    private String apellido;

    @Size(max = 50)
    private String telefono;

    @NotNull
    @Size(max = 255)
    private String correo;

    @NotNull
    @Size(max = 255)
    private String contrasena;

    private Long idPlan;

    private List<Long> idUsuario;

}
