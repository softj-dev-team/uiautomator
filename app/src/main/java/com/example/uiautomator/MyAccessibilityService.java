package com.example.uiautomator;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.graphics.Rect;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class MyAccessibilityService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            // 현재 화면의 루트 노드를 가져옵니다.
            AccessibilityNodeInfo rootNode = getRootInActiveWindow();

            if (rootNode == null) {
                return;
            }

            // ImageView 클래스의 요소를 찾습니다.
            findImageViews(rootNode);

            // rootNode를 해제합니다.
            rootNode.recycle();
        }
    }
    private void findImageViews(AccessibilityNodeInfo node) {
        if (node == null) {
            return;
        }
        if(node.getClassName().equals("android.widget.ImageView")){
            Rect bounds = new Rect();
            node.getBoundsInScreen(bounds);
            int width = bounds.width();
            int height = bounds.height();
        }


    }

    @Override
    public void onInterrupt() {
        // AccessibilityService가 중단될 때 호출됩니다.
    }
}