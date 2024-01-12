package com.example.uiautomator;


import static androidx.test.InstrumentationRegistry.getContext;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Random;

@RunWith(AndroidJUnit4.class)
public class uiAutoMator {
    private UiDevice device;

    @Before
    public void setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
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
    @Test
    public void playFirstVideoAfterDelay() throws InterruptedException {
        //최초 검색시
        String keyword ="실시간 바카라";
        // MainActivity 실행
        launchMainActivity(keyword);
        Thread.sleep(1000);
        // UiScrollable 인스턴스 생성
        UiScrollable scrollable = new UiScrollable(new UiSelector().scrollable(true));

        // 랜덤 객체 생성
        Random random = new Random();
        try {

            int randomScrollCount = random.nextInt(4) + 2; // 0~3의 랜덤값에 2를 더함

            // 랜덤한 횟수만큼 스크롤 수행
            for (int i = 0; i < randomScrollCount; i++) {
                scrollable.scrollForward();
                Thread.sleep(1000); // 스크롤 사이에 잠시 대기
            }

            boolean found = false;
            while (!found) {
                List<UiObject2> imageViews = device.findObjects(By.clazz(android.widget.ImageView.class));
                for (UiObject2 imageView : imageViews) {
                    Rect bounds = imageView.getVisibleBounds();
                    if (bounds.width() >= 500 && bounds.height() >= 500) {
                        device.click(bounds.centerX(), bounds.centerY());
                        found = true;
                        // 랜덤한 대기 시간(초) 생성 (예: 1부터 5초 사이의 랜덤한 대기 시간)
                        int randomWaitTimeInSeconds = random.nextInt(30) + 20;
                        // 생성된 랜덤 대기 시간(초)만큼 대기
                        Thread.sleep(randomWaitTimeInSeconds * 1000);

                        String inputString ="[바카라 실시간] 바카라교수 승부를 채우는요건은 무엇입니까? #바카라 #바카라실시간 -  -  - SONIA MEDIA - 803 watching - play video";

                        // 공백과 특수 문자를 제거한 문자열 생성
                        String cleanedString = inputString.replaceAll("[^a-zA-Z0-9가-힣]", "");

                        // "SONIA MEDIA"를 포함하는지 확인
                        int index = cleanedString.indexOf("SONIAMEDIA");

                        if (index != -1) {
                            // "SONIA MEDIA"를 포함하면 해당 부분 이전까지의 문자열을 추출
                            String resultString = inputString.substring(0, index);

                            // 결과 출력
                            System.out.println(resultString);
                        } else {
                            // "SONIA MEDIA"를 포함하지 않으면 원래 문자열을 그대로 출력
                            System.out.println(inputString);
                        }

                        // 다시검색
//                        launchMainActivity(keyword);

                        break;
                    }
                }

                if (!found) {
                    boolean canScrollMore = scrollable.scrollForward();
                    if (!canScrollMore) {
                        break; // 더 이상 스크롤할 수 없으면 반복 종료
                    }
                    Thread.sleep(1000);
                }
            }

        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }
}
