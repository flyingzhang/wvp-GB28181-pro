package com.genersoft.iot.vmp.utils;

import com.genersoft.iot.vmp.conf.UserSetting;
import com.genersoft.iot.vmp.conf.security.SecurityUtils;
import com.genersoft.iot.vmp.conf.security.dto.LoginUser;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MediaServerUtils {
    public static Map<String, String> urlParamToMap(String params) {
        HashMap<String, String> map = new HashMap<>();
        if (ObjectUtils.isEmpty(params)) {
            return map;
        }
        String[] paramsArray = params.split("&");
        if (paramsArray.length == 0) {
            return map;
        }
        for (String param : paramsArray) {
            String[] paramArray = param.split("=");
            if (paramArray.length == 2) {
                map.put(paramArray[0], paramArray[1]);
            }
        }
        return map;
    }

    public static String getPlayAuthString(UserSetting userSetting, String callId) {
        List<String> params = new ArrayList<>();
        if (Boolean.TRUE.equals(userSetting.getPlayAuthority())) {
            LoginUser user = SecurityUtils.getUserInfo();
            if (user != null && user.getShortToken() != null && !user.getShortToken().isEmpty()) {
                params.add("token=" + user.getShortToken());
            }
        }
        if (!ObjectUtils.isEmpty(callId)) {
            params.add("callId=" + callId);
        }
        return (params.isEmpty()) ? "" : "?" + params.stream().collect(Collectors.joining("&&"));
    }
}
