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
        try {
            // 홈 화면으로 이동
            device.pressHome();

            // 앱 드로어(모든 앱이 보이는 화면) 열기
            device.pressRecentApps();

            // 설정 앱 실행
            device.executeShellCommand("am start -a android.settings.SETTINGS");
            Thread.sleep(3000); // 초기 로딩 대기

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


            device.wait(Until.findObject(By.text("계정 만들기")), 8000).click();

            device.wait(Until.findObject(By.text("개인용")), 3000).click();


            List<UiObject2> testFields = device.wait(Until.findObjects(By.clazz("android.widget.EditText")), 8000);
            Thread.sleep(3000);
            testFields.get(0).setText(generateRandomAlphabet(4));

        } catch (InterruptedException | IOException e) {
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
