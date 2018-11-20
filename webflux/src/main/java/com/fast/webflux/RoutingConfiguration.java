package com.fast.webflux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;

/**
 * 输入描述.
 *
 * @author : weibo
 * @version : v1.0
 * @since : 2018/11/20 14:57
 */
@Configuration
public class RoutingConfiguration {

    /**
     * 路由.
     *
     * @param userHandler
     * @return
     */
    @Bean
    public RouterFunction<ServerResponse> monoRouterFunction(UserHandler userHandler) {
        return route(GET("/api/user").and(accept(MediaType.APPLICATION_JSON_UTF8)), userHandler::getUser)
            .andRoute(GET("/api/user-list").and(accept(MediaType.APPLICATION_JSON)), userHandler::userList);
    }
}
