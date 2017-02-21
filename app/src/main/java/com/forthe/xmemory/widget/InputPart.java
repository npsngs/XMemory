package com.forthe.xmemory.widget;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.forthe.xmemory.R;

public abstract class InputPart{
    private EditText et_label;
    private EditText et_title;
    private EditText et_content;

    private String label;
    private String title;
    private String content;

    public InputPart(Activity root) {
        this.et_content = (EditText) root.findViewById(R.id.et_content);
        this.et_label = (EditText) root.findViewById(R.id.et_label);
        this.et_title = (EditText) root.findViewById(R.id.et_title);

        et_content.addTextChangedListener(new TextChangeWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                content = s.toString();
                onTextChange();
            }
        });


        et_title.addTextChangedListener(new TextChangeWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                title = s.toString();
                onTextChange();
            }
        });


        et_label.addTextChangedListener(new TextChangeWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                label = s.toString();
                onTextChange();
            }
        });

    }

    protected abstract void onTextChange();

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.et_content.setText(content);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.et_label.setText(label);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.et_title.setText(title);
    }




    private static abstract class TextChangeWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    }

}
