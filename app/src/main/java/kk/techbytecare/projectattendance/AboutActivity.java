package kk.techbytecare.projectattendance;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.shashank.sony.fancyaboutpagelib.FancyAboutPage;

public class AboutActivity extends AppCompatActivity {

    FancyAboutPage aboutPage;
    String version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        aboutPage = findViewById(R.id.aboutPage);
        aboutPage.setCover(R.drawable.coverimg);
        aboutPage.setName("Kundan Kumar");
        aboutPage.setDescription("Android Developer | Android App, Game and Software Developer.");
        aboutPage.setAppIcon(R.mipmap.ic_launcher); //Pass your app icon image

        aboutPage.setAppName(getString(R.string.app_name));
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        aboutPage.setVersionNameAsAppSubTitle(version);
        aboutPage.setAppDescription("" +
                "Helperly is an Android app Designed to Help Students.\n\n" +
                "This app asks students to enter their total no. of attended/bunked class on the daily basis with beautiful ui. student can calculate how much they should bunk or attend the classes to get the required attendance percentage demanded by the college.\n\n"+
                "A fresh new take on Material layouts. It offers a beautiful ui and daily basis reminder notification to never miss to update the attendance.");

        aboutPage.addEmailLink("Kundan_kk52@outlook.com");     //Add your email id
        aboutPage.addFacebookLink("https://www.facebook.com/TechByteCare/");  //Add your facebook address url
        aboutPage.addTwitterLink("https://twitter.com/TechByteCare");
        aboutPage.addLinkedinLink("https://www.linkedin.com/in/kundan-kumar-a82472167/");
        aboutPage.addGitHubLink("https://github.com/UncagedMist");

    }

}
