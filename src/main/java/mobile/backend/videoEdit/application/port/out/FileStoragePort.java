package mobile.backend.videoEdit.application.port.out;

public interface FileStoragePort {

    /**
     * 파일을 S3에 업로드하고 URL을 반환
     * @param data 파일 바이트 데이터
     * @param fileName 원본 파일명
     * @param directory 저장할 디렉토리 (예: "thumbnails")
     * @return 업로드된 파일의 URL
     */
    String upload(byte[] data, String fileName, String directory);

    /**
     * S3에서 파일 삭제
     * @param fileUrl 삭제할 파일 URL
     */
    void delete(String fileUrl);
}
