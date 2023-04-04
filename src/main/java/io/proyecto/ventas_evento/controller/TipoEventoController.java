package io.proyecto.ventas_evento.controller;

import io.proyecto.ventas_evento.model.TipoEventoDTO;
import io.proyecto.ventas_evento.service.TipoEventoService;
import io.proyecto.ventas_evento.util.WebUtils;
import jakarta.validation.Valid;
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
@RequestMapping("/tipoEventos")
public class TipoEventoController {

    private final TipoEventoService tipoEventoService;

    public TipoEventoController(final TipoEventoService tipoEventoService) {
        this.tipoEventoService = tipoEventoService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("tipoEventos", tipoEventoService.findAll());
        return "tipoEvento/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("tipoEvento") final TipoEventoDTO tipoEventoDTO) {
        return "tipoEvento/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("tipoEvento") @Valid final TipoEventoDTO tipoEventoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "tipoEvento/add";
        }
        tipoEventoService.create(tipoEventoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("tipoEvento.create.success"));
        return "redirect:/tipoEventos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("tipoEvento", tipoEventoService.get(id));
        return "tipoEvento/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("tipoEvento") @Valid final TipoEventoDTO tipoEventoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "tipoEvento/edit";
        }
        tipoEventoService.update(id, tipoEventoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("tipoEvento.update.success"));
        return "redirect:/tipoEventos";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = tipoEventoService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            tipoEventoService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("tipoEvento.delete.success"));
        }
        return "redirect:/tipoEventos";
    }

}
