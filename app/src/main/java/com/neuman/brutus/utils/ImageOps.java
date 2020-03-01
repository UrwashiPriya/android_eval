package com.neuman.brutus.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.neuman.brutus.offline.mode.AccountOpsOffSync;
import com.neuman.brutus.retrofit.Client;
import com.neuman.brutus.retrofit.models.UploadResponse;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ImageOps {

//    public JsonArray upload_images(Context context, JsonArray image_attributes, JsonArray attribute_list) {
//        for (int i=0; i<image_attributes.size(); i++) {
//
//            File file = new File(image_attributes.get(i).getAsJsonObject().get("value").getAsString());
//            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
//
//            try {
//                UploadResponse response = Client.getService(context).upload_file(body, "1").execute().body();
//                image_attributes.get(i).getAsJsonObject().addProperty("value", response.getFile());
//
//                for (JsonElement jsonElement: attribute_list) {
//                    if (jsonElement.getAsJsonObject().get("id").equals(image_attributes.get(i).getAsJsonObject().get("id"))) {
//                        attribute_list.remove(jsonElement);
//                    }
//                }
//
//                attribute_list.add(image_attributes.get(i));
//                image_attributes.remove(i);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return attribute_list;
//    }

    public JsonArray upload_files_as_attributes(JsonArray file_attributes, JsonArray attribute_list, String code, Context context) {
        if (isNetworkAvailable(context)) {
            for (int i=0; i<file_attributes.size(); i++) {
                File file = new File(file_attributes.get(i).getAsJsonObject().get("value").getAsString());
                String attr_id = file_attributes.get(i).getAsJsonObject().get("id").getAsString();
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                try {
                    Client.getService(context).upload_file_as_attribute(body, "1", code, attr_id).execute().body();
//                    file_attributes.get(i).getAsJsonObject().addProperty("value", response.getFile());

                    // In case image is being replaced
                    for (JsonElement jsonElement : attribute_list) {
                        if (jsonElement.getAsJsonObject().get("id").equals(file_attributes.get(i).getAsJsonObject().get("id"))) {
                            attribute_list.remove(jsonElement);
                        }
                    }

//                    attribute_list.add(file_attributes.get(i));
//                    file_attributes.remove(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (file_attributes.size()>0) {
            AccountOpsOffSync accountOpsOffSync = new AccountOpsOffSync();
            accountOpsOffSync.writeImageUploadRequestOffSync(file_attributes.toString(), code, "1", "file_upload_requests", context);
        }
        return attribute_list;
    }

    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }

    public String getAbsolutePath(Uri uri, Context context) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
