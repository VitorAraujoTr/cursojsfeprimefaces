package br.com.project.bean.geral;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.web.context.request.FacesRequestAttributes;

import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ViewScope implements Scope, Serializable {

    private static final long serialVersionUID = 1L;

    public static final String VIEW_SCOPE_CALLBACKS = "viewScope.callBacks";

    //Retorna o objeto de scopo
    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        Object instance = getViewMap().get(name);
        if(Objects.isNull(instance)){
            instance = objectFactory;
            getViewMap().put(name, instance);
        }
        return instance;
    }

    @Override
    public Object remove(String s) {
        Object instance = getViewMap().remove(s);
        if(Objects.nonNull(instance)){
            Map<String, Runnable> callBacks = (Map<String, Runnable>) getViewMap().get(VIEW_SCOPE_CALLBACKS);
            if(Objects.nonNull(callBacks)){
                callBacks.remove(s);
            }
        }
        return instance;
    }

    @Override
    public void registerDestructionCallback(String s, Runnable runnable) {
        Map<String, Runnable> callbacks = (Map<String, Runnable>) getViewMap().get(VIEW_SCOPE_CALLBACKS);
        if(Objects.nonNull(callbacks)){
            callbacks.put(VIEW_SCOPE_CALLBACKS, runnable);
        }
    }

    @Override
    public Object resolveContextualObject(String s) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesRequestAttributes facesRequestAttributes = new FacesRequestAttributes(facesContext);
        return facesRequestAttributes.resolveReference(s);
    }

    @Override
    public String getConversationId() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesRequestAttributes facesRequestAttributes = new FacesRequestAttributes(facesContext);
        return facesRequestAttributes.getSessionId() + "-" + facesContext.getViewRoot().getViewId();
    }

    //getViewRoot()
    //Retorna o componente raiz que está associado a esta solicitacao(request).
    //getViewMap()
    //Retorna o mapa que atia com a interface para o armazenamento de dados
    private Map<String, Object> getViewMap() {
        return Objects.nonNull(FacesContext.getCurrentInstance()) ?
                FacesContext.getCurrentInstance().getViewRoot().getViewMap() : new HashMap<String, Object>();
    }
}
