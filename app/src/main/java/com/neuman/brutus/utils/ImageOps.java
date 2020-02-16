package com.neuman.brutus.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.neuman.brutus.retrofit.Client;
import com.neuman.brutus.retrofit.models.UploadResponse;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ImageOps {

    public JsonArray upload_images(Context context, JsonArray image_attributes, JsonArray attribute_list) {
        for (int i=0; i<image_attributes.size(); i++) {

            System.out.println("Uploading... "+image_attributes.get(i).getAsJsonObject().get("value").getAsString());

            File file = new File(image_attributes.get(i).getAsJsonObject().get("value").getAsString());
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            try {
                UploadResponse response = Client.getService(context).upload_file(body, "1").execute().body();
                image_attributes.get(i).getAsJsonObject().addProperty("value", response.getFile());

                for (JsonElement jsonElement: attribute_list) {
                    if (jsonElement.getAsJsonObject().get("id").equals(image_attributes.get(i).getAsJsonObject().get("id"))) {
                        attribute_list.remove(jsonElement);
                    }
                }

                attribute_list.add(image_attributes.get(i));
                image_attributes.remove(i);

//                .enqueue(new Callback<UploadResponse>() {
//                    @Override
//                    public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
//                        image_attributes.get(j).getAsJsonObject().addProperty("value", response.body().getFile());
//                    }
//
//                    @Override
//                    public void onFailure(Call<UploadResponse> call, Throwable t) {
//
//                    }
//                });
            } catch (IOException e) {
                e.printStackTrace();
            }
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

}
