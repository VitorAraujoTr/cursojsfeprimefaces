package br.com.project.util.all;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Objects;


public abstract class Messagens extends FacesContext implements Serializable {

    private static final long serialVersionUID = 1L;

    public Messagens(){

    }

    public static void responseOperation (StatusPersistencia status){
        if(Objects.nonNull(status) && status.equals(StatusPersistencia.SUCCESS)){
            success();
        } else if (Objects.nonNull(status) && status.equals(StatusPersistencia.OBJETO_REFERENCIADO)){
            msgSeverityFatal(StatusPersistencia.OBJETO_REFERENCIADO.toString());
        } else {
            operationError();
        }
    }

    public static void genericMessage(String s){
        if(facesContextValido()){
            getFacesContext().addMessage("msg", new FacesMessage(s));
        }
    }

    public static void success(){
        msgSeverityInfo(Constante.SUCESSO);
    }

    public static void operationError(){
        if(facesContextValido()){
            msgSeverityFatal(Constante.ERRO_NA_OPERACAO);
        }
    }

    public static FacesContext getFacesContext(){
        return FacesContext.getCurrentInstance();
    }

    private static boolean facesContextValido(){
        return Objects.nonNull(getFacesContext());
    }

    public static void msgSeverityWarn(String s){
        if(facesContextValido()){
            getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_WARN, s, s));
        }
    }

    public static void msgSeverityFatal(String s){
        if(facesContextValido()){
            getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL, s, s));
        }
    }

    public static void msgSeverityError(String s){
        if(facesContextValido()){
            getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_ERROR, s, s));
        }
    }

    public static void msgSeverityInfo(String s){
        if(facesContextValido()){
            getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO, s, s));
        }
    }
}
