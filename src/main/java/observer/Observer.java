package observer;

//questa interfaccia definisce il comportamento che seguiranno tutti gli osservatori

import java.sql.SQLException;

public interface Observer {

    //metodo update che notifica un cambiamento
    void update() throws SQLException;

}
