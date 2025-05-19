import javax.swing.*;
import java.awt.*;

public class PannelloRegistrazioneUtente extends JPanel {

    private JTextField campoUtente;
    private JPasswordField campoPassword, campoConferma;
    private JLabel messaggioErrore;
    private FinestraPrincipale mainFrame;
    private JButton terminiCondizioni;
    private JCheckBox checkboxAccetto;
    private JButton bottoneAccesso;
    private JButton domandaAccesso;
    private JButton bottoneRegistra;

    public PannelloRegistrazioneUtente(FinestraPrincipale mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoUtente = new JTextField(15);
        campoPassword = new JPasswordField(15);
        campoConferma = new JPasswordField(15);
        messaggioErrore = new JLabel();

        domandaAccesso = new JButton("Hai già un account?");
        domandaAccesso.setForeground(Color.BLUE);
        domandaAccesso.setFocusable(false);
        domandaAccesso.setBorderPainted(false);
        domandaAccesso.setContentAreaFilled(false);

        bottoneAccesso = new JButton("Accedi");
        bottoneAccesso.setFocusable(false);
        bottoneAccesso.setBorderPainted(false);
        bottoneAccesso.setContentAreaFilled(false);
        bottoneAccesso.setForeground(Color.BLUE);

        messaggioErrore.setForeground(Color.RED);
        messaggioErrore.setVisible(false);

        checkboxAccetto = new JCheckBox("Accetto");
        checkboxAccetto.setFocusable(false);
        checkboxAccetto.setBorderPainted(false);
        checkboxAccetto.setContentAreaFilled(false);

        checkboxAccetto.addActionListener(e -> {
            bottoneRegistra.setEnabled(checkboxAccetto.isSelected());
        });

        terminiCondizioni = new JButton("termini e condizioni");
        terminiCondizioni.setFocusable(false);
        terminiCondizioni.setBorderPainted(false);
        terminiCondizioni.setContentAreaFilled(false);
        terminiCondizioni.setForeground(Color.BLUE);

        domandaAccesso.addActionListener(e -> {
            mainFrame.mostraSchermata("login");
        });

        terminiCondizioni.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Linganguliguliguliwaza\n" +
                    "blah blah blah bleh bleh\n" +
                            "ti fotto i dati HAHAHAHAHAHAH\n" +
                            "Software approvato da Rockstar Games\n" +
                            "Non ci assumiamo responsabilità per eventuali danni\n" +
                            "tantomeno doxxing\n"+
             "Pasta con il pesto!!!!!!!!!!!!!\n"+
                    "segui su instagram @mr._.cedro e @galdo.trn\n");
        });

        bottoneRegistra = new JButton("Registrati");
        bottoneRegistra.setFocusable(false);
        bottoneRegistra.setEnabled(false);

        bottoneRegistra.addActionListener(e -> {
            String utente = campoUtente.getText();
            String pass1 = new String(campoPassword.getPassword());
            String pass2 = new String(campoConferma.getPassword());

            if (!pass1.equals(pass2)) {
                messaggioErrore.setText("Le password non coincidono");
                messaggioErrore.setVisible(true);
                return;
            }

            if (UtenteManager.nomeUtenteEsiste(utente)) { //controlla se l'username è già in uso controllando la lista di utenti gia registrati
                messaggioErrore.setText("Nome utente già in uso");
                messaggioErrore.setVisible(true);
                return;
            }

            if (utente.isEmpty() || pass1.isEmpty()) {
                messaggioErrore.setText("Compila tutti i campi");
                messaggioErrore.setVisible(true);
                return;
            }

            // Registrazione ok
            UtenteManager.registraUtente(utente, pass1); // tramtite la classe UtenteManager registra l'utente
            JOptionPane.showMessageDialog(this, "Registrazione completata!");
            JOptionPane.showMessageDialog(this, "Rientri tra i nostri primi 1000 utenti. Eccoti un bonus di 5000€", "CONGRATULAZIONI", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.mostraSchermata("login");
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Nome utente:"), gbc); // Scritta nome utente

        gbc.gridx = 1;
        add(campoUtente, gbc); // Campo nome utente

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Password:"), gbc); // Scritta password

        gbc.gridx = 1;
        add(campoPassword, gbc); // Campo password

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Conferma Password:"), gbc); // Scritta conferma password

        gbc.gridx = 1;
        add(campoConferma, gbc); // Campo di conferma password

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(bottoneRegistra, gbc); // Bottone registrati

        gbc.gridy = 4;
        add(messaggioErrore, gbc); // Messaggio errore

        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor=(GridBagConstraints.LINE_START);
        add(checkboxAccetto, gbc); // Checkbox accetto

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor=(GridBagConstraints.LINE_END);
        gbc.gridwidth = 1;
        add(terminiCondizioni, gbc); // Termini e condizioni

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        add(domandaAccesso, gbc); // Domanda accesso

    }
}

