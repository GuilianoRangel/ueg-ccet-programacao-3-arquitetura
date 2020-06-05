package persistence;

import lombok.Data;

public enum PersistenceAction{

    INSERT("INSERT","Insert"),
    DELETE("DELETE", "Delete"),
    UPDATE("UPDATE", "Update");


    private String id;
    private String descricao;

    PersistenceAction(final String id, final String descricao){
        this.id = id;
        this.descricao = descricao;
    }
    public String getId(){ return this.id; };
    public String getDescricao() { return this.descricao; };

}
