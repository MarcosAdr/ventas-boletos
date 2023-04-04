package io.proyecto.ventas_evento.controller;

import io.proyecto.ventas_evento.model.RolDTO;
import io.proyecto.ventas_evento.service.RolService;
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
@RequestMapping("/rols")
public class RolController {

    private final RolService rolService;

    public RolController(final RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("rols", rolService.findAll());
        return "rol/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("rol") final RolDTO rolDTO) {
        return "rol/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("rol") @Valid final RolDTO rolDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "rol/add";
        }
        rolService.create(rolDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("rol.create.success"));
        return "redirect:/rols";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("rol", rolService.get(id));
        return "rol/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("rol") @Valid final RolDTO rolDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "rol/edit";
        }
        rolService.update(id, rolDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("rol.update.success"));
        return "redirect:/rols";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = rolService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            rolService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("rol.delete.success"));
        }
        return "redirect:/rols";
    }

}
