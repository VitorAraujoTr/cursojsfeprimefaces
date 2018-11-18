package br.com.project.listener;

import br.com.framework.utils.UtilFramework;
import br.com.project.model.classes.Entidade;
import br.com.project.model.classes.InformacaoRevisao;
import org.hibernate.envers.RevisionListener;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Objects;

@Service
public class CustomListener implements RevisionListener, Serializable {

    @Override
    public void newRevision(Object revisionEntity) {
        InformacaoRevisao informacaoRevisao = (InformacaoRevisao) revisionEntity;
        Long codUser = UtilFramework.getThreadLocal().get();

        Entidade entidade = new Entidade();
        if(Objects.nonNull(codUser) && codUser != 0L){
            entidade.setEnt_codigo(codUser);
            informacaoRevisao.setEntidade(entidade);
        }
    }
}
