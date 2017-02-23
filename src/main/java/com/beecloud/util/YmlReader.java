package com.beecloud.util;

import com.beecloud.entity.DbConfig;
import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * @author hong.lin
 * @description
 * @date 2017/2/22.
 */
public class YmlReader {
    public static DbConfig getDbConfig(String dbConfig_Path) throws Exception{
        File file = new File(dbConfig_Path);
        if(!file.exists()){
            throw new Exception("file not found:"+dbConfig_Path);
        }
        YamlReader reader = new YamlReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
        DbConfig dbConfig = reader.read(DbConfig.class);
        reader.close();//此处不关闭,会导致该方法不能重复调用
        return dbConfig;
    }

    public static void main(String ...args){
        try {
            DbConfig dbConfig = YmlReader.getDbConfig("config/DbConfig.yml");
            System.out.println(dbConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
