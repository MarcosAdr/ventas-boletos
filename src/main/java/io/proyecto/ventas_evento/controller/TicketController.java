package io.proyecto.ventas_evento.controller;

import io.proyecto.ventas_evento.domain.Compra;
import io.proyecto.ventas_evento.domain.Evento;
import io.proyecto.ventas_evento.model.TicketDTO;
import io.proyecto.ventas_evento.repos.CompraRepository;
import io.proyecto.ventas_evento.repos.EventoRepository;
import io.proyecto.ventas_evento.service.TicketService;
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
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final CompraRepository compraRepository;
    private final EventoRepository eventoRepository;

    public TicketController(final TicketService ticketService,
            final CompraRepository compraRepository, final EventoRepository eventoRepository) {
        this.ticketService = ticketService;
        this.compraRepository = compraRepository;
        this.eventoRepository = eventoRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("idCompraValues", compraRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Compra::getId, Compra::getId)));
        model.addAttribute("eventoIdValues", eventoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Evento::getId, Evento::getNombre)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("tickets", ticketService.findAll());
        return "ticket/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("ticket") final TicketDTO ticketDTO) {
        return "ticket/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("ticket") @Valid final TicketDTO ticketDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "ticket/add";
        }
        ticketService.create(ticketDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("ticket.create.success"));
        return "redirect:/tickets";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("ticket", ticketService.get(id));
        return "ticket/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("ticket") @Valid final TicketDTO ticketDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "ticket/edit";
        }
        ticketService.update(id, ticketDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("ticket.update.success"));
        return "redirect:/tickets";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        ticketService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("ticket.delete.success"));
        return "redirect:/tickets";
    }

}
