# IoTivity 안드로이드 어플리케이션 제작
이제 IoTivity의 대략적인 부분은 파악했으니, 일단 안드로이드 클라이언트부터 작성해보자. 안드로이드의 경우 일반적인 텍스트 에디터로 작업을 하면 개발이 어려운 부분이 있으므로 안드로이드 스튜디오로 작업하는 방법에 대해 알아보자. 다음 링크의 아랫 부분에 있는 `Using IoTivity Base Android API in an Android Studio Project` 부분을 참고하여 Windows 계열 플랫폼에서 안드로이드 스튜디오를 이용하여 IoTivity 안드로이드 앱을 제작할 수 있다. jar파일과 aar파일을 import하는 방식으로 간단하게 개발할 수 있다. jar파일은 자바 아카이브 파일이고, aar파일은 안드로이드 아카이브 파일이다. 안드로이드 라이브러리 파일은 AAR 파일로 컴파일된다. JAR파일과의 차이점으로는 안드로이드 리소스와 매니페스트 파일이 포함될 수 있다는 점이다.

[안드로이드 라이브러리 관련 문서](https://developer.android.com/studio/projects/android-library.html?hl=ko)    
[IoTivity 안드로이드 앱 개발 관련 문서](https://wiki.iotivity.org/android_build_instructions)    

일단 첫번째로 안드로이드 스튜디오를 설치한다. Windows 10에서 진행하였다.

기존에 iotivity 프로젝트 디렉토리에 있는 안드로이드 프로젝트를 그대로 임포트해서 사용해보려고 했는데 안드로이드 스튜디오 기반과 조금 차이점이 있는 듯 했다. gradle path 정보관련 메타 파일을 생성하고 하는 부분이 있는데, 이러한 점을 배재하기 위해서 별도로 안드로이드 스튜디오 프로젝트를 생성해서 진행하기로 했다. iotivity wiki에서도 이러한 방식을 추천했다. 따라서 다음 단계를 통해서 진행하였다.

1. 안드로이드 스튜디오에서 프로젝트를 생성
2. 타겟 디바이스를 테블릿과 스마트폰으로 지정
3. Minimum SDK를 API 21 Lolipop으로 설정    
그 외 설정은 일반적인 안드로이드 스튜디오 프로젝트 생성과 비슷하다.

4. 이후 File - New - New Module 매뉴를 실행한다.
5. Import JAR/AAR Package를 선택한다.    
임포트 하는 아카이브 파일의 경로는 다음과 같다.    
```
<iotivity>/java/iotivity-android/build/outputs/aar/iotivity-base-<your arch>-<release mode>.aar
```
빌드가 되지 않은 경우 해당 아카이브 파일이 없다. 만약 다른곳에서 빌드를 한 뒤, 그 결과물인 aar파일을 git에서 관리하고자 하더라도, gitignore 파일에 build 디렉토리가 추가되어 있으므로, git에 의해 버전관리가 되지는 않는다. 따라서 해당 빌드된 aar파일만 별도로 가져와서 사용하기로 하였다.

6. 왼쪽 프로젝트 디렉토리 구조에서 최상단의 app을 선택하고 우측 마우스 클릭을 누른 뒤, Open Module Setting을 선택한다.
7. 상단 탭 중 Dependencies를 선택한다.
8. 우측 상단의 + 버튼을 누른 뒤, 3번의 Module dependency를 선택한다.
9. 나타나는 항목 중 :iotivity-base-release를 선택한다.    
임포트한 내용들이 제대로 잘 동작하는지 확인하기 위해서 안드로이드 예제 코드 중, simple client를 구현해본 뒤 디바이스에 설치해서 결과를 확인해본다.    
위 과정들을 진행해보니, scons로 빌드한 apk파일과 안드로이드 스튜디오로 만든 앱은 동일한 동작을 한다. 