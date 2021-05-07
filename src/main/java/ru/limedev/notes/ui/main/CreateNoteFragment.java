package ru.limedev.notes.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import ru.limedev.notes.R;
import ru.limedev.notes.model.ApplicationSettings;
import ru.limedev.notes.model.db.NoteDbManager;
import ru.limedev.notes.model.exceptions.ParseDataException;
import ru.limedev.notes.model.pojo.Notification;

import static ru.limedev.notes.model.Constants.ADDED;
import static ru.limedev.notes.model.Constants.DB_RETURN_ERROR;
import static ru.limedev.notes.model.Constants.ERROR_DURING_INSERT;
import static ru.limedev.notes.model.Constants.FILL_FIELDS;
import static ru.limedev.notes.model.Constants.INCORRECT_DATETIME;
import static ru.limedev.notes.model.Constants.INCORRECT_FUTURE_DATETIME;
import static ru.limedev.notes.model.Constants.NOTIFICATION_SETTINGS_ID;
import static ru.limedev.notes.model.Constants.UNKNOWN_INT_VALUE;
import static ru.limedev.notes.model.Constants.UNKNOWN_NUMBER_VALUE;
import static ru.limedev.notes.model.Utilities.checkStrings;
import static ru.limedev.notes.model.Utilities.showSnackbar;

public class CreateNoteFragment extends Fragment implements OnMapReadyCallback {

    private View fragmentView;
    private MapView createNoteMapView;
    private SwitchCompat switchCompat;
    private LatLng latitudeLongitude;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_note, container, false);
        createNoteMapView = view.findViewById(R.id.createNoteMap);
        switchCompat = view.findViewById(R.id.createNoteSwitch);
        switchCompat.setOnCheckedChangeListener(this::onSwitchChanged);
        createNoteMapView.onCreate(savedInstanceState);
        createNoteMapView.getMapAsync(this);
        latitudeLongitude = new LatLng(0, 0);
        fragmentView = view;
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this::onClick);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onClick(@NotNull View v) {
        if (v.getId() == R.id.fab) {
            String name = getTextFromTextInputLayout(R.id.createNoteFieldName);
            String text = getTextFromTextInputLayout(R.id.createNoteFieldText);
            String date = getTextFromTextInputLayout(R.id.createNoteFieldDate);
            String time = getTextFromTextInputLayout(R.id.createNoteFieldTime);
            try {
                if (checkStrings(name, text)) {
                    double ltd = UNKNOWN_NUMBER_VALUE, lgd = UNKNOWN_NUMBER_VALUE;
                    if (switchCompat.isChecked()) {
                        ltd = latitudeLongitude.latitude;
                        lgd = latitudeLongitude.longitude;
                    }
                    if (checkStrings(date, time)) {
                        int currentId = ApplicationSettings.loadInt(NOTIFICATION_SETTINGS_ID);
                        Notification notification = new Notification(date, time, currentId, name, text);
                        if (notification.isFutureDatetime()) {
                            notification.createNotification(getContext());
                            insertValues(name, text, notification.getStringDate(),
                                    notification.getStringTime(), currentId, ltd, lgd);
                            ApplicationSettings.saveInt(NOTIFICATION_SETTINGS_ID, ++currentId);
                        } else {
                            showSnackbar(fragmentView, INCORRECT_FUTURE_DATETIME);
                        }
                    } else {
                        insertValues(name, text, null, null, UNKNOWN_INT_VALUE, ltd, lgd);
                    }
                } else {
                    showSnackbar(fragmentView, FILL_FIELDS);
                }
            } catch (ParseDataException e) {
                showSnackbar(fragmentView, INCORRECT_DATETIME);
            }
        }
    }

    private final GoogleMap.OnMarkerDragListener onMarkerDragListener = new GoogleMap.OnMarkerDragListener() {
        @Override
        public void onMarkerDragStart(@NonNull Marker marker) {}

        @Override
        public void onMarkerDrag(@NonNull Marker marker) {}

        @Override
        public void onMarkerDragEnd(@NonNull Marker marker) {
            latitudeLongitude = marker.getPosition();
        }
    };

    public void onSwitchChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            createNoteMapView.setVisibility(View.VISIBLE);
        } else {
            createNoteMapView.setVisibility(View.GONE);
        }
    }

    private void insertValues(String name, String text, String date, String time, int notificationId,
                              double ltd, double lgd) {
        final Handler handler = new Handler();
        restartFragment();
        Thread insertValuesThread = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
                if (NoteDbManager.insertValuesToDb(name, text, date, time, notificationId,
                        ltd, lgd) != DB_RETURN_ERROR) {
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
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            Fragment currentFragment = getFragmentManager().findFragmentById(this.getId());
            if (currentFragment != null) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.detach(currentFragment);
                fragmentTransaction.attach(currentFragment);
                fragmentTransaction.commit();
            }
        }
    }

    private @Nullable String getTextFromTextInputLayout(int textInputLayoutId) {
        TextInputLayout textInputLayout = fragmentView.findViewById(textInputLayoutId);
        if (textInputLayout != null) {
            EditText editText = textInputLayout.getEditText();
            if (editText != null) {
                return editText.getText().toString();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .draggable(true)
                .position(latitudeLongitude)
        );
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latitudeLongitude));
        googleMap.setOnMarkerDragListener(onMarkerDragListener);
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        createNoteMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        createNoteMapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        createNoteMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        createNoteMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        createNoteMapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        createNoteMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        createNoteMapView.onLowMemory();
    }
}
