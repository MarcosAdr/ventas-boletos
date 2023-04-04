package io.proyecto.ventas_evento.controller;

import io.proyecto.ventas_evento.domain.Evento;
import io.proyecto.ventas_evento.domain.Usuario;
import io.proyecto.ventas_evento.model.ValoracionDTO;
import io.proyecto.ventas_evento.repos.EventoRepository;
import io.proyecto.ventas_evento.repos.UsuarioRepository;
import io.proyecto.ventas_evento.service.ValoracionService;
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
@RequestMapping("/valoracions")
public class ValoracionController {

    private final ValoracionService valoracionService;
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;

    public ValoracionController(final ValoracionService valoracionService,
            final EventoRepository eventoRepository, final UsuarioRepository usuarioRepository) {
        this.valoracionService = valoracionService;
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
        model.addAttribute("valoracions", valoracionService.findAll());
        return "valoracion/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("valoracion") final ValoracionDTO valoracionDTO) {
        return "valoracion/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("valoracion") @Valid final ValoracionDTO valoracionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "valoracion/add";
        }
        valoracionService.create(valoracionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("valoracion.create.success"));
        return "redirect:/valoracions";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("valoracion", valoracionService.get(id));
        return "valoracion/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("valoracion") @Valid final ValoracionDTO valoracionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "valoracion/edit";
        }
        valoracionService.update(id, valoracionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("valoracion.update.success"));
        return "redirect:/valoracions";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        valoracionService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("valoracion.delete.success"));
        return "redirect:/valoracions";
    }

}
