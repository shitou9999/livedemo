package tv.kuainiu.widget;

/**
 * Created by jack on 2016/9/6.
 */
public class MyHeader {
    private String title;
    private String color = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public MyHeader() {
    }

    public MyHeader(String title, String color) {
        this.title = title;
        this.color = color;
    }
}
