import java.util.EventObject; // EventObject è una classe di base per gli eventi in Java (gestisce gli eventi generali).

public class FormEvent extends EventObject {
    // FormEvent estende EventObject per rappresentare eventi specifici del form.

    // Contiene informazioni relative a un evento del form. in questo caso, un evento di creazione o modifica di un prodotto.
    private String nome;
    private double prezzo;
    private String statoProdotto;
    private String percorsoImmagine;
    private String descrizione;
    private String categoria;
    private String utenteCheHaCaricato;

    // Costruttore per inizializzare un evento del form con i dettagli del prodotto.
    public FormEvent(Object source, String nome, double prezzo, String statoProdotto, String percorsoImmagine, String descrizione, String categoria, String utenteCheHaCaricato) {
        super(source);
        this.nome = nome;
        this.prezzo = prezzo;
        this.statoProdotto = statoProdotto;
        this.percorsoImmagine = percorsoImmagine;
        this.descrizione = descrizione;
        this.categoria = categoria;
        this.utenteCheHaCaricato = utenteCheHaCaricato;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public String getStatoProdotto() {
        return statoProdotto;
    }

    public void setStatoProdotto(String statoProdotto) {
        this.statoProdotto = statoProdotto;
    }

    public String getPercorsoImmagine() {
        return percorsoImmagine;
    }

    public void setPercorsoImmagine(String percorsoImmagine) {
        this.percorsoImmagine = percorsoImmagine;
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

    public String getUtenteCheHaCaricato() {
        return utenteCheHaCaricato;
    }

    public void setUtenteCheHaCaricato(String utenteCheHaCaricato){
        this.utenteCheHaCaricato = utenteCheHaCaricato;
    }

    //Perche' stiamo usando questi FormEvent e FormListener?

    /*
        *Separazione delle responsabilità (Separation of Concerns)
        Il form (PannelloCaricaProdotto) si occupa solo dell'interfaccia grafica e della raccolta dei dati.
        La logica applicativa (come salvare il prodotto su file o aggiornarne la lista) è delegata al listener, fuori dal pannello.
        Così il pannello diventa riutilizzabile e indipendente.

        Senza listener: il pannello dovrebbe sapere come gestire un prodotto, dove salvarlo, come aggiornare la lista.
        Con listener: il pannello invia un "evento" e qualcun altro decide cosa farne.

        *Flessibilità
        Lo stesso form può essere usato in contesti diversi, con logiche diverse.
        Esempio: in un’app diversa potresti voler salvare su database invece che su file. Ti basta cambiare il listener, non il form.

        *Estendibilità
        Un giorno potresti voler eseguire più azioni al submit del form (es. salvare, aggiornare UI, inviare notifica).
        Con il pattern listener, puoi aggiungere tutto questo senza toccare la classe form.

        *Modularità
        Rende più facile fare test: puoi testare il form da solo, e il listener da solo.
        Il form diventa un modulo "muto" che notifica i dati. Il comportamento è "plug-in".
    */
}

