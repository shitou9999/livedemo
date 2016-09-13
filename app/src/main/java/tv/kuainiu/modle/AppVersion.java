package tv.kuainiu.modle;

/**
 * Created by jack on 2016/4/19.
 */
public class AppVersion {


    /**
     * version_dec : null
     * version_name : null
     * load_url : null
     * latest_version : null
     * is_force_update : 0
     * is_update : 0
     */

    public String version_dec;
    public String version_name;
    public String load_url;
    public int latest_version;
    public int is_force_update;
    public int is_update;

    public String getVersion_dec() {
        return version_dec;
    }

    public void setVersion_dec(String version_dec) {
        this.version_dec = version_dec;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public String getLoad_url() {
        return load_url;
    }

    public void setLoad_url(String load_url) {
        this.load_url = load_url;
    }

    public int getLatest_version() {
        return latest_version;
    }

    public void setLatest_version(int latest_version) {
        this.latest_version = latest_version;
    }

    public int getIs_force_update() {
        return is_force_update;
    }

    public void setIs_force_update(int is_force_update) {
        this.is_force_update = is_force_update;
    }

    public int getIs_update() {
        return is_update;
    }

    public void setIs_update(int is_update) {
        this.is_update = is_update;
    }
}
