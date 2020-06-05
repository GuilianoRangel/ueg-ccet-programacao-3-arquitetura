package utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public @Data class Return {
    private boolean sucesso;
    private String message;
    private List<String> erros;

    public Return(boolean sucesso, String message) {
        this.sucesso = sucesso;
        this.message = message;
        this.erros = new ArrayList<>();
    }
}
