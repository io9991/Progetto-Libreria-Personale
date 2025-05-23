//creazione del libro

public class Libro {

    private String titolo;
    private String autore;
    private String codice_ISBN;
    private String genere_appartenenza;
    private int valutazione;
    private Stato stato;


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
