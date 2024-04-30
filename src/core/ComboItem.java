package core;


public class ComboItem {
    private int key;
    private String value;
    private String moreValue;

    public ComboItem(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public ComboItem(int key, String value, String moreValue) {
        this.key = key;
        this.value = value;
        this.moreValue = moreValue;
    }

    public String getMoreValue() {
        return moreValue;
    }

    public void setMoreValue(String moreValue) {
        this.moreValue = moreValue;
    }


    @Override
    public String toString() {
        return value;
    }


    public int getKey() {
        return key;
    }


    public void setKey(int key) {
        this.key = key;
    }


    public String getValue() {
        return value;
    }


    public void setValue(String value) {
        this.value = value;
    }
}
