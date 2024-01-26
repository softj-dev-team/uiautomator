package com.example.uiautomator;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.BySelector;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class uiAutoMatorChangeLanguage {
    private UiDevice device;

    @Before
    public void setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }
    private String processContentDesc(String contentDesc) {
        // content-desc에서 특수 문자와 공백을 제거하고, 대문자를 소문자로 변환 후 [ 대괄호를 포함하여 반환
        return contentDesc.replaceAll("[^a-zA-Z0-9가-힣\\[\\]]", "")
                .replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .toLowerCase(); // 여기에 toLowerCase() 추가
    }
    private static final long TIMEOUT = 3000; // 3초 타임아웃 설정

    private UiObject2 findTextInAllElements(String searchText) {
        long startTime = System.currentTimeMillis();
        List<UiObject2> elements;

        while (System.currentTimeMillis() - startTime < TIMEOUT) {
            elements = device.findObjects(By.text(searchText));
            if (elements != null && !elements.isEmpty()) {
                for (UiObject2 element : elements) {
                    if (element.getText().equals(searchText)) {
                        return element;
                    }
                }
            }

            try {
                Thread.sleep(500); // 짧은 시간 간격으로 확인
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        return null;
    }
    @Test
    public void changeLanguageToKorean() {
        try {
            // 설정 앱 실행
            device.executeShellCommand("am start -a android.settings.SETTINGS");

            // 검색창 선택
            UiObject2 searchField = device.wait(Until.findObject(By.res("com.android.settings:id/search_action_bar_title")), 3000);
            if (searchField != null) {
                searchField.click();

                // 검색어 입력
                UiObject2 inputField = device.wait(Until.findObject(By.clazz("android.widget.EditText")), 3000);
                if (inputField != null) {
                    inputField.setText("Languages");
                    inputField.click(); // 클릭하여 키보드를 활성화
                    device.pressEnter(); // 엔터 키 누르기
                }
            }
            Thread.sleep(3000);
            // Language & input 요소를 찾고 클릭
            UiObject2 languageInput = device.wait(Until.findObject(By.textContains("System > Languages & input")), 3000);
            if (languageInput != null) {
                languageInput.click();
            } else {
                // System breadcrumb 요소 찾고 클릭
                UiObject2 systemBreadcrumb = device.wait(Until.findObject(By.res("com.google.android.settings.intelligence:id/breadcrumb").text("System")), 3000);
                if (systemBreadcrumb != null) {
                    systemBreadcrumb.click();
                }
            }
            Thread.sleep(3000);
            // 언어 추가 화면으로 이동
            // 'Add a language' 요소를 찾기
            UiObject2 add_language = device.wait(Until.findObject(By.res("com.android.settings:id/add_language").text("System")), 3000);
            if (add_language != null) {
                add_language.click();

            }else{
                UiObject2 add_language_step1 = device.wait(Until.findObject(By.res("android:id/title").text("Languages")), 2000);
                add_language_step1.click();

                UiObject2 add_language_step2 = device.wait(Until.findObject(By.res("android:id/title").text("System Languages")), 2000);
                add_language_step2.click();

                UiObject2 add_language_step3 = device.wait(Until.findObject(By.res("com.android.settings:id/add_language")), 2000);
                add_language_step3.click();
            }
            UiObject2 locale_search_menu = device.wait(Until.findObject(By.res("android:id/locale_search_menu")), 3000);
            locale_search_menu.click();

            // 검색어 입력
            UiObject2 inputField = device.wait(Until.findObject(By.clazz("android.widget.AutoCompleteTextView")), 3000);

            inputField.setText("korea");

            UiObject2 locale = device.wait(Until.findObject(By.res("android:id/locale")), 3000);
            locale.click();

            UiObject2 locale_step_one = device.wait(Until.findObject(By.res("android:id/locale").text("대한민국")), 3000);
            locale_step_one.click();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
