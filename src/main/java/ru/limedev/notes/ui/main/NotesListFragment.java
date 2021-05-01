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
import java.util.Collections;
import java.util.Iterator;
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
    private final List<NotesListItem> listItems = Collections.synchronizedList(new ArrayList<>());
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);
        fragmentView = view;
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
                synchronized (listItems) {
                    listItems.clear();
                    NoteDbManager.loadValuesFromDb(listItems);
                }
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
                synchronized (listItems) {
                    Iterator<NotesListItem> it = listItems.iterator();
                    while (it.hasNext()) {
                        NotesListItem s = it.next();
                        if (s.getId() == itemId) {
                            it.remove();
                        }
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
