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
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.TimeUnit;

import ru.limedev.notes.R;
import ru.limedev.notes.model.db.NoteDbManager;
import ru.limedev.notes.model.exceptions.ParseDateException;
import ru.limedev.notes.model.pojo.Notification;

import static ru.limedev.notes.model.Constants.ADDED;
import static ru.limedev.notes.model.Constants.DB_RETURN_ERROR;
import static ru.limedev.notes.model.Constants.ERROR_DURING_INSERT;
import static ru.limedev.notes.model.Constants.FILL_FIELDS;
import static ru.limedev.notes.model.Constants.INCORRECT_DATETIME;
import static ru.limedev.notes.model.Constants.INCORRECT_FUTURE_DATETIME;
import static ru.limedev.notes.model.Utilities.checkStrings;
import static ru.limedev.notes.model.Utilities.showSnackbar;

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
            try {
                if (checkStrings(name, text)) {
                    if (checkStrings(date, time)) {
                        Notification notification = new Notification(date, time);
                        if (notification.isFutureDatetime()) {
                            notification.createNotification(getContext());
                            insertValues(name, text, notification.getStringDate(),
                                    notification.getStringTime());
                        } else {
                            showSnackbar(fragmentView, INCORRECT_FUTURE_DATETIME);
                        }
                    } else {
                        insertValues(name, text, null, null);
                    }
                } else {
                    showSnackbar(fragmentView, FILL_FIELDS);
                }
            } catch (ParseDateException e) {
                showSnackbar(fragmentView, INCORRECT_DATETIME);
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
                    handler.post(() -> showSnackbar(fragmentView, ADDED));
                } else {
                    handler.post(() -> showSnackbar(fragmentView, ERROR_DURING_INSERT));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        insertValuesThread.start();
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
