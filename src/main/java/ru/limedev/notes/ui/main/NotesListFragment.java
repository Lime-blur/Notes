package ru.limedev.notes.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ru.limedev.notes.R;
import ru.limedev.notes.model.beans.NotesListItem;
import ru.limedev.notes.model.db.NoteDbManager;

import static ru.limedev.notes.model.Constants.ACTION;
import static ru.limedev.notes.model.Constants.REMOVED;

public class NotesListFragment extends Fragment {

    private View fragmentView;
    private RecyclerView recyclerView;
    private List<NotesListItem> listItems;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);
        fragmentView = view;
        listItems = new ArrayList<>();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = fragmentView.findViewById(R.id.notesListRecyclerView);
        swipeRefreshLayout = fragmentView.findViewById(R.id.notesListSwipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this::loadValues);
        loadValues();
    }

    private void loadValues() {
        final Handler handler = new Handler();
        Thread loadValuesThread = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
                listItems = NoteDbManager.loadValuesFromDb(listItems);
                handler.post(new Thread(this::updateUI));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        loadValuesThread.start();
    }

    private void removeValues(long itemId) {
        final Handler handler = new Handler();
        Thread removeValuesThread = new Thread(() -> {
            boolean removed = NoteDbManager.removeValuesFromDb(itemId);
            if (removed) {
                for (NotesListItem item : listItems) {
                    if (item.getId() == itemId) {
                        listItems.remove(item);
                    }
                }
                handler.post(new Thread(() -> {
                    updateUI();
                    Snackbar.make(fragmentView, REMOVED, Snackbar.LENGTH_LONG)
                            .setAction(ACTION, null).show();
                }));
            }
        });
        removeValuesThread.start();
    }

    private void updateUI() {
        NotesRecyclerViewAdapter notesAdapter = new NotesRecyclerViewAdapter(getContext(), listItems) {
            @Override
            public void onRemoveItem(long itemId) {
                removeValues(itemId);
            }
        };
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(notesAdapter);
        swipeRefreshLayout.setRefreshing(false);
    }
}
