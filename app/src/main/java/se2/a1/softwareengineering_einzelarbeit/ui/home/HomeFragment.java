package se2.a1.softwareengineering_einzelarbeit.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Objects;

import se2.a1.softwareengineering_einzelarbeit.MainActivity;
import se2.a1.softwareengineering_einzelarbeit.R;
import se2.a1.softwareengineering_einzelarbeit.databinding.FragmentHomeBinding;

/**
 * Fragment der Hauptseite
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;
    Handler handler = new Handler();


    // UI-Objekte
    private Button btn_sendMatrToServer;
    private EditText editTextNumber_Matr;
    private TextView textView_Matrikelnummer;
    private TextView textView_Serverantwort;
    private ProgressBar simpleProgressBar;
    private HomeViewModel homeViewModel;
    private Switch simpleSwitch;

    // Variablen
    private static String MY_MATR_NUMBER = "11907142";
    private boolean finished = true;
    private int progress = 0;
    private final int maxProgress = 100;
    private final int updateIntervalInMillis = 1000;
    private final int duration = 40;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // UI-Objekte
        editTextNumber_Matr = binding.edittnHomefragMatrikelnummer;
        textView_Matrikelnummer = binding.tvHomefragMatrikelnummer;
        textView_Serverantwort = binding.tvHomefragAntwort;
        simpleProgressBar = binding.fragmentHomeProgressBar;
        simpleProgressBar.setVisibility(View.VISIBLE);
        simpleSwitch = binding.switchAufgabe;


        // Button - set ClickListener
        btn_sendMatrToServer = binding.buttonHomefragMatrikelnummer;
        btn_sendMatrToServer.setOnClickListener(this);

        homeViewModel.getServerData().observe(getViewLifecycleOwner(), textView_Serverantwort::setText);

        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public int getMatrMod7(){
        String Matr = editTextNumber_Matr.getText().toString();
        return (Integer.parseInt(Matr)%7);
    }

    public boolean isFinished(){
        if (finished){
            return true;
        }else{
            Toast.makeText(getActivity(), "Neue Anfrage in " + (duration - progress) + " Sekunden.", Toast.LENGTH_LONG).show();
            return false;
        }
    }


    /**
     * Progressbar-Visualisierung während Server-Kommunikation
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        String matrikelNummer = editTextNumber_Matr.getText().toString();

        // close keyboard :
        // https://stackoverflow.com/questions/1109022/how-can-i-close-hide-the-android-soft-keyboard-programmatically
        ((InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(requireView().getWindowToken(), 0);

        if (!simpleSwitch.isChecked()) {
            // Aufgabe 1:

            if (matrikelNummer.isEmpty()) {
                Toast.makeText(getContext(), "Bitte eine Matrikelnummer eingeben", Toast.LENGTH_SHORT).show();
            } else {
                if (isFinished()){
                    homeViewModel.sendMatrToServer(matrikelNummer);
                    // Progressbar-Logik
                    startProgressBarThread();
                }
            }
        }else {
            // Aufgabe 2:
            textView_Serverantwort.setText(MessageFormat.format("Matrikelnummer mod 7 = {0}\n", getMatrMod7()));

            AlternierendeQuersumme alternierendeQuersumme = new AlternierendeQuersumme(matrikelNummer);
            int altQuer = alternierendeQuersumme.solve();
            if (altQuer % 2 == 0) {
                textView_Serverantwort.append(MessageFormat.format("Die alternierende Quersumme von {0} ist gerade. ->{1}", matrikelNummer,altQuer));
            } else {
                textView_Serverantwort.append(MessageFormat.format("Die alternierende Quersumme von {0} ist ungerade. ->{1}", matrikelNummer,altQuer));
            }

        }
    }

    public void startProgressBarThread(){
        final int increment = maxProgress * updateIntervalInMillis / (duration * updateIntervalInMillis);

        // Thread für Progressbar
        new Thread(new Runnable() {
            public void run() {
                finished = false;
                while (progress < maxProgress) {
                    progress += increment;

                    // Aktualisieren der Progressbar
                    handler.post(new Runnable() {
                        public void run() {
                            simpleProgressBar.setProgress(progress);
                        }
                    });
                    try {
                        Thread.sleep(updateIntervalInMillis);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                progress = 0;
                finished = true;
                textView_Serverantwort.setText(R.string.starte_mit_eingabe_der_matrikelnummer);
            }
        }).start();
    }
}