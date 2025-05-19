import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;

public class PannelloCarrello extends JPanel {

    private Utente utente;
    private FinestraPrincipale mainFrame;
    private JPanel listaProdotti;
    private JLabel labelTotale;

    public PannelloCarrello(Utente utente, FinestraPrincipale mainFrame) {
        this.utente = utente;
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());

        // indica il carrello dell'utente
        JLabel titolo = new JLabel("Carrello di " + utente.getUsername(), SwingConstants.CENTER);
        titolo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titolo, BorderLayout.NORTH);

        // lista dei prodotti nel carrello dell'utente
        listaProdotti = new JPanel();
        listaProdotti.setLayout(new BoxLayout(listaProdotti, BoxLayout.Y_AXIS));

        // aggiunge un bordo alla lista
        JScrollPane scrollPane = new JScrollPane(listaProdotti);
        add(scrollPane, BorderLayout.CENTER);

        // totale del carrello
        labelTotale = new JLabel();
        labelTotale.setFont(new Font("Arial", Font.BOLD, 16));
        aggiornaLista(); // aggiorna la lista dei prodotti e il totale

        JPanel bottomPanel = new JPanel(new BorderLayout());

        bottomPanel.add(labelTotale, BorderLayout.WEST);

        JButton acquistaTutto = new JButton("Acquista tutto");
        acquistaTutto.addActionListener(e -> acquistaProdotti());
        bottomPanel.add(acquistaTutto, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }


    // aggiorna la lista dei prodotti nel carrello e il totale
    public void aggiornaLista() {
        listaProdotti.removeAll(); // rimuove i prodotti esistenti dalla lista

        double totale = 0; // inizializza il totale a 0
        List<Prodotto> carrello = mainFrame.utenteLoggato.getCatarrello(); // ottiene il carrello dell'utente
        if (carrello.isEmpty()) { // se il carrello è vuoto
            listaProdotti.add(new JLabel("Il carrello è vuoto.")); //dillo
        } else {
            for (Prodotto p : carrello) { // per ogni prodotto nel carrello
                totale += p.getPrezzo(); // aggiungi il prezzo di ognuno di esso al totale

                JPanel panel = new JPanel(new BorderLayout());
                panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); // imposta la larghezza massima del pannello

                JLabel nome = new JLabel(p.getNome() + " - €" + p.getPrezzo()); // mostra il nome e il prezzo del prodotto
                JButton rimuovi = new JButton("Rimuovi"); // bottone per rimuovere il prodotto dal carrello
                rimuovi.addActionListener(e -> { // aggiungi un ActionListener al bottone di rimozione
                    mainFrame.getUtenteLoggato().removeProdottoCarrello(p); // rimuovi il prodotto dal carrello dell'utente
                    ManagerFileUtenti.aggiornaUtente(mainFrame.getUtenteLoggato()); // aggiorna il file degli utenti salvando le modifiche di questo utente
                    aggiornaLista(); // aggiorna la lista dei prodotti
                });

                panel.add(nome, BorderLayout.CENTER);
                panel.add(rimuovi, BorderLayout.EAST);

                listaProdotti.add(panel); // aggiungi il pannello del prodotto alla lista
            }
        }

        labelTotale.setText("Totale: €" + String.format("%.2f", totale)); // mostra il totale del carrello
        listaProdotti.revalidate(); // ricalcola il layout del pannello
        listaProdotti.repaint(); // ridisegna il pannello
    }

    // metodo per acquistare i prodotti nel carrello
    private void acquistaProdotti() {
        List<Prodotto> prodottiNelCarrello = new ArrayList<>(utente.getCatarrello()); // ottiene i prodotti nel carrello dell'utente

        // stream indica che stiamo usando le funzionalità di Java 8, mapToDouble serve per convertire il prezzo di ogni prodotto in un double, sum() calcola la somma totale dei prezzi
        double totale = prodottiNelCarrello.stream().mapToDouble(Prodotto::getPrezzo).sum();

        if (totale > utente.getSaldo()) { // se il totale è maggiore del saldo dell'utente
            JOptionPane.showMessageDialog(this, "Saldo insufficiente."); //dillo
            return;
        }

        int conferma = JOptionPane.showConfirmDialog(this, // mostra un dialog di conferma (si o no)
                "Confermi l'acquisto di tutti i prodotti per €" + String.format("%.2f", totale) + "?",
                "Conferma acquisto", JOptionPane.YES_NO_OPTION);

        if (conferma == JOptionPane.YES_OPTION) { // se l'utente conferma l'acquisto
            // Esegui acquisto
            utente.setSaldo(utente.getSaldo() - totale); // sottrai il totale dal saldo dell'utente
            utente.clearCarrello(); // svuota il carrello dell'utente

            // Chiedi se vuole la fattura
            int sceltaFattura = JOptionPane.showConfirmDialog(this, // mostra un dialog di conferma di scarico fattura(si o no)
                    "Vuoi scaricare la fattura?",
                    "Scarica fattura", JOptionPane.YES_NO_OPTION);

            if (sceltaFattura == JOptionPane.YES_OPTION) { // se l'utente vuole la fattura
                ManagerFileFattura.generaFatturaDaCarrelloTxt(utente, prodottiNelCarrello); // genera la fattura per i prodotti acquistati
            }

            // Salva modifiche utente
            ManagerFileUtenti.aggiornaUtente(utente);

            JOptionPane.showMessageDialog(this, "Acquisto completato!"); // mostra un messaggio di conferma dell'acquisto
            aggiornaLista();
            mainFrame.aggiornaProfiloUtente(); // utile se saldo o storico sono mostrati
        }
    }
}

