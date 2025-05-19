import java.io.Serializable;
import java.util.List;
import java.util.Objects;

//serializable è un'interfaccia marker che indica che la classe può essere serializzata( ovvero convertita in un flusso di byte), nel nostro caso per salvare i prodotti su file
public class Utente implements Serializable {

    //serialVersionUID è un identificatore univoco per la serializzazione, serve a garantire che la classe sia compatibile con la versione serializzata
    // se non viene specificato, il compilatore genera un valore di default che potrebbe cambiare tra diverse versioni della classe
    // questo potrebbe causare un'eccezione di serializzazione se si prova a deserializzare un oggetto di una versione diversa della classe
    // quindi puo' portare alla corruzione dei dati del file, causando errori o comportamenti imprevisti, ad esempio, tutti i dati potrebbero essere null
    // (nessun utente esiste) o potrebbero essere letti in modo errato
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private double saldo;
    private List<Prodotto> prodottiCaricati;
    private List<Prodotto> carrello;

    public Utente(String username, String password, double saldo, List<Prodotto> prodottiCaricati, List<Prodotto> carrello) {
        this.username = username;
        this.password = password;
        this.saldo = saldo;
        this.prodottiCaricati = prodottiCaricati;
        this.carrello = carrello;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public boolean checkPassword(String pw) { return this.password.equals(pw); }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public List<Prodotto> getProdottiCaricati() {
        return prodottiCaricati;
    }

    public void setProdottiCaricati(List<Prodotto> prodottiCaricati) {
        this.prodottiCaricati = prodottiCaricati;
    }

    public List<Prodotto> getCatarrello() {
        return carrello;
    }

    public void setCatarrello(List<Prodotto> catarrello) {
        this.carrello = catarrello;
    }

    public void addProdottoCaricato(Prodotto prodotto) {
        this.prodottiCaricati.add(prodotto);
    }

    public void removeProdottoCaricato(Prodotto prodotto) {
        this.prodottiCaricati.remove(prodotto);
    }

    public void addProdottoCarrello(Prodotto prodotto) {
        this.carrello.add(prodotto);
    }

    public void removeProdottoCarrello(Prodotto prodotto) {
        this.carrello.remove(prodotto);
    }

    public void clearCarrello() {
        this.carrello.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Utente utente = (Utente) o;
        return Double.compare(saldo, utente.saldo) == 0 && Objects.equals(username, utente.username) && Objects.equals(password, utente.password) && Objects.equals(prodottiCaricati, utente.prodottiCaricati) && Objects.equals(carrello, utente.carrello);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, saldo, prodottiCaricati, carrello);
    }
}
