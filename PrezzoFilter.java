/*
    questa classe estende DocumentFilter e serve a filtrare l'input dell'utente
    in un campo di testo per assicurarsi che sia un numero valido con al massimo due decimali.
    La classe DocumentFilter è una classe di filtro per i documenti Swing, che consente di
    modificare il contenuto di un documento prima che venga visualizzato in un componente Swing.
    In questo caso, viene utilizzata per filtrare l'input dell'utente in un campo di testo
    per assicurarsi che sia un numero valido con al massimo due decimali.
*/

import javax.swing.text.*; // Importa le classi per la gestione del testo in Swing

public class PrezzoFilter extends DocumentFilter {
    @Override
    // Questo metodo viene chiamato quando l'utente inserisce una stringa nel campo di testo.
    // Il metodo controlla se la stringa è valida e, in caso affermativo, la inserisce nel documento.
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string == null) return;
        if (isValidInput(fb.getDocument().getText(0, fb.getDocument().getLength()) + string)) { // controlla se l'input è valido
            super.insertString(fb, offset, string, attr); // inserisce la stringa nel documento
        }
    }

    @Override
    // Questo metodo viene chiamato quando l'utente modifica una parte esistente del testo nel campo di testo.
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text == null) return;
        String oldText = fb.getDocument().getText(0, fb.getDocument().getLength()); // ottiene il testo attuale del documento
        String newText = new StringBuilder(oldText).replace(offset, offset + length, text).toString(); // crea una nuova stringa con il testo modificato
        if (isValidInput(newText)) { // controlla se il nuovo testo è valido
            super.replace(fb, offset, length, text, attrs); // sostituisce il testo nel documento
        }
    }

    // serve per cancellare il testo se non rispetta al formato
    private boolean isValidInput(String text) {
        return text.matches("^\\d*([.,]\\d{0,2})?$");
    }
    // il regex "^\\d*([.,]\\d{0,2})?$" e' come un filtro per la tastiera.
    // ^ indica l'inizio della stringa
    // \\d* significa che può esserci zero o più cifre (0-9) all'inizio.
    // ([.,]\\d{0,2})? significa che può esserci un punto o una virgola seguito da zero, uno o due numeri.
    // $ indica la fine della stringa
    // Quindi, in sintesi, questa regex permette solo numeri interi o decimali con al massimo due cifre dopo il punto o la virgola.
    // Se l'input non rispetta questo formato, il metodo restituisce false e non permette l'inserimento.
    // Se l'input è valido, restituisce true e permette l'inserimento.
    // In questo modo, l'input dell'utente viene filtrato e si assicura che sia un numero valido con al massimo due decimali.
    // Questo è utile per evitare errori di formattazione nei campi di input numerici, come i prezzi dei prodotti.

    //ESEMPIO: 123.45 restituira' true. ad122 restituira' false per "ad"(non verrano scritti), mentre restituira' true per i numeri "122"(verranno scritti)
}
