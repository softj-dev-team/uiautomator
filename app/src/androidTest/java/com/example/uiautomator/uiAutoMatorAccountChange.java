package com.example.uiautomator;

import android.os.RemoteException;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.BySelector;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.Until;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class uiAutoMatorAccountChange {
    private UiDevice device;
    private final String email = "majorsafe4@gmail.com";
    private final String password = "!1qazsoftj";

    @Before
    public void setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void openAccountSettings() {
        Random random = new Random();
        try {
            // 홈 화면으로 이동
            device.pressHome();

            // 앱 드로어(모든 앱이 보이는 화면) 열기
            device.pressRecentApps();

            // 설정 앱 실행
            device.executeShellCommand("am start -a android.settings.SETTINGS");
            int randomWaitTimeInSeconds = random.nextInt(13) + 3;

            Thread.sleep(3000); // 초기 로딩 대기
            Thread.sleep(randomWaitTimeInSeconds * 1000);
            // 검색창 선택
            UiObject2 searchField = device.wait(Until.findObject(By.clazz("android.widget.Button")), 3000);
            if (searchField != null) {
                searchField.click();

                // 검색어 입력
                UiObject2 inputField = device.wait(Until.findObject(By.res("com.android.settings.intelligence:id/search_src_text")), 3000);
                if (inputField != null) {
                    inputField.setText("계정");
                    inputField.click(); // 클릭하여 키보드를 활성화
                }
            }


            device.wait(Until.findObject(By.text("계정 및 백업")), 8000).click();

            device.wait(Until.findObject(By.text("계정")), 3000).click();

            device.wait(Until.findObject(By.text("계정 추가")), 3000).click();

            device.wait(Until.findObject(By.text("Google")), 3000).click();
            Thread.sleep(5000);
            device.wait(Until.hasObject(By.text("계정 만들기")), 5000);

            device.wait(Until.findObject(By.text("계정 만들기")), 8000).click();
            device.wait(Until.hasObject(By.text("개인용")), 5000);
            device.wait(Until.findObject(By.text("개인용")), 3000).click();
            Thread.sleep(5000);
            List<UiObject2> testFields = device.wait(Until.findObjects(By.clazz("android.widget.EditText")), 8000);
            Thread.sleep(5000);
            String randomString = generateRandomAlphabet(4);
            testFields.get(1).setText(randomString);
            device.wait(Until.findObject(By.text("다음")), 5000).click();

            Thread.sleep(5000);

            device.wait(Until.hasObject(By.clazz("android.widget.EditText")), 5000);

            List<UiObject2> editTexts = device.findObjects(By.clazz("android.widget.EditText"));
//            UiObject2 editText =device.findObject(By.clazz("android.widget.EditText"));
            System.out.println(editTexts);


            int randomYear = random.nextInt(2001 - 1920) + 1920;
            editTexts.get(0).setText(String.valueOf(randomYear));

            device.wait(Until.findObject(By.text("월")), 3000).click();



            int month = random.nextInt(12) + 1; // 1월부터 12월 중 하나를 랜덤하게 선택
            String monthName = month + "월";

            UiObject2 monthButton = device.wait(Until.findObject(By.text(monthName)), 3000);
            monthButton.click();
//            UiObject2 dayText = device.findObject(By.text("일"));
//            device.wait(Until.findObject(By.text("일")), 3000).click();
            int day = random.nextInt(27) + 1;
            editTexts.get(1).setText(String.valueOf(day));
            device.wait(Until.findObject(By.text("성별")), 3000).click();
            device.wait(Until.findObject(By.text("남자")), 3000).click();
            device.wait(Until.findObject(By.text("다음")), 5000).click();
            Thread.sleep(5000);
            List<UiObject2> monthElements = device.wait(Until.findObjects(By.textContains(randomString.toLowerCase())), 3000);
            monthElements.get(0).click();
            Thread.sleep(5000);
            device.wait(Until.findObject(By.text("다음")), 5000).click();
            Thread.sleep(5000);
            UiObject2 password = device.wait(Until.findObject(By.clazz("android.widget.EditText")), 3000);
            password.setText("!1qazsoftj");
            device.wait(Until.findObject(By.text("다음")), 5000).click();
            Thread.sleep(5000);
            device.wait(Until.findObject(By.text("다음")), 5000).click();
            Thread.sleep(5000);
            // 스크롤 가능한 뷰를 선택합니다. 여기서는 vertical 스크롤을 가정합니다.
            UiScrollable scrollable = new UiScrollable(new UiSelector().scrollable(true));

            // 화면 끝에 도달할 때까지 아래로 스크롤합니다.
            scrollable.scrollToEnd(10); // 매개변수는 최대 스크롤 시도 횟수입니다.
            Thread.sleep(5000);
            List<UiObject2> checkboxs = device.wait(Until.findObjects(By.clazz("android.widget.CheckBox")), 3000);
            checkboxs.get(0).click();
            checkboxs.get(1).click();
            device.wait(Until.findObject(By.text("계정 만들기")), 5000).click();

        } catch (InterruptedException | IOException | UiObjectNotFoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    public String generateRandomAlphabet(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // 대문자 A-Z 또는 소문자 a-z 범위 내의 랜덤 문자를 생성
            char randomChar = (char) (random.nextBoolean() ?
                    'A' + random.nextInt('Z' - 'A' + 1) :
                    'a' + random.nextInt('z' - 'a' + 1));
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
