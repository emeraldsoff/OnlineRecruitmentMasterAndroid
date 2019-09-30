package inc.emeraldsoff.onlinerecruitmentmaster.retrofit_helper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.retrofit_helper.model.employee_model;

public class employeeAdapter extends RecyclerView.Adapter<employeeAdapter.CustomViewHolder> {

    private List<employee_model> dataList;
    private Context context;

    public employeeAdapter(Context context, List<employee_model> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        CardView bday_card;
        TextView name;
        TextView birthdy;

        CustomViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cliday_name);
            birthdy = itemView.findViewById(R.id.clibday_birthday);
            bday_card = itemView.findViewById(R.id.bday_card);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_mainbday, parent, false);
        return new employeeAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.name.setText(/*dataList.get(position).getId()*/dataList.get(position).getF_name());
        //holder.birthdy.setText(dataList.get(position).getF_name()+" "+dataList.get(position).getL_name());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
