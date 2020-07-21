package kk.techbytecare.projectattendance.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import kk.techbytecare.projectattendance.Model.Subject;
import kk.techbytecare.projectattendance.Model.User;

public class Common {

    public static final String FIREBASE_DB = "HELPERLY";
    public static final String USERS_DB = "Users";
    public static final String SUBJECTS_DB = "Subjects";
    public static final String DELETE = "Delete";
    //new
    public static String IS_LOGIN = "IsLogin";
    public static User currentUser;
    public static Subject currentSubject;

    public static final int REQUEST_CODE = 5152;


    public static boolean isConnectedToInternet(Context context)    {

        ConnectivityManager connectivityManager = (
                ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null)    {

            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();

            if (info != null)   {

                for (int i = 0; i <info.length;i++)   {

                    if (info[i].getState() == NetworkInfo.State.CONNECTED)  {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
