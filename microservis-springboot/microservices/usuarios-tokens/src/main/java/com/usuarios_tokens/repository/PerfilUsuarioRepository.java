package com.usuarios_tokens.repository;


import com.usuarios_tokens.entity.PerfilUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilUsuarioRepository extends JpaRepository<PerfilUsuario, Long> {

    Optional<PerfilUsuario> findByUsuario_UsuarioId(Long usuarioId);

    boolean existsByUsuario_UsuarioId(Long usuarioId);
}
