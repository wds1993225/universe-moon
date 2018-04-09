package com.meetcity.moon.util;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Created by moon on 2018/2/28 .
 */

public class JSUtil {


    public static String filePath = "core/src/main/resources/js/test.js";


    /**
     * 执行js脚本
     *
     * @param jsFilePath js的文件路径
     * @param methodName js文件中的方法名
     * @param arg        js文件中方法的参数
     */
    public static String executeScript(String jsFilePath, String methodName, Object... arg) {

        String result = null;
        try {
            ScriptEngineManager engineManager = new ScriptEngineManager();
            ScriptEngine engine = engineManager.getEngineByName("js");

            String script = FileUtil.readFileToString(jsFilePath);
            //TODO js引擎池

            engine.eval(script);
            Invocable invocable = (Invocable) engine;

            result = String.valueOf(invocable.invokeFunction(methodName, arg));
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;


    }

    public static Object executeScriptObj(String jsFilePath, String methodName, Object... arg) {

        Object result = null;
        try {
            ScriptEngineManager engineManager = new ScriptEngineManager();
            ScriptEngine engine = engineManager.getEngineByName("js");

            String script = FileUtil.readFileToString(jsFilePath);
            //TODO js引擎池

            engine.eval(script);
            Invocable invocable = (Invocable) engine;

            result = invocable.invokeFunction(methodName, arg);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;


    }


    public static void main(String[] args) {
        try {
            ScriptEngineManager engineManager = new ScriptEngineManager();
            ScriptEngine engine = engineManager.getEngineByName("js");


            String script = FileUtil.readFileToString(filePath);


            engine.eval(script);
            //Object object = engine.get("obj");

            Invocable invocable = (Invocable) engine;

            String result = String.valueOf(invocable.invokeFunction("test"));

            System.out.println("");
        } catch (Throwable t) {
            t.printStackTrace();
        }


    }


}
