package com.neuman.brutus.fragments;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.neuman.brutus.Home;
import com.neuman.brutus.R;
import com.neuman.brutus.adapters.ArrayAdapter;
import com.neuman.brutus.retrofit.Client;
import com.neuman.brutus.retrofit.models.Attributes;
import com.neuman.brutus.retrofit.models.ClusterResponse;
import com.neuman.brutus.retrofit.models.Clusters;
import com.neuman.brutus.retrofit.models.SimpleResponse;
import com.neuman.brutus.retrofit.models.UploadResponse;
import com.neuman.brutus.utils.DialogUtils;
import com.neuman.brutus.utils.ImageOps;
import com.neuman.brutus.utils.RomaOps;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class CreateRoma extends Fragment {

    private ArrayList<Attributes> attributes;
    private JsonArray attrsJson = new JsonArray();
    private JsonObject attr = new JsonObject();
    private JsonArray cluster_array = new JsonArray();
    private JsonArray image_attributes = new JsonArray();
    private ArrayList<String> cluster_raw = new ArrayList<>();
    private JsonObject add_roma_request = new JsonObject();
    private JsonObject add_cluster_request = new JsonObject();
    private NachoTextView nachoTextView;
    private RomaOps romaOps;
    private AssetFragment assets = new AssetFragment();
    private DialogUtils dialogs = new DialogUtils();
    private ImageOps imageOps = new ImageOps();
    private File image = null;
    private int current_attribute;
    private String last_captured_image_path;
    private View current_view;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_roma, container, false);
        romaOps = new RomaOps(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout add_roma_layout = view.findViewById(R.id.add_roma_layout);

        ImageButton add = view.findViewById(R.id.add_button_roma);
        add.setOnClickListener(v -> onClickOnCreateRoma(v));

        try { Bundle bundle = getArguments(); attributes = (ArrayList<Attributes>) bundle.getSerializable("attributes"); romaOps.existing_clusters = (ArrayList<Clusters>) bundle.getSerializable("clusters"); } catch (NullPointerException n) { Log.d("NPE", n.getMessage()); }

        if (attributes!=null && !attributes.toString().equals("false")) {

            int i = 0;

            add_roma_layout.addView(dialogs.makeTextView("Asset Code", getLayoutInflater()));

            View v1 = getLayoutInflater().inflate(R.layout.widget_input_text, null);
            TextInputEditText textInputEditText = v1.findViewById(R.id.textinputdis);
            textInputEditText.setTag("asset_code");
            textInputEditText.setHint("Scan your Asset QR Code");
            textInputEditText.addTextChangedListener(new GlobalTextWatcher(textInputEditText));
            add_roma_layout.addView(v1);

            add_roma_layout.addView(dialogs.makeTextView("Clusters", getLayoutInflater()));

            v1 = getLayoutInflater().inflate(R.layout.widget_chips, null);
            nachoTextView = v1.findViewById(R.id.chip_edtext);

            String[] cluster_suggestions = new String[romaOps.existing_clusters.size()];
            for (int m = 0; m<romaOps.existing_clusters.size();m++) { cluster_suggestions[m] = romaOps.existing_clusters.get(m).getCluster(); }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, cluster_suggestions);
            nachoTextView.setAdapter(adapter);
            nachoTextView.setThreshold(1);
            nachoTextView.enableEditChipOnTouch(false, true);
            nachoTextView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
            nachoTextView.setTag("clusters");
            nachoTextView.setHint("Hit Return to Add a Tag");
            nachoTextView.setOnFocusChangeListener((v, hasFocus) -> nachoTextView.chipifyAllUnterminatedTokens());

            add_roma_layout.addView(v1);

            while(i<attributes.size()) {
                final int j = i;
                switch (attributes.get(i).getVar_type()) {

                    case "string":

                        add_roma_layout.addView(dialogs.makeTextView(attributes.get(i).getName(), getLayoutInflater()));

                        v1 = getLayoutInflater().inflate(R.layout.widget_input_text, null);
                        textInputEditText = v1.findViewById(R.id.textinputdis);
                        textInputEditText.setHint(attributes.get(i).getPlaceholder());
                        textInputEditText.setTag(attributes.get(i).getId().toString());
                        textInputEditText.addTextChangedListener(new GlobalTextWatcher(textInputEditText));
                        add_roma_layout.addView(v1);
                        break;

                    case "boolean":

                        add_roma_layout.addView(dialogs.makeTextView(attributes.get(i).getName(), getLayoutInflater()));

                        v1 = getLayoutInflater().inflate(R.layout.widget_dropdown, null);
                        EditText editText = v1.findViewById(R.id.dropdown_lay);
                        editText.setText(attributes.get(i).getPlaceholder());
                        editText.setOnClickListener(v -> dialogs.showBoolDialog(v, getActivity()));
                        editText.setTag(attributes.get(i).getId().toString());
                        editText.addTextChangedListener(new GlobalEditTextWatcher(editText, i, "boolean"));
                        add_roma_layout.addView(v1);
                        break;

                    case "date":

                        add_roma_layout.addView(dialogs.makeTextView(attributes.get(i).getName(), getLayoutInflater()));

                        v1 = getLayoutInflater().inflate(R.layout.widget_dropdown, null);
                        editText = v1.findViewById(R.id.dropdown_lay);
                        editText.setText(attributes.get(i).getName());
                        editText.setOnClickListener(v->dialogs.dialogDatePickerLight(v, getResources(), getActivity().getFragmentManager()));
                        editText.setTag(attributes.get(i).getId().toString());
                        editText.addTextChangedListener(new GlobalEditTextWatcher(editText, i, "date"));
                        add_roma_layout.addView(v1);
                        break;

                    case "enum":

                        add_roma_layout.addView(dialogs.makeTextView(attributes.get(i).getName(), getLayoutInflater()));

                        v1 = getLayoutInflater().inflate(R.layout.widget_dropdown, null);
                        editText = v1.findViewById(R.id.dropdown_lay);
                        editText.setText(attributes.get(i).getPlaceholder());
                        editText.setOnClickListener((v) -> dialogs.showEnumDialog(v, attributes.get(j).getEnumsValues(), attributes.get(j).getName(), getActivity()));
                        editText.setTag(attributes.get(i).getId().toString());
                        editText.addTextChangedListener(new GlobalEditTextWatcher(editText, i, "enum"));
                        add_roma_layout.addView(v1);
                        break;

                    case "image":

                        add_roma_layout.addView(dialogs.makeTextView(attributes.get(i).getName(), getLayoutInflater()));

                        v1 = getLayoutInflater().inflate(R.layout.widget_image_picker, null);
                        TextView textView = v1.findViewById(R.id.image_picker_textview);
                        textView.setText(attributes.get(i).getPlaceholder());
                        textView.setTag(attributes.get(i).getId().toString());
                        v1.setOnClickListener(v -> makePickerDialog(j, v));
                        add_roma_layout.addView(v1);
                        break;
                }

                i++;
            }
        }
    }

    private class GlobalTextWatcher implements TextWatcher {

        TextInputEditText textInputEditText;
        Boolean flag = false;
        String attr_id;

        GlobalTextWatcher(TextInputEditText textInputEditText) {
            this.textInputEditText = textInputEditText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            flag = false;

            switch (textInputEditText.getTag().toString()) {
                case "asset_code":
                    add_roma_request.addProperty("code", s.toString());
                    break;
                case "clusters":
                    System.out.println("clusters");
                    break;
                default:
                    attr_id = (String) textInputEditText.getTag();
                    for (int k = 0; k < attrsJson.size(); k++) {
                        if(attrsJson.get(k).getAsJsonObject().get("id").getAsString().equals(attr_id)) {
                            flag = true; attrsJson.get(k).getAsJsonObject().addProperty("value", textInputEditText.getText().toString());
                        }
                    }
                    if (!flag) {
                        attr = new JsonObject(); attr.addProperty("id", attr_id);
                        attr.addProperty("value", textInputEditText.getText().toString()); attrsJson.add(attr);
                    }
            }
        }
    }

    private class GlobalEditTextWatcher implements TextWatcher {

        EditText editText;
        Boolean flag = false;
        String attr_id;
        int i;
        String type;

        GlobalEditTextWatcher(EditText editText, int i, String type) {
            this.editText = editText;
            this.i = i;
            this.type = type;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            flag = false;
            attr_id = (String) editText.getTag();
            for (int k = 0; k < attrsJson.size(); k++) {
                if(attrsJson.get(k).getAsJsonObject().get("id").getAsString().equals(attr_id)) {
                    flag = true;
                    if (type.equals("enum")) {
                        attrsJson.get(k).getAsJsonObject().addProperty("value", attributes.get(i).valToId(editText.getText().toString()));
                    } else {
                        attrsJson.get(k).getAsJsonObject().addProperty("value", editText.getText().toString());
                    }
                }
            }
            if (!flag) {
                attr = new JsonObject();
                attr.addProperty("id", attr_id);
                if (type.equals("enum")) {
                    attr.addProperty("value", attributes.get(i).valToId(editText.getText().toString()));
                } else {
                    attr.addProperty("value", editText.getText().toString());
                }
                attrsJson.add(attr);
            }
        }
    }


    private void makePickerDialog(int j, View v) {
        current_attribute = j;
        current_view = v;

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "WEBP_" + timeStamp + "_";
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());

        try {
            image = File.createTempFile(imageFileName,".webp", mediaStorageDir);
            last_captured_image_path = image.getAbsolutePath();
        } catch (IOException ex) {  }

        Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI = FileProvider.getUriForFile(getActivity(), "com.neuman.brutus.fileprovider", image);
        capture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

        Intent pick = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Capture an Image (or) Pick from Gallery?");
        builder.setPositiveButton("Capture", (dialogInterface, i) -> {
            if (EasyPermissions.hasPermissions(getActivity(), galleryPermissions)) {
                if (v.findViewById(R.id.attr_image_rel).getTag()!=null && v.findViewById(R.id.attr_image_rel).getTag().toString().equals("image_set")) {
                    startActivityForResult(capture, 0);
                } else {
                    startActivityForResult(capture, 0);
                }
            } else {
                EasyPermissions.requestPermissions(getActivity(), "Access for storage", 101, galleryPermissions);
            }
        });
        builder.setNegativeButton("Gallery", (dialogInterface, i) -> {
            if (EasyPermissions.hasPermissions(getActivity(), galleryPermissions)) {
                if (v.findViewById(R.id.attr_image_rel).getTag()!=null && v.findViewById(R.id.attr_image_rel).getTag().toString().equals("image_set")) {
                    startActivityForResult(pick, 1);
                } else {
                    startActivityForResult(pick, 1);
                }
            } else {
                EasyPermissions.requestPermissions(getActivity(), "Access for storage", 101, galleryPermissions);
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        ImageView imageview = current_view.findViewById(R.id.attr_image);
        RelativeLayout imagerel = current_view.findViewById(R.id.attr_image_rel);

        Bitmap image_bitmap;
        File file, file_output = null;
        boolean flag = true;

        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK) {
                    image_bitmap = imageOps.decodeFile(last_captured_image_path);
                    imagerel.setBackground(new BitmapDrawable(image_bitmap));
                    imageview.setVisibility(View.INVISIBLE);
                    imagerel.setTag("image_set");

                    file_output = new File(last_captured_image_path);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK) {
                    String selectedImagePath = imageOps.getAbsolutePath(imageReturnedIntent.getData(), getActivity());
                    image_bitmap = imageOps.decodeFile(selectedImagePath);
                    imagerel.setBackground(new BitmapDrawable(image_bitmap));
                    imageview.setVisibility(View.INVISIBLE);
                    imagerel.setTag("image_set");

                    file_output = new File(selectedImagePath);
                }
                break;
        }

        try {

            file = new Compressor(getActivity())
                    .setQuality(100)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + "Neuman" + File.separator + "images")
                    .compressToFile(file_output);

            String new_file_path = file.getAbsolutePath().replaceFirst("[.][^.]+$", ".webp");
            if (!file.getAbsolutePath().equals(new_file_path)) { boolean ret = file.renameTo(new File(new_file_path)); }

            for (int k = 0; k < image_attributes.size(); k++) {
                if(image_attributes.get(k).getAsJsonObject().get("id").getAsString().equals(attributes.get(current_attribute).getId().toString())) {
                    flag = false; image_attributes.get(k).getAsJsonObject().addProperty("value", new_file_path);
                }
            }
            if (flag) {
                attr = new JsonObject(); attr.addProperty("id", attributes.get(current_attribute).getId());
                attr.addProperty("value", new_file_path); image_attributes.add(attr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickOnCreateRoma(View v) {

        System.out.println("pout "+v.getId());
        if (v.getId() == R.id.add_button_roma) {
            cluster_array = new JsonArray();
            romaOps.clusters = new ArrayList<>();

            for (Chip chip: nachoTextView.getAllChips()) {
                boolean flag = true;
                for (Clusters cl: romaOps.existing_clusters) {
                    if(cl.getCluster().equals(chip.getText().toString())) {
                        romaOps.clusters.add(cl.getId());
                        flag = false;
                    }
                }
                if(flag) { cluster_array.add(chip.getText().toString()); }
            }

            add_cluster_request.addProperty("account", "1");
            add_cluster_request.add("cluster", new JsonParser().parse(cluster_array.toString()));

            add_roma_request.addProperty("roma_module_id", "1");
            add_roma_request.addProperty("account", "1");
            add_roma_request.add("attribute", new JsonParser().parse(attrsJson.toString()));

            Home homme = (Home) getActivity();
            Bundle bundle = new Bundle();
            bundle.putString("name", "Assets");
            assets.setArguments(bundle);

            if (add_roma_request.get("code")!=null && add_roma_request.get("code").getAsString().length() != 0 && nachoTextView.getAllChips().size()>0) {

                class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
                    protected Long doInBackground(URL... urls) {
                        imageOps.upload_images(getActivity(), image_attributes);

                        return null;
                    }

                    protected void onProgressUpdate(Integer... progress) {
                        // optionally report progress
                    }

                    protected void onPostExecute(Long result) {
                        romaOps.create_roma(getActivity(), add_cluster_request, add_roma_request, assets, homme);
                    }
                }

                new DownloadFilesTask().execute();
            }
        }
    }
}
