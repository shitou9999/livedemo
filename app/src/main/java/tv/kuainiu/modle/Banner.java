package tv.kuainiu.modle;


/**
 * Created by guxuan on 2016/3/28.
 */
public class Banner {
    public String linkurl;
    public String imageurl;
    public String alt;

    @Override
    public String toString() {
        return "Item{" +
                "alt='" + alt + '\'' +
                ", linkurl='" + linkurl + '\'' +
                ", imageurl='" + imageurl + '\'' +
                '}';
    }

}
