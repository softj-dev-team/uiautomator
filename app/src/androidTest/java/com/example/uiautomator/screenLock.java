package com.example.uiautomator;

import android.os.RemoteException;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Random;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class screenLock {
    private UiDevice device;
    @Before
    public void setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }
    @Test
    public void openAccountSettings() throws RemoteException, IOException, InterruptedException {
        Random random = new Random();
        // 홈 화면으로 이동
        device.pressHome();

        // 앱 드로어(모든 앱이 보이는 화면) 열기
        device.pressRecentApps();

        // 설정 앱 실행
        device.executeShellCommand("am start -a android.settings.SETTINGS");
        Thread.sleep(3000); // 초기 로딩 대기
        device.wait(Until.findObject(By.text("잠금화면")), 8000).click();
        device.wait(Until.findObject(By.text("화면 잠금 방식")), 8000).click();
        device.wait(Until.findObject(By.text("설정 안 함")), 8000).click();
    }
}
