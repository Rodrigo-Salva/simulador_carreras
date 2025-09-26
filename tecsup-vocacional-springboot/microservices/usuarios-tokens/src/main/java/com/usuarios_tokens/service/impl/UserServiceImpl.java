package com.usuarios_tokens.service.impl;

import com.usuarios_tokens.dto.user.UpdateUserDto;
import com.usuarios_tokens.dto.user.UserProfileDto;
import com.usuarios_tokens.entity.PerfilUsuario;
import com.usuarios_tokens.entity.TokenResetPassword;
import com.usuarios_tokens.entity.Usuario;
import com.usuarios_tokens.exception.AuthException;
import com.usuarios_tokens.repository.PerfilUsuarioRepository;
import com.usuarios_tokens.repository.ResetPasswordRepository;
import com.usuarios_tokens.repository.UsuarioRepository;
import com.usuarios_tokens.service.interfaces.IEmailService;
import com.usuarios_tokens.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilUsuarioRepository perfilUsuarioRepository;
    private final ResetPasswordRepository resetPasswordRepository;
    private final IEmailService emailService;

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getUserProfile(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new AuthException("Usuario no encontrado"));

        PerfilUsuario perfil = perfilUsuarioRepository.findByUsuario_UsuarioId(usuarioId)
                .orElse(null);

        return mapToUserProfileDto(usuario, perfil);
    }

    @Override
    @Transactional
    public UserProfileDto updateUserProfile(Long usuarioId, UpdateUserDto updateDto) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new AuthException("Usuario no encontrado"));

        // Actualizar datos del usuario
        if (updateDto.getNombre() != null) {
            usuario.setNombre(updateDto.getNombre());
        }
        if (updateDto.getApellido() != null) {
            usuario.setApellido(updateDto.getApellido());
        }
        if (updateDto.getTelefono() != null) {
            usuario.setTelefono(updateDto.getTelefono());
        }
        if (updateDto.getFechaNacimiento() != null) {
            usuario.setFechaNacimiento(updateDto.getFechaNacimiento());
        }

        usuario = usuarioRepository.save(usuario);

        // Actualizar o crear perfil
        PerfilUsuario perfil = perfilUsuarioRepository.findByUsuario_UsuarioId(usuarioId)
                .orElse(PerfilUsuario.builder().usuario(usuario).build());

        updatePerfilFromDto(perfil, updateDto);
        perfil = perfilUsuarioRepository.save(perfil);

        log.info("Perfil actualizado para usuario: {}", usuario.getNombreUsuario());

        return mapToUserProfileDto(usuario, perfil);
    }

    @Override
    @Transactional
    public void deleteUser(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new AuthException("Usuario no encontrado"));

        usuario.setEstadoCuenta(Usuario.EstadoCuenta.INACTIVO);
        usuarioRepository.save(usuario);

        log.info("Usuario desactivado: {}", usuario.getNombreUsuario());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileDto> getAllUsers() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(usuario -> {
                    PerfilUsuario perfil = perfilUsuarioRepository.findByUsuario_UsuarioId(usuario.getUsuarioId())
                            .orElse(null);
                    return mapToUserProfileDto(usuario, perfil);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileDto> getUsersByType(Usuario.TipoUsuario tipoUsuario) {
        List<Usuario> usuarios = usuarioRepository.findByTipoUsuario(tipoUsuario);
        return usuarios.stream()
                .map(usuario -> {
                    PerfilUsuario perfil = perfilUsuarioRepository.findByUsuario_UsuarioId(usuario.getUsuarioId())
                            .orElse(null);
                    return mapToUserProfileDto(usuario, perfil);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean verifyEmail(String token) {
        TokenResetPassword resetToken = resetPasswordRepository.findByToken(token)
                .orElse(null);

        if (resetToken == null || !resetToken.isValid() ||
                resetToken.getTipoToken() != TokenResetPassword.TipoToken.EMAIL_VERIFICATION) {
            return false;
        }

        Usuario usuario = resetToken.getUsuario();
        usuario.setEmailVerificado(true);
        usuarioRepository.save(usuario);

        resetToken.setUsado(true);
        resetPasswordRepository.save(resetToken);

        emailService.sendWelcomeEmail(usuario);

        log.info("Email verificado para usuario: {}", usuario.getNombreUsuario());
        return true;
    }

    @Override
    @Transactional
    public void resendEmailVerification(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new AuthException("Usuario no encontrado"));

        if (usuario.getEmailVerificado()) {
            throw new AuthException("Email ya está verificado");
        }

        // Invalidar tokens anteriores
        resetPasswordRepository.invalidateTokensByUserAndType(
                usuarioId, TokenResetPassword.TipoToken.EMAIL_VERIFICATION);

        String token = emailService.generateVerificationToken(usuario);
        emailService.sendEmailVerification(usuario, token);

        log.info("Email de verificación reenviado a: {}", usuario.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getCurrentUserProfile(String username) {
        Usuario usuario = usuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new AuthException("Usuario no encontrado"));

        PerfilUsuario perfil = perfilUsuarioRepository.findByUsuario_UsuarioId(usuario.getUsuarioId())
                .orElse(null);

        return mapToUserProfileDto(usuario, perfil);
    }

    @Override
    @Transactional
    public void resendEmailVerificationByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("Usuario no encontrado"));
        if (usuario.getEmailVerificado()) {
            throw new AuthException("Email ya está verificado");
        }
        resetPasswordRepository.invalidateTokensByUserAndType(
                usuario.getUsuarioId(), TokenResetPassword.TipoToken.EMAIL_VERIFICATION);
        String token = emailService.generateVerificationToken(usuario);
        emailService.sendEmailVerification(usuario, token);
        log.info("Email de verificación reenviado a: {}", usuario.getEmail());
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new AuthException("Las contraseñas no coinciden");
        }
        TokenResetPassword resetToken = resetPasswordRepository.findByToken(token)
                .orElseThrow(() -> new AuthException("Token inválido"));
        if (!resetToken.isValid() || resetToken.getTipoToken() != TokenResetPassword.TipoToken.RECUPERACION) {
            throw new AuthException("Token no válido para recuperación");
        }
        Usuario usuario = resetToken.getUsuario();
        usuario.setPassword(newPassword); // Encripta la contraseña si usas seguridad
        usuarioRepository.save(usuario);
        resetToken.setUsado(true);
        resetPasswordRepository.save(resetToken);
        log.info("Contraseña restablecida para usuario: {}", usuario.getNombreUsuario());
    }

    @Override
    @Transactional
    public void changePassword(String oldPassword, String newPassword) {
        // Aquí deberías obtener el usuario autenticado, por ejemplo con SecurityContextHolder
        // Supongamos que tienes el username:
        String username = "usuarioAutenticado"; // Reemplaza por la obtención real
        Usuario usuario = usuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new AuthException("Usuario no encontrado"));
        if (!usuario.getPassword().equals(oldPassword)) { // Si usas hash, compara con BCrypt
            throw new AuthException("Contraseña actual incorrecta");
        }
        usuario.setPassword(newPassword); // Encripta si es necesario
        usuarioRepository.save(usuario);
        log.info("Contraseña cambiada para usuario: {}", usuario.getNombreUsuario());
    }

    private UserProfileDto mapToUserProfileDto(Usuario usuario, PerfilUsuario perfil) {
        UserProfileDto.UserProfileDtoBuilder builder = UserProfileDto.builder()
                .usuarioId(usuario.getUsuarioId())
                .nombreUsuario(usuario.getNombreUsuario())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .fechaNacimiento(usuario.getFechaNacimiento())
                .tipoUsuario(usuario.getTipoUsuario().name())
                .emailVerificado(usuario.getEmailVerificado())
                .fechaCreacion(usuario.getFechaCreacion());

        if (perfil != null) {
            builder.biografia(perfil.getBiografia())
                    .carreraInteres(perfil.getCarreraInteres())
                    .nivelEducativo(perfil.getNivelEducativo())
                    .ciudad(perfil.getCiudad())
                    .region(perfil.getRegion())
                    .linkedinUrl(perfil.getLinkedinUrl())
                    .githubUrl(perfil.getGithubUrl())
                    .portafolioUrl(perfil.getPortafolioUrl())
                    .preferenciaNotificacion(perfil.getPreferenciaNotificacion())
                    .temaInterfaz(perfil.getTemaInterfaz())
                    .idiomaPreferido(perfil.getIdiomaPreferido());
        }

        return builder.build();
    }

    private void updatePerfilFromDto(PerfilUsuario perfil, UpdateUserDto dto) {
        if (dto.getBiografia() != null) {
            perfil.setBiografia(dto.getBiografia());
        }
        if (dto.getCarreraInteres() != null) {
            perfil.setCarreraInteres(dto.getCarreraInteres());
        }
        if (dto.getNivelEducativo() != null) {
            perfil.setNivelEducativo(dto.getNivelEducativo());
        }
        if (dto.getCiudad() != null) {
            perfil.setCiudad(dto.getCiudad());
        }
        if (dto.getRegion() != null) {
            perfil.setRegion(dto.getRegion());
        }
        if (dto.getLinkedinUrl() != null) {
            perfil.setLinkedinUrl(dto.getLinkedinUrl());
        }
        if (dto.getGithubUrl() != null) {
            perfil.setGithubUrl(dto.getGithubUrl());
        }
        if (dto.getPortafolioUrl() != null) {
            perfil.setPortafolioUrl(dto.getPortafolioUrl());
        }
        if (dto.getPreferenciaNotificacion() != null) {
            perfil.setPreferenciaNotificacion(dto.getPreferenciaNotificacion());
        }
        if (dto.getTemaInterfaz() != null) {
            perfil.setTemaInterfaz(dto.getTemaInterfaz());
        }
        if (dto.getIdiomaPreferido() != null) {
            perfil.setIdiomaPreferido(dto.getIdiomaPreferido());
        }
    }
}
