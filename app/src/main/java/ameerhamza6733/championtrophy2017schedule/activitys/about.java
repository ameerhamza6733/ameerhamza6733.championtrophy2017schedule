package ameerhamza6733.championtrophy2017schedule.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.vansuita.materialabout.builder.AboutBuilder;

import ameerhamza6733.championtrophy2017schedule.R;

public class about extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        final FrameLayout flHolder = (FrameLayout) findViewById(R.id.about);
        View view = AboutBuilder.with(this)
                .setPhoto(R.drawable.profile_photo)
                .setCover(R.mipmap.profile_cover)
                .setName("Ameer Hamza ")
                .setSubTitle("Mobile Developer")
                .setBrief("I'm warmed of mobile technologies. Ideas maker, curious and nature lover.")
                .setAppIcon(R.mipmap.ic_launcher)
                .setAppName(R.string.app_name)
                .addTwitterLink("ameer_hamza6733")
                .addGitHubLink("ameerhamza6733")

                .addMoreFromMeAction("ameerhamza6733")
                .addFiveStarsAction()
                .addFeedbackAction("ipl2017reviews@gmail.com")
                .setVersionNameAsAppSubTitle()
                .addShareAction(R.string.app_name)

                .setWrapScrollView(true)
                .setLinksAnimated(true)
                .build();

        addContentView(view, flHolder.getLayoutParams());

    }
}
