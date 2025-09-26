package com.usuarios_tokens.repository;

import com.usuarios_tokens.entity.TokenResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends JpaRepository<TokenResetPassword, Long> {

    Optional<TokenResetPassword> findByToken(String token);

    List<TokenResetPassword> findByUsuario_UsuarioId(Long usuarioId);

    List<TokenResetPassword> findByUsuario_UsuarioIdAndTipoToken(Long usuarioId, TokenResetPassword.TipoToken tipoToken);

    @Modifying
    @Query("UPDATE TokenResetPassword t SET t.usado = true WHERE t.usuario.usuarioId = :usuarioId AND t.tipoToken = :tipoToken AND t.usado = false")
    void invalidateTokensByUserAndType(@Param("usuarioId") Long usuarioId, @Param("tipoToken") TokenResetPassword.TipoToken tipoToken);

    @Modifying
    @Query("DELETE FROM TokenResetPassword t WHERE t.fechaExpiracion < :now OR t.usado = true")
    void deleteExpiredAndUsedTokens(@Param("now") LocalDateTime now);

    boolean existsByTokenAndUsadoFalse(String token);
}
