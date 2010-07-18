package steve.cgroups;

import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;

public final class ProcessInfoView extends Activity 
{	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		
		CGroupInfo cgInfo = new CGroupInfo();
		
		Bundle b = getIntent().getExtras();
		
		TextView  tv = new TextView(this);
		String pid = b.getString("myPID");
        tv.setText(cgInfo.getProcessInfo(pid));
        setContentView(tv);
    }
}
