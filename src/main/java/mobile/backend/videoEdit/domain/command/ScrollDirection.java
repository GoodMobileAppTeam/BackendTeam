package mobile.backend.videoEdit.domain.command;

/*
* INIT - 기준값에서 사이즈 만큼 조회
* DOWN - 기준값보다 오래된 데이터 사이즈 만큼 조회
* UP - 기준값보다 최신의 데이터 사이즈 만큼 조회
* */

public enum ScrollDirection {
    INIT,
    DOWN,
    UP
}
