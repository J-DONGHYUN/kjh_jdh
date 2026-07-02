package project.kjhjdh.ibid.auth.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "올바르지 않은 이메일 형식입니다.")
        String email,

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 4, max = 12, message = "비밀번호는 4자 이상 12자 이하여야 합니다.")
        String password,

        @NotBlank(message = "유저이름을 입력해주세요.")
        @Size(min = 4, max = 8, message = "유저이름은 4자 이상 8자 이하여야 합니다.")
        String username
) {
}