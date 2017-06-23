package com.boc.bocsoft.mobile.common.utils;

/**
 * java lib 使用的日志输出类
 * Created by Administrator on 2016/6/8.
 */
public class LoggerUtils {

    private static boolean isDebug = true;

    public static void Log(String message){
        if (isDebug) {
            System.out.println("[LOG]: " + message);
        }
    }

    public static void Error(String error){
        if (isDebug) {
            System.err.println("[ERROR]: " + error);
        }
    }

    public static void Warn(String warn){
        if (isDebug) {
            System.out.println("[WARN]: " + warn);
        }
    }

    public static void Info(String info){
        if (isDebug) {
            System.out.println("[INFO]: " + info);
        }
    }

    public static void success(String success) {
        if (isDebug) {
            System.out.println("[SUCCESS]: " + success);
        }
    }

    public static void thread(){
        if (isDebug) {
            System.out.println("[Thread]: " + Thread.currentThread());
        }
    }
}
