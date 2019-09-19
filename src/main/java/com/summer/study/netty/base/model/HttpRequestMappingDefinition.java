package com.summer.study.netty.base.model;

import java.lang.reflect.Method;

public class HttpRequestMappingDefinition {

    private String uri;

    private Object invoker;

    private Method executeMethod;

    public HttpRequestMappingDefinition(String uri, Object invoker, Method executeMethod) {
        this.uri = uri;
        this.invoker = invoker;
        this.executeMethod = executeMethod;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Object getInvoker() {
        return invoker;
    }

    public void setInvoker(Object invoker) {
        this.invoker = invoker;
    }

    public Method getExecuteMethod() {
        return executeMethod;
    }

    public void setExecuteMethod(Method executeMethod) {
        this.executeMethod = executeMethod;
    }
}
