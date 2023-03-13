package com.pods.fclabs.repositories;

import com.pods.fclabs.models.Endereco;
import com.pods.fclabs.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EnderecoRepository extends JpaRepository<Endereco, UUID> {
    List<Endereco> findByUsuario(Usuario usuario);
}
