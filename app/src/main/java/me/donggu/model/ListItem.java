package me.donggu.model;

/**
 * Created by Donggu on 2016/11/20.
 */
public class ListItem {
    String value;
    String content;

    public ListItem(String value, String content) {
        this.value = value;
        this.content = content;
    }

    public String getValue() {
        return value;
    }

    public String getContent() {
        return content;
    }
}
