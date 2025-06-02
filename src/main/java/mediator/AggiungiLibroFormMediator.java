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
        // Se il cambiamento proviene da uno dei campi di testo obbligatori
        if (widget == titoloField || widget == autoreField || widget == isbnField) {
            if (saveButton != null) {
                saveButton.setEnabled(areRequiredFieldsFilled());
            }
        }
        // Se il cambiamento proviene dal pulsante di salvataggio (nel caso volessi aggiungere logica qui, es. un JOptionPane di conferma)
        // else if (widget == saveButton) {
        //     // Potresti aggiungere qui una logica post-salvataggio o una conferma
        //     System.out.println("Pulsante Salva premuto.");
        // }
    }

    // Controlla se i campi obbligatori sono stati riempiti
    private boolean areRequiredFieldsFilled() {
        return titoloField != null && !titoloField.getText().trim().isEmpty() &&
                autoreField != null && !autoreField.getText().trim().isEmpty() &&
                isbnField != null && !isbnField.getText().trim().isEmpty();
    }
}
