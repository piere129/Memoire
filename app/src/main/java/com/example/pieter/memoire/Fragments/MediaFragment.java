package com.example.pieter.memoire.Fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
                final EditText inputFlickr = (EditText) dialogview.findViewById(R.id.flickr_input);
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

                            case 2:
                                Intent intent = new Intent();
                                intent.setType("video/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Video"), 2);
                                Toast.makeText(getActivity(), "lmao", Toast.LENGTH_SHORT).show();
                                break;

                            case 3:
                                Gson gson = new GsonBuilder().create();
                                FlickrData flickrData = gson.fromJson(getJSONFlickr(inputFlickr.getText().toString()), FlickrData.class);

                                if (flickrData.stat.equals("ok")) {
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
                            if (videoPath != null && !videoPath.isEmpty()) {
                                 final Card card = new Card(theme.getId(), videoPath, inputTitle.getText().toString()
                                        , inputDescription.getText().toString(), true);

                                        themeDatabase.getCardDao().addCard(card);
                                        //add card to theme?

                                theme.addCardToList(card);
                                mediaRecyclerView.getRecycledViewPool().clear();
                                adapter.notifyItemInserted(theme.getCards().size() - 1);
                                dialog.dismiss();
                            } else {
                                 Card card = new Card(theme.getId(), uri.toString(), inputTitle.getText().toString()
                                        , inputDescription.getText().toString(), false);

                                        themeDatabase.getCardDao().addCard(card);
                                        //add card to theme?

                                theme.addCardToList(card);
                                mediaRecyclerView.getRecycledViewPool().clear();
                                adapter.notifyItemInserted(theme.getCards().size() - 1);
                                dialog.dismiss();
                            }
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

                        Card card = theme.getCards().get(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        View dialogViewDetails = getLayoutInflater().inflate(R.layout.media_details, null);
                        TextView titleDetails = (TextView) dialogViewDetails.findViewById(R.id.title_details);
                        TextView descriptionDetails = (TextView) dialogViewDetails.findViewById(R.id.description_details);
                        ImageView imageDetails = (ImageView) dialogViewDetails.findViewById(R.id.image_details);
                        final VideoView videoDetails;

                        titleDetails.setText(card.getTitle());
                        descriptionDetails.setText(card.getDescription());
                        Picasso.get().load(Uri.parse(card.getUri())).into(imageDetails);

                        if (card.getHasVideo()) {
                            dialogViewDetails = getLayoutInflater().inflate(R.layout.media_details_video, null);
                            titleDetails = (TextView) dialogViewDetails.findViewById(R.id.title_details_video);
                            descriptionDetails = (TextView) dialogViewDetails.findViewById(R.id.description_details_video);
                            videoDetails = (VideoView) dialogViewDetails.findViewById(R.id.video_details);
                            titleDetails.setText(card.getTitle());
                            descriptionDetails.setText(card.getDescription());

                            videoDetails.setMediaController(new MediaController(getContext()));
                            videoDetails.setVideoPath(card.getUri());
                            videoDetails.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    videoDetails.start();
                                }
                            });


                        }

                        builder.setView(dialogViewDetails);
                        builder.show();
                    }

                    @Override
                    public void onLongClick(View v, int position) {

                        themeDatabase.getCardDao().deleteCard(theme.getCards().get(position));
                        theme.deleteCardFromList(position);
                        mediaRecyclerView.getRecycledViewPool().clear();
                        adapter.notifyItemRemoved(position);
                    }
                }));

        return v;
    }


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
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

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
}
