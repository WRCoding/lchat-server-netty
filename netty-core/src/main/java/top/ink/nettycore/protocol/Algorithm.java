package top.ink.nettycore.protocol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.charset.StandardCharsets;

/**
 * desc: Algorithm
 *
 * @author ink
 * date:2022-02-28 21:09
 */
public enum Algorithm implements Serializer {

    /** JSON序列化 */
    JSON{
        @Override
        public <T> T deserialize(Class<T> clazz, byte[] bytes) {
            Gson gson = new Gson();
            String json = new String(bytes, StandardCharsets.UTF_8);
            return gson.fromJson(json, clazz);
        }

        @Override
        public <T> byte[] serialize(T object) {
            Gson gson = new Gson();
            String json = gson.toJson(object);
            return json.getBytes(StandardCharsets.UTF_8);
        }
    };

}
