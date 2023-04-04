package io.proyecto.ventas_evento.rest;

import io.proyecto.ventas_evento.model.UbicacionDTO;
import io.proyecto.ventas_evento.service.UbicacionService;
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
@RequestMapping(value = "/api/ubicacions", produces = MediaType.APPLICATION_JSON_VALUE)
public class UbicacionResource {

    private final UbicacionService ubicacionService;

    public UbicacionResource(final UbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
    }

    @GetMapping
    public ResponseEntity<List<UbicacionDTO>> getAllUbicacions() {
        return ResponseEntity.ok(ubicacionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UbicacionDTO> getUbicacion(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(ubicacionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createUbicacion(
            @RequestBody @Valid final UbicacionDTO ubicacionDTO) {
        return new ResponseEntity<>(ubicacionService.create(ubicacionDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUbicacion(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final UbicacionDTO ubicacionDTO) {
        ubicacionService.update(id, ubicacionDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUbicacion(@PathVariable(name = "id") final Long id) {
        ubicacionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
