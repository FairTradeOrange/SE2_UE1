package se2.a1.softwareengineering_einzelarbeit.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Hauptseiten-Model-Klasse, welche die Netzwerkverbindungen und Datenverarbeitung verwaltet.
 */
public class HomeViewModel extends ViewModel {

    // Objekte:
    private final MutableLiveData<String> serverData;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    // Variablen:
    private static String MY_MATR_NUMBER = "11907142";
    private static int SERVER_PORT = 20080;
    private static String SERVER_DOMAIN = "se2-submission.aau.at";


    /**
     * Konstruktor für das Home-ViewModel
     */
    public HomeViewModel() {
        serverData = new MutableLiveData<>();
        serverData.setValue("Starte mit Eingabe der Matrikelnummer.");
    }

    /**
     * Sendet eine gegebene Matrikelnummer zum SE2-Server,
     * die Antwort wird an das HomeFragment übergeben.
     * Inspiration von: Java Network Programming - Developing Network Applications, 4th Edition, Eliotte Rusty Harold, O'REILLY
     * Kapitel: Streams(25ff), Socket for Clients (241ff),
     * @param matrikelNummer - Matrikelnummer eines Studenten
     */
    public void sendMatrToServer(String matrikelNummer) {
        executorService.execute(() -> {
            try (Socket socket = new Socket(SERVER_DOMAIN, SERVER_PORT)) {
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                bufferedWriter.write(matrikelNummer);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                String serverResponse = bufferedReader.readLine();

                serverData.postValue(serverResponse);
            } catch (IOException e) {
                e.printStackTrace();
                serverData.postValue("Fehler bei der Kommunikation mit dem Server.");
            }
        });
    }


    public LiveData<String> getServerData() {
        return serverData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}