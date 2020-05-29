package modelo;

import lombok.Data;

public @Data class Tabela<PKTYPE> {
    private PKTYPE pk;
    private String nome;
}
