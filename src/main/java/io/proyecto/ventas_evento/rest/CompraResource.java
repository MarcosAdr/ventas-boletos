package io.proyecto.ventas_evento.rest;

import io.proyecto.ventas_evento.model.CompraDTO;
import io.proyecto.ventas_evento.service.CompraService;
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
@RequestMapping(value = "/api/compras", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompraResource {

    private final CompraService compraService;

    public CompraResource(final CompraService compraService) {
        this.compraService = compraService;
    }

    @GetMapping
    public ResponseEntity<List<CompraDTO>> getAllCompras() {
        return ResponseEntity.ok(compraService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraDTO> getCompra(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(compraService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCompra(@RequestBody @Valid final CompraDTO compraDTO) {
        return new ResponseEntity<>(compraService.create(compraDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCompra(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CompraDTO compraDTO) {
        compraService.update(id, compraDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCompra(@PathVariable(name = "id") final Long id) {
        compraService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
