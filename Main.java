import javax.swing.*; //tutte(*) le calssi del pacchetto swing

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            // invokeLater assicura che il codice venga eseguito nel thread della GUI (Event Dispatch Thread).
            // Questo è importante per evitare problemi di concorrenza e mantenere la GUI reattiva.

            @Override
            public void run() {
                new FinestraPrincipale();
                // Crea e visualizza una nuova finestra principale dell'applicazione.
            }
        });
    }
}