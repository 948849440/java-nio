package com.summer.study.netty.base.manager;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class InitManager implements ApplicationContextAware {

    @Autowired
    private HttpManager httpManager;
    @Autowired
    private SerializeManager serializeManager;

    private ApplicationContext ctx;

    @PostConstruct
    private void init() {
        httpManager.initMapperMethodCache(ctx);
        serializeManager.initMessageClassTypeMap(ctx);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }
}
