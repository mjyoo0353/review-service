# Amazon Corretto 17을 기반으로 하는 JDK 이미지 사용
FROM amazoncorretto:17-alpine

# 작업 디렉토리 설정
WORKDIR /app

# 로컬 빌드된 JAR 파일을 컨테이너에 복사
COPY ./build/libs/*.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
