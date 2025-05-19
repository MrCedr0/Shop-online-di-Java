/*
    la classe si occupa della persistenza dei dati della lista di prodotti,
    cioè li salva su disco e li ricarica quando serve.
*/

import java.io.*; //libreria per la gestione dei file
import java.util.ArrayList; //libreria per le liste
import java.util.List; //libreria per le liste

public class ManagerFileProdotti {

    private static final String FILE_PATH = "files/prodotti.dat"; // percorso del file in cui salvare i prodotti
    // il percorso è relativo alla cartella del progetto, quindi "files/prodotti.dat" si riferisce a "OnlineShop/files/prodotti.dat"
    // questo permette di mantenere il codice portabile, senza dipendere da percorsi assoluti(direttamente dalla sorgente)
    // che potrebbero variare da un sistema all'altro.

    // metodo per salvare la lista di prodotti su file
    public static void salva(List<Prodotto> prodotti) { //prende in input una lista di prodotti
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) { // crea un ObjectOutputStream per scrivere oggetti su file, nel file specificato(sopra)
            out.writeObject(prodotti); // scrive la lista di prodotti nel file
        } catch (IOException e) { // gestisce eventuali eccezioni di input/output
            e.printStackTrace(); // stampa lo stack trace dell'eccezione(le informazioni sull'errore)
        }
    }

    @SuppressWarnings("unchecked") // sopprime l'avviso di tipo non controllato( ovvero se il cast è sicuro o meno)
    public static List<Prodotto> carica() { // metodo per caricare la lista di prodotti da file
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH))) { // crea un ObjectInputStream per leggere oggetti da file
            return (List<Prodotto>) in.readObject(); // legge la lista di prodotti dal file e la restituisce
        } catch (IOException | ClassNotFoundException e) { // gestisce eventuali eccezioni di input/output o di classe non trovata
            return new ArrayList<>(); // restituisce una lista vuota se si verifica un'eccezione
        }
    }
}
