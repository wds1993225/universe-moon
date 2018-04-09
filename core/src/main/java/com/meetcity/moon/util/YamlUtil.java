package com.meetcity.moon.util;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import com.meetcity.moon.spider.MoonSpiderConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Created by wds on 2017/11/20.
 * <p>
 * 用来读写Yaml文件的工具类
 *
 * @author moon
 */

@Slf4j
public class YamlUtil {


    /**
     * 读取yaml配置文件
     */
    public static Map<String, Object> readYaml(String filPath) {
        YamlReader reader = null;
        try {
            reader = new YamlReader(new FileReader(filPath));
            Map<String, Object> yamlMap = reader.read(Map.class);
            return yamlMap;

        } catch (IOException e) {
            log.error("read config file error, error is : ", e);
        }
        return null;
    }

    public static void writeYaml() {
        MoonSpiderConfig config = new MoonSpiderConfig();
        try {
            YamlWriter writer = new YamlWriter(new FileWriter("src/main/resources/MoonCrawel.yml"));
            writer.write(config);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //writer.write();
    }

    public static void main(String[] args) {
        readYaml("");
    }
}
