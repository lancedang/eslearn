package com.lance.es.eslearn.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lance.es.eslearn.entity.DocumentItem;

/**
 * es 保存数据格式为byte[] 和 String json
 */
public class ObjectToJsonUtil {

    //ElasticSearch已经使用了jackson，可以直接使用它把javabean转为json，这里使用 fastjson 好像也可以
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 以byte[]序列化到es
     *
     * @param item
     * @return
     */
    public static byte[] getByteJson(DocumentItem item) {
        try {
            //Jackson
            byte[] bytes = objectMapper.writeValueAsBytes(item);

            //fastjson
            //bytes = JSON.toJSONBytes(item);
            return bytes;
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * 以json string 保存到es
     * @param item
     * @return
     */
    public static String getStringJson(DocumentItem item) {
        String jsonString = JSON.toJSONString(item);
        return jsonString;
    }

}
