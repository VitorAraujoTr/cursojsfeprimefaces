package br.com.project.annotation;

import java.lang.annotation.*;

@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public abstract @interface IdentificaCampoPesquisa {

    String descricaoCampo(); //descricao do campo para a tela
    String campoConslta(); //campo do banco
    int principal() default 10000; //posicao que irá aparecer no combo

}
