package com.ssk.hfb.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author:
 * @create: 2018-04-24 17:20
 **/
public class ListUtils {

    //json化
    public static Boolean contains(List<String> pathList, String path) {
        for (String p : pathList){
            if (path.startsWith(p)){
                return true;
            }
        }
        return false;
    }
}
