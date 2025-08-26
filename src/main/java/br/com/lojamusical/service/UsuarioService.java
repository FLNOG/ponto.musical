package br.com.lojamusical.service;

import br.com.lojamusical.model.Usuario;
import br.com.lojamusical.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario cadastro(Usuario usuario) throws Exception {
        if(usuarioRepository.findByEmail(usuario.getEmail())!=null){
            throw new RuntimeException("Email já cadastrado!");
        }

        if (!validarCPF(usuario.getCpf())) {
            throw new RuntimeException("CPF inválido!");
        }

        if (usuarioRepository.findByCpf(usuario.getCpf()) != null) {
            throw new RuntimeException("CPF já cadastrado!");
        }

        usuario.setCpf(usuario.getCpf().replaceAll("[^0-9]", ""));
        usuario.setSenha(codifica(usuario.getSenha()));

        if (usuario.getConfirmarSenha() != null && !usuario.getConfirmarSenha().trim().isEmpty()) {
            usuario.setConfirmarSenha(codifica(usuario.getConfirmarSenha()));
        }

        System.out.println("Senha: " + usuario.getSenha());
        System.out.println("ConfirmarSenha: " + usuario.getConfirmarSenha());

        return usuarioRepository.save(usuario);
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public String codifica(String senha) throws Exception {
        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser nula ou vazia");
        }

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

    public boolean validarCPF(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }

        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito > 9) {
            primeiroDigito = 0;
        }

        if (Character.getNumericValue(cpf.charAt(9)) != primeiroDigito) {
            return false;
        }

        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito > 9) {
            segundoDigito = 0;
        }

        return Character.getNumericValue(cpf.charAt(10)) == segundoDigito;
    }
}