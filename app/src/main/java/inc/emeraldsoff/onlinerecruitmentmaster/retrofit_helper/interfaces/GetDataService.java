package inc.emeraldsoff.onlinerecruitmentmaster.retrofit_helper.interfaces;

import java.util.List;

import inc.emeraldsoff.onlinerecruitmentmaster.retrofit_helper.model.employee_model;
import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {
    @GET("employee_recruit")
    Call<List<employee_model>> getemployees();

    @GET("user")
    Call<List<employee_model>> getallusers();
}
