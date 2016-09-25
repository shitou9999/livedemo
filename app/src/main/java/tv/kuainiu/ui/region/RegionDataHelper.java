package tv.kuainiu.ui.region;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.LogUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author nanck on 2016/5/31.
 */
public class RegionDataHelper {

    public static List<Region> getRegionDataList(String json) {
        Type listType = new TypeToken<List<Region>>() {
        }.getType();
        return new DataConverter<Region>().JsonToListObject(json, listType);
    }

    public static Map<String, Region> getRegionDataMap(String json) {
        Map<String, Region> map = new HashMap<>();
        List<Region> list = getRegionDataList(json);
        for (Region region : list) {
            map.put(String.valueOf(region.getRegionCode()), region);
        }
        return map;
    }

    public static Map<String, Region> getRegionDataMap(Context context) {
        return getRegionDataMap(getStringFromJson(context));
    }

    /**
     * 直接从json中读取地区code
     *
     * @return
     */
    public static LinkedList<Region> getDataFromJson(Context context) {
        LinkedList<Region> cities = new LinkedList<>();
        StringBuilder fileContent = new StringBuilder("");
        BufferedReader reader = null;
        try {
            InputStream is = context.getAssets().open("jsonAreaCode.json");
            reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line);
            }
            DataConverter<Region> da = new DataConverter<>();
            cities = da.JsonToLinkedListObject(fileContent.toString(), new TypeToken<LinkedList<Region>>() {
            }.getType());
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
        return cities;
    }

    /**
     * 直接从json中读取地区code
     *
     * @return
     */
    public static String getStringFromJson(final Context context) {
        LinkedList<Region> cities = new LinkedList<>();
        StringBuilder fileContent = new StringBuilder("");
        BufferedReader reader = null;
        InputStream is = null;
        try {
            is = context.getAssets().open("jsonAreaCode.json");
            reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line);
            }
        } catch (Exception e) {
            LogUtils.e("RegionDataHelper", "读取jsonAreaCode.json失败", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                LogUtils.e("RegionDataHelper", "读取jsonAreaCode.json 关闭BufferedReader或InputStream失败", e);
            }
        }
        return fileContent.toString();
    }
}
