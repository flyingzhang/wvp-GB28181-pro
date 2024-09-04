package com.genersoft.iot.vmp.conf.security;

import com.genersoft.iot.vmp.conf.security.dto.JwtUser;
import com.genersoft.iot.vmp.utils.LogonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 退出登录成功
 */
@Component
public class LogoutHandler implements LogoutSuccessHandler, org.springframework.security.web.authentication.logout.LogoutHandler {

    private final static Logger logger = LoggerFactory.getLogger(LogoutHandler.class);
    private final StringRedisTemplate stringRedisTemplate;

    public LogoutHandler(StringRedisTemplate redisTemplate, StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String username = request.getParameter("username");
        if (StringUtils.isBlank(username)) {
            username = (String)request.getAttribute("username");
        }
        logger.info("[退出登录成功] - [{}]", username);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader(JwtUtils.getHeader());
        if (StringUtils.isNotBlank(token)) {
            JwtUser user = JwtUtils.verifyToken(token);
            request.setAttribute("username", user.getUserName());
            logger.info("[退出登录]- [{}]", user.getUserName());
            if (StringUtils.isNotBlank(user.getShortToken())) {
                stringRedisTemplate.delete(LogonUtils.SHORT_TOKEN_PREFIX + user.getShortToken());
            }
        }
    }
}
