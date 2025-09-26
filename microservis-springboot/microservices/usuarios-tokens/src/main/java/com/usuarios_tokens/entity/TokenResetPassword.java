package com.usuarios_tokens.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens_reset_password")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TokenResetPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    @EqualsAndHashCode.Include
    private Long tokenId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "token", nullable = false, unique = true, length = 255)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_token")
    private TipoToken tipoToken;

    @Column(name = "fecha_creacion")
    @Builder.Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_expiracion")
    private LocalDateTime fechaExpiracion;

    @Column(name = "usado")
    @Builder.Default
    private Boolean usado = false;

    public enum TipoToken {
        EMAIL_VERIFICATION,
        RECUPERACION, PASSWORD_RESET
    }

    public boolean isExpired() {
        return fechaExpiracion.isBefore(LocalDateTime.now());
    }

    public boolean isValid() {
        return !usado && !isExpired();
    }
}
