import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.event.ActionEvent;

public class PannelloDettagliProdotto extends JPanel {

    private JLabel labelNomeProdotto;
    private JLabel labelPrezzoProdotto;
    private JLabel labelCaricatoDa;
    private JLabel labelDescrizioneProdotto;
    private JLabel labelCategoriaProdotto;
    private JLabel labelStatoProdotto;
    private JLabel labelImmagineProdotto;

    private JButton modifica;
    private JButton acquista;
    private JButton aggiungiAlCarrello;
    private JButton rimuoviProdotto;

    public PannelloDettagliProdotto(Prodotto prodotto, Utente utenteLoggato, FinestraPrincipale mainFrame, CatalogoProdottiPanel catalogoPanel){

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        labelImmagineProdotto = new JLabel("Immagine: " + prodotto.getPercorsoImmagine());
        if (prodotto.getPercorsoImmagine() != null && !prodotto.getPercorsoImmagine().isEmpty()) {
            ImageIcon icon = new ImageIcon(prodotto.getPercorsoImmagine());
            Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            labelImmagineProdotto.setIcon(new ImageIcon(img));
            labelImmagineProdotto.setText(null);
        }

        labelImmagineProdotto.setHorizontalAlignment(SwingConstants.CENTER);
        add(labelImmagineProdotto);

        labelNomeProdotto = new JLabel("Nome: " + prodotto.getNome());
        labelNomeProdotto.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridy = 1;
        add(labelNomeProdotto, gbc);

        labelPrezzoProdotto = new JLabel("Prezzo: €" + prodotto.getPrezzo());
        labelPrezzoProdotto.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(labelPrezzoProdotto, gbc);

        labelCaricatoDa = new JLabel("Caricato da: " + prodotto.getUtenteCheHaCaricato());
        labelCaricatoDa.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(labelCaricatoDa, gbc);

        labelCategoriaProdotto = new JLabel("Categoria: " + prodotto.getCategoria());
        labelCategoriaProdotto.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(labelCategoriaProdotto, gbc);

        labelStatoProdotto = new JLabel("Stato: " + prodotto.getStatoProdotto());
        labelStatoProdotto.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(labelStatoProdotto, gbc);

        labelDescrizioneProdotto = new JLabel("Descrizione: " + prodotto.getDescrizione());
        labelDescrizioneProdotto.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridy = 5;
        add(labelDescrizioneProdotto, gbc);

        // Pulsanti
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;

        if (utenteLoggato.getUsername().equals(prodotto.getUtenteCheHaCaricato())) {
            modifica = new JButton("Modifica");
            modifica.addActionListener(e -> {
                new PannelloModificaProdotto(SwingUtilities.getWindowAncestor(this),prodotto, mainFrame, catalogoPanel);
            });


            rimuoviProdotto = new JButton("Rimuovi prodotto");

            rimuoviProdotto.addActionListener(e -> {
                int conferma = JOptionPane.showConfirmDialog(this, "Sei sicuro di voler rimuovere questo prodotto?", "Conferma rimozione", JOptionPane.YES_NO_OPTION);
                if (conferma == JOptionPane.YES_OPTION) {
                    mainFrame.rimuoviProdotto(prodotto);
                    catalogoPanel.aggiornaCatalogo(ManagerFileProdotti.carica(), mainFrame, utenteLoggato);
                    JOptionPane.showMessageDialog(this, "Prodotto rimosso con successo.");
                    SwingUtilities.getWindowAncestor(this).dispose(); // chiude il dialog
                }
            });
            btnPanel.add(rimuoviProdotto);
            btnPanel.add(modifica);
        } else {
            acquista = new JButton("Acquista");

            acquista.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(confermaAcquisto()){
                        if (prodotto.getPrezzo() <= utenteLoggato.getSaldo()) {
                            mainFrame.getUtenteLoggato().setSaldo(mainFrame.getUtenteLoggato().getSaldo() - prodotto.getPrezzo());
                            ManagerFileUtenti.aggiornaUtente(mainFrame.getUtenteLoggato());
                            System.out.println( "DEBUG - Saldo aggiornato: " + utenteLoggato.getSaldo());
                            JOptionPane.showMessageDialog(PannelloDettagliProdotto.this, "Acquisto effettuato con successo!");
                            int scelta = JOptionPane.showConfirmDialog(null, "Vuoi scaricare la fattura?", "Scarica fattura", JOptionPane.YES_NO_OPTION);

                            if (scelta == JOptionPane.YES_OPTION) {
                                ManagerFileFattura.generaFatturaTxt(utenteLoggato, prodotto);
                            }
                            mainFrame.rimuoviProdotto(prodotto);
                            catalogoPanel.aggiornaCatalogo(ManagerFileProdotti.carica(), mainFrame, utenteLoggato);
                            SwingUtilities.getWindowAncestor(PannelloDettagliProdotto.this).dispose(); // chiude il dialog
                        } else {
                            JOptionPane.showMessageDialog(PannelloDettagliProdotto.this, "Saldo insufficiente per acquistare questo prodotto.");
                        }
                    }
                }
            });

            aggiungiAlCarrello = new JButton("Aggiungi al carrello");
            aggiungiAlCarrello.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainFrame.getUtenteLoggato().addProdottoCarrello(prodotto);
                    ManagerFileUtenti.aggiornaUtente(mainFrame.getUtenteLoggato());
                    JOptionPane.showMessageDialog(PannelloDettagliProdotto.this, "Prodotto aggiunto al carrello.");
                }
            });
            btnPanel.add(acquista);
            btnPanel.add(aggiungiAlCarrello);
        }

        add(btnPanel, gbc);
    }

    public boolean confermaAcquisto(){
        int conferma = JOptionPane.showConfirmDialog(this, "Sei sicuro di voler acquistare questo prodotto?", "Conferma acquisto", JOptionPane.YES_NO_OPTION);
        if (conferma == JOptionPane.YES_OPTION) {
            return true;
        } else {
            return false;
        }
    }
}
