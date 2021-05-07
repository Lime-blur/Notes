package ru.limedev.notes.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.limedev.notes.R;
import ru.limedev.notes.model.parcels.NotesListItem;

public abstract class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<NotesListItem> listItems;

    public NotesRecyclerViewAdapter(Context context, List<NotesListItem> listItems) {
        this.inflater = LayoutInflater.from(context);
        this.listItems = listItems;
    }

    public abstract void onRemoveItem(long itemId);
    public abstract void onClickItem(NotesListItem item);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.notes_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotesListItem listItem = listItems.get(position);
        holder.holderFieldName.setText(listItem.getName());
        holder.holderFieldText.setText(listItem.getText());
        if (listItem.getDate() != null && listItem.getTime() != null) {
            holder.holderFieldDatetime.setText(listItem.getDatetime());
        }
        holder.holderRemoveButton.setOnClickListener(v -> onRemoveItem(listItem.getId()));
        holder.itemView.setOnClickListener(v -> onClickItem(listItem));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public List<NotesListItem> getNotesItems() {
        return listItems;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView holderFieldName, holderFieldText, holderFieldDatetime;
        final Button holderRemoveButton;

        ViewHolder(View view) {
            super(view);
            holderFieldName = view.findViewById(R.id.noteListName);
            holderFieldText = view.findViewById(R.id.noteListText);
            holderFieldDatetime = view.findViewById(R.id.noteListDatetime);
            holderRemoveButton = view.findViewById(R.id.noteListDelete);
        }
    }
}
