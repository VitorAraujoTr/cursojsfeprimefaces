package br.com.project.exception;

import br.com.framework.hibernate.session.HibernateUtil;
import org.hibernate.SessionFactory;
import org.primefaces.context.RequestContext;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private ExceptionHandler wrapperd;

    final FacesContext facesContext = FacesContext.getCurrentInstance();

    final Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();

    final NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();

    public CustomExceptionHandler(ExceptionHandler ex){
        this.wrapperd = ex;
    }

    // Sobrescreve o metodo ExceptionHandler que retorna a "pilha" de excecoes

    @Override
    public ExceptionHandler getWrapped() {
        return wrapperd;
    }

    //Sobrescreve o metodo handle que é responsavel por manipular as excecoes do JSF

    @Override
    public void handle() throws FacesException{
        final Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();

        while(iterator.hasNext()){
            ExceptionQueuedEvent event = iterator.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

            //Recupera a excecao do contexto
            Throwable exception = context.getException();

            //Aqui se trabalha a excecao
            try{
                requestMap.put("exceptionMessage", exception.getMessage());
                if(Objects.nonNull(exception)
                        && Objects.nonNull(exception.getMessage())
                        && Objects.nonNull(exception.getMessage().indexOf("ConstraintViolationException") != -1)){
                    FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(
                            FacesMessage.SEVERITY_WARN, "Registro não pode ser removido por estar associado",
                            ""));
                } else if(Objects.nonNull(exception) && Objects.nonNull(exception.getMessage()) && Objects.nonNull(exception.getMessage().indexOf("org.hibernate.StaleObjectStateException") != -1)){
                    FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Registro foi atualizado ou excluído por outro usuário" +
                            " Consulte novamente.",
                            ""));
                } else {
                    //Avisa o usuario do erro
                    FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(
                            FacesMessage.SEVERITY_FATAL, "O sistema se recuperou de um erro inesperado.",
                            ""));
                    FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(
                            FacesMessage.SEVERITY_INFO, "Você pode continuar utilizando o sistema normalmente!",
                            ""));
                    FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(
                            FacesMessage.SEVERITY_WARN, "O erro foi causado por :\n"+exception.getMessage(),
                            ""));

                    //
                    //Este alert só será exibido se a pagina não redirecionar
                    RequestContext.getCurrentInstance().execute("alert('O sistema se recuperou de um erro inesperado.')");

                    //Este alert utiliza um dialog
                    RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "O sistema se recuperou de um erro inesperado.."));

                    //redireciona para a pagina de erro
                    navigationHandler.handleNavigation(facesContext, null,
                            "/error/error.jsf?faces-redirect=true&expired=true");
                }

                //Renderiza a pagina de erro e exive as mensagens
                facesContext.renderResponse();
            } finally {
                SessionFactory sf = HibernateUtil.getSessionFactory();
                if(sf.getCurrentSession().getTransaction().isActive()){
                    sf.getCurrentSession().getTransaction().rollback();
                }
                //Imprime o erro no console
                exception.printStackTrace();
                iterator.remove();
            }

        }
        getWrapped().handle();
    }
}
