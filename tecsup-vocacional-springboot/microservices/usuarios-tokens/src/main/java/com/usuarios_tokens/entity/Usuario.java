package com.usuarios_tokens.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios",
        indexes = {
                @Index(name = "idx_usuarios_email", columnList = "email"),
                @Index(name = "idx_usuarios_nombre_usuario", columnList = "nombre_usuario"),
                @Index(name = "idx_usuarios_tipo", columnList = "tipo_usuario"),
                @Index(name = "idx_usuarios_estado", columnList = "estado_cuenta")
        })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    @EqualsAndHashCode.Include
    private Long usuarioId;

    @Column(name = "nombre_usuario", unique = true, nullable = false, length = 50)
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9_]+$")
    private String nombreUsuario;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    @Email
    @Pattern(regexp = ".*@tecsup\\.edu\\.pe$")
    private String email;

    @Column(name = "password_encriptado", nullable = false)
    @Size(max = 255)
    private String passwordEncriptado;

    @Column(name = "nombre", nullable = false, length = 50)
    @Size(min = 2, max = 50)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 50)
    @Size(min = 2, max = 50)
    private String apellido;

    @Column(name = "telefono", length = 20)
    @Pattern(regexp = "^[0-9]{9}$")
    private String telefono;

    @Column(name = "fecha_nacimiento")
    @Past
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario")
    @Builder.Default
    private TipoUsuario tipoUsuario = TipoUsuario.ESTUDIANTE;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cuenta")
    @Builder.Default
    private EstadoCuenta estadoCuenta = EstadoCuenta.ACTIVO;

    @Column(name = "email_verificado")
    @Builder.Default
    private Boolean emailVerificado = false;

    @Column(name = "fecha_creacion")
    @Builder.Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_actualizacion")
    @Builder.Default
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    public void setPassword(String newPassword) {
    }

    public enum TipoUsuario {
        ESTUDIANTE("Estudiante"),
        ADMIN("Administrador"),
        INSTRUCTOR("Instructor");

        private final String descripcion;

        TipoUsuario(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    public enum EstadoCuenta {
        ACTIVO("Cuenta activa"),
        INACTIVO("Cuenta inactiva"),
        SUSPENDIDO("Cuenta suspendida");

        private final String descripcion;

        EstadoCuenta(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + tipoUsuario.name()));
    }

    @Override
    public String getPassword() {
        return passwordEncriptado;
    }

    @Override
    public String getUsername() {
        return nombreUsuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return estadoCuenta != EstadoCuenta.SUSPENDIDO;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return estadoCuenta == EstadoCuenta.ACTIVO;
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public boolean isAdmin() {
        return TipoUsuario.ADMIN.equals(this.tipoUsuario);
    }

    public boolean isEstudiante() {
        return TipoUsuario.ESTUDIANTE.equals(this.tipoUsuario);
    }
}
