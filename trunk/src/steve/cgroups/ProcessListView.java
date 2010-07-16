package steve.cgroups;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;

public class ProcessListView extends Activity 
{
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        ListView  lv = new ListView(this);
        
        CGroupInfo cgInfo = new CGroupInfo();
        
    	String procArray[] = cgInfo.getProcessList();
    	
    	ArrayAdapter<String> procAdapter = 
    				new ArrayAdapter<String>(this, 
    										android.R.layout.simple_list_item_1, 
    										procArray);
    	
        lv.setAdapter(procAdapter);
        setContentView(lv);
    }  
}
