package mediator;

import javax.swing.*;

public class AggiungiLibroFormMediator implements MediatorIF{

    private JTextField titoloField;
    private JTextField autoreField;
    private JTextField isbnField;
    private JButton saveButton;

    // Metodi per settare i riferimenti ai componenti
    public void setTitoloField(JTextField titoloField) {
        this.titoloField = titoloField;
    }

    public void setAutoreField(JTextField autoreField) {
        this.autoreField = autoreField;
    }

    public void setIsbnField(JTextField isbnField) {
        this.isbnField = isbnField;
    }

    public void setSaveButton(JButton saveButton) {
        this.saveButton = saveButton;
    }

    // Metodo chiamato dagli elementi della form quando cambiano
    @Override
    public void widgetCambiato(JComponent widget) {

        boolean allFieldsFilled = !titoloField.getText().trim().isEmpty() &&
                !autoreField.getText().trim().isEmpty() &&
                !isbnField.getText().trim().isEmpty();
        saveButton.setEnabled(allFieldsFilled);
        }

}
