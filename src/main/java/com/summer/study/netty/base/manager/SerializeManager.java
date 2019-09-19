package com.summer.study.netty.base.manager;

import com.google.common.collect.Maps;
import com.summer.study.netty.base.annotation.MessageId;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class SerializeManager {

    private static SerializeManager INSTANCE;

    private Map<Integer, Class> messageClassTypeMap;

    @PostConstruct
    private void init() {
        INSTANCE = this;
    }

    public static SerializeManager getInstance() {
        return INSTANCE;
    }

    public void initMessageClassTypeMap(ApplicationContext ctx) {
        Map<Integer, Class> cacheMap = Maps.newHashMap();
        Map<String, Object> messageBeanMap = ctx.getBeansWithAnnotation(MessageId.class);
        if (messageBeanMap == null || messageBeanMap.size() == 0) {
            return;
        }
        messageBeanMap.values().forEach(messageObject -> {
            MessageId anno = messageObject.getClass().getAnnotation(MessageId.class);
            cacheMap.put(anno.id(), messageObject.getClass());
        });
        messageClassTypeMap = cacheMap;
    }

    public Class getMessageClassTypeById(int id) {
        Class messageClass = messageClassTypeMap.get(id);
        if (messageClass == null) {
            //log
            return null;
        }
        return messageClass;
    }
}
