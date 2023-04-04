package io.proyecto.ventas_evento.controller;

import io.proyecto.ventas_evento.domain.Plan;
import io.proyecto.ventas_evento.domain.Rol;
import io.proyecto.ventas_evento.model.UsuarioDTO;
import io.proyecto.ventas_evento.repos.PlanRepository;
import io.proyecto.ventas_evento.repos.RolRepository;
import io.proyecto.ventas_evento.service.UsuarioService;
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
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PlanRepository planRepository;
    private final RolRepository rolRepository;

    public UsuarioController(final UsuarioService usuarioService,
            final PlanRepository planRepository, final RolRepository rolRepository) {
        this.usuarioService = usuarioService;
        this.planRepository = planRepository;
        this.rolRepository = rolRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("idPlanValues", planRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Plan::getId, Plan::getNombre)));
        model.addAttribute("idUsuarioValues", rolRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Rol::getId, Rol::getNombre)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());
        return "usuario/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("usuario") final UsuarioDTO usuarioDTO) {
        return "usuario/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("usuario") @Valid final UsuarioDTO usuarioDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("correo") && usuarioService.correoExists(usuarioDTO.getCorreo())) {
            bindingResult.rejectValue("correo", "Exists.usuario.correo");
        }
        if (bindingResult.hasErrors()) {
            return "usuario/add";
        }
        usuarioService.create(usuarioDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("usuario.create.success"));
        return "redirect:/usuarios";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("usuario", usuarioService.get(id));
        return "usuario/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("usuario") @Valid final UsuarioDTO usuarioDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final UsuarioDTO currentUsuarioDTO = usuarioService.get(id);
        if (!bindingResult.hasFieldErrors("correo") &&
                !usuarioDTO.getCorreo().equalsIgnoreCase(currentUsuarioDTO.getCorreo()) &&
                usuarioService.correoExists(usuarioDTO.getCorreo())) {
            bindingResult.rejectValue("correo", "Exists.usuario.correo");
        }
        if (bindingResult.hasErrors()) {
            return "usuario/edit";
        }
        usuarioService.update(id, usuarioDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("usuario.update.success"));
        return "redirect:/usuarios";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = usuarioService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            usuarioService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("usuario.delete.success"));
        }
        return "redirect:/usuarios";
    }

}
