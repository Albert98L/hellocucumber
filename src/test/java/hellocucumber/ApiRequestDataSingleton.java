package hellocucumber;

import java.util.List;

public class ApiRequestDataSingleton {
    private static ApiRequestDataSingleton instance = null;
    private List<Integer> ids;
    private List<Integer> code;

    private ApiRequestDataSingleton(List<Integer> ids, List<Integer> code) {
        this.ids = ids;
        this.code = code;

    }

    public static ApiRequestDataSingleton getInstance(List<Integer> ids, List<Integer> code) {
        if (instance == null) {
            instance = new ApiRequestDataSingleton(ids, code);
        }
        return instance;
    }

    public static ApiRequestDataSingleton getInstance() {
        return instance;
    }


    public List<Integer> getIds() {
        return ids;
    }

    public List<Integer> getCode() {
        return code;
    }
}

