package com.example.jinanuniversity.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/15.
 */
public class ActivityCollector {

    public static List<Activity> activities=new ArrayList<Activity>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void removeActivity(Activity activity){
        if (activities.contains(activity)){
            activities.remove(activity);
        }
    }
    public static void finishAll(){
        if (!activities.isEmpty()){
            for (Activity activity:activities){
                if (!activity.isFinishing()){
                    activity.finish();
                }
            }
        }
    }

}
