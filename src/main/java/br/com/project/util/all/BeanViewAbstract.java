package br.com.project.util.all;

import java.io.Serializable;

public class BeanViewAbstract implements ActionViewPadrao, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public void limparLista() throws Exception {

    }

    @Override
    public String save() throws Exception {
        return null;
    }

    @Override
    public void saveNotReturn() throws Exception {

    }

    @Override
    public void saveEdit() throws Exception {

    }

    @Override
    public void excluir() throws Exception {

    }

    @Override
    public String ativat() throws Exception {
        return null;
    }

    @Override
    public String novo() throws Exception {
        return null;
    }

    @Override
    public String editar() throws Exception {
        return null;
    }

    @Override
    public void setarVariaveisNulas() throws Exception {

    }

    @Override
    public void consultarEntidade() throws Exception {

    }

    @Override
    public void statusOperation(StatusPersistencia status) throws Exception {
        Messagens.responseOperation(status);
    }

    protected void success() throws Exception{
        statusOperation(StatusPersistencia.SUCCESS);
    }

    protected void error() throws Exception{
        statusOperation(StatusPersistencia.ERROR);
    }

    @Override
    public String redirectNreEntidade() throws Exception {
        return null;
    }

    @Override
    public String redirectFindEntidade() throws Exception {
        return null;
    }

    @Override
    public void addMsg(String msg) {
        Messagens.genericMessage(msg);
    }
}
