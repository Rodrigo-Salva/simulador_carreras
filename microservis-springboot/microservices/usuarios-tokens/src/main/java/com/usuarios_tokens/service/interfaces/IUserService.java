package com.usuarios_tokens.service.interfaces;


import com.usuarios_tokens.dto.user.UpdateUserDto;
import com.usuarios_tokens.dto.user.UserProfileDto;
import com.usuarios_tokens.entity.Usuario;

import java.util.List;

public interface IUserService {

    UserProfileDto getUserProfile(Long usuarioId);

    UserProfileDto updateUserProfile(Long usuarioId, UpdateUserDto updateDto);

    void deleteUser(Long usuarioId);

    List<UserProfileDto> getAllUsers();

    List<UserProfileDto> getUsersByType(Usuario.TipoUsuario tipoUsuario);

    boolean verifyEmail(String token);

    void resendEmailVerification(Long usuarioId);

    UserProfileDto getCurrentUserProfile(String username);

    void resendEmailVerificationByEmail(String email);

    void changePassword(String oldPassword, String newPassword);

    void resetPassword(String token, String newPassword, String confirmPassword);
}
