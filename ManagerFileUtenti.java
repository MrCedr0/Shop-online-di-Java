import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ManagerFileUtenti{
    private static final String FILE_PATH = "files/utenti.dat"; // percorso del file in cui salvare gli utenti

    public static void salva(List<Utente> utenti) { // salva la lista di utenti su file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) { // crea un ObjectOutputStream per scrivere oggetti su file
            oos.writeObject(utenti); // scrive la lista di utenti nel file
        } catch (IOException e) { // gestisce eventuali eccezioni di input/output
            e.printStackTrace(); // stampa lo stack trace dell'eccezione
        }
    }

    public static List<Utente> carica() { // carica la lista di utenti da file
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) { // crea un ObjectInputStream per leggere oggetti da file
            return (List<Utente>) ois.readObject(); // legge la lista di utenti dal file e la restituisce
        } catch (IOException | ClassNotFoundException e) { // gestisce eventuali eccezioni di input/output o di classe non trovata
            return new ArrayList<>(); // se il file non esiste o è vuoto
        }
    }

    //metodo per aggiornare lo stato di un utente(nel nostro caso quello che sta usando il programma)
    public static void aggiornaUtente(Utente utenteAggiornato) {
        List<Utente> utenti = carica(); // prendi la lista già in memoria
        for (int i = 0; i < utenti.size(); i++) { // scorre la lista di utenti
            if (utenti.get(i).getUsername().equals(utenteAggiornato.getUsername())) { // se trova l'utente da aggiornare
                utenti.set(i, utenteAggiornato); // aggiorna l'utente nella lista
                break; // esce dal ciclo
            }
        }
        salva(utenti); //richiama il metodo salva per salvare la nuova lista aggiornata
    }
}


