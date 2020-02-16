package com.neuman.brutus.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

public class TextInputEditTextWatcher implements TextWatcher {

    private TextInputEditText textInputEditText;
    private Boolean flag = false;
    private String attr_id;
    private Globals utils;

    public TextInputEditTextWatcher(TextInputEditText textInputEditText, Globals utils) {
        this.textInputEditText = textInputEditText;
        this.utils = utils;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
    public void afterTextChanged(Editable s) {
        flag = false;

//        switch (textInputEditText.getTag().toString()) {
//            case "clusters":
//                System.out.println("clusters");
//                break;
//            default:

        attr_id = (String) textInputEditText.getTag();
        for (int k = 0; k < utils.attribute_request.size(); k++) {
            if(utils.attribute_request.get(k).getAsJsonObject().get("id").getAsString().equals(attr_id)) {
                flag = true; utils.attribute_request.get(k).getAsJsonObject().addProperty("value", textInputEditText.getText().toString());
            }
        }
        if (!flag) {
            JsonObject attr = new JsonObject(); attr.addProperty("id", attr_id);
            attr.addProperty("value", textInputEditText.getText().toString()); utils.attribute_request.add(attr);
        }
    }
}