package builder;

/*
affido la creazione del libro al design pattern builder,
dato che offre la possibilità di funzionare bene come costruttore
quando si hanno più parametri e non tutti sono obbligatori
 */

public class Libro {

    private String titolo;
    private String autore;
    private String codice_ISBN;
    private String genere_appartenenza;
    private int valutazione;
    private Stato stato;

    /*
        metodi get e set, al cui interno non inserisco il set dell'isbn che
        non sarà modificabile per scelta di progettazione
     */

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


    public void setGenere_appartenenza(String genere_appartenenza) {
        this.genere_appartenenza = genere_appartenenza;
    }

    /*
        la valutazione come richiesto dovrà essere tra 0 e 5
        dunque se viene indicata una valutazione non conforme
        alle direttive allora si solleva una eccezione
     */

    public void setValutazione(int valutazione) {
        if (valutazione >= 0 && valutazione <= 5) {
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
        /*
            se valutazione e stato non vengono specificati
            inserisco come parametri di default 0 per la valutazione
            e DA_LEGGERE per lo stato di lettura
         */
        private int valutazione = 0;
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

        /*
            per scelte progettuali un libro non può essere aggiunto e
            tenuto in considerazione dal momento in cui non presenta
            tutti e tre i campi : titolo, autore e codiceISBN compilati
         */

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
