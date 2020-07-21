package kk.techbytecare.projectattendance.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kk.techbytecare.projectattendance.Common.Common;
import kk.techbytecare.projectattendance.Model.Subject;
import kk.techbytecare.projectattendance.R;
import kk.techbytecare.projectattendance.ViewHolder.SubjectViewHolder;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectViewHolder> {

    private List<Subject> subjectList;
    private Context context;

    public SubjectAdapter(List<Subject> subjectList, Context context) {
        this.subjectList = subjectList;
        this.context = context;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.subject_layout,parent,false);
        return new SubjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubjectViewHolder holder, final int position) {

        holder.txt_subject_name.setText(subjectList.get(holder.getAdapterPosition()).getSubjectName());
        holder.txt_bunk_count.setNumber(subjectList.get(holder.getAdapterPosition()).getSubjectBunked());
        holder.txt_attend_count.setNumber(subjectList.get(holder.getAdapterPosition()).getSubjectAttended());
        holder.txt_free_bunks.setText(subjectList.get(holder.getAdapterPosition()).getFreeBunks());
        holder.txt_present_percent.setText(subjectList.get(holder.getAdapterPosition()).getPresentPercent());
        holder.txt_require_percent.setNumber(subjectList.get(holder.getAdapterPosition()).getReqPercent());

//        holder.txt_bunk_count.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
//            @Override
//            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
//                Subject subject = subjectList.get(holder.getAdapterPosition());
//                subject.setSubjectBunked(String.valueOf(newValue));
//                new Database(context).updateSubjects(subject);
//
//                List<Subject> subjects = new Database(context).getAllSubjects(Common.currentUser.getPhone());
//
//                int present_percent;
//                int free;
//
//                for (Subject item : subjects) {
//
//                    if (Integer.parseInt(item.getSubjectAttended()) == 0 && Integer.parseInt(item.getSubjectBunked()) >= 0) {
//                        return;
//                    }
//
//                    present_percent = (Integer.parseInt(item.getSubjectAttended()) * 100) / (Integer.parseInt(item.getSubjectAttended()) + Integer.parseInt(item.getSubjectBunked()));
//                    free = (((Integer.parseInt(item.getSubjectAttended()) * 100) / Integer.parseInt(item.getReqPercent())) - Integer.parseInt(item.getSubjectAttended()) - Integer.parseInt(item.getSubjectBunked()));
//
////                    holder.txt_free_bunks.setText(String.valueOf(" "+free));
////                    holder.txt_present_percent.setText(String.valueOf(" "+present_percent));
//                }
//            }
//        });
//
//        holder.txt_attend_count.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
//            @Override
//            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
//                Subject subject = subjectList.get(holder.getAdapterPosition());
//                subject.setSubjectAttended(String.valueOf(newValue));
//                new Database(context).updateSubjects(subject);
//
//                List<Subject> subjects = new Database(context).getAllSubjects(Common.currentUser.getPhone());
//
//                int present_percent;
//                int free;
//
//                for (Subject item : subjects) {
//
//                    if (Integer.parseInt(item.getSubjectAttended()) == 0 && Integer.parseInt(item.getSubjectBunked()) >= 0) {
//                        return;
//                    }
//
//                    present_percent = (Integer.parseInt(item.getSubjectAttended()) * 100) / (Integer.parseInt(item.getSubjectAttended()) + Integer.parseInt(item.getSubjectBunked()));
//                    free = (((Integer.parseInt(item.getSubjectAttended()) * 100) / Integer.parseInt(item.getReqPercent())) - Integer.parseInt(item.getSubjectAttended()) - Integer.parseInt(item.getSubjectBunked()));
//
////                    holder.txt_free_bunks.setText(String.valueOf(" "+free));
////                    holder.txt_present_percent.setText(String.valueOf(" "+present_percent));
//                }
//            }
//        });
//
//        holder.txt_require_percent.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
//            @Override
//            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
//                Subject subject = subjectList.get(holder.getAdapterPosition());
//                subject.setReqPercent(String.valueOf(newValue));
//                new Database(context).updateSubjects(subject);
//
//                List<Subject> subjects = new Database(context).getAllSubjects(Common.currentUser.getPhone());
//
//                int present_percent;
//                int free;
//
//                for (Subject item : subjects) {
//
//                    if (Integer.parseInt(item.getSubjectAttended()) == 0 && Integer.parseInt(item.getSubjectBunked()) >= 0) {
//                        return;
//                    }
//
//                    present_percent = (Integer.parseInt(item.getSubjectAttended()) * 100) / (Integer.parseInt(item.getSubjectAttended()) + Integer.parseInt(item.getSubjectBunked()));
//                    free = (((Integer.parseInt(item.getSubjectAttended()) * 100) / Integer.parseInt(item.getReqPercent())) - Integer.parseInt(item.getSubjectAttended()) - Integer.parseInt(item.getSubjectBunked()));
//
////                    holder.txt_free_bunks.setText(String.valueOf(" "+free));
////                    holder.txt_present_percent.setText(String.valueOf(" "+present_percent));
//                }
//            }
//        });

        holder.btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int percent = Integer.parseInt(holder.txt_require_percent.getNumber());
                int attend = Integer.parseInt(holder.txt_attend_count.getNumber());
                int bunk = Integer.parseInt(holder.txt_bunk_count.getNumber());
                int present_percent;
                int free;

                if (attend == 0 && bunk >= 0) {
                    return;
                }
                present_percent = (attend * 100) / (attend + bunk);
                free = (((attend * 100) / percent) - attend - bunk);

                holder.txt_subject_name.setText(subjectList.get(holder.getAdapterPosition()).getSubjectName());
                holder.txt_require_percent.setNumber(String.valueOf(percent));
                holder.txt_bunk_count.setNumber(String.valueOf(bunk));
                holder.txt_attend_count.setNumber(String.valueOf(attend));

                holder.txt_free_bunks.setText(String.valueOf(" "+free));
                holder.txt_present_percent.setText(String.valueOf(" "+present_percent));
            }
        });
//        Common.currentSubject = subjectList.get(position);
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }
}
