package mediator;

import javax.swing.*;

public interface MediatorIF {
    // Viene notificato il cambiamento dello stato di un widget da parte di un componente
    void widgetCambiato(JComponent widget);
}
