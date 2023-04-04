package io.proyecto.ventas_evento.controller;

import io.proyecto.ventas_evento.domain.Categoria;
import io.proyecto.ventas_evento.domain.TipoEvento;
import io.proyecto.ventas_evento.domain.Ubicacion;
import io.proyecto.ventas_evento.domain.Usuario;
import io.proyecto.ventas_evento.model.EventoDTO;
import io.proyecto.ventas_evento.repos.CategoriaRepository;
import io.proyecto.ventas_evento.repos.TipoEventoRepository;
import io.proyecto.ventas_evento.repos.UbicacionRepository;
import io.proyecto.ventas_evento.repos.UsuarioRepository;
import io.proyecto.ventas_evento.service.EventoService;
import io.proyecto.ventas_evento.util.CustomCollectors;
import io.proyecto.ventas_evento.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/eventos")
public class EventoController {

    private final EventoService eventoService;
    private final TipoEventoRepository tipoEventoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UbicacionRepository ubicacionRepository;
    private final UsuarioRepository usuarioRepository;

    public EventoController(final EventoService eventoService,
            final TipoEventoRepository tipoEventoRepository,
            final CategoriaRepository categoriaRepository,
            final UbicacionRepository ubicacionRepository,
            final UsuarioRepository usuarioRepository) {
        this.eventoService = eventoService;
        this.tipoEventoRepository = tipoEventoRepository;
        this.categoriaRepository = categoriaRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("idTipoEventoValues", tipoEventoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(TipoEvento::getId, TipoEvento::getNombre)));
        model.addAttribute("idCategoriaValues", categoriaRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Categoria::getId, Categoria::getNombre)));
        model.addAttribute("idUbicacionValues", ubicacionRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Ubicacion::getId, Ubicacion::getDireccion)));
        model.addAttribute("idUsuarioValues", usuarioRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Usuario::getId, Usuario::getNombre)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("eventos", eventoService.findAll());
        return "evento/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("evento") final EventoDTO eventoDTO) {
        return "evento/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("evento") @Valid final EventoDTO eventoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "evento/add";
        }
        eventoService.create(eventoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("evento.create.success"));
        return "redirect:/eventos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("evento", eventoService.get(id));
        return "evento/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("evento") @Valid final EventoDTO eventoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "evento/edit";
        }
        eventoService.update(id, eventoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("evento.update.success"));
        return "redirect:/eventos";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = eventoService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            eventoService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("evento.delete.success"));
        }
        return "redirect:/eventos";
    }

}
