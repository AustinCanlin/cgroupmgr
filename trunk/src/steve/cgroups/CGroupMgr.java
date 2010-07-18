package steve.cgroups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import java.lang.String;

public class CGroupMgr extends Activity 
{		
	private TextView cgroupInfo;
	private TextView cgroupPath;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        cgroupInfo = (TextView)findViewById(R.id.txtCGroupInfo);
        cgroupInfo.setText("1234567890123456789012345678901234567890\n1234567890123456789012345678901234567890/n1234567890123456789012345678901234567890\n");
        
        cgroupPath = (TextView)findViewById(R.id.txtCGroupPath);
        cgroupPath.setText("/cpuctl");
        
        final Button button = (Button) findViewById(R.id.btnOpenProcList);
        button.setOnClickListener(new View.OnClickListener() 
        {
        	public void onClick(View v) 
        	{
        		openProcessList();
        	}
        });
    }    
    
    private void openProcessList()
    {
    	Intent i = new Intent(this, ProcessListView.class);
    	
    	startActivity(i);
    }

}