package se2.a1.softwareengineering_einzelarbeit.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Starte mit Eingabe der Matrikelnummer.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}