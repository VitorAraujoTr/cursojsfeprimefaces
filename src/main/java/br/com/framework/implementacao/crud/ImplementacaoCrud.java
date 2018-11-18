package br.com.framework.implementacao.crud;

import br.com.framework.hibernate.session.HibernateUtil;
import br.com.framework.interfaces.crud.InterfaceCrud;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Component
@Transactional
public class ImplementacaoCrud<T> implements InterfaceCrud<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SimpleJdbcTemplateImpl simpleJdbcTemplate;

    @Autowired
    private SimpleJdbcInsertImplements simpleJdbcInsertImplements;

    @Autowired
    private SimpleJdbcTemplateImpl simpleJdbcTemplateImpl;

    private SimpleJdbcTemplate getSimpleJdbcTemplateImpl(){
        return simpleJdbcTemplateImpl;
    }

    @Override
    public void save(Object obj) throws Exception {
        validaSessionFactory();
        sessionFactory.getCurrentSession().save(obj);
        executeFlushSession();
    }
    @Override
    public void persist(Object obj) throws Exception {
        validaSessionFactory();
        sessionFactory.getCurrentSession().persist(obj);
        executeFlushSession();
    }
    @Override
    public void saveOrUpdate(Object obj) throws Exception {
        validaSessionFactory();
        sessionFactory.getCurrentSession().saveOrUpdate(obj);
        executeFlushSession();
    }
    @Override
    public void update(Object obj) throws Exception {
        validaSessionFactory();
        sessionFactory.getCurrentSession().update(obj);
        executeFlushSession();
    }
    @Override
    public void delete(Object obj) throws Exception {
        validaSessionFactory();
        sessionFactory.getCurrentSession().delete(obj);
        executeFlushSession();
    }
    @Override
    public Object merge(Object obj) throws Exception {
        validaSessionFactory();
        obj = (T) sessionFactory.getCurrentSession().merge(obj);
        executeFlushSession();
        return obj;
    }
    @Override
    public List findList(Class<T> entidade) throws Exception {
        validaSessionFactory();
        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT(entity)")
                .append(entidade.getSimpleName()).append(" entity ");
        List<T> lista = sessionFactory.getCurrentSession().createQuery(query.toString()).list();
        return lista;
    }
    @Override
    public Object findById(Class<T> entidade, Long id) throws Exception {
        validaSessionFactory();
        Object obj = sessionFactory.getCurrentSession().load(getClass(), id);
        return obj;
    }
    @Override
    public T findByPorId(Class entidade, Long id) throws Exception {
        validaSessionFactory();
        T obj = (T) sessionFactory.getCurrentSession().load(getClass(), id);
        return obj;
    }
    @Override
    public List<T> findListByQueryDinamica(String s) throws Exception {
        validaSessionFactory();
        List<T> lista;
        lista = sessionFactory.getCurrentSession().createQuery(s).list();
        return lista;
    }
    @Override
    public void executeUpdateQueryDinamica(String s) throws Exception {
        validaSessionFactory();
        sessionFactory.getCurrentSession().createQuery(s).executeUpdate();
        executeFlushSession();
    }
    @Override
    public void executeUpdateSQLDinamica(String s) throws Exception {
        validaSessionFactory();
        sessionFactory.getCurrentSession().createSQLQuery(s).executeUpdate();
        executeFlushSession();
    }
    @Override
    public void clearSession() throws Exception {
        validaSessionFactory();
        sessionFactory.getCurrentSession().clear();
    }
    @Override
    public void evict(Object objs) throws Exception {
        validaSessionFactory();
        sessionFactory.getCurrentSession().evict(objs);
    }
    @Override
    public Session getSession() throws Exception {
        validaSessionFactory();
        return sessionFactory.getCurrentSession();
    }
    @Override
    public List<?> getListSQLDinamica(String sql) throws Exception {
        validaSessionFactory();
        List<?> lista = sessionFactory.getCurrentSession().createSQLQuery(sql).list();
        return lista;
    }
    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
    @Override
    public SimpleJdbcTemplate getSimpleJdbcTemplate() {
        return simpleJdbcTemplate;
    }
    @Override
    public SimpleJdbcInsert getSimpleJdbcInsert() {
        return simpleJdbcInsertImplements;
    }
    @Override
    public Long totalRegistro(String table) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COUNT(1) FROM ").append(table);
        return jdbcTemplate.queryForLong(sql.toString());
    }
    @Override
    public Query obterQuery(String query) throws Exception {
        validaSessionFactory();
        Query queryReturn = sessionFactory.getCurrentSession().createQuery(query.toString());
        return queryReturn;
    }

    public List<Object[]> getListSQLDinamicaArray(String sql) throws Exception{
        validaSessionFactory();
        List<Object[]> lista = (List<Object[]>) sessionFactory.getCurrentSession().createSQLQuery(sql).list();
        return lista;
    }

    //Realiza consulta no banco de dados, iniciando o carregamento a partir do
    //registro passado no parametro -> iniciaNoRegistro e obtendo maximo de
    //resultados passados em -> maximoResultado.
    @Override
    public List findListByQueryDinamica(String query, int iniciaNoRegistro, int maximoResultado) throws Exception {
        validaSessionFactory();
        List<T> lista;
        lista = sessionFactory.getCurrentSession().createQuery(query).setFirstResult(iniciaNoRegistro).setMaxResults(maximoResultado).list();
        return lista;
    }

    private void validaSessionFactory(){
        if(Objects.isNull(sessionFactory)){
            sessionFactory = HibernateUtil.getSessionFactory();
        }
        validarTransaction();
    }

    private void validarTransaction(){
        if(!sessionFactory.getCurrentSession().getTransaction().isActive()){
            sessionFactory.getCurrentSession().beginTransaction();
        }
    }

    private void commitProcessoAjax(){
        sessionFactory.getCurrentSession().beginTransaction().commit();
    }

    private void rollBackProcessoAjax(){
        sessionFactory.getCurrentSession().beginTransaction().rollback();
    }
    //Roda instantaneamente o SQL no banco de dados
    private void executeFlushSession(){
        sessionFactory.getCurrentSession().flush();
    }
}
