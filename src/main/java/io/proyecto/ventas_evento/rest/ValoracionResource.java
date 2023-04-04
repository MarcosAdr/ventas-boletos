package io.proyecto.ventas_evento.rest;

import io.proyecto.ventas_evento.model.ValoracionDTO;
import io.proyecto.ventas_evento.service.ValoracionService;
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
@RequestMapping(value = "/api/valoracions", produces = MediaType.APPLICATION_JSON_VALUE)
public class ValoracionResource {

    private final ValoracionService valoracionService;

    public ValoracionResource(final ValoracionService valoracionService) {
        this.valoracionService = valoracionService;
    }

    @GetMapping
    public ResponseEntity<List<ValoracionDTO>> getAllValoracions() {
        return ResponseEntity.ok(valoracionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ValoracionDTO> getValoracion(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(valoracionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createValoracion(
            @RequestBody @Valid final ValoracionDTO valoracionDTO) {
        return new ResponseEntity<>(valoracionService.create(valoracionDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateValoracion(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ValoracionDTO valoracionDTO) {
        valoracionService.update(id, valoracionDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteValoracion(@PathVariable(name = "id") final Long id) {
        valoracionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
