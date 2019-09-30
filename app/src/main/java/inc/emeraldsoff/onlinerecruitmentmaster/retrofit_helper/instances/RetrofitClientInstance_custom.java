package inc.emeraldsoff.onlinerecruitmentmaster.retrofit_helper.instances;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static inc.emeraldsoff.onlinerecruitmentmaster.retrofit_helper.retrofit_controller.base_url_remotehost;

public class RetrofitClientInstance_custom {
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(base_url_remotehost)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
