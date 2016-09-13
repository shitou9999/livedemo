package tv.kuainiu.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class DataConverter<T> {
    private Gson gson;

    public DataConverter() {
        super();
//		this.gson = new Gson();
        this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    /**
     * 将对象转换成JSON字符串
     *
     * @param obj
     * @return
     */
    public String ObjectToJson(Object obj) {
        String json = gson.toJson(obj);
        return json;
    }

    /**
     * 将JSON字符串 转换成对象
     *
     * @param json
     * @param cls
     * @return
     */
    public T JsonToObject(String json, Class<?> cls) {
        @SuppressWarnings("unchecked")
        T obj = (T) gson.fromJson(json, cls);
        return obj;
    }

    /**
     * 将JSON字符串 转换成List<T>对象
     *
     * @param json
     * @param type new TypeToken<ArrayList<T>>() {}.getType()获得
     * @return
     */
    public List<T> JsonToListObject(String json, Type type) {
        List<T> list = gson.fromJson(json, type);
        return list;
    }

    /**
     * 将JSON字符串 转换成LinkedList<T>对象
     *
     * @param json
     * @param type new TypeToken<LinkedList<T>>() {}.getType()获得
     * @return
     */
    public LinkedList<T> JsonToLinkedListObject(String json, Type type) {
        LinkedList<T> list = gson.fromJson(json, type);
        return list;

    }
}
