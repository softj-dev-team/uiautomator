package com.example.uiautomator;

import android.os.Bundle;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class wifiRunConfig {
    private UiDevice device;
    @Before
    public void setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }
    @Test
    public void configStart() throws InterruptedException, UiObjectNotFoundException {
        // WiFi 설정 화면 열기
        device.pressHome();
        device.openQuickSettings();
        device.findObject(new UiSelector().textContains("Wi-Fi")).click();
        Thread.sleep(1000);
        UiObject2 advancedSettings = device.findObject(By.text("상세설정"));
        if (advancedSettings != null) {
            advancedSettings.click();
        } else {
            System.out.println("상세 설정 항목을 찾을 수 없습니다.");
        }
        Thread.sleep(1000);
        if (device.findObject(By.text("네트워크 추가")) != null) {
            device.findObject(By.text("네트워크 추가")).click();
        } else {
            System.out.println("네트워크 추가.");
        }
        List<UiObject2> editTexts = device.findObjects(By.res("com.android.settings:id/edittext"));

        UiObject2 editText = device.findObject(By.res("com.android.settings:id/edittext"));
        editText.setText("esaydroid5G");

        // 스피너 클릭
        UiObject2 spinner = device.findObject(By.res("com.android.settings:id/spinner"));
        if (spinner != null) {
            spinner.click();
        } else {
            System.out.println("Spinner not found");
        }

        // "WPA/WPA2-Personal" 옵션 선택
        UiObject2 wpaOption = device.wait(Until.findObject(By.text("WPA/WPA2-Personal")), 2000);
        if (wpaOption != null) {
            wpaOption.click();
        } else {
            System.out.println("\"WPA/WPA2-Personal\" option not found");
        }
        List<UiObject2> editTexts2 = device.findObjects(By.res("com.android.settings:id/edittext"));
        UiObject2 editTextsPass = editTexts2.get(1);
        editTextsPass.setText("!1qazsoftj");

        UiObject2 hightOption = device.wait(Until.findObject(By.text("고급")), 2000);

        if (hightOption != null) {
            hightOption.click();
        } else {
            System.out.println("고급 option not found");
        }
        Thread.sleep(2000);
        UiObject2 spinnerIp = device.findObject(By.res("com.android.settings:id/spinner"));
        spinnerIp.click();

        UiObject2 spinnersIpSelect = device.wait(Until.findObject(By.text("고정")), 2000);
        spinnersIpSelect.click();

        Thread.sleep(2000);


        Bundle arguments = InstrumentationRegistry.getArguments();
        String ip = arguments.getString("ip");
        String gateway = arguments.getString("gateway");
        String dns1 = arguments.getString("dns1");
        String dns2 = arguments.getString("dns2");


        List<UiObject2> editTextsIp = device.findObjects(By.res("com.android.settings:id/edittext"));
        editTextsIp.get(0).setText(ip);
        editTextsIp.get(1).setText(gateway);
        editTextsIp.get(3).setText(dns1);
        editTextsIp.get(4).setText(dns2);
        Thread.sleep(2000);
        device.findObject(By.res("com.android.settings:id/save_button")).click();
        Thread.sleep(2000);
        device.findObject(By.text("저장")).click();

    }

}
