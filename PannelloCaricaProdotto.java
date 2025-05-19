import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.List;

public class PannelloCaricaProdotto extends JPanel {

    /*
        INFO GENERALI SULLE COSE UTILIZZATE
        - GridBagLayout: layout manager che permette di posizionare i componenti in una griglia
        - GridBagConstraints: classe che definisce le restrizioni per i componenti in un GridBagLayout
        - JLabel: etichetta per visualizzare del testo
        - JButton: bottone per l'interazione dell'utente
        - JPanel: pannello per raggruppare i componenti
        - border: bordo per i pannelli
        - JTextField: campo di testo per l'input del prezzo/nome/descrizione/immagine(vedrai in seguito)
        - JRadioButton: bottone radio per selezionare lo stato del prodotto
            Il radio button è un componente grafico che consente di selezionare una sola opzione tra un gruppo di opzioni.
        - ButtonGroup: classe che raggruppa i radio button per garantire che solo uno sia selezionato alla volta
        - JCheckBox: casella di controllo per selezionare le categorie del prodotto
            La checkbox è un componente grafico che consente di selezionare più opzioni tra un gruppo di opzioni.
        - ImageIcon: classe per gestire le immagini
        - JFileChooser: classe per selezionare un file dal file system
        - File: classe per gestire i file
        - FileNameExtensionFilter: classe per filtrare i file in base all'estensione
        - StandardCopyOption: classe per gestire le opzioni di copia dei file
        - JScrollPane: classe per aggiungere una barra di scorrimento a un componente
        - Dimension: classe per gestire le dimensioni dei componenti
        - Color: classe per definire i colori
        - Insets: classe per definire i margini tra i componenti( distanza tra i componenti in px)
        - JOptionPane: classe per visualizzare finestre di dialogo
        - ActionListener: interfaccia per gestire gli eventi dei componenti
        - FormListener: interfaccia per gestire gli eventi del form
        - FormEvent: classe per gestire gli eventi del form
        - Prodotto: classe per gestire i prodotti
        - FinestraPrincipale: classe per gestire la finestra principale dell'applicazione
        - PrezzoFilter: classe per filtrare i caratteri inseriti nel campo prezzo
        - Utente: classe per gestire gli utenti
        - UtenteManager: classe per gestire gli utenti
        - ManagerFileProdotti: classe per gestire i file dei prodotti
        - ManagerFileUtenti: classe per gestire i file degli utenti
        - ManagerFileFattura: classe per gestire i file delle fatture
        - PannelloCaricaProdotto: classe per gestire il pannello di caricamento del prodotto
    */

    private JLabel labelNome;
    private JTextField campoNome;

    private JLabel labelPrezzo;
    private JTextField campoPrezzo;

    private JLabel labelStatoProdotto;
    private JRadioButton usato;
    private JRadioButton nuovo;
    private ButtonGroup gruppoRadioUsatoNuovo;

    private JLabel labelImmagine;
    private JTextField campoImmagine;
    private JButton bottoneSfoglia;
    private JLabel anteprimaImmagine;

    private JLabel labelCategoria;
    private JCheckBox[] categorieProdotto;
    private String[] categorie = { // categorie del prodotto generali(se ne servono altre basta aggiungerle qui)
            "Elettronica", "Abbigliamento", "Casa", "Cibo", "Sport",
            "Giocattoli", "Libri", "accessori", "Veicoli", "Armi", "Musica", "Hot"
    };

    private JLabel descrizione;
    private JTextArea campoDescrizione;
    private JScrollPane scrollDescrizione;

    private JButton bottoneAggiungi;

    private FormListener formListener;

    public PannelloCaricaProdotto(FinestraPrincipale mainFrame, List <Prodotto> elencoProdotti) {

        setPreferredSize(new Dimension(600, 800)); // setta la dimensione preferita del pannello
        setLayout(new GridBagLayout()); // setta il layout del pannello
        setSize( 100, 800); // setta la dimensione del pannello
        setBackground(Color.WHITE); // setta il colore di sfondo del pannello

        Border bordoInterno = BorderFactory.createTitledBorder("Carica Prodotto"); // crea un bordo interno con il titolo "Carica Prodotto"
        Border bordoEsterno = BorderFactory.createEmptyBorder(5, 5, 5, 5); // crea un bordo esterno vuoto con margini di 5px
        Border bordoFinale = BorderFactory.createCompoundBorder(bordoEsterno, bordoInterno); // crea un bordo finale unendo il bordo esterno e il bordo interno
        setBorder(bordoFinale); // setta il bordo finale al pannello

        labelNome = new JLabel("Nome*: "); // crea un'etichetta per il nome del prodotto
        campoNome = new JTextField(10); // crea un campo di testo per il nome del prodotto

        labelPrezzo = new JLabel("Prezzo*: ");
        campoPrezzo = new JTextField(10);

        ///((AbstractDocument)) sta a dire che il campo prezzo è un documento astratto (ovvero un documento che non ha una rappresentazione fisica)
        ((AbstractDocument) campoPrezzo.getDocument()).setDocumentFilter(new PrezzoFilter()); // applica un filtro al campo prezzo per limitare l'input a numeri e punti

        labelStatoProdotto = new JLabel("Stato del prodotto: ");
        nuovo = new JRadioButton("Nuovo", true); //opzione prodotto nuovo(true per default, quindi selezionato di default)
        usato = new JRadioButton("Usato"); // opzione prodotto usato
        gruppoRadioUsatoNuovo = new ButtonGroup(); // crea un gruppo di bottoni radio per garantire che solo uno sia selezionato alla volta
        gruppoRadioUsatoNuovo.add(nuovo); // aggiunge il bottone radio nuovo al gruppo
        gruppoRadioUsatoNuovo.add(usato); // aggiunge il bottone radio usato al gruppo
        nuovo.setActionCommand("Nuovo"); // setta il comando di azione per il bottone radio nuovo
        usato.setActionCommand("Usato"); // setta il comando di azione per il bottone radio usato
        nuovo.setSelected(true); // setta il bottone radio nuovo come selezionato di default

        labelImmagine = new JLabel("Immagine:");
        campoImmagine = new JTextField(10);
        campoImmagine.setEditable(false); // rende il campo immagine non modificabile
        bottoneSfoglia = new JButton("Sfoglia");
        anteprimaImmagine = new JLabel();
        anteprimaImmagine.setPreferredSize(new Dimension(100, 100)); // setta la dimensione preferita dell'anteprima immagine
        anteprimaImmagine.setMinimumSize(new Dimension(100, 100)); //dimensione minima dell'anteprima immagine (garantisce che non si riduca troppo)
        anteprimaImmagine.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // crea un bordo grigio attorno all'anteprima immagine
        anteprimaImmagine.setHorizontalAlignment(SwingConstants.CENTER); // allinea l'anteprima immagine al centro
        anteprimaImmagine.setVerticalAlignment(SwingConstants.CENTER); // allinea l'anteprima immagine al centro

        labelCategoria = new JLabel("Categoria*:");
        categorieProdotto = new JCheckBox[categorie.length]; // crea un array di JCheckBox per le categorie del prodotto
        for (int i = 0; i < categorie.length; i++) { //gli aggiunge l'array di categorie inizializzato prima
            categorieProdotto[i] = new JCheckBox(categorie[i]);
        }

        descrizione = new JLabel("Descrizione:");
        campoDescrizione = new JTextArea(5, 10);
        campoDescrizione.setLineWrap(true); // abilita il ritorno a capo automatico
        campoDescrizione.setWrapStyleWord(true); // abilita il ritorno a capo automatico per le parole
        campoDescrizione.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // crea un bordo grigio attorno al campo descrizione
        campoDescrizione.setPreferredSize(new Dimension(200, 200)); // setta la dimensione preferita del campo descrizione
        campoDescrizione.setMinimumSize(new Dimension(200, 200)); // setta la dimensione minima del campo descrizione

        bottoneSfoglia.addActionListener(e -> { // aggiunge un ActionListener al bottone Sfoglia
            JFileChooser fileChooser = new JFileChooser(); // crea un JFileChooser per selezionare un file
            fileChooser.setFileFilter(new FileNameExtensionFilter("Immagini", "jpg", "png", "jpeg", "gif")); // filtra i file per mostrare solo le immagini con estensioni specifiche
            int result = fileChooser.showOpenDialog(null); // mostra la finestra di dialogo per selezionare un file

            if (result == JFileChooser.APPROVE_OPTION) { // se l'utente ha selezionato un file

                File selectedFile = fileChooser.getSelectedFile(); // ottiene il file selezionato

                File cartellaImmaginiProdotti = new File("files/ImmaginiProdotti"); // crea un oggetto File per la cartella delle immagini dei prodotti
                if(!cartellaImmaginiProdotti.exists()) { //se la cartella non esiste
                    cartellaImmaginiProdotti.mkdir(); // crea la cartella
                }

                // crea un nuovo file con il percorso della cartella delle immagini dei prodotti e il nome del file selezionato
                File nuovoPercorsoImmagini = new File(cartellaImmaginiProdotti, selectedFile.getName());
                try{ // copia il file selezionato nella cartella delle immagini dei prodotti
                    java.nio.file.Files.copy( // copia il file
                            selectedFile.toPath(), // percorso del file selezionato
                            nuovoPercorsoImmagini.toPath(), // percorso della cartella delle immagini dei prodotti
                            StandardCopyOption.REPLACE_EXISTING // sovrascrive il file se esiste già
                    );
                }catch(IOException ex){ // gestisce eventuali eccezioni di input/output
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, ex.getMessage()); // mostra un messaggio di errore
                    return;
                }

                /*
                    Tutta questa parte di codice serve a copiare il file selezionato nella cartella delle immagini dei prodotti
                    in modo da non avere problemi di percorso quando si carica il prodotto.
                    In questo modo il file selezionato viene copiato nella cartella delle immagini dei prodotti
                    e il percorso del file viene aggiornato nel campo immagine.
                    Cio garantisce che il file selezionato esista sempre nella cartella delle immagini dei prodotti
                    e che il percorso del file sia corretto quando si carica il prodotto.
                    Inoltre, se il file selezionato esiste già nella cartella delle immagini dei prodotti,
                    viene sovrascritto senza creare un nuovo file con un nome diverso.
                    Questo evita di avere più file con lo stesso nome nella cartella delle immagini dei prodotti.

                    In sintesi, risparmi memoria, garantisci la portabilita' dell'applicazione e eviti problemi di percorso.
                */

                campoImmagine.setText("files/ImmaginiProdotti/"+selectedFile.getName()); // setta il percorso del file selezionato nel campo immagine

                ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath()); // crea un oggetto ImageIcon con il percorso del file selezionato
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // ridimensiona l'immagine a 100x100 pixel
                anteprimaImmagine.setIcon(new ImageIcon(img)); // setta l'icona dell'anteprima immagine con l'immagine ridimensionata
            }
        });

        bottoneAggiungi = new JButton("Carica"); // crea un bottone per caricare il prodotto
        bottoneAggiungi.addActionListener(new ActionListener() { // aggiunge un ActionListener al bottone Carica
            @Override
            public void actionPerformed(ActionEvent e) {
                // Controlla se i campi obbligatori sono stati compilati
                if(campoNome.getText().isEmpty() || campoPrezzo.getText().isEmpty() || campoPrezzo.getText().equals(",") || campoPrezzo.getText().equals(".")){
                    JOptionPane.showMessageDialog(null, "Compliare i campi obbligatori(*)", "Errore", JOptionPane.ERROR_MESSAGE);
                }
                else{
                    // variabili ( temporane) per il prodotto
                    String nome = campoNome.getText(); // prende il nome del prodotto dal campo di testo
                    double prezzo = Double.parseDouble(campoPrezzo.getText()); // prende il prezzo del prodotto dal campo di testo e lo converte in double
                    String statoProdotto = gruppoRadioUsatoNuovo.getSelection().getActionCommand(); // prende lo stato del prodotto selezionato dal gruppo di bottoni radio
                    String immaginePath = campoImmagine.getText(); // prende il percorso dell'immagine dal campo di testo
                    String descrizione = campoDescrizione.getText(); // prende la descrizione del prodotto dal campo di testo

                    if(descrizione.isEmpty()){ // se la descrizione è vuota
                        descrizione = "Nessuna descrizione fornita"; // il suo contenuto è "Nessuna descrizione fornita"
                    }

                    if (immaginePath.isEmpty()) { // se il percorso dell'immagine è vuoto, quindi, non è stata selezionata un'immagine
                        immaginePath = "files/noImageAvaiable.png"; // il suo contenuto è "files/noImageAvaiable.png"(ovvero un percorso locale di un'immagine di default)
                    }

                    List<String>categorieSelezionate = new ArrayList<>(); // crea una lista per le categorie selezionate
                    for(JCheckBox checkBox : categorieProdotto) { // scorre le checkbox delle categorie
                        if (checkBox.isSelected()) { // se la checkbox è selezionata (categoria selezionata)
                            categorieSelezionate.add(checkBox.getText()); // aggiunge il testo della checkbox alla lista delle categorie selezionate
                        }
                    }

                    if (categorieSelezionate.isEmpty()) { // se non è stata selezionata nessuna categoria mostra un messaggio di errore
                        JOptionPane.showMessageDialog(null, "Selezionare almeno una categoria", "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // String.join() unisce gli elementi della lista in una stringa separata (in questo caso) da una virgola
                    String unisciCategorieSelezionate = String.join(", ", categorieSelezionate);

                    // crea un nuovo oggetto Prodotto con i dati inseriti
                    FormEvent formEvent = new FormEvent( this, nome, prezzo, statoProdotto, immaginePath, descrizione, unisciCategorieSelezionate, mainFrame.getUtenteLoggato().getUsername());
                    formEvent.setPercorsoImmagine(immaginePath); // setta il percorso dell'immagine
                    formEvent.setDescrizione(descrizione); // setta la descrizione del prodotto
                    formEvent.setNome(nome); // setta il nome del prodotto
                    formEvent.setPrezzo(prezzo); // setta il prezzo del prodotto
                    formEvent.setStatoProdotto(statoProdotto); // setta lo stato del prodotto
                    formEvent.setUtenteCheHaCaricato(mainFrame.getUtenteLoggato().getUsername()); // setta l'utente che ha caricato il prodotto direttamente dalla finestra principale
                    formEvent.setCategoria(unisciCategorieSelezionate); // setta la categoria del prodotto

                    if (formListener != null) { // se il formListener non è nullo
                        formListener.formEventListener(formEvent); // chiama il metodo formEventListener del formListener
                    }

                    // salva il prodotto nella lista dei prodotti
                    JOptionPane.showMessageDialog(null, "Prodotto caricato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                    mainFrame.mostraSchermata("Catalogo");

                    // appena il prodotto e' stato caricato

                    campoNome.setText(""); // svuota il campo nome
                    campoPrezzo.setText(""); // svuota il campo prezzo
                    campoImmagine.setText(""); // svuota il campo immagine
                    campoDescrizione.setText(""); // svuota il campo descrizione
                    for (JCheckBox checkBox : categorieProdotto) {
                        checkBox.setSelected(false); // deseleziona tutte le checkbox delle categorie
                    }
                    gruppoRadioUsatoNuovo.clearSelection(); // deseleziona tutti i bottoni radio
                    anteprimaImmagine.setIcon(null); // svuota l'icona dell'anteprima immagine
                }
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END; // allinea il componente a destra
        add(labelNome, gbc); // aggiunge l'etichetta nome al pannello, con le restrizioni specificate da gbc

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START; // allinea il componente a sinistra
        gbc.fill = GridBagConstraints.HORIZONTAL; // riempi il componente orizzontalmente ( riempi tutto lo spazio disponibile)
        add(campoNome, gbc);

        gbc = new GridBagConstraints(); // il motivo per cui lo ricreo e' che non voglio che si crei un conflitto con le altre variabili (ovvero quando sono una sopra l'altra)
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(labelPrezzo, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(campoPrezzo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(labelStatoProdotto, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(nuovo, gbc);
        gbc.gridy = 3;
        add(usato, gbc);

        gbc = new GridBagConstraints(); // il motivo per cui lo ricreo e' che non voglio che si crei un conflitto con le altre variabili (ovvero quando sono una sopra l'altra)
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(descrizione, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridwidth = 2;
        scrollDescrizione = new JScrollPane(campoDescrizione);
        scrollDescrizione.setPreferredSize(new Dimension(200, 50));
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(scrollDescrizione, gbc);

        gbc = new GridBagConstraints(); // il motivo per cui lo ricreo e' che non voglio che si crei un conflitto con le altre variabili (ovvero quando sono una sopra l'altra)
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(labelCategoria, gbc);

        JPanel pannelloCheck = new JPanel(new GridLayout(0, 2, 5, 5));
        for (JCheckBox checkBox : categorieProdotto) {
            pannelloCheck.add(checkBox);
        }

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(pannelloCheck, gbc);

        gbc = new GridBagConstraints(); // il motivo per cui lo ricreo e' che non voglio che si crei un conflitto con le altre variabili (ovvero quando sono una sopra l'altra)
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(labelImmagine, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(bottoneSfoglia, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(campoImmagine, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(anteprimaImmagine, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(bottoneAggiungi, gbc);
    }

    public void setFormListener(FormListener formListener) {
        this.formListener = formListener;
    }
}


