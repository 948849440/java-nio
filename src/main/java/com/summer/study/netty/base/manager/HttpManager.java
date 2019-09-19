package com.summer.study.netty.base.manager;

import com.google.common.collect.Maps;
import com.summer.study.netty.base.annotation.HttpController;
import com.summer.study.netty.base.annotation.MethodMapper;
import com.summer.study.netty.base.model.HttpRequestMappingDefinition;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@Component
public class HttpManager {

    private Map<String, HttpRequestMappingDefinition> mapperMethodCache;

    public void initMapperMethodCache(ApplicationContext ctx){
        Map<String, HttpRequestMappingDefinition> initMap = Maps.newHashMap();
        Map<String, Object> controllers = ctx.getBeansWithAnnotation(HttpController.class);
        controllers.values().forEach(controller ->{
            HttpController httpControllerAnno = controller.getClass().getAnnotation(HttpController.class);
            String prefixPath = httpControllerAnno.value();
            for(Method method : controller.getClass().getMethods()){
                if(! method.isAnnotationPresent(MethodMapper.class)){
                    return;
                }
                MethodMapper methodMapperAnno = method.getAnnotation(MethodMapper.class);
                String suffixPath = methodMapperAnno.value();
                String uri = prefixPath + suffixPath;
                HttpRequestMappingDefinition definition = new HttpRequestMappingDefinition(uri, controller, method);
                initMap.put(uri, definition);
            }
        });
        mapperMethodCache = initMap;
    }

    public void handleRequest(String uri){
        HttpRequestMappingDefinition definition = mapperMethodCache.get(uri);
        if(definition == null){
            System.out.println("NO METHOD MATCH URI");
            return;
        }
        try {
            definition.getExecuteMethod().setAccessible(true);
            definition.getExecuteMethod().invoke(definition.getInvoker(), definition.getExecuteMethod().getParameters());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
