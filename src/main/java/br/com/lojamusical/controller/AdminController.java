package br.com.lojamusical.controller;

import br.com.lojamusical.model.Usuario;
import br.com.lojamusical.service.UsuarioService;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/admin")
    public String adminPage() {
        return "/admin/admin";
    }

    @GetMapping("/usuarios")
    public String listaUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());
        return "/admin/usuarios";
    }

    @GetMapping("/cadastro_adm")
    public String exibirCadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "/admin/cadastro_usuario";
    }

    @PostMapping ("/cadastro_adm")
    public String cadastraUsuario(@ModelAttribute Usuario usuario, Model model) {
        try {
            usuario.setAtivo(true);  // Definir como ativo
            usuarioService.cadastro(usuario);  // A codificação já é feita no service

            model.addAttribute("sucesso", "Usuário cadastrado com sucesso!");
            model.addAttribute("usuario", new Usuario()); // Limpar formulário
            return "/admin/cadastro_usuario";

        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("usuario", usuario); // Manter dados preenchidos
            return "/admin/cadastro_usuario";
        }
    }
}