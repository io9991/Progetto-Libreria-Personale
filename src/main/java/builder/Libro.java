package builder;//creazione del libro

public class Libro {

    private String titolo;
    private String autore;
    private String codice_ISBN;
    private String genere_appartenenza;
    private int valutazione;
    private Stato stato;

    public String getAutore() {
        return autore;
    }

    public String getCodice_ISBN() {
        return codice_ISBN;
    }

    public String getGenere_appartenenza() {
        return genere_appartenenza;
    }

    public Stato getStato() {
        return stato;
    }

    public String getTitolo() {
        return titolo;
    }

    public int getValutazione() {
        return valutazione;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    // Generalmente l'ISBN non si modifica, ma se la tua logica lo richiede, puoi includerlo.
    // Però, essendo una chiave primaria, la sua modifica richiede attenzione nel DB.
    // Per ora, lo lascio immutabile come pratica comune.
    // public void setCodice_ISBN(String codice_ISBN) {
    //     this.codice_ISBN = codice_ISBN;
    // }

    public void setGenere_appartenenza(String genere_appartenenza) {
        this.genere_appartenenza = genere_appartenenza;
    }

    public void setValutazione(int valutazione) {
        if (valutazione >= 0 && valutazione <= 5) { // Aggiungi validazione
            this.valutazione = valutazione;
        } else {
            throw new IllegalArgumentException("La valutazione deve essere tra 0 e 5.");
        }
    }

    public void setStato(Stato stato) {
        this.stato = stato;
    }

    //creo il builder
    public static class Builder{

        private String titolo;
        private String autore;
        private String codice_ISBN;
        private String genere_appartenenza;
        private int valutazione = 0;
        //metto lo stato a DA LEGGERE
        private Stato stato = Stato.DA_LEGGERE; //successivamente vedo se nella classe enum aggiungo la descrizione todo

        public Builder setTitolo(String titolo){
            this.titolo = titolo;
            return this;
        }

        public Builder setAutore(String autore){
            this.autore = autore;
            return this;
        }

        public Builder setCodiceISBN(String codice){
            this.codice_ISBN = codice;
            return this;
        }

        public Builder setGenereAppartenenza(String genere){
            this.genere_appartenenza = genere;
            return this;
        }

        public Builder setValutazione(int valutazione){
            this.valutazione = valutazione;
            return this;
        }

        public Builder setStato(Stato stato){
            this.stato = stato;
            return this;
        }

        public Libro build(){
            if(titolo == null || autore == null || codice_ISBN == null){
                throw new IllegalStateException("completa");
            }

            return new Libro(this);
        }

    }




    private Libro(Builder builder){
        titolo = builder.titolo;
        autore = builder.autore;
        codice_ISBN = builder.codice_ISBN;
        genere_appartenenza = builder.genere_appartenenza;
        valutazione = builder.valutazione;
        stato = builder.stato;
    }

}
