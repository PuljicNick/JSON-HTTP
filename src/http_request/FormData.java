package http_request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
//Group developed in class - Not part of IA Code just for HTTP usage

public class FormData {
    private ArrayList<DataItem> items;

    public FormData() {
        items = new ArrayList<DataItem>(5);
    }

    public void add(DataItem item) {
        int i;
        for (i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equals(item.getName())) {
                items.get(i).setValue(item.getValue());
                break;
            }
        }
        if (i >= items.size()) {
            items.add(item);
        }
    }

    public void add(String s, String v) {
        add(new DataItem(s,v));
    }

    public void add(String s, int v) {
        add(new DataItem(s,v));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        try {
            for (int i = 0; i < items.size(); i++) {
                if (i > 0)
                    sb.append("&");
                sb.append(items.get(i).getName());
                sb.append("=");
                sb.append(URLEncoder.encode(items.get(i).getValue(), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
