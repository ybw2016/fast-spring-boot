package com.example.webflux;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * 输入描述.
 *
 * @author : weibo
 * @version : v1.0
 * @since : 2018/11/20 14:32
 */
@Component
public class UserHandler {

    /**
     * 查询单个对象.
     *
     * @param request
     * @return
     */
    public Mono<ServerResponse> getUser(ServerRequest request) {
        Mono<User> user = Mono.justOrEmpty(new User("jack", 34));
        return ok().contentType(APPLICATION_JSON).body(fromPublisher(user, User.class));
    }


    /**
     * 查询多个对象.
     *
     * @param request
     * @return
     */
    public Mono<ServerResponse> userList(ServerRequest request) {
        Flux<User> userFlux =
            Flux.fromArray(new User[] {new User("ybw", 30), new User("lsj", 33)});
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(userFlux, User.class);
    }
}
