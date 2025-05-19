import java.util.ArrayList;
import java.util.List;

public class UtenteManager {
    private static List<Utente> utenti = ManagerFileUtenti.carica();
    private static Utente utenteLoggato = new Utente("", "", 0, new ArrayList<>(), new ArrayList<>());

    static {
        System.out.println("Utenti caricati: " + utenti.size());
        for(Utente u : utenti) {
            System.out.println("Utente: " + u.getUsername() + " - " + u.getPassword());
        }
    }

    public static boolean nomeUtenteEsiste(String nome) {
        return utenti.stream().anyMatch(u -> u.getUsername().equals(nome));
    }

    public static boolean registraUtente(String nome, String password) {
        if (nomeUtenteEsiste(nome)) return false;

        double saldo = 5000.0;
        Utente nuovoUtente = new Utente(nome, password, saldo, new ArrayList<>(), new ArrayList<>());

        utenti.add(nuovoUtente); // aggiorna la lista in memoria
        ManagerFileUtenti.salva(utenti); // salva su file

        System.out.println("Lista utenti aggiornata in memoria:");
        for (Utente u : utenti) {
            System.out.println("-> " + u.getUsername());
        }

        return true;
    }

    public static boolean verificaCredenziali(String nome, String password) {

        System.out.println("Verifica accesso. Utenti presenti:");
        for (Utente u : utenti) {
            System.out.println("-> " + u.getUsername());
        }

        for (Utente u : utenti) {
            //System.out.println("Controllo utente: " + u.getUsername());
            if (u.getUsername().equals(nome) && u.getPassword().equals(password)) {
                utenteLoggato.setUsername(nome);
                //System.out.println("Utente verificato: " + nome);
                return true;
            }
        }
        return false;
    }

    public static Utente getUtenteLoggato() {
        return utenteLoggato;
    }

    public static void setUtenteLoggato(String utente) {
        utenteLoggato.setUsername(utente);
    }

    public static List<Utente> getUtenti() {
        return utenti;
    }
}


