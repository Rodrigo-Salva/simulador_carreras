package com.usuarios_tokens.repository;


import com.usuarios_tokens.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    Optional<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE u.nombreUsuario = :usernameOrEmail OR u.email = :usernameOrEmail")
    Optional<Usuario> findByNombreUsuarioOrEmail(@Param("usernameOrEmail") String usernameOrEmail);

    boolean existsByNombreUsuario(String nombreUsuario);

    boolean existsByEmail(String email);

    List<Usuario> findByTipoUsuario(Usuario.TipoUsuario tipoUsuario);

    List<Usuario> findByEstadoCuenta(Usuario.EstadoCuenta estadoCuenta);

    List<Usuario> findByEmailVerificadoFalse();

    List<Usuario> findByFechaCreacionAfter(LocalDateTime fecha);

    long countByTipoUsuario(Usuario.TipoUsuario tipoUsuario);

    @Query("SELECT u FROM Usuario u WHERE " +
            "LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) OR " +
            "LOWER(u.apellido) LIKE LOWER(CONCAT('%', :apellido, '%'))")
    List<Usuario> findByNombreOrApellidoContainingIgnoreCase(
            @Param("nombre") String nombre,
            @Param("apellido") String apellido
    );

    @Query("SELECT u FROM Usuario u WHERE u.estadoCuenta = 'ACTIVO' AND u.emailVerificado = true")
    List<Usuario> findActiveVerifiedUsers();
}
