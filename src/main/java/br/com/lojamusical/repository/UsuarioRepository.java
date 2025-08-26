package br.com.lojamusical.repository;

import br.com.lojamusical.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {
    Usuario findByEmail(String email);
    Usuario findByCpf(String cpf);
}