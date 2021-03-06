package com.example.pieter.memoire.Fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.DocumentsContract;
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
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.pieter.memoire.Adapters.MediaAdapter;
import com.example.pieter.memoire.ClickListeners.ClickListener;
import com.example.pieter.memoire.ClickListeners.ItemTouchListener;
import com.example.pieter.memoire.Models.Card;
import com.example.pieter.memoire.Models.Theme;
import com.example.pieter.memoire.Persistence.ThemeDatabase;
import com.example.pieter.memoire.R;
import com.example.pieter.memoire.Utilities.FlickrData;
import com.example.pieter.memoire.Utilities.GridAutofitLayoutManager;
import com.example.pieter.memoire.Utilities.Photo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;


import static android.app.Activity.RESULT_OK;

public class MediaFragment extends Fragment {


    @BindView(R.id.fab_photos)
    FloatingActionButton fab_photos;

    Theme theme;
    int position;
    ImageView dialogImage;
    Uri uri;
    String videoPath;
    ThemeDatabase themeDatabase;
    CompositeDisposable compositeDisposable;
    MediaAdapter adapter;
    RecyclerView mediaRecyclerView;


    /**
     * Initialises the Fragment and sets up the functionalities needed
     * for the Fragment to work properly, like the setup for the RecyclerView
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.media_activity, container, false);
        ButterKnife.bind(this, v);

        themeDatabase = ThemeDatabase.getInstance(getActivity());

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

        adapter = new MediaAdapter(theme.getCards(), getActivity());
        mediaRecyclerView = (RecyclerView) v.findViewById(R.id.photosRecyclerView);
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
                startCardDialog(false, -1);
            }
        });

        initOnClickRecyclerView();

        return v;
    }

    /**
     * Initialises the OnClick events for the RecyclerView.
     * <p>
     * This includes initialising an OnClick event showing a new dialog,
     * with its' contents being based upon whether the Card has a Video or an Image.
     * <p>
     * The LongClick Event deletes the item
     */
    private void initOnClickRecyclerView() {
        mediaRecyclerView.addOnItemTouchListener(new

                ItemTouchListener(getContext(), mediaRecyclerView, new

                ClickListener() {
                    @Override
                    public void onClick(View v, final int position) {

                        final Card card = theme.getCards().get(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        View dialogViewDetailsImage = getLayoutInflater().inflate(R.layout.media_details, null);
                        View dialogViewDetailsVideo = getLayoutInflater().inflate(R.layout.media_details_video, null);

                        TextView titleDetails = (TextView) dialogViewDetailsImage.findViewById(R.id.title_details);
                        TextView descriptionDetails = (TextView) dialogViewDetailsImage.findViewById(R.id.description_details);
                        TextView dateDetails = (TextView) dialogViewDetailsImage.findViewById(R.id.date_details);
                        ImageView imageDetails = (ImageView) dialogViewDetailsImage.findViewById(R.id.image_details);
                        Button editButtonImage = (Button) dialogViewDetailsImage.findViewById(R.id.btn_edit_card);
                        Button editButtonVideo = (Button) dialogViewDetailsVideo.findViewById(R.id.btn_edit_card_video);
                        Button cardToTimelineButtonImage = (Button) dialogViewDetailsImage.findViewById(R.id.btn_add_card_to_timeline);
                        Button cardToTimelineButtonVideo = (Button) dialogViewDetailsVideo.findViewById(R.id.btn_add_card_to_timeline_video);

                        if (card.getInTimeline() == 1) {
                            cardToTimelineButtonVideo.setText("Remove from timeline");
                            cardToTimelineButtonImage.setText("Remove from timeline");
                        } else {
                            cardToTimelineButtonVideo.setText("Add to timeline");
                            cardToTimelineButtonImage.setText("Add to timeline");
                        }

                        final VideoView videoDetails;

                        if (card.getHasVideo()) {
                            titleDetails = (TextView) dialogViewDetailsVideo.findViewById(R.id.title_details_video);
                            descriptionDetails = (TextView) dialogViewDetailsVideo.findViewById(R.id.description_details_video);
                            dateDetails = (TextView) dialogViewDetailsVideo.findViewById(R.id.date_details_video);
                            videoDetails = (VideoView) dialogViewDetailsVideo.findViewById(R.id.video_details);

                            videoDetails.setMediaController(new MediaController(getContext()));
                            videoDetails.setVideoPath(card.getUri());
                            videoDetails.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    videoDetails.start();
                                }
                            });


                        }

                        if (card.getHasVideo()) {
                            builder.setView(dialogViewDetailsVideo);
                        } else {
                            builder.setView(dialogViewDetailsImage);
                        }

                        final AlertDialog dialog = builder.show();

                        titleDetails.setText(card.getTitle());
                        descriptionDetails.setText(card.getDescription());
                        dateDetails.setText(card.getDate());
                        Picasso.get().load(Uri.parse(card.getUri())).into(imageDetails);
                        editButtonImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog.dismiss();
                                startCardDialog(true, position);
                            }
                        });
                        editButtonVideo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog.dismiss();
                                startCardDialog(true, position);
                            }
                        });

                        cardToTimelineButtonVideo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (card.getInTimeline() == 1) {
                                    card.setInTimeline(0);
                                } else {
                                    card.setInTimeline(1);
                                }
                                themeDatabase.getCardDao().modifyCard(card);
                                theme.editCardFromList(card, position);
                                adapter.notifyItemChanged(position);
                                dialog.dismiss();
                            }
                        });

                        cardToTimelineButtonImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (card.getInTimeline() == 1) {
                                    card.setInTimeline(0);
                                } else {
                                    card.setInTimeline(1);
                                }
                                themeDatabase.getCardDao().modifyCard(card);
                                theme.editCardFromList(card, position);
                                adapter.notifyItemChanged(position);
                                dialog.dismiss();
                            }
                        });

                    }

                    @Override
                    public void onLongClick(View v, final int position) {

                        new AlertDialog.Builder(getContext())
                                .setTitle("Delete Media")
                                .setMessage("Do you really want to delete this item?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("Delete card", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int buttonClicked) {
                                        themeDatabase.getCardDao().deleteCard(theme.getCards().get(position));
                                        theme.deleteCardFromList(position);
                                        mediaRecyclerView.getRecycledViewPool().clear();
                                        adapter.notifyItemRemoved(position);
                                    }})
                                .setNegativeButton("No", null).show();


                    }
                }));
    }


    /**
     * Saves and persists the data passed through the Create Media dialog screen,
     * with a switch case based upon which technique was used to import the media.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    saveImage(data);
                    videoPath = null;
                }
                break;

            case 1:
                if (resultCode == RESULT_OK) {
                    uri = data.getData();
                    dialogImage.setImageURI(uri);
                    videoPath = null;
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getActivity(), getRealPathFromURIForVideo(data.getData()), Toast.LENGTH_LONG).show();
                    videoPath = getRealPathFromURIForVideo(data.getData());
                    Bitmap bitmap2 = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MICRO_KIND);
                    dialogImage.setImageBitmap(bitmap2);
                }
                break;

        }
    }

    /**
     * Calculates the real path from an URI for a video.
     *
     * @param selectedVideoUri
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getRealPathFromURIForVideo(Uri selectedVideoUri) {
        String wholeID = DocumentsContract.getDocumentId(selectedVideoUri);
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Video.Media.DATA};
        String sel = MediaStore.Video.Media._ID + "=?";
        Cursor cursor = getContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, column, sel, new String[]{id}, null);
        String filePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    /**
     * Converts the Flickr Result to a JSON file and returns its' toString value
     *
     * @param inputTag
     * @return
     */
    private String getJSONFlickr(String inputTag) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringBuffer buffer = null;

        String tag;
        if (inputTag.isEmpty() || inputTag.equals(null)) {
            tag = "waffles";
        } else {
            tag = inputTag;
        }
        String urlToRead = "https://api.flickr.com/services/rest/" +
                "?method=flickr.photos.search&" +
                "api_key=a3c7977a32472ad4d62c1f2af1daf309" +
                "&tags=%s" +
                "&format=json" +
                "&nojsoncallback=1";

        String baseUrl = String.format(urlToRead, tag);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            URL url = new URL(baseUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            buffer = new StringBuffer();

            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer.toString();
    }

    /**
     * Saves the image imported by Camera
     *
     * @param data
     */
    private void saveImage(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");

        dialogImage.setImageBitmap(imageBitmap);
        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
        uri = getImageUri(getContext(), imageBitmap);

        // CALL THIS METHOD TO GET THE ACTUAL PATH
        Toast.makeText(getActivity(), "Here " + getRealPathFromURI(uri), Toast.LENGTH_LONG).show();
    }


    /**
     * Converts the result for importing images from gallery to a usable Image URI
     *
     * @param inContext
     * @param inImage
     * @return
     */
    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /**
     * Returns the realPath of a URI for a picture
     *
     * @param uri
     * @return
     */
    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    /**
     * Verifies permissions for localstorage, Camera, et cetera.
     */
    private void verifyPermissions() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.INTERNET};

        if (ContextCompat.checkSelfPermission(
                getActivity().getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), permissions[1]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), permissions[2]) ==
                        PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), permissions[3]) ==
                PackageManager.PERMISSION_GRANTED) {
            Log.d("granted", "permissions granted already!");
        } else {
            ActivityCompat.requestPermissions(getActivity(), permissions, 22);
        }
    }


    /**
     * Shows an edit / create media dialog based upon the isEdit boolean.
     * <p>
     * It implements a spinner with 4 options: taking a picture with camera, importing
     * a video / photo from the gallery or using the Flickr api to return a picture using
     * a custom tag.
     *
     * @param isEdit
     * @param position
     */
    private void startCardDialog(final boolean isEdit, final int position) {

         if (position != -1) {
            Card card = theme.getCards().get(position);
            uri = Uri.parse(card.getUri());
        }
         if (uri == null || !isEdit) {
            uri = Uri.parse("android.resource://com.example.pieter.memoire/drawable/default_image_card");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogview = isEdit ? getLayoutInflater().inflate(R.layout.dialog_edit_media, null) :
                getLayoutInflater().inflate(R.layout.dialog_create_media, null);
        final EditText inputTitle = isEdit ? (EditText) dialogview.findViewById(R.id.input_title_edit) :
                (EditText) dialogview.findViewById(R.id.input_title);
        Button btnCreateTheme = isEdit ? (Button) dialogview.findViewById(R.id.btn_edit_media) :
                (Button) dialogview.findViewById(R.id.btn_create_media);
        final EditText inputDescription = isEdit ? (EditText) dialogview.findViewById(R.id.input_description_edit) :
                (EditText) dialogview.findViewById(R.id.input_description);
        final EditText inputFlickr = isEdit ? (EditText) dialogview.findViewById(R.id.flickr_input_edit) :
                (EditText) dialogview.findViewById(R.id.flickr_input);
        dialogImage = isEdit ? (ImageView) dialogview.findViewById(R.id.input_media_edit) :
                (ImageView) dialogview.findViewById(R.id.input_media);
        final Spinner spinner = isEdit ? (Spinner) dialogview.findViewById(R.id.spinner_edit) :
                (Spinner) dialogview.findViewById(R.id.spinner);
        Button btnImportMedia = isEdit ? (Button) dialogview.findViewById(R.id.btn_import_media_edit) :
                (Button) dialogview.findViewById(R.id.btn_import_media);
        if (isEdit && position != -1) {
            Card card = theme.getCards().get(position);
            inputTitle.setText(card.getTitle());
            inputDescription.setText(card.getDescription());
        }

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 3) {
                    inputFlickr.setVisibility(View.VISIBLE);
                } else {
                    inputFlickr.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (uri != null) {
            Picasso.get().load(uri).fit().centerCrop().into(dialogImage);
        } else {
            dialogImage.setImageURI(Uri.parse(""));
        }
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

                    case 2:
                        Intent intent = new Intent();
                        intent.setType("video/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Video"), 2);
                        break;

                    case 3:
                        Gson gson = new GsonBuilder().create();
                        FlickrData flickrData = new FlickrData();
                        try {
                             flickrData = gson.fromJson(getJSONFlickr(inputFlickr.getText().toString()), FlickrData.class);
                            if (flickrData.stat.equals("ok")) {

                                if(flickrData.photos.photo.size() == 0)
                                {
                                    Toast.makeText(getActivity(), "Please enter a valid Search term!", Toast.LENGTH_LONG).show();
                                }
                                for (Photo data : flickrData.photos.photo) {
                                    // retrieve one photo
                                    // http://farm{farmid}.staticflickr.com/{server-id}/{id}_{secret}{size}.jpg

                                    String photoUrl = "http://farm%d.staticflickr.com/%s/%s_%s_n.jpg";
                                    String baseurl = String.format(photoUrl, data.farm, data.server, data.id, data.secret);
                                    uri = Uri.parse(baseurl);
                                    //Bitmap bitmap = getImageBitmapFromUrl(baseurl);
                                    //  dialogImage.setImageBitmap(bitmap);
                                    Picasso.get().load(uri).fit().centerCrop().into(dialogImage);
                                    videoPath = null;
                                    break;

                                }
                            }
                        }catch(Exception e)
                        {
                            Toast.makeText(getActivity(), "Something went wrong, please ensure you have a valid internet connection!", Toast.LENGTH_LONG).show();

                        }

                }


            }
        });
        builder.setView(dialogview);

        final AlertDialog dialog = builder.show();
        btnCreateTheme.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {

                if (inputTitle.getText().toString().isEmpty() || inputDescription.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Name and Description field can't be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    if (videoPath != null && !videoPath.isEmpty()) {


                        if (isEdit) {
                            Card card = theme.getCards().get(position);
                            card.setHasVideo(true);
                            card.setTitle(inputTitle.getText().toString());
                            card.setDescription(inputDescription.getText().toString());
                            card.setUri(videoPath);
                            card.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                            themeDatabase.getCardDao().modifyCard(card);
                            theme.editCardFromList(card, position);
                            mediaRecyclerView.getRecycledViewPool().clear();
                            adapter.notifyItemChanged(position);
                        } else {
                            Card card = new Card(theme.getId(), videoPath, inputTitle.getText().toString()
                                    , inputDescription.getText().toString(), true);
                            themeDatabase.getCardDao().addCard(card);
                            theme.addCardToList(card);
                            adapter.notifyItemInserted(theme.getCards().size() - 1);
                        }
                        dialog.dismiss();
                    } else {

                        if (isEdit) {
                            Card card = theme.getCards().get(position);
                            card.setHasVideo(false);
                            card.setTitle(inputTitle.getText().toString());
                            card.setDescription(inputDescription.getText().toString());
                            card.setUri(uri.toString());
                            card.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                            themeDatabase.getCardDao().modifyCard(card);
                            theme.editCardFromList(card, position);
                            adapter.notifyItemChanged(position);
                        } else {
                            Card card = new Card(theme.getId(), uri.toString(), inputTitle.getText().toString()
                                    , inputDescription.getText().toString(), false);
                            themeDatabase.getCardDao().addCard(card);
                            theme.addCardToList(card);
                            adapter.notifyItemInserted(theme.getCards().size() - 1);
                        }
                        dialog.dismiss();
                    }
                }
            }
        });
    }

}
