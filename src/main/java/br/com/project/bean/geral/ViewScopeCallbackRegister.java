package br.com.project.bean.geral;

import javax.faces.component.UIViewRoot;
import javax.faces.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ViewScopeCallbackRegister implements ViewMapListener {



    @Override
    public void processEvent(SystemEvent systemEvent) throws AbortProcessingException {
        if(systemEvent instanceof PostConstructViewMapEvent){
            PostConstructViewMapEvent viewMapEvent = (PostConstructViewMapEvent) systemEvent;
            UIViewRoot uiViewRoot = (UIViewRoot) viewMapEvent.getComponent();
            uiViewRoot.getViewMap().put(ViewScope.VIEW_SCOPE_CALLBACKS, new HashMap<String, Runnable>());
        } else if(systemEvent instanceof PreDestroyViewMapEvent){
            PreDestroyViewMapEvent viewMapEvent = (PreDestroyViewMapEvent) systemEvent;
            UIViewRoot viewRoot = (UIViewRoot) viewMapEvent.getComponent();
            Map<String, Runnable> callbacks = (Map<String, Runnable>) viewRoot.getViewMap().get(ViewScope.VIEW_SCOPE_CALLBACKS);
            if(Objects.nonNull(callbacks)){
                callbacks.values().forEach(c -> c.run());
            }
        }
    }

    @Override
    public boolean isListenerForSource(Object o) {
        return o instanceof UIViewRoot;
    }
}
