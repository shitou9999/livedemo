package tv.kuainiu.modle;

/**
 * 文章栏目分类
 * Created by sirius on 2016/10/13.
 */

public class Categroy {

    /**
     * id : 1
     * catname : 股票
     * orderlist : 0
     * create_date : 1473481794
     * status : 1
     */

    private String id;
    private String catname;
    private int orderlist;
    private long create_date;
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public int getOrderlist() {
        return orderlist;
    }

    public void setOrderlist(int orderlist) {
        this.orderlist = orderlist;
    }

    public long getCreate_date() {
        return create_date;
    }

    public void setCreate_date(long create_date) {
        this.create_date = create_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
