package com.example.pieter.memoire.Fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pieter.memoire.Activities.MainActivity;
import com.example.pieter.memoire.Adapters.MediaAdapter;
import com.example.pieter.memoire.ClickListeners.ClickListener;
import com.example.pieter.memoire.ClickListeners.ItemTouchListener;
import com.example.pieter.memoire.Models.Card;
import com.example.pieter.memoire.Models.Theme;
import com.example.pieter.memoire.R;
import com.example.pieter.memoire.Utilities.GridAutofitLayoutManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaFragment extends Fragment {


    @BindView(R.id.fab_photos)
    FloatingActionButton fab_photos;

    Theme theme;
    int position;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.media_activity, container, false);
        ButterKnife.bind(this, v);

        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        theme = getArguments().getParcelable("theme");
        position = getArguments().getInt("position",-1);
        Log.d("position1",Integer.toString(position));

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_media);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu_camera));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",theme);
                returnIntent.putExtra("position",position);
                Log.d("positionInFragment2",Integer.toString(position));
                getActivity().setResult(Activity.RESULT_OK,returnIntent);
                getActivity().finish();
            }
        });

        final MediaAdapter adapter = new MediaAdapter(theme.getCards(), getActivity());
        final RecyclerView mediaRecyclerView = (RecyclerView) v.findViewById(R.id.photosRecyclerView);
        mediaRecyclerView.setAdapter(adapter);

        //width is decided by Picasso in viewholder
        RecyclerView.LayoutManager layoutManager = new GridAutofitLayoutManager(getActivity(),400);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();


        mediaRecyclerView.setLayoutManager(layoutManager);

        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        mediaRecyclerView.setItemAnimator(itemAnimator);

        fab_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final View dialogview = getLayoutInflater().inflate(R.layout.dialog_create_media, null);
                final EditText inputTitle = (EditText) dialogview.findViewById(R.id.input_title);
                final EditText inputDescription = (EditText) dialogview.findViewById(R.id.input_description);
                ImageView image = (ImageView) dialogview.findViewById(R.id.input_media);
                Button btnCreateTheme = (Button) dialogview.findViewById(R.id.btn_create_media);
                builder.setView(dialogview);

                final AlertDialog dialog = builder.show();
                btnCreateTheme.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Card card = new Card(inputTitle.getText().toString(),inputDescription.getText().toString());
                        theme.addCardToList(card);
                        mediaRecyclerView.getRecycledViewPool().clear();
                        adapter.notifyItemInserted(theme.getCards().size() -1);
                        dialog.dismiss();
                    }
                });


            }
        });

        mediaRecyclerView.addOnItemTouchListener(new ItemTouchListener(getContext(), mediaRecyclerView, new ClickListener() {
            @Override
            public void onClick(View v, int position) {
                Toast.makeText(getActivity(), "short tap", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View v, int position) {
                theme.deleteCardFromList(position);
                mediaRecyclerView.getRecycledViewPool().clear();
                adapter.notifyItemRemoved(position);
            }
        }));

        return v;
    }
}
