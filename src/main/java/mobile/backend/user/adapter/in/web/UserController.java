package mobile.backend.user.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mobile.backend.global.adapter.in.web.response.BaseResponse;
import mobile.backend.global.security.CustomUserDetails;
import mobile.backend.user.application.port.in.UserQueryUseCase;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import mobile.backend.user.adapter.in.web.response.UserResponse;
import mobile.backend.user.domain.model.User;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "User API", description = "회원 관련 API")
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserQueryUseCase userQueryUseCase;

    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<BaseResponse<UserResponse>> getMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = userQueryUseCase.getUserInfo(userDetails.getUserId());

        return ResponseEntity.ok(BaseResponse.success(UserResponse.from(user)));
    }


    @Operation(summary = "회원 탈퇴", description = "현재 로그인한 사용자의 계정을 삭제합니다. 모든 연관 데이터(비디오, 프로필 이미지 등)가 함께 삭제됩니다.")
    @DeleteMapping("/me")
    public ResponseEntity<BaseResponse<Void>> deleteUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletResponse httpResponse) {

        userQueryUseCase.deleteUser(userDetails.getUserId());

        // RefreshToken 쿠키 삭제
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        httpResponse.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        return ResponseEntity.ok(BaseResponse.success("User deleted", null));
    }

    @Operation(
            summary = "프로필 수정",
            description = "현재 로그인한 사용자의 이름 및 프로필 이미지를 수정합니다. 이름만, 이미지만, 또는 둘 다 수정할 수 있습니다."
    )
    @PatchMapping(value = "/me", consumes = "multipart/form-data")
    public ResponseEntity<BaseResponse<UserResponse>> updateMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) String name,
            @RequestPart(required = false) MultipartFile profileImage) {

        User updatedUser = userQueryUseCase.updateUserProfile(
                userDetails.getUserId(),
                name,
                profileImage
        );

        return ResponseEntity.ok(BaseResponse.success(UserResponse.from(updatedUser)));
    }

}