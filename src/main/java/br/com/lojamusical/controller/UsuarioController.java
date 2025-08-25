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
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("login")
    public String exibirLogin(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    @PostMapping("login")
    public String login(@ModelAttribute Usuario usuario, Model model) {
        try {
            if (usuario.getEmail() == null || usuario.getSenha() == null) {
                model.addAttribute("erro", "Campo em branco");
                return "login";
            }

            boolean autenticado = usuarioService.login(usuario);
            if (!autenticado) {
                model.addAttribute("erro", "Email ou senha incorretos");
                return "login";
            }

            return "index";

        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            return "login";

        }
    }

    @GetMapping("cadastro")
    public String exibirCadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cadastro";
    }

    @PostMapping("cadastro")
    public String cadastrarUsuario(@ModelAttribute Usuario usuario, Model model) {
        try {
            if (!usuario.getSenha().equals(usuario.getConfirmarSenha())) {
                System.out.println(usuario.getSenha());
                System.out.println(usuario.getConfirmarSenha());

                model.addAttribute("erro", "Senhas não coincidem");
                return "cadastro";
            }

            Usuario teste = usuarioService.cadastro(usuario);
            System.out.println("Usuario salvo: " + teste);

            model.addAttribute("sucesso", "Usuario cadastrado com sucesso" );
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