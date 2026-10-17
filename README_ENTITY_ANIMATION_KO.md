# DIVINE ARSENAL v0.5.0 - 거대 3D 연출

## 구현
- 롱기누스: 4개 BlockDisplay 조합의 거대 창이 하늘에서 낙하하며 회전하는 쌍날 연출
- 바빌론: 36개 BlockDisplay로 만든 세 겹 황금 회전 포탈 + 기존 36회 레이저 포격
- 아폴론: 39개 BlockDisplay로 구성한 구체형 태양이 성장하며 하강
- 서버 틱에서 변환을 갱신하며 종료 시 엔티티 제거
- art_source/blockbench 안의 geo.json 및 animation.json은 별도의 편집용 애니메이션 설계 자료이며, 게임 내 렌더링은 BlockDisplay 코드가 담당함

## 주의
Forge 1.21.1 빌드/런타임 검증은 수행되지 않았습니다. 버전별 API 차이로 수정이 필요할 수 있습니다. 기존 소스에 Gradle wrapper가 없으며, Gradle/Forge 의존성을 확보해 빌드해야 합니다. 엔티티 수 제한을 위해 동시 시전 수는 기존 MAX_ACTIVE=8을 유지합니다.
