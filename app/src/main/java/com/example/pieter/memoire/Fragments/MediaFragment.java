package com.example.pieter.memoire.Fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pieter.memoire.Activities.MainActivity;
import com.example.pieter.memoire.Activities.TestActivity;
import com.example.pieter.memoire.Adapters.MediaAdapter;
import com.example.pieter.memoire.ClickListeners.ClickListener;
import com.example.pieter.memoire.ClickListeners.ItemTouchListener;
import com.example.pieter.memoire.Models.Card;
import com.example.pieter.memoire.Models.Theme;
import com.example.pieter.memoire.R;
import com.example.pieter.memoire.Utilities.GridAutofitLayoutManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Array;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class MediaFragment extends Fragment {


    @BindView(R.id.fab_photos)
    FloatingActionButton fab_photos;

    Theme theme;
    int position;
    ImageView dialogImage;
    Uri uri;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.media_activity, container, false);
        ButterKnife.bind(this, v);

        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        theme = getArguments().getParcelable("theme");
        position = getArguments().getInt("position", -1);
        Log.d("position1", Integer.toString(position));

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_media);
        toolbar.setNavigationIcon(R.drawable.left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                getActivity().overridePendingTransition(R.anim.end, R.anim.start);
                returnIntent.putExtra("result", theme);
                returnIntent.putExtra("position", position);
                Log.d("positionInFragment2", Integer.toString(position));
                getActivity().setResult(RESULT_OK, returnIntent);
                getActivity().finish();
            }
        });

        final MediaAdapter adapter = new MediaAdapter(theme.getCards(), getActivity());
        final RecyclerView mediaRecyclerView = (RecyclerView) v.findViewById(R.id.photosRecyclerView);
        mediaRecyclerView.setAdapter(adapter);

        //width is decided by Picasso in viewholder
        RecyclerView.LayoutManager layoutManager = new GridAutofitLayoutManager(getActivity(), 400);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();


        mediaRecyclerView.setLayoutManager(layoutManager);

        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        mediaRecyclerView.setItemAnimator(itemAnimator);

        fab_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uri = Uri.parse("android.resource://com.example.pieter.memoire/drawable/default_image_card");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final View dialogview = getLayoutInflater().inflate(R.layout.dialog_create_media, null);
                final EditText inputTitle = (EditText) dialogview.findViewById(R.id.input_title);
                final EditText inputDescription = (EditText) dialogview.findViewById(R.id.input_description);
                dialogImage = (ImageView) dialogview.findViewById(R.id.input_media);
                dialogImage.setImageURI(uri);
                final Spinner spinner = (Spinner) dialogview.findViewById(R.id.spinner);
                ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                        getContext(), R.array.options, android.R.layout.simple_spinner_item);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getContext(), "position:" + spinner.getSelectedItemPosition(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                Button btnImportMedia = (Button) dialogview.findViewById(R.id.btn_import_media);

                btnImportMedia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        verifyPermissions();
                        switch (spinner.getSelectedItemPosition()) {
                            case 0:
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, 0);
                                break;//zero can be replaced with any action code}

                            case 1:
                                Intent choosePicture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                startActivityForResult(choosePicture, 1);
                                break;//zero can be replaced with any action code}

                        }


                    }
                });
                Button btnCreateTheme = (Button) dialogview.findViewById(R.id.btn_create_media);
                builder.setView(dialogview);

                final AlertDialog dialog = builder.show();
                btnCreateTheme.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View view) {
                        if (inputTitle.getText().toString().isEmpty() || inputDescription.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Name and Description field can't be empty!", Toast.LENGTH_SHORT).show();
                        } else {
                            Card card = new Card(uri.toString(), inputTitle.getText().toString(), inputDescription.getText().toString());
                            theme.addCardToList(card);
                            mediaRecyclerView.getRecycledViewPool().clear();
                            adapter.notifyItemInserted(theme.getCards().size() - 1);
                            dialog.dismiss();
                        }
                    }
                });


            }
        });

        mediaRecyclerView.addOnItemTouchListener(new

                ItemTouchListener(getContext(), mediaRecyclerView, new

                ClickListener() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    saveImage(data);
                }break;

            case 1: {
                if (resultCode == RESULT_OK) {
                    uri = data.getData();
                    dialogImage.setImageURI(uri);
                }break;
            }
        }
    }

    private void saveImage(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");

        dialogImage.setImageBitmap(imageBitmap);
        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
        uri = getImageUri(getContext(), imageBitmap);

        // CALL THIS METHOD TO GET THE ACTUAL PATH
        Toast.makeText(getActivity(), "Here " + getRealPathFromURI(uri), Toast.LENGTH_LONG).show();
    }


    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void verifyPermissions() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(
                getActivity().getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), permissions[1]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), permissions[2]) ==
                        PackageManager.PERMISSION_GRANTED) {
            Log.d("granted", "permissions granted already!");
        } else {
            ActivityCompat.requestPermissions(getActivity(), permissions, 22);
        }
    }
}
