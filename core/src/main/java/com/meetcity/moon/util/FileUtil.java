package com.meetcity.moon.util;

import com.meetcity.moon.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.tika.Tika;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wds on 2017/10/19.
 *
 * @author moon
 */
@Slf4j
public class FileUtil {

    /**
     * 创建文件夹
     * eg:
     * D:/train/
     * D:/train
     * 后面加不加 "/"都行
     */
    public static void createFolder(String path) {
        try {
            if (!StringUtil.isEmpty(path)) {
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdirs();      //这个方法创建多层目录
                    log.debug("Folder create success , path is :{}", path);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            log.error("Folder create error ,path is :{} , error is : ", path, t);

        }
    }

    /**
     * 创建文件
     * 需要文件路径存在，否则报异常
     */
    public static void createFile(String fileName) {
        try {
            if (!StringUtil.isEmpty(fileName)) {
                File file = new File(fileName);
                if (!file.exists()) {
                    file.createNewFile();       //这个方法创建一个文件夹
                    log.debug("File create success , fileName is :{}", fileName);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            log.error("File create error ,fileName is :{} , error is :  ", fileName, t);
        }
    }


    /**
     * 创建文件夹和文件
     */
    public static void createFile(String path, String fileName) {
        createFolder(path);
        createFile(path + "/" + fileName);
    }

    /**
     * 将文字写入文件
     */
    public static void writeToFile(String path, String fileName, String content) {
        try {
            createFile(path, fileName);
            File file = new File(path + "/" + fileName);
            FileWriter fileWriter = new FileWriter(file, true);

            fileWriter.write(content);
            fileWriter.write("\r\n");

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将文字写入文件
     */
    public static void writeToFile(String path, String fileName, String[] content) {
        try {
            createFile(path, fileName);
            File file = new File(path + "/" + fileName);
            FileWriter fileWriter = new FileWriter(file, true);
            for (String str : content) {
                fileWriter.write(str);
                fileWriter.write("\r\n");
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * EOF来控制输入.
     *
     *
     * 如果字节流的话，读取到了-1说明到了文件尾。
     int i=a.read();
     while(i!=-1){
     //继续读取
     i=a.read();
     }
     对于读取字符流的话，如果到了文件尾则返回null
     String i=a.readLine();
     while(i!=null){
     //继续读取
     i=a.readLine();
     */

    /**
     * 逐行读取
     */
    public static List<String> readFileByLines(String fileName) {
        if (StringUtil.isEmpty(fileName)) {
            return null;
        }
        List<String> list = new ArrayList<String>();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                list.add(tempString);
                Util.log("line" + (line++) + ":    " + tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * 将文件中的内容读成String
     *
     * @param fileName 文件路径
     * @return 读取的内容
     */
    public static String readFileToString(String fileName) {
        if (StringUtil.isEmpty(fileName)) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                stringBuilder.append(tempString);
                //Util.log("line" + (line++) + ":    " + tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }


    /**
     * 下载文件，里面带有循环重试
     *
     * @param url      要下载的文件链接
     * @param path     要保存的路径
     * @param fileName 保存的文件名
     */
    public static boolean readResponseToFile(String url, String path, String fileName) {
        Response response = null;
        for (int i = 0; i < Constants.RETRY_TIMES; i++) {
            response = OkHttpUtil.get(url, null);
            if (response != null && response.body() != null && response.isSuccessful()) {
                log.debug("ReadResponse success , url is : {} ,  response is : {}", url, response);
                break;
            }
            log.warn("ReadResponse retry , retry time is : {} , url is : {}", i, url);
        }
        if (response == null) {
            log.error("ReadResponse error , response is null ,  url is : {}", url);
            return false;
        }
        if (response.body() == null) {
            log.error("ReadResponse error , response body is null ,  url is : {}", url);
            return false;
        }
        InputStream inputStream = response.body().byteStream();
        if (readStreamToFile(inputStream, path, fileName)) {
            response.close();
        }
        return true;

    }

    /**
     * 从流读取到文件
     *
     * @param inputStream 要读的流
     * @param path        保存的文件路径
     * @param fileName    保存的文件名
     */
    public static boolean readStreamToFile(InputStream inputStream, String path, String fileName) {
        boolean isSuccess = false;
        if (inputStream == null || StringUtil.isEmpty(path) || StringUtil.isEmpty(fileName)) {
            return false;
        }
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        int len = 0;
        long current = 0;
        byte[] buf = new byte[2048];
        try {
            FileOutputStream fos = new FileOutputStream(path + fileName);
            while ((len = inputStream.read(buf)) != -1) {
                current += len;
                fos.write(buf, 0, len);
            }
            fos.flush();
            fos.close();
            isSuccess = true;
            log.debug("Read stream to file success , path is :  {} , fileName is :  {}", path, fileName);
        } catch (Throwable t) {
            t.printStackTrace();
            log.error("Read stream to file error ,path is :  {} , fileName is : {}", path, fileName);

        }
        return isSuccess;
    }


    /**
     * 将文件读成流
     */
    public static InputStream readFileToInputStream(String filePath) {
        File f = new File(filePath);
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * 获取一个文件夹下的所有文件
     *
     * @param folderPath        文件夹路径
     * @param isUseAbsolutePath 是否使用绝对路径
     * @return 返回一组文件名
     */
    public static List<String> getAllFile(String folderPath, boolean isUseAbsolutePath) {
        if (StringUtil.isEmpty(folderPath)) {
            log.warn("folderPath is null");
        }
        File rootDir = new File(folderPath);
        List<String> list = null;
        if (!rootDir.isDirectory()) {
            log.debug("current file is not a fold , this is a file , file name is : {} ", rootDir.getAbsolutePath());
        } else {
            list = new ArrayList<>();
            String[] files = rootDir.list();
            if (files == null) {
                log.warn("there is not file here .");
                return null;
            }
            if (!folderPath.endsWith("/")) {
                folderPath = folderPath + "/";
            }
            for (String fileName : files) {
                if (isUseAbsolutePath) {
                    list.add(folderPath + fileName);      //这里使用绝对路径  (文件路径 + / + 文件名)
                } else {
                    list.add(fileName);
                }
            }
        }
        return list;
    }


    public static void p(InputStream inputStream) {
        try {
            Tika tika = new Tika();
            String a = tika.detect(inputStream);
            System.out.println("");
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }


}
