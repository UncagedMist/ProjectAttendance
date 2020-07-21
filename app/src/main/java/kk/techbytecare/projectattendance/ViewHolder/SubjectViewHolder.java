package kk.techbytecare.projectattendance.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import kk.techbytecare.projectattendance.Common.Common;
import kk.techbytecare.projectattendance.R;

public class SubjectViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    public TextView txt_subject_name,txt_free_bunks,txt_present_percent;
    public Button btn_check;
    public ElegantNumberButton txt_bunk_count,txt_attend_count,txt_require_percent;
    public ImageView delete_icon;

    public RelativeLayout view_background;
    public LinearLayout view_foreground;

    public SubjectViewHolder(View itemView) {
        super(itemView);

        txt_subject_name = itemView.findViewById(R.id.txt_subject_name);
        txt_free_bunks = itemView.findViewById(R.id.txt_free_bunks);
        txt_present_percent = itemView.findViewById(R.id.txt_present_percent);

        btn_check = itemView.findViewById(R.id.btn_check);

        txt_bunk_count = itemView.findViewById(R.id.txt_bunk_count);
        txt_attend_count = itemView.findViewById(R.id.txt_attend_count);
        txt_require_percent = itemView.findViewById(R.id.txt_require_percent);

        view_background = itemView.findViewById(R.id.view_background);
        view_foreground = itemView.findViewById(R.id.view_foreground);

        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select the action");
        menu.add(0,0,getAdapterPosition(), Common.DELETE);
    }
}
