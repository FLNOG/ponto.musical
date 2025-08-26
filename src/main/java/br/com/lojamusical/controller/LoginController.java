package br.com.lojamusical.controller;

import br.com.lojamusical.model.Usuario;
import br.com.lojamusical.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String exibirLogin(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Usuario usuario, Model model) {
        try {
            if (usuario.getEmail() == null || usuario.getEmail().isBlank() ||
                    usuario.getSenha() == null || usuario.getSenha().isBlank()) {
                model.addAttribute("erro", "Campo em branco");
                return "login";
            }

            Usuario u = usuarioService.buscarPorEmail(usuario.getEmail());
            if (u == null) {
                model.addAttribute("erro", "Usuário não cadastrado");
                return "login";
            }

            if (!u.getSenha().equals(usuarioService.codifica(usuario.getSenha()))) {
                model.addAttribute("erro", "Email ou senha incorretos");
                return "login";
            }

            if (Usuario.Perfil.CLIENTE.equals(u.getPerfil())) {
                return "index";
            } else {
                return "/admin/admin";
            }

        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/cadastro")
    public String exibirCadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrarCliente(@ModelAttribute Usuario usuario, Model model) {
        try {
            if (!usuario.getSenha().equals(usuario.getConfirmarSenha())) {
                model.addAttribute("erro", "Senhas não coincidem");
                return "cadastro";
            }

            usuario.setPerfil(Usuario.Perfil.CLIENTE);
            usuario.setAtivo(true);

            usuarioService.cadastro(usuario);

            model.addAttribute("sucesso", "Usuário cadastrado com sucesso");
            model.addAttribute("usuario", new Usuario());
            return "index";

        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());
            return "cadastro";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}