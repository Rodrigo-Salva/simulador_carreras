package com.usuarios_tokens.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "perfiles_usuario")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PerfilUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perfil_id")
    @EqualsAndHashCode.Include
    private Long perfilId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "biografia", length = 500)
    @Size(max = 500)
    private String biografia;

    @Column(name = "carrera_interes", length = 100)
    private String carreraInteres;

    @Column(name = "nivel_educativo", length = 50)
    private String nivelEducativo;

    @Column(name = "ciudad", length = 100)
    private String ciudad;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "linkedin_url", length = 255)
    private String linkedinUrl;

    @Column(name = "github_url", length = 255)
    private String githubUrl;

    @Column(name = "portafolio_url", length = 255)
    private String portafolioUrl;

    @Column(name = "preferencias_notificacion")
    @Builder.Default
    private Boolean preferenciaNotificacion = true;

    @Column(name = "tema_interfaz")
    @Builder.Default
    private String temaInterfaz = "CLARO";

    @Column(name = "idioma_preferido")
    @Builder.Default
    private String idiomaPreferido = "ES";

    @Column(name = "fecha_actualizacion")
    @Builder.Default
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}
