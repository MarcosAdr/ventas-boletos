package io.proyecto.ventas_evento.controller;

import io.proyecto.ventas_evento.model.UbicacionDTO;
import io.proyecto.ventas_evento.service.UbicacionService;
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
@RequestMapping("/ubicacions")
public class UbicacionController {

    private final UbicacionService ubicacionService;

    public UbicacionController(final UbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("ubicacions", ubicacionService.findAll());
        return "ubicacion/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("ubicacion") final UbicacionDTO ubicacionDTO) {
        return "ubicacion/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("ubicacion") @Valid final UbicacionDTO ubicacionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "ubicacion/add";
        }
        ubicacionService.create(ubicacionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("ubicacion.create.success"));
        return "redirect:/ubicacions";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("ubicacion", ubicacionService.get(id));
        return "ubicacion/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("ubicacion") @Valid final UbicacionDTO ubicacionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "ubicacion/edit";
        }
        ubicacionService.update(id, ubicacionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("ubicacion.update.success"));
        return "redirect:/ubicacions";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = ubicacionService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            ubicacionService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("ubicacion.delete.success"));
        }
        return "redirect:/ubicacions";
    }

}
