package io.proyecto.ventas_evento.rest;

import io.proyecto.ventas_evento.model.TipoEventoDTO;
import io.proyecto.ventas_evento.service.TipoEventoService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/tipoEventos", produces = MediaType.APPLICATION_JSON_VALUE)
public class TipoEventoResource {

    private final TipoEventoService tipoEventoService;

    public TipoEventoResource(final TipoEventoService tipoEventoService) {
        this.tipoEventoService = tipoEventoService;
    }

    @GetMapping
    public ResponseEntity<List<TipoEventoDTO>> getAllTipoEventos() {
        return ResponseEntity.ok(tipoEventoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoEventoDTO> getTipoEvento(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(tipoEventoService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createTipoEvento(
            @RequestBody @Valid final TipoEventoDTO tipoEventoDTO) {
        return new ResponseEntity<>(tipoEventoService.create(tipoEventoDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTipoEvento(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final TipoEventoDTO tipoEventoDTO) {
        tipoEventoService.update(id, tipoEventoDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTipoEvento(@PathVariable(name = "id") final Long id) {
        tipoEventoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
