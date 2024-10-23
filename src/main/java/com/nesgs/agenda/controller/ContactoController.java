package com.nesgs.agenda.controller;

import com.nesgs.agenda.entity.Contacto;
import com.nesgs.agenda.repository.ContactoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ContactoController {

    @Autowired
    private ContactoRepository contactoRepository;

    @GetMapping({"/",""})
    public String verPaginaDeInicio(Model modelo) {
        List<Contacto> contactos = contactoRepository.findAll();
        modelo.addAttribute("contactos", contactos);
        return "index";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeRegistrarContacto(Model modelo) {
        modelo.addAttribute("contacto", new Contacto());
        return "nuevo";
    }

    @PostMapping("/nuevo")
    public String guardarContacto(@Validated Contacto contacto, BindingResult bindingResult, RedirectAttributes redirect, Model modelo) {
        // @Validated indica que se va a validar el objeto Contacto
        // @BindingResult nos da todos los errores al validar el objeto, para mostrarlos en el FrontEnd
        // IMPORTANTE: El @BindingResult debe estar a continuación del objeto, si no no funcionará correctamente.
        if(bindingResult.hasErrors()) {
            modelo.addAttribute("contacto", contacto); // Si hay un error, nos entrega un nuevo formulario
            return "nuevo";
        }

        contactoRepository.save(contacto);
        redirect.addFlashAttribute("msgExito","El contacto ha sido agregado con éxito");
        return "redirect:/";

    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioDeEditarContacto(@PathVariable Integer id, Model modelo) {
        Contacto contacto = contactoRepository.getById(id);
        modelo.addAttribute("contacto", contacto);
        return "nuevo";
    }

    @PostMapping("/{id}/editar")
    public String actualizarContacto(@PathVariable Integer id, @Validated Contacto contacto, BindingResult bindingResult, RedirectAttributes redirect, Model modelo) {
        Contacto contactoDB = contactoRepository.getById(id);
        if(bindingResult.hasErrors()) {
            modelo.addAttribute("contacto", contacto); // Si hay un error, nos entrega un nuevo formulario
            return "nuevo";
        }

        contactoDB.setNombre(contacto.getNombre());
        contactoDB.setTelefono(contacto.getTelefono());
        contactoDB.setEmail(contacto.getEmail());
        contactoDB.setFechaNacimiento(contacto.getFechaNacimiento());

        contactoRepository.save(contactoDB);
        redirect.addFlashAttribute("msgExito","El contacto ha sido actualizado correctamente");
        return "redirect:/";

    }
}
