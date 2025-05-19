import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class CatalogoProdottiPanel extends JPanel {

    private JTextField campoRicerca;
    private JComboBox<String> filtroCategoria; // combo box per le categorie
    private JComboBox<String> ordinePrezzo; // combo box con le opzioni di ordinamento

    /*
        La JComboBox è una lista a cascata che permette di selezionare un'opzione da un elenco predefinito.
    */

    private JTextField campoPrezzoMin;
    private JTextField campoPrezzoMax;
    private JLabel labelPrezzoDa;
    private JLabel labelPrezzoA;

    private JButton bottoneFiltra;
    private JLabel labelCatalogoVuoto;

    private JButton bottoneDettaglio;

    private JLabel labelNomeProdotto;
    private JLabel labelPrezzoProdotto;
    private JLabel labelCaricatoDa;

    private JPanel pannelloProdotti;

    private List<Prodotto> prodottiTotali; // tutti i prodotti da cui filtrare

    private FinestraPrincipale mainFrame;

    public CatalogoProdottiPanel(List<Prodotto> prodottiIniziali, Utente utenteLoggato, FinestraPrincipale mainFrame) {
        this.prodottiTotali = prodottiIniziali;
        this.mainFrame = mainFrame;

        if(prodottiTotali.isEmpty()) { // se non ci sono prodotti
            labelCatalogoVuoto = new JLabel("Nessun prodotto disponibile."); // dillo
            labelCatalogoVuoto.setHorizontalAlignment(SwingConstants.CENTER);
            add(labelCatalogoVuoto);
        } else {
            labelCatalogoVuoto = null;
        }

        setLayout(new BorderLayout());

        // ---------------- Barra di filtro ----------------
        JPanel barraSuperiore = new JPanel();
        barraSuperiore.setLayout(new FlowLayout(FlowLayout.LEFT));

        campoRicerca = new JTextField(10);
        filtroCategoria = new JComboBox<>(new String[]{ // combo box per le categorie
                "Tutte", "Elettronica", "Abbigliamento", "Casa", "Sport",
                "Giocattoli", "Libri", "accessori", "Veicoli", "Armi", "Musica", "Hot"
        });

        //campo per il prezzo
        labelPrezzoDa = new JLabel("Prezzo da:");
        campoPrezzoMin = new JTextField(5);
        labelPrezzoA = new JLabel("a:");
        campoPrezzoMax = new JTextField(5);

        // Combo box con le opzioni di ordinamento
        ordinePrezzo = new JComboBox<>(new String[]{"Prezzo: Basso -> Alto", "Prezzo: Alto -> Basso","Prezzo: prezzo A -> prezzo B"});
        ordinePrezzo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean mostraCampiPrezzo = ordinePrezzo.getSelectedItem().toString().equals("Prezzo: prezzo A -> prezzo B");

                // Mostra o nascondi i campi di prezzo in base alla selezione

                labelPrezzoDa.setVisible(mostraCampiPrezzo); // mostraCampiPrezzo puo essere true o false in base a quando viene selezionata nella ComboBox
                campoPrezzoMin.setVisible(mostraCampiPrezzo);
                labelPrezzoA.setVisible(mostraCampiPrezzo);
                campoPrezzoMax.setVisible(mostraCampiPrezzo);

                if (!mostraCampiPrezzo) { // se non e' selezionata l'opzione "Prezzo: prezzo A -> prezzo B"
                    // Nascondi i campi di prezzo
                    labelPrezzoDa.setVisible(mostraCampiPrezzo);
                    campoPrezzoMin.setVisible(mostraCampiPrezzo);
                    labelPrezzoA.setVisible(mostraCampiPrezzo);
                    campoPrezzoMax.setVisible(mostraCampiPrezzo);
                    campoPrezzoMin.setText("");
                    campoPrezzoMax.setText("");
                }
            }
        });


        // i filtri per i campi di testo
        ((AbstractDocument) campoPrezzoMin.getDocument()).setDocumentFilter(new PrezzoFilter());
        ((AbstractDocument) campoPrezzoMax.getDocument()).setDocumentFilter(new PrezzoFilter());

        labelPrezzoDa.setVisible(false); // all inizio non sono visibili i componenti dell'intervallo di prezzo
        campoPrezzoMin.setVisible(false);
        labelPrezzoA.setVisible(false);
        campoPrezzoMax.setVisible(false);

        bottoneFiltra = new JButton("Aggiorna");

        barraSuperiore.add(new JLabel("Cerca:"));
        barraSuperiore.add(campoRicerca);
        barraSuperiore.add(new JLabel("Categoria:"));

        // aggiungo i componenti alla barra superiore
        barraSuperiore.add(filtroCategoria);
        barraSuperiore.add(labelPrezzoDa);
        barraSuperiore.add(campoPrezzoMin);
        barraSuperiore.add(labelPrezzoA);
        barraSuperiore.add(campoPrezzoMax);
        barraSuperiore.add(ordinePrezzo);
        barraSuperiore.add(bottoneFiltra);

        add(barraSuperiore, BorderLayout.NORTH); // aggiungo la barra superiore in alto al pannello

        // ---------------- Pannello centrale prodotti ----------------
        pannelloProdotti = new JPanel();
        pannelloProdotti.setLayout(new GridLayout(10, 4, 2, 2)); // 1 colonne

        JScrollPane scrollPane = new JScrollPane(pannelloProdotti);
        add(scrollPane, BorderLayout.CENTER);

        bottoneFiltra.addActionListener(e -> filtraProdotti());

        aggiornaCatalogo(prodottiTotali, this.mainFrame, utenteLoggato);
    }

    public void aggiornaCatalogo(List<Prodotto> prodotti, FinestraPrincipale mainFrame, Utente utenteLoggato) {
        pannelloProdotti.removeAll(); // rimuove i prodotti esistenti dalla lista

        for (Prodotto p : prodotti) { // per ogni prodotto nella lista
            JPanel card = new JPanel(new BorderLayout(5, 5));
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY));// aggiunge un bordo grigio
            card.setBackground(Color.WHITE);// sfondo bianco
            card.setPreferredSize(new Dimension(250, 120)); // dimensioni del pannello

            // Immagine (posta a sinistra)
            JLabel immagine = new JLabel();
            if (p.getPercorsoImmagine() != null && !p.getPercorsoImmagine().isEmpty()) {
                ImageIcon icon = new ImageIcon(p.getPercorsoImmagine());
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                immagine.setIcon(new ImageIcon(img));
            } else {
                immagine.setPreferredSize(new Dimension(100, 100));
            }
            card.add(immagine, BorderLayout.WEST);

            // Info centrale
            JPanel infoCentro = new JPanel();
            infoCentro.setLayout(new BoxLayout(infoCentro, BoxLayout.Y_AXIS));
            infoCentro.setBackground(Color.WHITE);

            JLabel nomeLabel = new JLabel(p.getNome());
            nomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

            JLabel creatoreLabel = new JLabel("Caricato da: " + p.getUtenteCheHaCaricato());
            creatoreLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            creatoreLabel.setForeground(Color.DARK_GRAY);

            JLabel categoriaLabel = new JLabel("Categoria: " + p.getCategoria());
            categoriaLabel.setFont(new Font("Arial", Font.PLAIN, 12));

            bottoneDettaglio = new JButton("Dettagli"); // bottone per i dettagli per ogni prodotto
            bottoneDettaglio.addActionListener(new ActionListener() { // aggiungo un ActionListener al bottone di dettaglio
                @Override
                public void actionPerformed(ActionEvent e) {
                    // mostra i dettagli del prodotto in una finestra di dialogo
                    // il JDialog e' una finestra di dialogo modale(come un alert) che blocca l'interazione con la finestra principale
                    // JDialog((Frame) SwingUtilities.getWindowAncestor indica la finestra padre, ovvero quella principale( CatalogoProdottiPanel.this)
                    JDialog finestraDettagli = new JDialog((Frame) SwingUtilities.getWindowAncestor(CatalogoProdottiPanel.this),
                            "Dettagli Prodotto", true);
                    finestraDettagli.setContentPane(new PannelloDettagliProdotto(p, utenteLoggato, mainFrame, CatalogoProdottiPanel.this));
                    finestraDettagli.pack();// dimensiona la finestra in base al contenuto
                    finestraDettagli.setLocationRelativeTo(CatalogoProdottiPanel.this); // centra la finestra rispetto alla finestra principale
                    finestraDettagli.setVisible(true); // mostra la finestra
                }
            });

            infoCentro.add(nomeLabel);
            infoCentro.add(creatoreLabel);
            infoCentro.add(Box.createVerticalStrut(4));
            infoCentro.add(categoriaLabel);
            infoCentro.add(bottoneDettaglio);

            card.add(infoCentro, BorderLayout.CENTER);

            // Prezzo (dx)
            JLabel prezzoLabel = new JLabel("€" + String.format("%.2f", p.getPrezzo()));
            prezzoLabel.setFont(new Font("Arial", Font.BOLD, 18));
            prezzoLabel.setForeground(new Color(0, 128, 0));
            prezzoLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            JPanel prezzoPanel = new JPanel(new BorderLayout());
            prezzoPanel.setBackground(Color.WHITE);
            prezzoPanel.add(prezzoLabel, BorderLayout.NORTH);
            card.add(prezzoPanel, BorderLayout.EAST);

            pannelloProdotti.add(card);
        }

        pannelloProdotti.revalidate(); // ricalcola il layout del pannello
        pannelloProdotti.repaint(); // ridisegna il pannello
    }

    // Metodo per filtrare i prodotti in base ai criteri selezionati
    private void filtraProdotti() {
        // ricerca per nome
        // il .trim() rimuove gli spazi iniziali e finali, toLowerCase() converte tutto in minuscolo
        String ricercaNome = campoRicerca.getText().trim().toLowerCase();

        // ricerca per categorie selezionate
        String categoria = filtroCategoria.getSelectedItem().toString();

        // ricerca per prezzo in base alla selezione dell'utente convertendo i campi di testo
        String ordine = ordinePrezzo.getSelectedItem().toString();

        double prezzoMin = 0;
        double prezzoMax = Double.MAX_VALUE; // inizializza a un valore molto alto

        try {
            if (!campoPrezzoMin.getText().isEmpty()) { // se il campo non e' vuoto
                prezzoMin = Double.parseDouble(campoPrezzoMin.getText()); // converte il testo in un numero
            }
            if (!campoPrezzoMax.getText().isEmpty()) { // se il campo non e' vuoto
                prezzoMax = Double.parseDouble(campoPrezzoMax.getText()); // converte il testo in un numero
            }
        } catch (NumberFormatException e) { // se il campo non e' un numero
            JOptionPane.showMessageDialog(this, "Inserire prezzi validi."); // dillo
            return;
            // questa eccezione non dovrebbe mai verificarsi grazie al filtro di prezzo inizializzato in alto
        }

        double finalPrezzoMin = prezzoMin; // variabile finale per l'uso nel lambda (finale perche non puo essere modificata)
        double finalPrezzoMax = prezzoMax;
        List<Prodotto> filtrati = prodottiTotali.stream() // inizia lo stream per filtrare i prodotti
                // il .filter() filtra i prodotti in base ai criteri selezionati
                // il .sorted() ordina i prodotti in base al prezzo
                // il ,contains() controlla se il nome del prodotto contiene la stringa di ricerca (in pratica gli basta che contenga una parte del nome, anche se non e' esatto)
                .filter(p -> p.getNome().toLowerCase().contains(ricercaNome)) // filtra per nome, la ricerca e' case insensitive
                .filter(p -> categoria.equals("Tutte") || checkContieneCategoria(p.getCategoria(), categoria))
                .filter(p -> {
                    try {
                        double prezzo = p.getPrezzo();
                        return (prezzo >= finalPrezzoMin && prezzo <= finalPrezzoMax);
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .sorted((p1, p2) -> { // ordina i prodotti dal prezzo piu' basso al piu' alto
                    try {
                        double prezzo1 = p1.getPrezzo();
                        double prezzo2 = p2.getPrezzo();
                        return ordine.equals("Prezzo: Basso -> Alto") ? Double.compare(prezzo1, prezzo2) : Double.compare(prezzo2, prezzo1);
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .collect(Collectors.toList()); // il .collect(Collectors.toList()) raccoglie i risultati in una lista

        aggiornaCatalogo(filtrati, mainFrame, mainFrame.getUtenteLoggato()); // aggiorna il catalogo con i prodotti filtrati dopo tutti questi filtri
    }

    // Metodo per controllare se una categoria e' presente in una stringa di categorie
    private boolean checkContieneCategoria(String categorieProdotto, String categoriaFiltro) {
        String[] categorie = categorieProdotto.split(","); // splitto la stringa in base alla virgola quando ci sono piu' categorie
        for (String c : categorie) { // per ogni categoria
            if (c.trim().equalsIgnoreCase(categoriaFiltro)) { // trim() rimuove gli spazi iniziali e finali, equalsIgnoreCase() controlla se sono uguali senza considerare il case
                return true; // se la categoria e' presente allora restituisco true
            }
        }
        return false; // altrimenti restituisco false
    }
}