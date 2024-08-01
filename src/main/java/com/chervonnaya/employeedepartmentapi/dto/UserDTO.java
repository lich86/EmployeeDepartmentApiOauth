package com.chervonnaya.employeedepartmentapi.dto;

import com.chervonnaya.employeedepartmentapi.model.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends BaseDTO{
    @NotBlank(message = "{email.notBlank}")
    @Email
    private String email;
    @NotBlank(message = "{password.notBlank}")
    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;
    @NotNull
    private Role role;

}
