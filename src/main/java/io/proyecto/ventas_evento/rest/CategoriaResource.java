package io.proyecto.ventas_evento.rest;

import io.proyecto.ventas_evento.model.CategoriaDTO;
import io.proyecto.ventas_evento.service.CategoriaService;
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
@RequestMapping(value = "/api/categorias", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoriaResource {

    private final CategoriaService categoriaService;

    public CategoriaResource(final CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> getAllCategorias() {
        return ResponseEntity.ok(categoriaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> getCategoria(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(categoriaService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCategoria(
            @RequestBody @Valid final CategoriaDTO categoriaDTO) {
        return new ResponseEntity<>(categoriaService.create(categoriaDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategoria(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CategoriaDTO categoriaDTO) {
        categoriaService.update(id, categoriaDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCategoria(@PathVariable(name = "id") final Long id) {
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
