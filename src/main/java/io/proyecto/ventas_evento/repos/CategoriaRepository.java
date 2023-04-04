package io.proyecto.ventas_evento.repos;

import io.proyecto.ventas_evento.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
