/**
 * Classe di utility per la gestione delle fatture.
 * Permette di salvare una fattura come file .txt per un singolo prodotto
 * oppure per l'intero carrello. Utilizza JFileChooser per selezionare
 * il percorso di salvataggio e BufferedWriter per scrivere i dati su file.
 */

import javax.swing.*;
import java.io.*; // Importa la libreria per la gestione dei file(input o output che sia)
import java.text.SimpleDateFormat; // Importa la libreria per la formattazione delle date
import java.util.Date; // Importa la libreria per la gestione delle date
import java.util.List;

// Genera una fattura in formato .txt per un singolo prodotto acquistato.
// L'utente sceglie dove salvare il file tramite un JFileChooser.
public class ManagerFileFattura{

    public static void generaFatturaTxt(Utente utente, Prodotto prodotto) {
        JFileChooser fileChooser = new JFileChooser(); // Crea un selettore di file per scegliere dove salvare la fattura
        fileChooser.setDialogTitle("Scegli dove salvare la fattura"); // Imposta il titolo della finestra di dialogo
        fileChooser.setSelectedFile(new File("Fattura_" + prodotto.getNome() + ".txt")); // Imposta il nome predefinito del file

        int scelta = fileChooser.showSaveDialog(null); // Mostra la finestra di dialogo per il salvataggio del file

        if (scelta == JFileChooser.APPROVE_OPTION) { // Se l'utente ha scelto un file
            File fileSelezionato = fileChooser.getSelectedFile(); // Ottiene il file selezionato

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileSelezionato))) { // Crea un BufferedWriter per scrivere nel file

                /*
                    Il BufferedWriter è una classe Java usata per scrivere testo in un file in modo efficiente.(della java.io)
                    BufferedWriter usa un buffer per memorizzare temporaneamente i dati prima di scriverli su disco.
                    Questo migliora le prestazioni rispetto alla scrittura diretta su disco, riducendo il numero di operazioni di I/O.
                    BufferedWriter è utile quando si scrivono grandi quantità di dati o quando si desidera ridurre il numero di accessi al disco.
                    Il fileWriter è una classe Java usata per scrivere caratteri in un file.
                */

                writer.write("===== FATTURA =====\n"); // Scrive l'intestazione della fattura
                writer.write("Data: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "\n"); // Scrive la data e l'ora attuale( presa dal sistema)
                writer.write("Utente: " + utente.getUsername() + "\n"); // Scrive il nome dell'utente che ha effettuato l'acquisto
                writer.write("Prodotto acquistato: " + prodotto.getNome() + "\n"); // Scrive il nome del prodotto acquistato
                writer.write("Descrizione: " + prodotto.getDescrizione() + "\n"); // Scrive la descrizione del prodotto
                writer.write("Categoria: " + prodotto.getCategoria() + "\n"); // Scrive la categoria del prodotto
                writer.write("Prezzo: " + prodotto.getPrezzo() + " €\n"); // Scrive il prezzo del prodotto
                writer.write("===================\n");
                JOptionPane.showMessageDialog(null, "Fattura salvata correttamente!"); // Mostra un messaggio di conferma all'utente
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Errore nel salvataggio della fattura: " + e.getMessage()); // Mostra un messaggio di errore se si verifica un'eccezione durante la scrittura del file
            }
        }
    }

    // Genera una fattura .txt per tutti i prodotti acquistati presenti nel carrello.
    // Mostra il totale complessivo alla fine.
    public static void generaFatturaDaCarrelloTxt(Utente utente, List<Prodotto> prodottiAcquistati) {
            JFileChooser fileChooser = new JFileChooser(); // Crea un selettore di file per scegliere dove salvare la fattura
            fileChooser.setDialogTitle("Scegli dove salvare la fattura"); // Imposta il titolo della finestra di dialogo
            fileChooser.setSelectedFile(new File("Fattura_" + utente.getUsername() + "_" + System.currentTimeMillis() + ".txt")); // Imposta il nome predefinito del file

            int scelta = fileChooser.showSaveDialog(null); // Mostra la finestra di dialogo per il salvataggio del file

            if (scelta == JFileChooser.APPROVE_OPTION) { // Se l'utente ha scelto un file
                File fileSelezionato = fileChooser.getSelectedFile(); // Ottiene il file selezionato

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileSelezionato))) { // Crea un BufferedWriter per scrivere nel file
                    writer.write("===== FATTURA =====\n");
                    writer.write("Data: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "\n");
                    writer.write("Utente: " + utente.getUsername() + "\n\n");

                    double totale = 0.0; // Inizializza il totale a 0
                    for (Prodotto prodotto : prodottiAcquistati) { //Scorre la lista dei prodotti acquistati
                        writer.write("Prodotto: " + prodotto.getNome() + "\n"); // Scrive il nome del prodotto
                        writer.write("Descrizione: " + prodotto.getDescrizione() + "\n"); // Scrive la descrizione del prodotto
                        writer.write("Categoria: " + prodotto.getCategoria() + "\n"); // Scrive la categoria del prodotto
                        writer.write("Prezzo: " + prodotto.getPrezzo() + " €\n"); // Scrive il prezzo del prodotto
                        writer.write("---------------------------\n"); // Scrive una linea di separazione
                        totale += prodotto.getPrezzo(); // Aggiunge il prezzo del prodotto al totale
                    }

                    //non appena ha finito di scorrere la lista dei prodotti, scrive il totale

                    writer.write("\nTOTALE: " + totale + " €\n"); // Scrive il totale complessivo
                    writer.write("===========================\n"); // Scrive una linea di chiusura

                    JOptionPane.showMessageDialog(null, "Fattura salvata correttamente!"); // Mostra un messaggio di conferma all'utente

                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Errore nel salvataggio della fattura: " + e.getMessage()); // Mostra un messaggio di errore se si verifica un'eccezione durante la scrittura del file
                }
            }
    }
}

