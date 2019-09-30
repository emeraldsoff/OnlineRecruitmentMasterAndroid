package inc.emeraldsoff.onlinerecruitmentmaster.retrofit_helper.instances;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static inc.emeraldsoff.onlinerecruitmentmaster.retrofit_helper.retrofit_controller.base_url_localhost;

public class RetrofitClientInstance_localhost {
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(base_url_localhost)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
