package ru.limedev.notes.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.TimeUnit;

import ru.limedev.notes.R;
import ru.limedev.notes.model.db.NoteDbManager;
import ru.limedev.notes.model.exceptions.ParseStringException;
import ru.limedev.notes.model.pojo.Datetime;

import static ru.limedev.notes.model.Constants.ACTION;
import static ru.limedev.notes.model.Constants.ADDED;
import static ru.limedev.notes.model.Constants.DB_RETURN_ERROR;
import static ru.limedev.notes.model.Constants.ERROR_DURING_INSERT;
import static ru.limedev.notes.model.Constants.FILL_FIELDS;
import static ru.limedev.notes.model.Constants.INCORRECT_DATETIME;
import static ru.limedev.notes.model.Utilities.checkStrings;

public class CreateNoteFragment extends Fragment implements View.OnClickListener {

    private View fragmentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_note, container, false);
        fragmentView = view;
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            String name = getTextFromTextInputLayout(R.id.createNoteFieldName);
            String text = getTextFromTextInputLayout(R.id.createNoteFieldText);
            String date = getTextFromTextInputLayout(R.id.createNoteFieldDate);
            String time = getTextFromTextInputLayout(R.id.createNoteFieldTime);
            if (fragmentView != null) {
                try {
                    Datetime datetime = new Datetime(date, time);
                    if (checkStrings(name, text)) {
                        insertValues(name, text, datetime.getDate(), datetime.getTime());
                    } else {
                        showSnackbar(FILL_FIELDS);
                    }
                } catch (ParseStringException e) {
                    showSnackbar(INCORRECT_DATETIME);
                }
            }
        }
    }

    private void insertValues(String name, String text, String date, String time) {
        final Handler handler = new Handler();
        restartFragment();
        Thread insertValuesThread = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
                if (NoteDbManager.insertValuesToDb(name, text, date, time) != DB_RETURN_ERROR) {
                    handler.post(() -> showSnackbar(ADDED));
                } else {
                    handler.post(() -> showSnackbar(ERROR_DURING_INSERT));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        insertValuesThread.start();
    }

    private void showSnackbar(String text) {
        Snackbar.make(fragmentView, text, Snackbar.LENGTH_LONG).setAction(ACTION, null).show();
    }

    private void restartFragment() {
        Fragment currentFragment = getFragmentManager().findFragmentById(this.getId());
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(currentFragment);
        fragmentTransaction.attach(currentFragment);
        fragmentTransaction.commit();
    }

    private @Nullable String getTextFromTextInputLayout(int textInputLayoutId) {
        TextInputLayout textInputLayout = fragmentView.findViewById(textInputLayoutId);
        if (textInputLayout != null) {
            return textInputLayout.getEditText().getText().toString();
        } else {
            return null;
        }
    }
}
