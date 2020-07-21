package kk.techbytecare.projectattendance.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kk.techbytecare.projectattendance.Common.Common;
import kk.techbytecare.projectattendance.Model.Subject;
import kk.techbytecare.projectattendance.R;
import kk.techbytecare.projectattendance.ViewHolder.SubjectViewHolder;

public class HomeFragment extends Fragment {

    @BindView(R.id.title_name)
    TextView title;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

//    SubjectAdapter adapter;

    private EditText edt_subject_name,edt_require_percent,edt_attended,edt_bunked;
//    RelativeLayout layout;

    private DatabaseReference subjects;
    private FirebaseRecyclerAdapter<Subject, SubjectViewHolder> adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        subjects = database.getReference()
                .child(Common.FIREBASE_DB)
                .child(Common.USERS_DB)
                .child(Common.currentUser.getPhoneNumber())
                .child(Common.SUBJECTS_DB);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myFragment = inflater.inflate(R.layout.fragment_home, container, false);

        Unbinder unbinder = ButterKnife.bind(this,myFragment);

        title.setText(getString(R.string.app_name));
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FloatingActionButton fab = myFragment.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddSubjectDialog();
            }
        });

        if (Common.isConnectedToInternet(getActivity())) {
            loadAllAvailableSubjects();
        }
        else    {
            Toast.makeText(getActivity(), "Please Check Your Internet Connection!!!", Toast.LENGTH_SHORT).show();
        }

        return myFragment;
    }

    private void loadAllAvailableSubjects() {

        FirebaseRecyclerOptions<Subject> options = new FirebaseRecyclerOptions.Builder<Subject>()
                .setQuery(subjects,Subject.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Subject, SubjectViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final SubjectViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull final Subject model) {

                holder.txt_subject_name.setText(adapter.getItem(position).getSubjectName().toUpperCase());
                holder.txt_require_percent.setNumber(adapter.getItem(position).getReqPercent());
                holder.txt_attend_count.setNumber(adapter.getItem(position).getSubjectAttended());
                holder.txt_bunk_count.setNumber(adapter.getItem(position).getSubjectBunked());
                holder.txt_free_bunks.setText(adapter.getItem(position).getFreeBunks());
                holder.txt_present_percent.setText(adapter.getItem(position).getPresentPercent());

                holder.btn_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int percent = Integer.parseInt(holder.txt_require_percent.getNumber());
                        int attend = Integer.parseInt(holder.txt_attend_count.getNumber());
                        int bunk = Integer.parseInt(holder.txt_bunk_count.getNumber());
                        final int present_percent;
                        final int free;

                        if (attend == 0 && bunk >= 0) {
                            return;
                        }
                        present_percent = (attend * 100) / (attend + bunk);
                        free = (((attend * 100) / percent) - attend - bunk);

                        holder.txt_present_percent.setText(String.valueOf(" "+present_percent));
                        holder.txt_free_bunks.setText(String.valueOf(" "+free));

                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                DatabaseReference reference = subjects.child(adapter.getItem(position).getSubjectId());

                                Subject subject = new Subject(
                                        adapter.getItem(position).getSubjectId(),
                                        adapter.getItem(position).getSubjectName(),
                                        holder.txt_require_percent.getNumber(),
                                        holder.txt_bunk_count.getNumber(),
                                        holder.txt_attend_count.getNumber(),
                                        String.valueOf(" "+free),
                                        String.valueOf(" "+present_percent),
                                        Common.currentUser.getPhoneNumber()
                                );
                                if (Common.isConnectedToInternet(getContext())) {
                                    reference.setValue(subject);
                                }
                                else    {
                                    Toast.makeText(getActivity(), "Please connect with Internet to Store Your Data!!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }

            @NonNull
            @Override
            public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View itemView = LayoutInflater.from(getContext())
                        .inflate(R.layout.subject_layout,parent,false);

                return new SubjectViewHolder(itemView);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();
    }

    private void showAddSubjectDialog() {

        LayoutInflater inflater = this.getLayoutInflater();
        View add_new_subject = inflater.inflate(R.layout.add_new_subject,null);

        edt_subject_name = add_new_subject.findViewById(R.id.edt_subject_name);
        edt_require_percent = add_new_subject.findViewById(R.id.edt_require_percent);
        edt_attended = add_new_subject.findViewById(R.id.edt_attended);
        edt_bunked = add_new_subject.findViewById(R.id.edt_bunked);

        NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(getActivity());
        dialogBuilder
                .withTitle("Add new Subject")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage("Please fill all information")                     //.withMessage(null)  no Msg
                .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                .withDialogColor("#FFE74C3C")                               //def  | withDialogColor(int resid)
                .withIcon(getResources().getDrawable(R.drawable.ic_event_note_black_24dp))
                .withDuration(700)                                          //def
                .withEffect(Effectstype.Newspager)                                         //def Effectstype.Slidetop
                .withButton1Text("Cancel")                                      //def gone
                .withButton2Text("Add Subject")                                  //def gone
                .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
                .setCustomView(add_new_subject,getContext())         //.setCustomView(View or ResId,context)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(edt_subject_name.getText().toString()))   {
                            Toast.makeText(getActivity(), "Plz enter subject name..", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(edt_require_percent.getText().toString()))   {
                            Toast.makeText(getActivity(), "Plz enter Required Percentage..", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(edt_attended.getText().toString()))   {
                            Toast.makeText(getActivity(), "Plz enter how many class you have attended.. if not then enter 0", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(edt_bunked.getText().toString()))   {
                            Toast.makeText(getActivity(), "Plz enter how many class you have bunked.. if not then enter 0", Toast.LENGTH_SHORT).show();
                        }
                        else    {
                            if (Integer.parseInt(edt_require_percent.getText().toString()) == 0 ||
                                    Integer.parseInt(edt_require_percent.getText().toString()) > 100) {
                                Toast.makeText(getActivity(), "Invalid percentage entered", Toast.LENGTH_SHORT).show();
                            }
                            else    {
                                String id = subjects.push().getKey();

                                Subject subject = new Subject(
                                        id,
                                        edt_subject_name.getText().toString().toUpperCase(),
                                        edt_require_percent.getText().toString(),
                                        edt_bunked.getText().toString(),
                                        edt_attended.getText().toString(),
                                        String.valueOf(0),
                                        String.valueOf(0),
                                        Common.currentUser.getPhoneNumber()
                                );

                                if (Common.isConnectedToInternet(getContext())) {
                                    subjects.child(id).setValue(subject);
                                    dialogBuilder.dismiss();
                                    Common.currentSubject = subject;
                                }
                                else    {
                                    Toast.makeText(getContext(), "Please Check your internet connection...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }).show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }
}
