package com.neuman.brutus.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import com.google.gson.JsonObject;

public class EditTextWatcher implements TextWatcher {

    EditText editText;
    Boolean flag = false;
    String attr_id;
    int i;
    String type;
    Globals utils;

    public EditTextWatcher(EditText editText, int i, String type, Globals utils) {
        this.editText = editText;
        this.i = i;
        this.type = type;
        this.utils = utils;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
    public void afterTextChanged(Editable s) {
        flag = false;
        attr_id = (String) editText.getTag();
        for (int k = 0; k < utils.attribute_request.size(); k++) {
            if(utils.attribute_request.get(k).getAsJsonObject().get("id").getAsString().equals(attr_id)) {
                flag = true;
                if (type.equals("enum")) {
                    utils.attribute_request.get(k).getAsJsonObject().addProperty("value", utils.roma_attributes.get(i).valToId(editText.getText().toString()));
                } else {
                    utils.attribute_request.get(k).getAsJsonObject().addProperty("value", editText.getText().toString());
                }
            }
        }
        if (!flag) {
            JsonObject attr = new JsonObject();
            attr.addProperty("id", attr_id);
            if (type.equals("enum")) {
                attr.addProperty("value", utils.roma_attributes.get(i).valToId(editText.getText().toString()));
            } else {
                attr.addProperty("value", editText.getText().toString());
            }
            utils.attribute_request.add(attr);
        }
    }
}
