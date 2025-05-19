import java.util.EventListener;

public interface FormListener extends EventListener {
    void formEventListener(FormEvent e); // metodo per gestire l'evento del form

    /*
        Un FormListener è un’interfaccia personalizzata che definisce cosa fare quando
        un evento FormEvent viene generato. Ad esempio, potrebbe salvare un prodotto in una lista
        o aggiornarlo sul file.
    */
}
