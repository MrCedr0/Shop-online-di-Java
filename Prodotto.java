import java.io.Serializable;

//serializable è un'interfaccia marker che indica che la classe può essere serializzata( ovvero convertita in un flusso di byte), nel nostro caso per salvare i prodotti su file
public class Prodotto implements Serializable {
    private String nome;
    private String descrizione;
    private String categoria;
    private String percorsoImmagine;
    private double prezzo;
    private String utenteCheHaCaricato;
    private String statoProdotto;

    public Prodotto(String nome, String descrizione, String categoria, String percorsoImmagine, double prezzo, String utenteCheHaCaricato, String statoProdotto) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.categoria = categoria;
        this.percorsoImmagine = percorsoImmagine;
        this.prezzo = prezzo;
        this.utenteCheHaCaricato = utenteCheHaCaricato;
        this.statoProdotto = statoProdotto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPercorsoImmagine() {
        return percorsoImmagine;
    }

    public void setPercorsoImmagine(String percorsoImmagine) {
        this.percorsoImmagine = percorsoImmagine;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public String getUtenteCheHaCaricato() {
        return utenteCheHaCaricato;
    }

    public void setUtenteCheHaCaricato(String utenteCheHaCaricato) {
        this.utenteCheHaCaricato = utenteCheHaCaricato;
    }

    public String getStatoProdotto() {
        return statoProdotto;
    }

    public void setStatoProdotto(String statoProdotto) {
        this.statoProdotto = statoProdotto;
    }
}

