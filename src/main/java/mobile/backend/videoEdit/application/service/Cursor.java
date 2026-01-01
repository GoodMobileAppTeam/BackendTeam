package mobile.backend.videoEdit.application.service;

import mobile.backend.global.exception.CustomException;
import mobile.backend.videoEdit.exception.VideoErrorCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Cursor 기반 페이징에서 "조회 기준 위치"를 표현하는 값 객체(Value Object).
 *
 * =====================================================
 * [계층 간 이동 흐름 (Flow)]
 *
 * Client
 *   ↓
 * Controller
 *   - 요청 파라미터(cursor 관련 값) 수신
 *   - Cursor.of(...) 생성 ★
 *   ↓
 * Service (Application Layer)
 *   - Cursor를 기준으로 조회 방향(DOWN / UP) 판단
 *   - 다음/이전 페이지 조회 조건 계산
 *   ↓
 * Repository
 *   - Cursor 내부 필드(createdAt, id 등)를 사용하여
 *     where / order by 조건 구성
 *   ↓
 * Service
 *   - 조회 결과 가공
 *   - CursorPageResult 생성
 *
 * =====================================================
 *
 * [왜 이 객체가 필요한가]
 * - cursor 페이징은 단일 id 기준으로는 안정성이 떨어진다.
 * - createdAt, saveTime 등 정렬 기준 컬럼을 함께 묶어
 *   조회 기준을 고정해야 중복/누락을 방지할 수 있다.
 * - 여러 파라미터를 의미 단위로 묶기 위한 객체다.
 *
 * [어떤 계층의 개념인가]
 * - application/service 계층의 입력 모델
 *
 * [사용 원칙]
 * - Controller: Cursor 생성 책임
 * - Service: Cursor 해석 및 조회 기준 계산
 * - Repository: Cursor 필드를 쿼리에만 활용
 */
public record Cursor(
        LocalDate saveTime,
        LocalDateTime createdAt,
        Long id
) {

    public static Cursor of(
            LocalDate saveTime,
            LocalDateTime createdAt,
            Long id
    ) {
        if (saveTime == null || createdAt == null || id == null) {
            throw new CustomException(VideoErrorCode.INVALID_CURSOR);
        }
        return new Cursor(saveTime, createdAt, id);
    }
}
