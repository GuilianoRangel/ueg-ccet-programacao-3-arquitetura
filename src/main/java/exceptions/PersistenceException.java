package exceptions;

import lombok.Data;
import persistence.PersistenceAction;

public @Data class PersistenceException extends Exception{
    private PersistenceAction action;

    public PersistenceException(PersistenceAction action, String message, Throwable e){
        super(message, e);
        this.action = action;
    }
    public PersistenceException(PersistenceAction action, String message){
        super(message);
        this.action = action;
    }
}
