package br.com.framework.utils;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class UtilFramework implements Serializable {

    private static final long serialVersionUID = 1L;

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public synchronized static ThreadLocal<Long> getThreadLocal(){
        return threadLocal;
    }
}
