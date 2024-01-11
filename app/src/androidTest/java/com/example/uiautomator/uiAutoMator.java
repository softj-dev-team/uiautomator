package com.example.uiautomator;


import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.graphics.Rect;
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

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Random;

@RunWith(AndroidJUnit4.class)
public class uiAutoMator {
    @Test
    public void playFirstVideoAfterDelay() throws InterruptedException {

        // UiDevice 인스턴스 가져오기
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        // UiScrollable 인스턴스 생성
        UiScrollable scrollable = new UiScrollable(new UiSelector().scrollable(true));

        // 랜덤 객체 생성
        Random random = new Random();
        try {
            // 2에서 5 사이의 랜덤한 숫자 생성
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
