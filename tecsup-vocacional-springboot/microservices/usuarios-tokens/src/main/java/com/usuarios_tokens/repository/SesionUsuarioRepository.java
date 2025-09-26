package com.usuarios_tokens.repository;

import com.usuarios_tokens.entity.SesionUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SesionUsuarioRepository extends JpaRepository<SesionUsuario, Long> {

    Optional<SesionUsuario> findByRefreshToken(String refreshToken);

    List<SesionUsuario> findByUsuario_UsuarioId(Long usuarioId);

    List<SesionUsuario> findByUsuario_UsuarioIdAndActivaTrue(Long usuarioId);

    @Modifying
    @Query("UPDATE SesionUsuario s SET s.activa = false WHERE s.usuario.usuarioId = :usuarioId")
    void deactivateAllSessionsByUserId(@Param("usuarioId") Long usuarioId);

    @Modifying
    @Query("DELETE FROM SesionUsuario s WHERE s.fechaExpiracion < :now OR s.activa = false")
    void deleteExpiredAndInactiveSessions(@Param("now") LocalDateTime now);

    long countByUsuario_UsuarioIdAndActivaTrue(Long usuarioId);
}
