import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class PannelloProfiloUtente extends JPanel {

    private JLabel labelUtente;
    private JLabel labelSaldo;
    private JPanel pannelloProdottiUtente;
    private FinestraPrincipale mainFrame;

    public PannelloProfiloUtente(Utente utenteLoggato, List<Prodotto> prodotti, FinestraPrincipale mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        // Intestazione
        JPanel pannelloInfo = new JPanel(new GridLayout(2, 1));
        labelUtente = new JLabel("Ciao, " + mainFrame.getUtenteLoggato().getUsername()+"!");
        labelUtente.setFont(new Font("Arial", Font.BOLD, 16));

        // (Per ora saldo fittizio)
        labelSaldo = new JLabel("Saldo: "+ mainFrame.getUtenteLoggato().getSaldo()+ "€");
        System.out.println("DEBUG - Utente username: " + utenteLoggato.getUsername());
        System.out.println("DEBUG - Utente saldo: " + utenteLoggato.getSaldo());
        labelSaldo.setFont(new Font("Arial", Font.PLAIN, 14));

        pannelloInfo.add(labelUtente);
        pannelloInfo.add(labelSaldo);
        add(pannelloInfo, BorderLayout.NORTH);

        // Prodotti caricati dall’utente
        pannelloProdottiUtente = new JPanel();
        pannelloProdottiUtente.setLayout(new BoxLayout(pannelloProdottiUtente, BoxLayout.Y_AXIS));

        List<Prodotto> prodottiUtente = prodotti.stream()
                .filter(p -> p.getUtenteCheHaCaricato().equals(utenteLoggato.getUsername()))
                .collect(Collectors.toList());

        if (prodottiUtente.isEmpty()) {
            pannelloProdottiUtente.add(new JLabel("Non hai ancora caricato prodotti."));
        } else {
            for (Prodotto prodotto : prodottiUtente) {
                JPanel card = new JPanel(new BorderLayout());
                card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

                JLabel nome = new JLabel(prodotto.getNome());
                nome.setFont(new Font("Arial", Font.BOLD, 14));

                JLabel prezzo = new JLabel("Prezzo: €" + prodotto.getPrezzo());

                JButton dettagli = new JButton("Mostra Dettagli");
                dettagli.addActionListener( (e) -> {
                    JDialog finestraDettagli = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Dettagli Prodotto", true);
                    finestraDettagli.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    finestraDettagli.setContentPane(
                            new PannelloDettagliProdotto(prodotto, mainFrame.getUtenteLoggato(), mainFrame, null)
                    );
                    finestraDettagli.pack();
                    finestraDettagli.setLocationRelativeTo(this);
                    finestraDettagli.setVisible(true);
                });

                JPanel sinistra = new JPanel(new GridLayout(2, 1));
                sinistra.add(nome);
                sinistra.add(prezzo);

                card.add(sinistra, BorderLayout.CENTER);
                card.add(dettagli, BorderLayout.EAST);

                pannelloProdottiUtente.add(card);
            }
        }

        JScrollPane scroll = new JScrollPane(pannelloProdottiUtente);
        add(scroll, BorderLayout.CENTER);
    }

    public void aggiornaSaldo() {
        labelSaldo.setText("Saldo: " + mainFrame.getUtenteLoggato().getSaldo() + "€");
    }
}

