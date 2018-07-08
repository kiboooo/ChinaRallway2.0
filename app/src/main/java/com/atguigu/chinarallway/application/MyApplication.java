package com.atguigu.chinarallway.application;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiboooo on 2017/9/15.
 *
 */

@SuppressLint("Registered")
public class MyApplication extends Application {
        private List<Activity> activityList = new ArrayList<Activity>();
        private static MyApplication instance;


    // 单列模式获取唯一MyApplication的实例
        public static MyApplication getinstance() {
            if (instance == null) {
                instance = new MyApplication();
            }
            return instance;
        }

        // 添加Activity到容器中
    public void addActivity(Activity acivity) {
            activityList.add(acivity);
        }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

        // 遍历所有的Activity并fanish
    public void closeAllActiivty() {
            for (int i = 0; i < activityList.size(); i++) {
                Activity activity = activityList.get(i);
                activity.finish();
            }
            System.exit(0);
        }


}
