package com.example.pieter.memoire.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.pieter.memoire.ClickListeners.ButtonClickListener;
import com.example.pieter.memoire.Models.Theme;
import com.example.pieter.memoire.Persistence.ThemeDao;
import com.example.pieter.memoire.Persistence.ThemeDatabase;
import com.example.pieter.memoire.R;
import com.example.pieter.memoire.ViewHolders.ThemesViewHolder;

import java.util.List;

public class ThemeAdapter extends RecyclerView.Adapter<ThemesViewHolder> {

    private List<Theme> list;
    private Context context;
    private ButtonClickListener buttonClickListener;

    public ThemeAdapter(List<Theme> list, Context context, ButtonClickListener buttonClickListener) {
        this.list = list;
        this.context = context;
        this.buttonClickListener = buttonClickListener;
    }

    /**
     * Inflates the elements added to the Recyclerview
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ThemesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.theme_activity_card, parent, false);
        ThemesViewHolder themesViewHolder = new ThemesViewHolder(v);
        return themesViewHolder;
    }

    /**
     * Prompts the Viewholder to set the data of the Object
     * to the view. It also sets OnclickListeners to the various
     * Buttons added to the view item
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ThemesViewHolder holder, final int position) {
        holder.setData(list.get(position));
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogview = layoutInflater.inflate(R.layout.dialog_edit_theme, null);
                final EditText inputName = (EditText) dialogview.findViewById(R.id.input_name);
                inputName.setText(list.get(position).getName());
                Button btnCreateTheme = (Button) dialogview.findViewById(R.id.btn_create_theme);

                builder.setView(dialogview);
                final AlertDialog dialog = builder.show();

                btnCreateTheme.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (!inputName.getText().toString().isEmpty()) {

                            ThemeDao themeDao = ThemeDatabase.getInstance(context).getThemeDao();
                            list.get(position).setName(inputName.getText().toString());
                            themeDao.modifyTheme(list.get(position));
                            notifyDataSetChanged();
                            dialog.dismiss();


                        } else {
                            Toast.makeText(context, "Please fill in the name field!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        holder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClickListener.startIntentToCards(position);
            }
        });

    }

    /**
     * Returns the itemcount of Objects
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }
}


