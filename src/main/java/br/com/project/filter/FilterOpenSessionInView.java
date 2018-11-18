package br.com.project.filter;

import br.com.framework.hibernate.session.HibernateUtil;
import br.com.framework.utils.UtilFramework;
import br.com.project.listener.ContextLoaderListenerCaixakiUtils;
import br.com.project.model.classes.Entidade;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

//Filtra todas as requisicoes
@WebFilter(filterName = "conexaoFilter")
public class FilterOpenSessionInView extends DelegatingFilterProxy implements Serializable {

    private static SessionFactory sessionFactory;

    //Executada apenas uma vez
    //Executado quando a aplicacao esta sendo iniciada
    @Override
    protected void initFilterBean() throws ServletException {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    //Invocado para toda requisicao e toda resposta
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws ServletException, IOException {
        // JDBC Spring
        BasicDataSource springBasicDataSource = (BasicDataSource) ContextLoaderListenerCaixakiUtils.getBean("springDataSource");
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(springBasicDataSource);
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            request.setCharacterEncoding("UTF-8");//Define codificacao

            //captura usuario que faz a operacao
            HttpServletRequest request2 = (HttpServletRequest) request;
            HttpSession sessao = request2.getSession();
            Entidade userLogadoSessao = (Entidade) sessao.getAttribute("userLogadoSessao");

            if (Objects.nonNull(userLogadoSessao)) {
                UtilFramework.getThreadLocal().set(userLogadoSessao.getEnt_codigo());
            }

            sessionFactory.getCurrentSession().beginTransaction();
            //Antes de executar acao (Request)
            chain.doFilter(request, response);//Executa a nossa acao no servidor
            //Apos de executar acao (Response)

            transactionManager.commit(status);

            if (sessionFactory.getCurrentSession().getTransaction().isActive()) {
                sessionFactory.getCurrentSession().flush();
                sessionFactory.getCurrentSession().getTransaction().commit();
            }
            //fecha a sessao
            if (sessionFactory.getCurrentSession().isOpen()) {
                sessionFactory.getCurrentSession().close();
            }
            response.setContentType("text/html; charset=UTF-8");
        } catch (Exception e) {
            transactionManager.rollback(status);
            e.printStackTrace();
            if (sessionFactory.getCurrentSession().getTransaction().isActive()) {
                sessionFactory.getCurrentSession().getTransaction().rollback();
            }
            if (sessionFactory.getCurrentSession().isOpen()) {
                sessionFactory.getCurrentSession().close();
            }
        } finally {
            if (sessionFactory.getCurrentSession().isOpen()) {
                if (sessionFactory.getCurrentSession().beginTransaction().isActive()) {
                    sessionFactory.getCurrentSession().flush();
                    sessionFactory.getCurrentSession().clear();
                }
                if (sessionFactory.getCurrentSession().isOpen()) {
                    sessionFactory.getCurrentSession().close();
                }
            }
        }
    }
}
