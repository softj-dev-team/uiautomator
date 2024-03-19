package com.example.uiautomator;
import static androidx.test.InstrumentationRegistry.getContext;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
@RunWith(AndroidJUnit4.class)
public class uiAutoMator {
    private UiDevice device;

    @Before
    public void setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }
    @Test
    public void playFirstVideoAfterDelay() throws InterruptedException {
        int maxAttempts = 10;//루프횟수
        int attempts = 0;
        try {
            closeApp("com.google.android.youtube");
            Thread.sleep(3000);
            // MainActivity 실행
            Map<String, String> keywordData = fetchKeywordFromServer();
            launchMainActivity(keywordData.get("keyword"));
            Thread.sleep(3000);
            // 최초 검색
            if(keywordData.get("use_random_play").equals("Y")){
                firstSearch();
            }
            // 최초 검색 및 필터 설정 수행
            if(keywordData.get("use_filter").equals("Y")){
                performSearchAndFilter(keywordData.get("keyword"));
            }

            //루프 초기화
            boolean found = false;
            while (!found && attempts < maxAttempts) {
                // UiScrollable 인스턴스 생성
                UiScrollable scrollable = new UiScrollable(new UiSelector().scrollable(true));
                Thread.sleep(3000);
                String inputString = processContentDesc(keywordData.get("title"));
                // 입력 문자열의 길이 계산
                int inputStringLength = inputString.length();
                List<UiObject2> viewGroups = device.findObjects(By.clazz(android.view.ViewGroup.class));
                for (UiObject2 viewGroup : viewGroups) {
                    try {
                        String contentDesc = viewGroup.getContentDescription();
                        if (contentDesc != null) {
                            String processContentDesc=  processContentDesc(contentDesc);
                            // contentDesc 문자열을 입력 문자열의 길이만큼 추출
                            String extractedContentDesc = processContentDesc.substring(0, Math.min(contentDesc.length(), inputStringLength));
                            // 추출된 문자열과 입력 문자열 비교
                            // 콘솔에 로그 출력
                            System.out.println("Input String: " + inputString);
                            System.out.println("Extracted Content Description: " + extractedContentDesc);
                            if (inputString.equals(extractedContentDesc)) {
                                UiObject2 targetObject = viewGroup;
                                // 요소의 위치 확인
                                Rect bounds = targetObject.getVisibleBounds();

                                // 화면의 크기 구하기
                                int screenHeight = device.getDisplayHeight();
                                int screenWidth = device.getDisplayWidth();

                                // 화면의 수직 중앙 좌표 구하기
                                int screenCenterY = screenHeight / 2;

                                // 요소의 수직 중앙 좌표 구하기
                                int centerY = (bounds.top + bounds.bottom) / 2;

                                if (centerY < screenCenterY) {
                                    // 요소가 화면 중앙보다 위에 있을 때
                                    int scrollToY = screenCenterY - centerY;
                                    device.swipe(bounds.centerX(), centerY, bounds.centerX(), centerY + scrollToY, 10); // 아래로 스와이프
                                } else if (centerY > screenCenterY) {
                                    // 요소가 화면 중앙보다 아래에 있을 때
                                    int scrollToY = centerY - screenCenterY;
                                    device.swipe(bounds.centerX(), centerY, bounds.centerX(), centerY - scrollToY, 10); // 위로 스와이프
                                }

                                Thread.sleep(3000);
                                // 스크롤 후 요소의 위치 확인
                                Rect finalBounds = targetObject.getVisibleBounds();
                                // 요소의 중앙을 클릭
                                int targetCenterX = (finalBounds.left + finalBounds.right) / 2;
                                int targetCenterY = (finalBounds.top + finalBounds.bottom) / 2;
                                device.click(targetCenterX, targetCenterY);

                                found = true; // found를 true로 설정
                                if (found) {
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        // getContentDescription()에서 발생하는 예외 처리
                        e.printStackTrace();
                    }
                }


                if (!found) {
                    boolean canScrollMore = scrollable.scrollForward();
                    if (!canScrollMore) {
                        attempts++;
                        if (attempts < maxAttempts) {
                            // 최초 검색
                            if(keywordData.get("use_random_play").equals("Y")){
                                firstSearch();
                            }
                            // 최초 검색 및 필터 설정 수행
                            if(keywordData.get("use_filter").equals("Y")){
                                performSearchAndFilter(keywordData.get("keyword"));
                            }
                        }
                    }
                    Thread.sleep(1000);
                }
            }

        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }
    // MainActivity를 실행하는 메서드
    private void launchMainActivity(String keyword) {
        Uri webPage = Uri.parse("https://www.youtube.com/results?search_query=" + keyword);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        intent.setPackage("com.google.android.youtube");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // FLAG_ACTIVITY_NEW_TASK 플래그 추가

        // Intent를 실행하여 YouTube 앱을 시작
        getContext().startActivity(intent);
    }
    // content-desc와 inputString을 비교하는 메서드
    private String processContentDesc(String contentDesc) {
        // content-desc에서 특수 문자와 공백을 제거하고 [ 대괄호를 포함하여 반환
        return contentDesc.replaceAll("[^a-zA-Z0-9가-힣\\[\\]]", "").replaceAll("\\[", "").replaceAll("\\]", "");
    }
    // 앱이 열려있는지 확인하는 메서드 추가
    private boolean isAppOpen(String packageName) {
        ActivityManager am = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        if (runningAppProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcesses) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
    // 앱을 종료하는 메서드 추가
    private void closeApp(String packageName) {
        try {
            device.executeShellCommand("am force-stop " + packageName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 서버에서 키워드를 가져오는 메서드
    private Map<String, String> fetchKeywordFromServer() {
        try {
            String apiUrl = "https://esaydroid.appccrs.com/api/search-title";
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            br.close();
            conn.disconnect();

            JSONObject jsonObject = new JSONObject(response.toString());
            Map<String, String> result = new HashMap<>();

            // title 및 keyword를 Map에 추가
            result.put("title", jsonObject.getString("title"));
            result.put("keyword", jsonObject.getString("keyword"));
            result.put("use_random_play", jsonObject.getString("use_random_play"));
            result.put("use_filter", jsonObject.getString("use_filter"));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>(); // 에러 발생 시 빈 Map 반환
        }
    }

    // 최초 검색 및 필터 설정 메소드
    public void performSearchAndFilter(String keyword) throws InterruptedException,UiObjectNotFoundException  {
        try {
            // 최초 검색시
            Thread.sleep(3000);
            // MainActivity 실행
//            launchMainActivity(keyword);
            // 앱이 열릴 때까지 1초마다 확인 (최대 10번)
            for (int i = 0; i < 3; i++) {
                if (isAppOpen("com.google.android.youtube")) {
                    break; // 앱이 열려있으면 확인 중단
                }
                Thread.sleep(1000);
            }
            boolean found = false;
            boolean foundOption = false;

            //헴버거메뉴 클릭
            while (!found) {
                List<UiObject2> imageViews = device.findObjects(By.clazz(android.widget.ImageView.class));

                for (UiObject2 imageView : imageViews) {
                    String contentDesc = imageView.getContentDescription();
                    if (contentDesc != null && (contentDesc.contains("옵션 더보기"))) {
                        imageView.click();
                        foundOption=false;//옵션더보기가 없는 기종
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    Thread.sleep(1000); // 요소를 찾지 못했을 때 잠시 대기 후 다시 시도
                    // 'android.widget.ImageButton' 중 'content-desc'가 '필터'인 요소를 찾기
                    List<UiObject2> filterButtons = device.findObjects(By.clazz("android.widget.ImageButton").desc("필터"));

                    // 찾은 요소 중 특정 조건(예: 첫 번째 요소)에 맞는 요소를 클릭
                    if (!filterButtons.isEmpty()) {
                        // 예시: 첫 번째 요소 클릭
                        foundOption=true;
                        filterButtons.get(0).click();
                    }
                }
            }

            Thread.sleep(2000);
            //루프 초기화
            found = false;
            //검색필터 클릭
            while (!found && !foundOption) {
                List<UiObject2> titles = device.findObjects(By.res("com.google.android.youtube:id/title"));

                for (UiObject2 title : titles) {
                    String titleText = title.getText();
                    if ("검색 필터".equals(titleText) || "Search filters".equals(titleText)) {
                        title.click();
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    Thread.sleep(1000); // 요소를 찾지 못했을 때 잠시 대기 후 다시 시도
                }
            }
            Thread.sleep(2000);
            //루프 초기화
            found = false;
            //실시간 클릭

            while (!found) {
                List<UiObject2> titles = device.findObjects(By.clazz("android.widget.TextView"));

                for (UiObject2 title : titles) {
                    String titleText = title.getText();
                    if ("라이브".equals(titleText)) {
                        title.click();
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    Thread.sleep(1000); // 요소를 찾지 못했을 때 잠시 대기 후 다시 시도
                }
            }
            Thread.sleep(2000);
            // 적용버튼 클릭
            UiObject2 applyButton = device.findObject(By.res("com.google.android.youtube:id/apply"));
            if (applyButton != null) {
                applyButton.click();
            }
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e; // 예외 발생 시 다시 호출한 곳으로 예외 전파
        }
    }
    public void firstSearch() throws InterruptedException,UiObjectNotFoundException{
        try {
            Thread.sleep(3000);
            // 랜덤 객체 생성
            Random random = new Random();
            int randomScrollCount = random.nextInt(3) + 2; // 0~3의 랜덤값에 2를 더함
            // UiScrollable 인스턴스 생성
            UiScrollable scrollable = new UiScrollable(new UiSelector().scrollable(true));
            // 랜덤한 횟수만큼 스크롤 수행
            for (int i = 0; i < randomScrollCount; i++) {
                scrollable.scrollForward();
                Thread.sleep(1000); // 스크롤 사이에 잠시 대기
            }
            //루프 초기화
            boolean found = false;
            while (!found) {
                List<UiObject2> imageViews = device.findObjects(By.clazz(android.widget.ImageView.class));
                for (UiObject2 imageView : imageViews) {
                    Rect bounds = imageView.getVisibleBounds();
                    if (bounds.width() >= 500 && bounds.height() >= 500) {
                        device.click(bounds.centerX(), bounds.centerY());
                        found = true; // found를 true로 설정
                        break; // for 루프를 빠져나옴
                    }
                }
                // found가 true이면 while 루프도 종료
                if (found) {
                    break;
                }
            }
            // 랜덤한 대기 시간(초) 생성 (예: 1부터 5초 사이의 랜덤한 대기 시간)
            int randomWaitTimeInSeconds = random.nextInt(181) + 120;
            // 생성된 랜덤 대기 시간 만큼 영상 시청
            Thread.sleep(randomWaitTimeInSeconds * 1000);

            Thread.sleep(5000);
            closeApp("com.google.android.youtube");
        }catch (InterruptedException | UiObjectNotFoundException e) {
            e.printStackTrace();
            throw e; // 예외 발생 시 다시 호출한 곳으로 예외 전파
        }
    }
}
