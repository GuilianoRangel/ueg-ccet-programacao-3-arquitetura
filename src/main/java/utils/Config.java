package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static Config config;
    private Properties properties = new Properties();

    private Config(){
        try {
            System.out.println(new java.io.File( "." ).getCanonicalPath());
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("app.properties");
            properties.load(resourceAsStream);
        } catch (IOException e) {
            System.out.println("Erro ao fazer leitura do arquivo de configuração - Utilizando Arquivo padrão da ARquitetura");
            e.printStackTrace();
        } catch (NullPointerException e){
            System.out.println("utilizando arquivo de configuração da Arquitetura");
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("app-arquitetura.properties");
            try {
                properties.load(resourceAsStream);
            } catch (IOException ex) {
                System.out.println("Erro ao fazer leitura do arquivo de configuração da ARquitetura");
                ex.printStackTrace();
            }
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
        return this.properties.getProperty("database.user","postgres");
    }

    public String getDatabasePassword(){
        return this.properties.getProperty("database.password","postgres");
    }

}
