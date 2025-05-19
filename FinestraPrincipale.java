import javax.swing.*; // tutte(*) le classi del pacchetto swing
import java.awt.*; // tutte(*) le classi del pacchetto awt (per layout, eventi, ecc.)
import java.awt.event.ActionEvent; // per gestire gli eventi dei pulsanti
import java.util.ArrayList; // per utilizzare ArrayList
import java.util.List; // per utilizzare List ( interfaccia di ArrayList)

public class FinestraPrincipale extends JFrame {

    //lista di prodotti e utenti caricati da file, presi da ManagerFileProdotti e ManagerFileUtenti
    private static List<Prodotto> elencoProdotti = ManagerFileProdotti.carica(); // Carica i prodotti da file
    private List<Utente> elencoUtenti = UtenteManager.getUtenti(); // Carica gli utenti da file

    // Utente loggato, inizialmente vuoto
    Utente utenteLoggato = new Utente("", "", 0, new ArrayList<>(), new ArrayList<>());

    //gestione delle schermate usando un CardLayout
    private CardLayout cardLayout;
    /*
        Il CardLayout in Java è un layout manager che ti permette di gestire più pannelli (card)
        all'interno di uno stesso contenitore, mostrando uno alla volta.
        È utile quando vuoi cambiare "vista" nella stessa finestra, come ad esempio passare da una schermata di login a una schermata di catalogo prodotti.
        Puoi pensarlo come un mazzo di carte: ogni carta è un pannello e solo una carta (pannello) è visibile alla volta.
    */
    private JPanel pannelloContenitore; // contenitore principale per i pannelli

    //pannelli principali
    private PannelloCarrello pannelloCarrello;
    private PannelloProfiloUtente pannelloProfilo;
    private CatalogoProdottiPanel pannelloCatalogo;

    //pannello di navigazione con i suoi pulsanti
    private static JPanel barraNavigazione;
    private JButton btnCatalogo;
    private JButton btnCarica;
    private JButton btnProfilo;
    private JButton btnCarrello;

    public FinestraPrincipale() {
        super("Negozio Online"); // titolo della finestra
        setDefaultCloseOperation(EXIT_ON_CLOSE); // chiude l'applicazione quando si chiude la finestra
        setSize(900, 600); // dimensioni della finestra
        setLocationRelativeTo(null); // centra la finestra sullo schermo

        //JScrollPane scrollPaneCatalogo = new JScrollPane(pannelloCatalogo) (non farci caso);

        // Inizializza il CardLayout e il pannello contenitore
        cardLayout = new CardLayout();
        pannelloContenitore = new JPanel(cardLayout);

        // Carica i prodotti e gli utenti da file (DEBUG)
        for (Utente u : elencoUtenti) {
            System.out.println("Utente caricato: " + u.getUsername() + " | Saldo: " + u.getSaldo());
        }

        // pannelli iniziali per registrazione e/o login
        PannelloAccessoUtente pannelloLogin = new PannelloAccessoUtente(this);
        PannelloRegistrazioneUtente pannelloRegistrazione = new PannelloRegistrazioneUtente(this);

        // pannello per caricare i prodotti
        PannelloCaricaProdotto pannelloCaricaProdotto = new PannelloCaricaProdotto(this, elencoProdotti);
        JScrollPane scrollPaneCarica = new JScrollPane(pannelloCaricaProdotto); // JScrollPane per il pannello di caricamento permettendo lo scroll verticale
        scrollPaneCarica.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // solo scroll verticale
        scrollPaneCarica.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // scroll verticale solo se necessario

        //listener per il pannello di caricamento prodotto
        pannelloCaricaProdotto.setFormListener(new FormListener() {
            @Override
            public void formEventListener(FormEvent event) {
                Prodotto prodotto = new Prodotto(
                        event.getNome(),
                        event.getDescrizione(),
                        event.getCategoria(),
                        event.getPercorsoImmagine(),
                        event.getPrezzo(),
                        event.getUtenteCheHaCaricato(),
                        event.getStatoProdotto()
                );

                elencoProdotti.add(prodotto); // aggiungi alla lista
                ManagerFileProdotti.salva(elencoProdotti); // salva su file

                System.out.println("Prodotto salvato: " + prodotto.getNome()); //DEBUG
            }
        });

        // Aggiungi ogni pannello al contenitore
        //comprende nome del pannello creato e il nome a cui si fara' riferimento
        pannelloContenitore.add(pannelloLogin, "login");
        pannelloContenitore.add(pannelloRegistrazione, "registrazione");
        pannelloContenitore.add(scrollPaneCarica, "carica");

        // Barra inferiore con pulsanti di navigazione
        barraNavigazione = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Creazione e settings del pulsante "Catalogo"
        btnCatalogo = new JButton("Catalogo"); //cosa c'e' scritto nel pulsante
        btnCatalogo.addActionListener(e ->{ //action listener per il pulsante
            btnCarica.setEnabled(true); //lascia tutti i bottoni attivi tranne quello cliccato
            btnProfilo.setEnabled(true);
            btnCatalogo.setEnabled(false);
            btnCarrello.setEnabled(true);

            aggiornaProfiloUtente(); // aggiorna il profilo utente (metodo definito sotto)
            cardLayout.show(pannelloContenitore, "catalogo"); // mostra il pannello catalogo
        });

        // Creazione e settings del pulsante "Carica Prodotto" (la logica è la stessa di sopra)
        btnCarica = new JButton("Carica Prodotto");
        btnCarica.addActionListener(e ->{
            btnCatalogo.setEnabled(true);
            btnProfilo.setEnabled(true);
            btnCarica.setEnabled(false);
            btnCarrello.setEnabled(true);
            cardLayout.show(pannelloContenitore, "carica");
        });

        // Creazione e settings del pulsante "Carrello" (la logica è la stessa di sopra)
        btnCarrello = new JButton("Carrello");
        btnCarrello.addActionListener(e ->{
            btnCatalogo.setEnabled(true);
            btnCarica.setEnabled(true);
            btnProfilo.setEnabled(true);
            btnCarrello.setEnabled(false);

            aggiornaCarrello();
            cardLayout.show(pannelloContenitore, "carrello");
        });

        // Creazione e settings del pulsante "Profilo" (la logica è la stessa di sopra)
        btnProfilo = new JButton("Profilo");
        btnProfilo.setEnabled(false); // Inizialmente disabilitato
        btnProfilo.addActionListener(e ->{
            btnCatalogo.setEnabled(true);
            btnCarica.setEnabled(true);
            btnProfilo.setEnabled(false);
            btnCarrello.setEnabled(true);
            cardLayout.show(pannelloContenitore, "profilo");
        });

        barraNavigazione.add(btnCatalogo); // aggiungi il pulsante catalogo nella barra di navigazione
        barraNavigazione.add(btnCarica); // aggiungi il pulsante carica prodotto nella barra di navigazione
        barraNavigazione.add(btnCarrello); // aggiungi il pulsante carrello nella barra di navigazione
        barraNavigazione.add(btnProfilo); // aggiungi il pulsante profilo nella barra di navigazione
        barraNavigazione.setVisible(false); // Nascondi inizialmente la barra di navigazione

        // Layout generale finestra
        setLayout(new BorderLayout());
        add(pannelloContenitore, BorderLayout.CENTER); // aggiungi il pannello contenitore al centro della finestra
        add(barraNavigazione, BorderLayout.SOUTH); // aggiungi la barra di navigazione in basso allo schermo

        // Mostra la schermata iniziale
        cardLayout.show(pannelloContenitore, "login");

        setVisible(true); // mostra la finestra (senza questo non si vede nulla)
    }

    public void mostraSchermata(String nome) {
        cardLayout.show(pannelloContenitore, nome); //metodo per chiamare il pannello che si deve visualizzare
        //prende il nome del pannello e lo mostra ovvero i nomi associati in alto all' inizio
    }

    ///al momento che l'utente accede, avviene questo metodo
    public void setUtenteLoggato(String nome) { // metodo per settare l'utente loggato
        for (Utente u : elencoUtenti) { // scorre tutta la lista di utenti
            if (u.getUsername().equals(nome)) { // se l'utente loggato (in base al nome, quindi gia' con un account) è presente nella lista
                this.utenteLoggato = u; // setta l'utente loggato
                break; //interrompe il ciclo per risparmiare tempo
            }
        }

        System.out.println( "DEBUG - Utente loggato: " + utenteLoggato.getUsername()
        + " | Saldo: " + utenteLoggato.getSaldo()); //DEBUG


        //rimuove e aggiunge i pannelli per evitare duplicati e per garantire l'aggiornamento dei dati(prodotti, saldo utente ecc...)
        if (pannelloCarrello != null) pannelloContenitore.remove(pannelloCarrello);
        if (pannelloProfilo != null) pannelloContenitore.remove(pannelloProfilo);
        if (pannelloCatalogo != null) pannelloContenitore.remove(pannelloCatalogo);

        pannelloCarrello = new PannelloCarrello(utenteLoggato, this);
        pannelloProfilo = new PannelloProfiloUtente(utenteLoggato, elencoProdotti, this);
        pannelloCatalogo = new CatalogoProdottiPanel(elencoProdotti, utenteLoggato, this);

        // Aggiungi i pannelli al contenitore
        pannelloContenitore.add(pannelloCarrello, "carrello");
        pannelloContenitore.add(pannelloProfilo, "profilo");
        pannelloContenitore.add(pannelloCatalogo, "catalogo");

        // al momento dell'accesso, ti porta direttamente al profilo
        cardLayout.show(pannelloContenitore, "profilo");
    }

    //metodo per ritornare l'utente loggato (in modo da poterlo usare in altri pannelli)
    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }

    //metodo per mostrare la barra di navigazione
    public void mostraBarraDiNavigazione() {
        barraNavigazione.setVisible(true);
    }

    //metodo per rimuovere un prodotto dalla lista e salvarlo su file
    public void rimuoviProdotto(Prodotto prodotto) {
        elencoProdotti.remove(prodotto);
        ManagerFileProdotti.salva(elencoProdotti); // Salva la lista aggiornata su file
    }

    //metodo per ritornare l'elenco dei prodotti
    public List<Prodotto> getElencoProdotti() {
        return elencoProdotti;
    }

    //metodo per ritornare il pannello del catalogo(non piu' necessario)
    public JPanel getCatalogoPanel() {
        return (JPanel) pannelloContenitore.getComponent(2);
    }

    //metodo per aggiornare il carrello
    public void aggiornaCarrello() {
        if (pannelloCarrello != null) {
            pannelloContenitore.remove(pannelloCarrello); // rimuove il pannello carrello atuale
        }
        pannelloCarrello = new PannelloCarrello(utenteLoggato, this); // lo ricrea
        pannelloContenitore.add(pannelloCarrello, "carrello"); // lo riaggiunge al contenitore
        pannelloContenitore.revalidate(); // rende il pannello di nuovo visibile
        pannelloContenitore.repaint(); // aggiorna i componenti visivi (scritte, colori, oggetti ecc...)
    }

    //metodo per aggiornare il profilo utente
    public void aggiornaProfiloUtente() {
        elencoUtenti = ManagerFileUtenti.carica(); // Ricarica la lista di utenti da file
        for (Utente u : elencoUtenti) { // scorre la lista di utenti
            if (u.getUsername().equals(utenteLoggato.getUsername())) { // se l'utente loggato è presente nella lista
                this.utenteLoggato = u; // aggiorna l'utente loggato
                break;
            }
        }

        if (pannelloProfilo != null) { // se il pannello profilo esiste già
            pannelloContenitore.remove(pannelloProfilo); // rimuovilo
        }
        pannelloProfilo = new PannelloProfiloUtente(utenteLoggato, elencoProdotti, this); // ricrealo
        pannelloContenitore.add(pannelloProfilo, "profilo"); //riaggiungilo al contenitore
        pannelloContenitore.revalidate(); // rende il pannello di nuovo visibile
        pannelloContenitore.repaint(); // aggiorna i componenti visivi (scritte, colori, oggetti ecc...)
    }
}