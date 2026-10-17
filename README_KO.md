# DIVINE ARSENAL v0.2.0 (개발 소스)

Minecraft Java 1.21.1 / Forge 52.1.0 / Java 21

## 무기
- 롱기누스의 창: 우클릭으로 지정 위치에 신창 낙하 및 충격파
- 게이트 오브 바빌론: 황금빛 포탈 형태의 파티클과 다중 포격
- 아폴론의 활: 태양 형태의 파티클과 낙하 폭발

## 설치 / 빌드
1. Forge 1.21.1 MDK 환경과 Java 21, Gradle 8.x를 준비하세요.
2. 프로젝트 폴더에서 `gradle build`를 실행하세요.
3. 빌드에 성공하면 `build/libs`의 JAR을 Forge mods 폴더에 넣으세요.

## 안전 및 성능
- 지형 파괴는 기본적으로 꺼져 있습니다. `config/divinearsenal-common.toml`의 `terrainDestruction`으로 변경 가능합니다.
- `particleDensity`로 연출 밀도를 조정할 수 있습니다.
- 동시 진행 공격은 16개로 제한합니다.

## 개발 상태
실제 Forge 컴파일/인게임 검증을 완료하지 못한 소스 프로젝트입니다.
초거대 3D 모델과 커스텀 셰이더, 애니메이션은 아직 포함되지 않습니다.
