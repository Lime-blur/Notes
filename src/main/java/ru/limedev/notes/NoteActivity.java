package ru.limedev.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import ru.limedev.notes.model.parcels.NotesListItem;

import static ru.limedev.notes.model.Constants.NOTES_EXTRA_NOTES_LIST;
import static ru.limedev.notes.model.Constants.UNKNOWN_NUMBER_VALUE;

public class NoteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView noteMapView;
    private NotesListItem notesListItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        noteMapView = findViewById(R.id.activityNoteMap);
        noteMapView.onCreate(savedInstanceState);
        noteMapView.getMapAsync(this);
        Intent intent = getIntent();
        if (intent != null) {
            notesListItem = intent.getParcelableExtra(NOTES_EXTRA_NOTES_LIST);
            showListItemData();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        double ltd = notesListItem.getLtd();
        double lgd = notesListItem.getLgd();
        if (ltd != UNKNOWN_NUMBER_VALUE && lgd != UNKNOWN_NUMBER_VALUE) {
            LatLng latLng = new LatLng(ltd, lgd);
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(notesListItem.getName()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            noteMapView.setVisibility(View.VISIBLE);
        } else {
            noteMapView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        noteMapView.onSaveInstanceState(outState);
    }

    private void showListItemData() {
        if (notesListItem != null) {
            TextView noteName = findViewById(R.id.activityNoteName);
            TextView noteText = findViewById(R.id.activityNoteText);
            noteName.setText(notesListItem.getName());
            noteText.setText(notesListItem.getText());
        }
    }

    @Override
    protected void onPause() {
        noteMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        noteMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        noteMapView.onLowMemory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteMapView.onStop();
    }
}
