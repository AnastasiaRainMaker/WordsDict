package com.project.wordsdict.main.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.project.wordsdict.R;
import com.project.wordsdict.main.views.TranslationActivity;
import com.project.wordsdict.model.WordModelRealm;
import java.text.DateFormat;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

public class RealmWordsListAdapter extends RealmRecyclerViewAdapter<WordModelRealm, RealmWordsListAdapter.MyViewHolder> {

    private Integer selectedItem;
    private Activity activity;
    private ActionMode mActionMode;
    private Realm mRealm;

    public RealmWordsListAdapter(OrderedRealmCollection<WordModelRealm> data, Activity activity, Realm mRealm) {
        super(data, true);
        setHasStableIds(true);
        this.activity = activity;
        this.mRealm = mRealm;
        deselectAll();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.word_row, parent, false);
        return new MyViewHolder(itemView);
    }

    private void deselectAll(){
        mRealm.executeTransaction(realm -> {
            for (WordModelRealm word : realm.where(WordModelRealm.class).findAll()) {
                word.setSelected(false);
            }
        });
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        final WordModelRealm obj = getItem(position);
        holder.data = obj;

        if ((holder.data != null ? holder.data.getCreationDate() : null) != null) {
            holder.date.setText(DateFormat.getDateInstance().format(obj.getCreationDate()));
        } else {
            holder.date.setText("");
        }
        holder.priority.setText(String.format("Сложность %s", obj != null ? obj.getWordPriority() : ""));
        holder.title.setText(obj != null ? obj.getWordName() : "");
        holder.frequency.setText(String.format(
                activity.getResources().getString(R.string.word_frequency_text),
                obj != null ? obj.getWordFrequency() : ""));
        holder.itemView.setOnClickListener((v -> activity.startActivity(new Intent(activity, TranslationActivity.class)
                .putExtra("translation", obj != null ? obj.getWordTranslation() : "")
                .putExtra("word", obj != null ? obj.getWordName() : ""))));
        holder.itemView.setOnLongClickListener(v-> {
            if (mActionMode != null) {
                return false;
            }
            mActionMode = activity.startActionMode(mActionModeCallback);
            mRealm.executeTransaction(realm -> {
                if (obj != null) {
                    obj.setSelected(true);
                }
            });
            selectedItem = holder.getAdapterPosition();
            return true;
        });
        assert obj != null;
        if (obj.isSelected()){
            holder.itemView.setBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
        } else
        if (obj.getWordPriority() > 0){
            holder.itemView.setBackgroundColor(activity.getResources().getColor(R.color.word_priority_color));
        } else {
            holder.itemView.setBackgroundColor(activity.getResources().getColor(R.color.word_row_color));
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView frequency;
        private TextView date;
        private TextView priority;
        public WordModelRealm data;

        MyViewHolder(View view) {
            super(view);
            priority = view.findViewById(R.id.word_priority);
            title = view.findViewById(R.id.word_title);
            frequency = view.findViewById(R.id.word_frequency);
            date = view.findViewById(R.id.word_date);
        }
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.word_context_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (selectedItem != null) {
                WordModelRealm mItem = getItem(selectedItem);
                switch (item.getItemId()) {
                    case R.id.context_delete:
                        mRealm.executeTransaction(realm -> mItem.deleteFromRealm());
                        handleMenuFinish(mode);
                        return true;
                    case R.id.context_word_difficalty_up:
                        mRealm.executeTransaction(realm -> {
                            mItem.setWordPriority(mItem.getWordPriority() + 1);
                            mItem.setSelected(false);
                        });
                        handleMenuFinish(mode);
                        return true;
                    case R.id.context_word_difficalty_down:
                        mRealm.executeTransaction(realm -> {
                            if (mItem.getWordPriority() > 0)
                                mItem.setWordPriority(mItem.getWordPriority() - 1);
                            mItem.setSelected(false);
                        });
                        handleMenuFinish(mode);
                        return true;
                    default:
                        return false;
                }
            } else return false;
        }



        public void handleMenuFinish(ActionMode mode) {
            deselectAll();
            notifyItemChanged(selectedItem);
            mode.finish();
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (selectedItem != null) {
                WordModelRealm mItem = getItem(selectedItem);
                if (mItem != null) {
                    mActionMode = null;
                    mRealm.executeTransaction(realm -> mItem.setSelected(false));
                    selectedItem = null;
                    notifyDataSetChanged();
                }
            }
        }
    };

    public void menuFinish() {
        if (mActionMode != null) {
            mActionMode.finish();
        }
    }

}