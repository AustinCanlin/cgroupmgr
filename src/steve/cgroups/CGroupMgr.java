package steve.cgroups;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import java.lang.String;

public class CGroupMgr extends Activity 
{		
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        TextView  tv = new TextView(this);
        tv.setText("Hello!");
        tv.setText( getProcessList() );
        setContentView(tv);
    }    
    
    public String getProcessList()
    {
    	String procList = new String();
    	
    	CGroupInfo cgInfo = new CGroupInfo();
    	String procArray[] = cgInfo.getProcessList();
	
    	for(int i = 0; i < procArray.length; i++)
    	{
    		procList += procArray[i] + "\n";
    	}
    	
    	return procList;
    }

}