package mobile.backend.videoEdit.application.service;

import java.util.List;

/**
 * Cursor 기반 페이징의 조회 결과를 표현하는 DTO.
 *
 * =====================================================
 * [계층 간 이동 흐름 (Flow)]
 *
 * Client
 *   ↓
 * Controller
 *   - 요청 파라미터 수신
 *   - Service 호출
 *   ↓
 * Service (Application Layer)
 *   - Cursor 기반 조회 로직 수행
 *   - 다음/이전 페이지 존재 여부 계산
 *   - CursorPageResult 생성 ★
 *   ↓
 * Controller
 *   - API 응답 DTO로 변환 또는 그대로 반환
 *   ↓
 * Client
 *
 * =====================================================
 *
 * [왜 이 객체가 필요한가]
 * - cursor 페이징은 단순한 목록 조회가 아니라
 *   "다음/이전 페이지 존재 여부"가 핵심 정보다.
 * - Spring Data Page<T>는 offset 기반 개념에 묶여 있어
 *   cursor 페이징의 의도를 정확히 표현하지 못한다.
 *
 * [어떤 계층의 개념인가]
 * - application/service 계층의 응답 모델
 *
 * [설계 의도]
 * - 페이징 결과의 의미를 코드 수준에서 명확히 드러낸다.
 * - Controller가 페이징 계산 책임을 가지지 않도록 한다.
 *
 * content - 실제 조회된 데이터 목록
 * hasNext - 다음 페이지 존재 여부
 * hasPrev - 이전 페이지 존재 여부
 */
public record CursorPageResult<T>(
        List<T> content,
        boolean hasNext,
        boolean hasPrev
) {
}
