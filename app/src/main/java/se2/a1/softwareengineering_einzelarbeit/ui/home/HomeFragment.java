package se2.a1.softwareengineering_einzelarbeit.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import se2.a1.softwareengineering_einzelarbeit.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Button server_send;
    private EditText matrikel_number;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        server_send = binding.buttonHomefragMatrikelnummer;
        matrikel_number = binding.edittnHomefragMatrikelnummer;

        final TextView textView_Matrikelnummer = binding.tvHomefragMatrikelnummer;
        final TextView textView_Serverantwort = binding.tvHomefragAntwort;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView_Serverantwort::setText);
        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}