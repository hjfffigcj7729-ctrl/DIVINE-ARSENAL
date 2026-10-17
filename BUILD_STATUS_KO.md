# DIVINE ARSENAL 빌드 상태

- 대상: Minecraft Java 1.21.1, Forge 52.1.0, JDK 21.
- 현재 제공물: 소스 프로젝트. 정상 작동하는 JAR은 아직 생성하거나 검증하지 못함.
- 이유: 현재 환경에 Gradle/ForgeGradle 및 Minecraft 의존성이 없고 외부 다운로드 연결이 불가능함.
- Windows: JDK 21과 Gradle 8.x를 설치한 후 BUILD_WINDOWS.bat 실행.
- 성공하면 build/libs 아래 JAR을 Minecraft Forge 1.21.1 mods 폴더로 복사.
- 주의: 프로젝트 소스 자체도 실제 Forge 컴파일/실행 검증 전이며 오류가 발생할 수 있음.
