package br.com.lojamusical.service;

import br.com.lojamusical.model.Usuario;
import br.com.lojamusical.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean login(Usuario usuario) throws Exception {
        Usuario usuarioBanco = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioBanco == null) {
            throw new RuntimeException("Usuário não cadastrado no sistema");
        }

        String senhaCodificada = codifica(usuario.getSenha());
        if (!usuarioBanco.getSenha().equals(senhaCodificada)) {
            throw new RuntimeException("Senha incorreta");
        }

        return true;
    }

    public Usuario cadastro(Usuario usuario) throws Exception {
        if(usuarioRepository.findByEmail(usuario.getEmail())!=null){
            throw new RuntimeException("Email já cadastrado!");
        }

        usuario.setSenha(codifica(usuario.getSenha()));
        usuario.setConfirmarSenha(codifica(usuario.getConfirmarSenha()));
        System.out.println("Senha: " + usuario.getSenha());
        System.out.println("ConfirmarSenha: " + usuario.getConfirmarSenha());;
        return usuarioRepository.save(usuario);
    }

    private String codifica(String senha) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(senha.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}