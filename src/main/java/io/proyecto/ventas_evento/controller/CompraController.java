package io.proyecto.ventas_evento.controller;

import io.proyecto.ventas_evento.domain.Evento;
import io.proyecto.ventas_evento.domain.Usuario;
import io.proyecto.ventas_evento.model.CompraDTO;
import io.proyecto.ventas_evento.repos.EventoRepository;
import io.proyecto.ventas_evento.repos.UsuarioRepository;
import io.proyecto.ventas_evento.service.CompraService;
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
@RequestMapping("/compras")
public class CompraController {

    private final CompraService compraService;
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;

    public CompraController(final CompraService compraService,
            final EventoRepository eventoRepository, final UsuarioRepository usuarioRepository) {
        this.compraService = compraService;
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("idEventoValues", eventoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Evento::getId, Evento::getNombre)));
        model.addAttribute("idUsuarioValues", usuarioRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Usuario::getId, Usuario::getNombre)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("compras", compraService.findAll());
        return "compra/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("compra") final CompraDTO compraDTO) {
        return "compra/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("compra") @Valid final CompraDTO compraDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "compra/add";
        }
        compraService.create(compraDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("compra.create.success"));
        return "redirect:/compras";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("compra", compraService.get(id));
        return "compra/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("compra") @Valid final CompraDTO compraDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "compra/edit";
        }
        compraService.update(id, compraDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("compra.update.success"));
        return "redirect:/compras";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = compraService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            compraService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("compra.delete.success"));
        }
        return "redirect:/compras";
    }

}
