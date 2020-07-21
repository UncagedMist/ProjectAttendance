package kk.techbytecare.projectattendance;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import kk.techbytecare.projectattendance.Common.Common;
import kk.techbytecare.projectattendance.Fragments.HomeFragment;
import kk.techbytecare.projectattendance.Fragments.ProfileFragment;
import kk.techbytecare.projectattendance.Helper.CurvedBottomNavigationView;
import kk.techbytecare.projectattendance.Model.User;
import kk.techbytecare.projectattendance.Receiver.Receiver;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.customBottomBar)
    CurvedBottomNavigationView navigationView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    CollectionReference userRef;

    AlertDialog dialog;

    TimePicker timePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(HomeActivity.this);

        userRef = FirebaseFirestore.getInstance().collection(Common.FIREBASE_DB);

        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();


        boolean isLogin = getIntent().getBooleanExtra(Common.IS_LOGIN, false);

        if (isLogin) {

            dialog.show();

            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(Account account) {

                    if (account != null) {

                        DocumentReference currentUser = userRef.document(account.getPhoneNumber().toString());
                        currentUser.get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        if (task.isSuccessful()) {

                                            DocumentSnapshot userSnapshot = task.getResult();

                                            if (!userSnapshot.exists()) {
                                                showAddDetailsDialog(account.getPhoneNumber().toString());
                                            } else {
                                                Common.currentUser = userSnapshot.toObject(User.class);
                                                navigationView.setSelectedItemId(R.id.action_home);
                                            }

                                            if (dialog.isShowing()) {
                                                dialog.dismiss();
                                            }
                                        }
                                    }
                                });
                    }

                }

                @Override
                public void onError(AccountKitError accountKitError) {
                    Toast.makeText(HomeActivity.this, ""
                            + accountKitError.getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            Fragment fragment;

            @SuppressLint({"RestrictedApi", "ResourceAsColor"})
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_home) {
                    fragment = new HomeFragment();
                    fab.setBackgroundTintList(getResources().getColorStateList(R.color.profilePrimaryDark));
                    fab.setImageResource(R.drawable.ic_home_black_24dp);

                } else if (menuItem.getItemId() == R.id.action_profile) {
                    fragment = new ProfileFragment();
                    fab.setBackgroundTintList(getResources().getColorStateList(R.color.bgRowBackground));
                    fab.setImageResource(R.drawable.ic_person_black_24dp);
                }

                return loadFragment(fragment);
            }
        });
    }

    private void showAddDetailsDialog(String phoneNumber) {
        LayoutInflater inflater = this.getLayoutInflater();
        View user_details_layout = inflater.inflate(R.layout.info_details, null);

        CircleImageView imgUpload = user_details_layout.findViewById(R.id.imgUpload);
        EditText edtFName = user_details_layout.findViewById(R.id.edtFName);
        EditText edtLName = user_details_layout.findViewById(R.id.edtLName);
        EditText edtClg = user_details_layout.findViewById(R.id.edtClgName);
        EditText edtPhone = user_details_layout.findViewById(R.id.edtPhone);

        edtPhone.setText(phoneNumber);

        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "You can upload your image in the profile section.", Toast.LENGTH_SHORT).show();

            }
        });

        NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder
                .withTitle("Profile Details")
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage(null)                     //.withMessage(null)  no Msg
                .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                .withDialogColor("#FFE74C3C")                               //def  | withDialogColor(int resid)
                .withIcon(getResources().getDrawable(R.drawable.ic_person_black_24dp))
                .withDuration(700)                                          //def
                .withEffect(Effectstype.Newspager)                                         //def Effectstype.Slidetop
                .withButton1Text("Cancel")                                      //def gone
                .withButton2Text("Add My Details")                                  //def gone
                .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
                .setCustomView(user_details_layout, this)         //.setCustomView(View or ResId,context)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(edtFName.getText().toString())) {
                            Toast.makeText(HomeActivity.this, "Plz enter First name..", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(edtLName.getText().toString())) {
                            Toast.makeText(HomeActivity.this, "Plz enter last name..", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(edtClg.getText().toString())) {
                            Toast.makeText(HomeActivity.this, "Plz enter name of your college or school which you are studying.", Toast.LENGTH_SHORT).show();
                        } else {
                            User user = new User(
                                    edtFName.getText().toString().trim(),
                                    edtLName.getText().toString().trim(),
                                    edtClg.getText().toString().trim(),
                                    phoneNumber
                            );

                            userRef.document(phoneNumber)
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            dialogBuilder.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(HomeActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            dialogBuilder.dismiss();
                                        }
                                    });
                        }
                    }
                }).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_reminder) {
            showReminderDialog();
        } else if (id == R.id.action_out) {
            signOut();
        } else if (id == R.id.action_about) {
            startActivity(new Intent(HomeActivity.this, AboutActivity.class));
        } else if (id == R.id.action_refer) {
            showShareAppDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showShareAppDialog() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String message = "Never Run Out of Attendance. Install HELPERLY and keep your attendance in Control! \n https://play.google.com/store/apps/details?id=kk.techbytecare.projectattendance";
        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(intent, "Share HELPERLY Using"));
    }

    private void showReminderDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Attendance Reminder");

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.reminder_layout, null);

        timePicker = view.findViewById(R.id.timePicker);

        alertDialog.setView(view);

        alertDialog.setPositiveButton("SET REMINDER", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                saveAlarm();
                Toast.makeText(HomeActivity.this, "Don't worry. I'll remind you...", Toast.LENGTH_SHORT).show();

            }
        }).setNegativeButton("LEAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void saveAlarm() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(HomeActivity.this, Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        Date today = Calendar.getInstance().getTime();

        calendar.set(today.getYear(), today.getMonth(), today.getDay(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void signOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit Application");
        builder.setMessage("Do you want to End the Current Session?");

        builder.setPositiveButton("TAKE ME OUT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                AccountKit.logOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("STAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
