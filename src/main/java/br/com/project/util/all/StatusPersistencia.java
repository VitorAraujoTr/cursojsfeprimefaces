package br.com.project.util.all;

import org.apache.commons.digester.annotations.rules.ObjectCreate;

public enum StatusPersistencia {

    ERROR("Erro"),
    SUCCESS("Sucesso"),
    OBJETO_REFERENCIADO("Esse registro possúi dependência. Verifique!");

    private String name;

    StatusPersistencia(String s){
        this.name = s;
    }

    @Override
    public String toString(){
        return name;
    }

}
