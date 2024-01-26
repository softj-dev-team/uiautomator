package com.example.uiautomator;

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
            // 설정 앱 실행
            device.executeShellCommand("am start -a android.settings.SETTINGS");
            Thread.sleep(3000); // 초기 로딩 대기

            // 검색창 선택
            UiObject2 searchField = device.wait(Until.findObject(By.res("com.android.settings:id/search_action_bar_title")), 3000);
            if (searchField != null) {
                searchField.click();

                // 검색어 입력
                UiObject2 inputField = device.wait(Until.findObject(By.clazz("android.widget.EditText")), 3000);
                if (inputField != null) {
                    inputField.setText("계정");
                    inputField.click(); // 클릭하여 키보드를 활성화
                }
            }


            // '계정 추가' 옵션으로 이동
            UiObject2 addAccountOption = device.wait(Until.findObject(By.res("android:id/title").text("계정")), 3000);
            if(addAccountOption != null){
                addAccountOption.click();
            }else{
                UiObject2 addAccountOption_false = device.wait(Until.findObject(By.res("android:id/title").text("비밀번호 및 계정")), 3000);
                if(addAccountOption_false != null) {
                    addAccountOption_false.click();
                }
            }


            // Google 계정 선택
            UiObject2 googleOption = device.wait(Until.findObject(By.res("android:id/title").text("계정 추가")), 3000);
            googleOption.click();

            // Google 계정 선택
            UiObject2 google = device.wait(Until.findObject(By.res("android:id/title").text("Google")), 3000);
            google.click();

            // 이메일 주소 입력
            UiObject2 emailField = device.wait(Until.findObject(By.clazz("android.widget.EditText")), 8000);
            Thread.sleep(3000);
            emailField.setText(email);

            // '다음' 버튼 클릭
            UiObject2 nextButton = device.wait(Until.findObject(By.textContains("다음")), 10000);
            nextButton.click();

            Thread.sleep(5000);
            // 패스워드 입력
            UiObject2 passwordField = device.wait(Until.findObject(By.clazz("android.widget.EditText")), 10000);
            Thread.sleep(3000);
            passwordField.setText(password);

            // '다음' 버튼 클릭
            UiObject2 nextButtonPassword = device.wait(Until.findObject(By.textContains("다음")), 5000);
            nextButtonPassword.click();

            Thread.sleep(3000); // 추가 처리를 위한 대기

            // '다음' 버튼 클릭
            UiObject2 apply = device.wait(Until.findObject(By.res("signinconsentNext")), 5000);
            apply.click();

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
