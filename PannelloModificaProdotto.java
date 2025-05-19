import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class PannelloModificaProdotto extends JDialog {

    private JTextField campoNome;
    private JTextField campoPrezzo;
    private JTextArea campoDescrizione;
    private JCheckBox[] checkCategorie;
    private JRadioButton nuovo, usato;
    private JTextField campoImmagine;
    private JLabel anteprima;

    private JButton salvaBtn, annullaBtn;

    public PannelloModificaProdotto(Window owner, Prodotto prodotto, FinestraPrincipale mainFrame, CatalogoProdottiPanel catalogoPanel) {
        super(owner, "Modifica Prodotto", Dialog.ModalityType.APPLICATION_MODAL);
        setSize(500, 600);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        // Nome
        formPanel.add(new JLabel("Nome:"), gbc);
        campoNome = new JTextField(prodotto.getNome());
        gbc.gridx = 1;
        formPanel.add(campoNome, gbc);

        // Prezzo
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Prezzo:"), gbc);
        campoPrezzo = new JTextField(String.valueOf(prodotto.getPrezzo()));
        gbc.gridx = 1;
        formPanel.add(campoPrezzo, gbc);

        // Stato
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Stato:"), gbc);
        nuovo = new JRadioButton("Nuovo");
        usato = new JRadioButton("Usato");
        ButtonGroup statoGroup = new ButtonGroup();
        statoGroup.add(nuovo);
        statoGroup.add(usato);
        if (prodotto.getStatoProdotto().equalsIgnoreCase("Nuovo")) {
            nuovo.setSelected(true);
        } else {
            usato.setSelected(true);
        }
        JPanel statoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statoPanel.add(nuovo); statoPanel.add(usato);
        gbc.gridx = 1;
        formPanel.add(statoPanel, gbc);

        // Descrizione
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Descrizione:"), gbc);
        campoDescrizione = new JTextArea(5, 20);
        campoDescrizione.setText(prodotto.getDescrizione());
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(campoDescrizione), gbc);

        // Categorie (checkbox)
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Categorie:"), gbc);
        String[] tutteCategorie = {"Elettronica", "Abbigliamento", "Casa", "Cibo", "Sport",
                "Giocattoli", "Libri", "accessori", "Veicoli", "Armi", "Musica", "Hot"};
        JPanel categoriaPanel = new JPanel(new GridLayout(0, 2));
        checkCategorie = new JCheckBox[tutteCategorie.length];
        for (int i = 0; i < tutteCategorie.length; i++) {
            checkCategorie[i] = new JCheckBox(tutteCategorie[i]);
            if (prodotto.getCategoria().contains(tutteCategorie[i])) {
                checkCategorie[i].setSelected(true);
            }
            categoriaPanel.add(checkCategorie[i]);
        }
        gbc.gridx = 1;
        formPanel.add(categoriaPanel, gbc);

        // Immagine
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Immagine:"), gbc);
        JPanel imgPanel = new JPanel(new BorderLayout());
        campoImmagine = new JTextField(prodotto.getPercorsoImmagine());
        campoImmagine.setEditable(false);
        JButton sfoglia = new JButton("Sfoglia");

        anteprima = new JLabel();
        anteprima.setPreferredSize(new Dimension(150, 150));
        if (prodotto.getPercorsoImmagine() != null && !prodotto.getPercorsoImmagine().isEmpty()) {
            ImageIcon icon = new ImageIcon(prodotto.getPercorsoImmagine());
            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            anteprima.setIcon(new ImageIcon(img));
        }

        sfoglia.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                campoImmagine.setText("files/ImmaginiProdotti/" + file.getName());
                try {
                    File dest = new File("files/ImmaginiProdotti", file.getName());
                    Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    ImageIcon icon = new ImageIcon(dest.getAbsolutePath());
                    anteprima.setIcon(new ImageIcon(icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Errore caricamento immagine.");
                }
            }
        });

        JPanel imgWrapper = new JPanel(new BorderLayout());
        imgWrapper.add(campoImmagine, BorderLayout.CENTER);
        imgWrapper.add(sfoglia, BorderLayout.EAST);

        gbc.gridx = 1;
        formPanel.add(imgWrapper, gbc);

        gbc.gridy++;
        formPanel.add(anteprima, gbc);

        // Pulsanti
        JPanel btnPanel = new JPanel();
        salvaBtn = new JButton("Salva");
        annullaBtn = new JButton("Annulla");

        salvaBtn.addActionListener(e -> {
            prodotto.setNome(campoNome.getText());
            prodotto.setPrezzo(Double.parseDouble(campoPrezzo.getText()));
            prodotto.setDescrizione(campoDescrizione.getText());
            prodotto.setStatoProdotto(nuovo.isSelected() ? "Nuovo" : "Usato");
            prodotto.setPercorsoImmagine(campoImmagine.getText());

            ArrayList<String> categorie = new ArrayList<>();
            for (JCheckBox box : checkCategorie) {
                if (box.isSelected()) categorie.add(box.getText());
            }
            prodotto.setCategoria(String.join(", ", categorie));

            ManagerFileProdotti.salva(mainFrame.getElencoProdotti());
            catalogoPanel.aggiornaCatalogo(mainFrame.getElencoProdotti(), mainFrame , mainFrame.getUtenteLoggato());
            dispose();
        });

        annullaBtn.addActionListener(e -> dispose());

        btnPanel.add(salvaBtn);
        btnPanel.add(annullaBtn);

        add(new JScrollPane(formPanel), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
}

