package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static Config config;
    private Properties properties = new Properties();

    private Config(){
        try {
            properties.load(new FileInputStream("resources/configuration"));
        } catch (IOException e) {
            System.out.println("Erro ao fazer leitura do arquivo de configuração");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static Config getInstance(){
        if (config == null ){
            config = new Config();
        }
        return config;
    }

    public String getDatabaseDriver(){
        return this.properties.getProperty("database.driver","org.postgresql.Driver");
    }

    public String getDatabaseURL(){
        return this.properties.getProperty("database.url","jdbc:postgresql://localhost:5433/lanmanager");
    }

    public String getDatabaseDatabase(){
        return this.properties.getProperty("database.database","lanmanager");
    }

    public String getDatabaseUser(){
        return this.properties.getProperty("database.password","postgres");
    }

    public String getDatabasePassword(){
        return this.properties.getProperty("database.user","postgres");
    }

}
