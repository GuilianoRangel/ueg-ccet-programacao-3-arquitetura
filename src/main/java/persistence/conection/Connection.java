package persistence.conection;

import utils.Config;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {
    private static Connection con;
    private java.sql.Connection db;
    private Connection(){
        Config conf = Config.getInstance();
        try {
            Class.forName(conf.getDatabaseDriver());
            this.db = DriverManager.getConnection(conf.getDatabaseURL(), conf.getDatabaseUser(), conf.getDatabasePassword());
        } catch (ClassNotFoundException e) {
            System.out.println("Erro Driver de JDBC não encontrado");
            e.printStackTrace();
            System.exit(1);
        } catch (SQLException e) {
            System.out.println("Erro de conexão com o banco");
            e.printStackTrace();
            System.exit(1);
        }
    }
    public static Connection getIntance(){
        if(con == null){
            con = new Connection();
        }
        return con;
    }

    public java.sql.Connection getDb() {
        return db;
    }
}
