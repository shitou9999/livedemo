package tv.kuainiu.modle.push;

/**
 * Created by jack on 2016/4/18.
 */
public class NewsMessage extends BaseMeassge{
    private int cat_id;//栏目ID
    private String id;//文章id

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
