package ameerhamza6733.championtrophy2017schedule.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by AmeerHamza on 3/4/2017.
 */

public class mySinglton
{
    private static SharedPreferences mySingltonSharePraf;


    private mySinglton() {
    }

    public static SharedPreferences getInStance(Context context)

   {
       if(mySingltonSharePraf==null)
       {

           mySingltonSharePraf = context.getSharedPreferences("mPreference1",0);

       }
       return mySingltonSharePraf;
   }




}

