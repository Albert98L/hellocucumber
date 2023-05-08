package hellocucumber;

import java.util.List;

public class ApiRequestDataSingleton2 {
    private static ApiRequestDataSingleton2 instance = null;
    private List<Integer> ids;

    private ApiRequestDataSingleton2(List<Integer> ids) {
        this.ids = ids;

    }

    public static ApiRequestDataSingleton2 getInstance(List<Integer> ids) {
        if (instance == null) {
            instance = new ApiRequestDataSingleton2(ids);
        }
        return instance;
    }

    public static ApiRequestDataSingleton2 getInstance() {
        return instance;
    }


    public List<Integer> getIds() {
        return ids;
    }

}

