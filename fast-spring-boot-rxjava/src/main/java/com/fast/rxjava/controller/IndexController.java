package com.fast.rxjava.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import io.reactivex.Observable;

/**
 * @author bowen.yan
 * @date 2018-11-13
 */
@RestController
public class IndexController {
    @RequestMapping(method = RequestMethod.GET, value = "/index", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Observable<Invoice> getInvoices() {
        return Observable.just(
                new Invoice("Acme", new Date()),
                new Invoice("Oceanic", new Date())
        );

    }
}
