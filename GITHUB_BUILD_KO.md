# GitHub Actions JAR 자동 빌드

1. GitHub에서 새 저장소를 만듭니다. README 자동 생성은 끕니다.
2. 이 ZIP의 `divine_arsenal` 폴더 **안에 있는 파일과 폴더들**을 저장소 최상위에 업로드합니다. `.github/workflows/build.yml`도 반드시 포함해야 합니다.
3. GitHub 저장소의 Actions 탭에서 `Build Forge Mod JAR`를 선택합니다.
4. `Run workflow` 버튼을 누릅니다. main/master 브랜치에 push해도 실행됩니다.
5. 초록색 체크가 뜨면 해당 실행 페이지 아래 Artifacts의 `DIVINE_ARSENAL_Forge_1.21.1_JAR`를 다운로드합니다.
6. 빨간 X가 뜨면 빌드가 실패한 것이며, 로그를 확인하여 소스 오류를 수정해야 합니다. 현재 소스의 빌드 성공 여부는 미검증입니다.

주의: 압축 파일을 그대로 업로드하지 말고 내부 파일을 올리세요. 저장소 루트에 `build.gradle`이 보여야 합니다.
