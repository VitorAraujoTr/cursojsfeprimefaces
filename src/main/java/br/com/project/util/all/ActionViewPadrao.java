package br.com.project.util.all;

import javax.annotation.PostConstruct;
import java.io.Serializable;

public interface ActionViewPadrao extends Serializable {

    abstract void limparLista() throws Exception;

    abstract String save() throws Exception;

    abstract void saveNotReturn() throws Exception;

    abstract void saveEdit() throws Exception;

    abstract void excluir() throws Exception;

    abstract String ativat() throws Exception;

    //Realoza iniciacao de metodos, valores ou variaveis
    @PostConstruct
    abstract String novo() throws Exception;

    abstract String editar() throws Exception;

    abstract void setarVariaveisNulas() throws Exception;

    abstract void consultarEntidade() throws Exception;

    abstract void statusOperation(StatusPersistencia status) throws Exception;

    abstract String redirectNreEntidade() throws Exception;

    abstract String redirectFindEntidade() throws Exception;

    abstract void addMsg(String msg);

}
