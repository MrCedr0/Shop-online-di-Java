import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PannelloAccessoUtente extends JPanel {

    /*
        INFO GENERALI SULLE COSE UTILIZZATE
        - GridBagLayout: layout manager che permette di posizionare i componenti in una griglia
        - GridBagConstraints: classe che definisce le restrizioni per i componenti in un GridBagLayout
        - JTextField: campo di testo per l'input dell'utente
        - JPasswordField: campo di testo per l'input della password(rende i caratteri invisibili)
        - JLabel: etichetta per visualizzare del testo
        - JButton: bottone per l'interazione dell'utente
        - Color: classe per definire i colori
        - Insets: classe per definire i margini tra i componenti( distanza tra i componenti in px)
        - JOptionPane: classe per visualizzare finestre di dialogo
        - ActionListener: interfaccia per gestire gli eventi dei componenti
        - EventObject: classe di base per gli eventi in Java (gestisce gli eventi generali).
        - EventListener: interfaccia per gestire gli eventi dei componenti
        - FormEvent: classe per gestire gli eventi del form
        - FormListener: interfaccia per gestire gli eventi del form
        - UtenteManager: classe per gestire gli utenti
        - FinestraPrincipale: classe per gestire la finestra principale dell'applicazione
        - PannelloAccessoUtente: classe per gestire il pannello di accesso dell'utente
        - PannelloRegistrazioneUtente: classe per gestire il pannello di registrazione dell'utente
    */

    private JTextField campoUtente; //campo di testo per l'username
    private JPasswordField campoPassword;
    private JLabel messaggioErrore;
    private JButton bottoneLogin;
    private JButton bottonePasswordDimenticata;
    private FinestraPrincipale mainFrame;
    private JButton btnRegistrati;

    public PannelloAccessoUtente(FinestraPrincipale mainFrame) { //perche' ci poertiamo l'oggetto FinestraPrincipale?
        //perche' ci serve per accedere ai metodi di FinestraPrincipale( per esempio per mostrare la schermata di registrazione o registrare l'utente cosi da usare i suoi dati)
        this.mainFrame = mainFrame;

        setLayout(new GridBagLayout()); //nuovo layout di tipo GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints(); // crea un oggetto GridBagConstraints per gestire le restrizioni dei componenti
        gbc.insets = new Insets(8, 8, 8, 8); //distanza tra i componenti in px
        gbc.fill = GridBagConstraints.HORIZONTAL; //riempi il componente orizzontalmente ( riempi tutto lo spazio disponibile)

        campoUtente = new JTextField(15); //campo di testo per l'username (15 caratteri di larghezza, ovvero quantita' di caratteri che puo' contenere)
        campoPassword = new JPasswordField(15); //campo di testo per la password (15 caratteri di larghezza)

        bottoneLogin = new JButton("Accedi"); // bottone per accedere
        bottoneLogin.setFocusable(false); // non lo ricalca quando ci clicchi sopra

        messaggioErrore = new JLabel("Credenziali errate"); // messaggio di errore
        messaggioErrore.setForeground(Color.RED); //di colore rosso
        messaggioErrore.setVisible(false); // non visibile all'inizio

        btnRegistrati = new JButton("Nuovo utente?"); // bottone per registrarsi
        btnRegistrati.setFocusable(false); // non lo ricalca quando ci clicchi sopra
        btnRegistrati.setBorderPainted(false); // non mostra il bordo
        btnRegistrati.setContentAreaFilled(false); // non riempie l'area del bottone
        btnRegistrati.setForeground(Color.BLUE); //setta il testo di colore blu

        //aggiungo un ActionListener al bottone per registrarsi. Ti porta alla schermata di registrazione
        btnRegistrati.addActionListener(e -> mainFrame.mostraSchermata("registrazione"));

        //aggiungo un ActionListener al bottone per accedere. Controlla se le credenziali sono corrette
        bottoneLogin.addActionListener((ActionEvent e) -> {
            String utente = campoUtente.getText(); //prende il testo inserito nel campo di testo (ovvero l'username)
            String password = new String(campoPassword.getPassword()); //prende la password inserita nel campo di testo (converte il char[] in String)

            if (UtenteManager.verificaCredenziali(utente, password)) { //controlla se le credenziali sono corrette usando la classe UtenteManager
                /// se le credenziali sono corrette, setta l'utente loggato
                messaggioErrore.setVisible(false); // nasconde il messaggio di errore (in caso di credenziali errate in precedenza)
                mainFrame.setUtenteLoggato(utente); //setta l'utente loggato
                mainFrame.mostraBarraDiNavigazione(); //mostra la barra di navigazione
                mainFrame.mostraSchermata("profilo"); //mostra la schermata del profilo al momento del login
            } else {
                messaggioErrore.setVisible(true); /// mostra il messaggio di errore (in caso di credenziali errate)
            }

            //DEBUG
            System.out.println("Username inserito: " + utente);
            System.out.println("Password inserita: " + password);
            System.out.println("Utente loggato da UtenteManager: " + UtenteManager.getUtenteLoggato().getUsername());
            System.out.println("Utente loggato da mainFrame: " + mainFrame.getUtenteLoggato().getUsername());
            System.out.println("Accesso eseguito da: "+ mainFrame.getUtenteLoggato().getUsername());
        });

        bottonePasswordDimenticata = new JButton("Password dimenticata?"); // bottone per la password dimenticata
        bottonePasswordDimenticata.setFocusable(false); // non lo ricalca quando ci clicchi sopra
        bottonePasswordDimenticata.setBorderPainted(false); // non mostra il bordo
        bottonePasswordDimenticata.setContentAreaFilled(false); // non riempie l'area del bottone
        bottonePasswordDimenticata.setForeground(Color.BLUE); //setta il testo di colore blu

        // aggiungo un ActionListener al bottone per la password dimenticata.
        bottonePasswordDimenticata.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Non e' problema mio"); // mostra questa finestra di dialogo
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Nome utente:"), gbc); //scritta "Nome utente:" a sinistra

        gbc.gridx = 1;
        add(campoUtente, gbc); //campo di testo a destra

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Password:"), gbc); //scritta "Password:" a sinistra

        gbc.gridx = 1;
        add(campoPassword, gbc); //campo di testo a destra

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(bottoneLogin, gbc); //bottone "Accedi" sotto i campi di testo

        gbc.gridy = 2;
        add(messaggioErrore, gbc); //messaggio di errore sotto il bottone "Accedi"

        gbc.gridy = 4;
        add(bottonePasswordDimenticata, gbc); //bottone "Password dimenticata?" sotto i campi di testo

        gbc.gridy = 5;
        add(btnRegistrati, gbc); //bottone "Registrati" sotto il messaggio di errore
    }

    public void addActionListener(Object catalogo) { //non piu utilizzato
    }
}
