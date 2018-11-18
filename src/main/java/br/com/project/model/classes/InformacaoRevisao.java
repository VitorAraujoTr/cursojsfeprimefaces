package br.com.project.model.classes;

import br.com.project.listener.CustomListener;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "revinfo")
@RevisionEntity(CustomListener.class)
public class InformacaoRevisao extends DefaultRevisionEntity implements Serializable {

    @ManyToOne
    @ForeignKey(name = "entidade_fk")
    @JoinColumn(nullable = false, name = "entidade")
    private Entidade entidade;

    public Entidade getEntidade() {
        return entidade;
    }

    public void setEntidade(Entidade entidade) {
        this.entidade = entidade;
    }
}
