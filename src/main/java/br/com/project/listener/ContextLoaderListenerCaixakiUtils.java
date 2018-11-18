package br.com.project.listener;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.faces.bean.ApplicationScoped;
import java.io.Serializable;

//Consegue informação, objeto ou configuração do contexto do spring
@ApplicationScoped
public class ContextLoaderListenerCaixakiUtils extends ContextLoaderListener implements Serializable {

    private static WebApplicationContext getWebApplicationContext(){
        //Retorna todo o contexto de ambiente de excecucao do spring
        return WebApplicationContextUtils.getWebApplicationContext(getCurrentWebApplicationContext().getServletContext());
    }

    public static Object getBean(String idNomeBean){
        return getWebApplicationContext().getBean(idNomeBean);
    }

    public static Object getBean(Class<?> classe){
        return getWebApplicationContext().getBean(classe);
    }

}