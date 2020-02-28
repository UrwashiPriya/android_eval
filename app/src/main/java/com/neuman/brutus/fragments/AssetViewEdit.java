package com.neuman.brutus.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
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
import com.neuman.brutus.offline.mode.AccountOpsOffSync;
import com.neuman.brutus.retrofit.Client;
import com.neuman.brutus.retrofit.models.AttributeReponse;
import com.neuman.brutus.retrofit.models.Attributes;
import com.neuman.brutus.retrofit.models.ClusterResponse;
import com.neuman.brutus.retrofit.models.Clusters;
import com.neuman.brutus.retrofit.models.Roma;
import com.neuman.brutus.retrofit.models.SimpleResponse;
import com.neuman.brutus.utils.DialogUtils;
import com.neuman.brutus.utils.Globals;
import com.neuman.brutus.utils.ImageOps;
import com.neuman.brutus.utils.RomaOps;
import com.neuman.brutus.utils.EditTextWatcher;
import com.neuman.brutus.utils.TextInputEditTextWatcher;
import com.wdullaer.materialdatetimepicker.Utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import id.zelory.compressor.Compressor;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class AssetViewEdit extends Fragment {

    private Roma roma;
    private LinearLayout asset_layout;
    private TextInputEditText textInputEditText;
    private DialogUtils dialogs = new DialogUtils();
    private NachoTextView nachoTextView;
    private JsonObject attr = new JsonObject();
    private JsonArray file_attributes = new JsonArray();
    private JsonObject update_roma_request = new JsonObject();
    private JsonObject add_cluster_request = new JsonObject();
    private ImageOps imageOps = new ImageOps();
    private File image = null;
    private int current_attribute;
    private String last_captured_image_path;
    private View current_view;
    private View v;
    private Globals utils;
    private RomaOps romaOps;
    private Home homme;
    private EditText editText;
    AccountOpsOffSync accountOpsOffSync = new AccountOpsOffSync();
    private String[] galleryPermissions = { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA };

    AssetViewEdit() {
        romaOps = new RomaOps(getActivity());
        utils  = new Globals();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_assets, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        homme = (Home) getActivity();

        try { roma = (Roma) getArguments().getSerializable("asset"); utils.roma_attributes = roma.getAttributes(); } catch (NullPointerException n) { Log.d("NPE", n.getMessage()); }

        JsonObject tag_params = new JsonObject();
        tag_params.addProperty("offset", "0");
        tag_params.addProperty("limit", "10");
        tag_params.addProperty("account", "1");

        ClusterResponse clusterResponse = accountOpsOffSync.fetchClustersOffSync(tag_params, getActivity(), new retrofit2.Callback<ClusterResponse>() {
            @Override
            public void onResponse(Call<ClusterResponse> call, Response<ClusterResponse> response) {
                if (response.body()!=null && response.body().getSuccess().contains("true")) {
                    romaOps.existing_clusters = response.body().getClusters();
                    make_asset_view(getView());
                    accountOpsOffSync.writeResponseOffSync(response.body(), tag_params, getActivity(), "subbed_clusters", 1);
                }
            }

            @Override
            public void onFailure(Call<ClusterResponse> call, Throwable t) { }
        });

        if (clusterResponse != null && clusterResponse.getSuccess().contains("true")) {
            romaOps.existing_clusters = clusterResponse.getClusters();
            make_asset_view(getView());
        }
    }

    private void make_asset_view(View view) {
        if (roma != null && !roma.toString().equals("false")) {
            asset_layout = view.findViewById(R.id.asset_layout);
            ((ImageButton) view.findViewById(R.id.enable_edit_button_roma)).setOnClickListener(vee->enableRomaEdit(vee, view));
            ((ImageButton) view.findViewById(R.id.apply_edit_button_roma)).setOnClickListener(vee->applyRomaEdits(vee, view));
            ((ImageButton) view.findViewById(R.id.bt_close)).setOnClickListener(vee->homme.fragmentHandler.transition("ASSETS", null));

            ((TextView) view.findViewById(R.id.view_code)).setText(roma.getCode());

            update_roma_request.addProperty("code", roma.getCode());

            asset_layout.addView(dialogs.makeTextView("Clusters", getLayoutInflater()));

            String[] cluster_suggestions = new String[romaOps.existing_clusters.size()];
            for (int m = 0; m < romaOps.existing_clusters.size(); m++) { cluster_suggestions[m] = romaOps.existing_clusters.get(m).getCluster(); }
            v = getLayoutInflater().inflate(R.layout.widget_chips, null);
            nachoTextView = v.findViewById(R.id.chip_edtext);
            nachoTextView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, cluster_suggestions));
            nachoTextView.setText(roma.fetchClustersAsList());
            nachoTextView.enableEditChipOnTouch(false, true);
            nachoTextView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
            nachoTextView.setOnFocusChangeListener((vee, hasFocus) -> nachoTextView.chipifyAllUnterminatedTokens());
            v.setEnabled(false);
            asset_layout.addView(v);

            if (utils.roma_attributes!=null && !utils.roma_attributes.toString().equals("false")) {

                int i = 0;
                while(i < utils.roma_attributes.size()) {

                    final int j = i;
                    int visibility = View.VISIBLE;

                    if (utils.roma_attributes.get(i).getAttr_data_id() == null && utils.roma_attributes.get(i).getAttr_data_value() == null && utils.roma_attributes.get(i).getRaw_data() == null) {
                        visibility = View.GONE;
                    }

                    switch (utils.roma_attributes.get(i).getVar_type()) {

                        case "string":

                            v = dialogs.makeTextView(utils.roma_attributes.get(i).getName(), getLayoutInflater());
                            v.setTag("attr_label"+utils.roma_attributes.get(i).getId().toString());
                            v.setVisibility(visibility);
                            asset_layout.addView(v);

                            v = getLayoutInflater().inflate(R.layout.widget_input_text, null);
                            textInputEditText = v.findViewById(R.id.textinputdis);
                            textInputEditText.setHint(utils.roma_attributes.get(i).getPlaceholder());
                            textInputEditText.setTag(utils.roma_attributes.get(i).getId().toString());
                            textInputEditText.setText(utils.roma_attributes.get(i).getRaw_data());
                            textInputEditText.addTextChangedListener(new TextInputEditTextWatcher(textInputEditText, utils));

                            v.setEnabled(false);
                            v.setVisibility(visibility);
                            asset_layout.addView(v);
                            break;

                        case "boolean":

                            v = dialogs.makeTextView(utils.roma_attributes.get(i).getName(), getLayoutInflater());
                            v.setTag("attr_label"+utils.roma_attributes.get(i).getId().toString());
                            v.setVisibility(visibility);
                            asset_layout.addView(v);

                            v = getLayoutInflater().inflate(R.layout.widget_dropdown, null);
                            editText = v.findViewById(R.id.dropdown_lay);
                            editText.setHint(utils.roma_attributes.get(i).getPlaceholder());
                            editText.setText(utils.roma_attributes.get(i).getRaw_data());
                            editText.setOnClickListener(vee -> dialogs.showBoolDialog(vee, getActivity()));
                            editText.setTag(utils.roma_attributes.get(i).getId().toString());
                            editText.addTextChangedListener(new EditTextWatcher(editText, i, "boolean", utils));

                            v.setEnabled(false);
                            v.setVisibility(visibility);
                            asset_layout.addView(v);
                            break;

                        case "date":

                            v = dialogs.makeTextView(utils.roma_attributes.get(i).getName(), getLayoutInflater());
                            v.setTag("attr_label"+utils.roma_attributes.get(i).getId().toString());
                            v.setVisibility(visibility);
                            asset_layout.addView(v);

                            v = getLayoutInflater().inflate(R.layout.widget_dropdown, null);
                            editText = v.findViewById(R.id.dropdown_lay);
                            editText.setHint(utils.roma_attributes.get(i).getName());
                            editText.setText(utils.roma_attributes.get(i).getRaw_data());
                            editText.setOnClickListener(vee->dialogs.dialogDatePickerLight(vee, getResources(), getActivity().getFragmentManager()));
                            editText.setTag(utils.roma_attributes.get(i).getId().toString());
                            editText.addTextChangedListener(new EditTextWatcher(editText, i, "date", utils));

                            v.setEnabled(false);
                            v.setVisibility(visibility);
                            asset_layout.addView(v);
                            break;

                        case "enum":

                            v = dialogs.makeTextView(utils.roma_attributes.get(i).getName(), getLayoutInflater());
                            v.setTag("attr_label"+utils.roma_attributes.get(i).getId().toString());
                            v.setVisibility(visibility);
                            asset_layout.addView(v);

                            v = getLayoutInflater().inflate(R.layout.widget_dropdown, null);
                            editText = v.findViewById(R.id.dropdown_lay);
                            editText.setHint(utils.roma_attributes.get(i).getPlaceholder());
                            editText.setOnClickListener((vee) -> dialogs.showEnumDialog(vee, utils.roma_attributes.get(j).getEnumsValues(), utils.roma_attributes.get(j).getName(), getActivity()));
                            editText.setTag(utils.roma_attributes.get(i).getId().toString());
                            editText.setText(utils.roma_attributes.get(i).getAttr_data_value());
                            editText.addTextChangedListener(new EditTextWatcher(editText, i, "enum", utils));

                            v.setEnabled(false);
                            v.setVisibility(visibility);
                            asset_layout.addView(v);
                            break;

                        case "image":

                            v = dialogs.makeTextView(utils.roma_attributes.get(i).getName(), getLayoutInflater());
                            v.setTag("attr_label"+utils.roma_attributes.get(i).getId().toString());
                            v.setVisibility(visibility);
                            asset_layout.addView(v);

                            v = getLayoutInflater().inflate(R.layout.widget_image_picker, null);
                            TextView textView = v.findViewById(R.id.image_picker_textview);
                            textView.setText(utils.roma_attributes.get(i).getPlaceholder());
                            textView.setTag(utils.roma_attributes.get(i).getId().toString());
                            v.setOnClickListener(vee -> makePickerDialog(j, vee));

                            ImageView imv = v.findViewById(R.id.attr_image);
                            RelativeLayout rel = v.findViewById(R.id.attr_image_rel);

                            Glide.with(getActivity()).load(utils.FILE_UPLOAD_ENDPOINT+"1/"+utils.roma_attributes.get(i).getRaw_data()).error(R.drawable.placeholder).into(new SimpleTarget<GlideDrawable>() {
                                @Override
                                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                    imv.setVisibility(View.INVISIBLE);
                                    rel.setBackground(resource);
                                }
                            });

                            v.setEnabled(false);
                            v.setVisibility(visibility);
                            asset_layout.addView(v);
                            break;
                    }

                    i++;
                }
            }
        }
    }

    private void enableRomaEdit(View v, View global_view) {

        ImageButton ib = global_view.findViewById(R.id.enable_edit_button_roma);
        ImageButton ibedit = global_view.findViewById(R.id.apply_edit_button_roma);
        ib.setVisibility(View.GONE);
        ibedit.setVisibility(View.VISIBLE);

        asset_layout = global_view.findViewById(R.id.asset_layout);

        for (int k=0; k<asset_layout.getChildCount(); k++) {
            View cur = asset_layout.getChildAt(k);
            cur.setVisibility(View.VISIBLE);
            cur.setEnabled(true);
        }
    }




    private void applyRomaEdits(View v, View global_view) {
        JsonArray new_clusters = new JsonArray();
        romaOps.current_roma_clusters = new ArrayList<>();

        for (Chip chip: nachoTextView.getAllChips()) {
            boolean flag = true;
            for (Clusters cl: romaOps.existing_clusters) {
                if(cl.getCluster().equals(chip.getText().toString())) {
                    romaOps.current_roma_clusters.add(cl.getId());
                    flag = false;
                }
            }

            if(flag) { new_clusters.add(chip.getText().toString()); }
        }

        if (update_roma_request.get("code")!=null && update_roma_request.get("code").getAsString().length() != 0 && nachoTextView.getAllChips().size()>0) {

            add_cluster_request.addProperty("account", "1");
            add_cluster_request.add("cluster", new JsonParser().parse(new_clusters.toString()));

            class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
                protected Long doInBackground(URL... urls) {
                    utils.attribute_request = imageOps.upload_files_as_attributes(file_attributes, utils.attribute_request, update_roma_request.get("code").getAsString(), getActivity());
                    return null;
                }

                protected void onProgressUpdate(Integer... progress) { }

                protected void onPostExecute(Long result) {
                    update_roma_request.add("attribute", new JsonParser().parse(utils.attribute_request.toString()));
                    Boolean isClusAddDone = accountOpsOffSync.createClusterOffSync(add_cluster_request, getActivity(), new Callback<ClusterResponse>() {
                        @Override
                        public void onResponse(Call<ClusterResponse> call, Response<ClusterResponse> response) {
                            if (response.body() != null && response.body().getSuccess().equals("true")) {
                                for (Clusters cl : response.body().getClusters()) {
                                    romaOps.existing_clusters.add(cl);
                                    romaOps.current_roma_clusters.add(cl.getId());
                                }

                                update_roma_request.add("cluster", new JsonParser().parse(romaOps.current_roma_clusters.toString()));
                                accountOpsOffSync.editRomaOffSync(update_roma_request, getActivity(), new Callback<SimpleResponse>() {
                                    @Override
                                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                                        if (response.body() != null && response.body().getSuccess().equals("true")) {
                                            homme.fragmentHandler.transition(new AssetFragment(), "ASSETS", null);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<SimpleResponse> call, Throwable t) { }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<ClusterResponse> call, Throwable t) {
                            update_roma_request.add("cluster", new JsonParser().parse(romaOps.current_roma_clusters.toString()));
                            accountOpsOffSync.editRomaOffSync(update_roma_request, getActivity(), new Callback<SimpleResponse>() {
                                @Override
                                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                                    if (response.body() != null && response.body().getSuccess().equals("true")) {
                                        homme.fragmentHandler.transition(new AssetFragment(), "ASSETS", null);
                                    }
                                }

                                @Override
                                public void onFailure(Call<SimpleResponse> call, Throwable t) { }
                            });
                        }
                    });

                    if (!isClusAddDone) {
                        // If cluster add failed due to network, store roma edit request offline
                        update_roma_request.add("cluster", new JsonParser().parse(romaOps.current_roma_clusters.toString()));
                        accountOpsOffSync.writeRequestOffSync(update_roma_request.toString(), "edit_roma_request", getActivity());
                        homme.fragmentHandler.transition(new AssetFragment(), "ASSETS", null);
                    }
                }
            }

            new DownloadFilesTask().execute();
        }
    }










    private void makePickerDialog(int j, View v) {
        current_attribute = j;
        current_view = v;

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "WEBP_" + timeStamp + "_";
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());

        try {
            if (EasyPermissions.hasPermissions(getActivity(), galleryPermissions)) {

                image = File.createTempFile(imageFileName,".webp", mediaStorageDir);
                last_captured_image_path = image.getAbsolutePath();

                Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri photoURI = FileProvider.getUriForFile(getActivity(), "com.neuman.brutus.fileprovider", image);
                capture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                Intent pick = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Capture an Image (or) Pick from Gallery?");
                builder.setPositiveButton("Capture", (dialogInterface, i) -> {
                    if (v.findViewById(R.id.attr_image_rel).getTag()!=null && v.findViewById(R.id.attr_image_rel).getTag().toString().equals("image_set")) {
                        startActivityForResult(capture, 0);
                    } else {
                        startActivityForResult(capture, 0);
                    }
                });
                builder.setNegativeButton("Gallery", (dialogInterface, i) -> {
                    if (v.findViewById(R.id.attr_image_rel).getTag()!=null && v.findViewById(R.id.attr_image_rel).getTag().toString().equals("image_set")) {
                        startActivityForResult(pick, 1);
                    } else {
                        startActivityForResult(pick, 1);
                    }
                });
                builder.show();

            } else {

                EasyPermissions.requestPermissions(getActivity(), "Access for storage", 101, galleryPermissions);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
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
                // Capture
                if(resultCode == RESULT_OK) {
                    image_bitmap = imageOps.decodeFile(last_captured_image_path);
                    imagerel.setBackground(new BitmapDrawable(image_bitmap));
                    imageview.setVisibility(View.INVISIBLE);
                    imagerel.setTag("image_set");

                    file_output = new File(last_captured_image_path);
                }

                break;
            case 1:
                // Gallery
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

            for (int k = 0; k < file_attributes.size(); k++) {
                if(file_attributes.get(k).getAsJsonObject().get("id").getAsString().equals(utils.roma_attributes.get(current_attribute).getId().toString())) {
                    flag = false; file_attributes.get(k).getAsJsonObject().addProperty("value", new_file_path);
                }
            }
            if (flag) {
                attr = new JsonObject(); attr.addProperty("id", utils.roma_attributes.get(current_attribute).getId());
                attr.addProperty("value", new_file_path); file_attributes.add(attr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
