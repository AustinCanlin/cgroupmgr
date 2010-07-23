package steve.cgroups;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.content.Intent;
import java.util.StringTokenizer;

public final class CGroupListView extends Activity 
{
	private String strBasePath = "/dev/cpuctl";
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
     
        Intent i = getIntent();
     
        if(null != i)
        {
	        Bundle b = i.getExtras();
	     
	        if(null != b)
	        {
		        if(null != b.getString("cgroupPath"))
		        {
		        	strBasePath = b.getString("cgroupPath");        	
		        }
	        }
        }

        this.setTitle("Children of " + strBasePath);
        
        ListView  lv = new ListView(this);
         
        lv.setOnItemClickListener(new OnItemClickListener() 
        {
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) 
            {
            	String text = ((TextView) view).getText().toString();
            	onListItemSelected(view, text);              
            }
        });
        
        CGroupInfo cgInfo = new CGroupInfo();
        
    	String procArray[] = cgInfo.getCGroupChildList(strBasePath);
    	
    	ArrayAdapter<String> procAdapter = 
    				new ArrayAdapter<String>(this, 
    										android.R.layout.simple_list_item_1, 
    										procArray);
    	
        lv.setAdapter(procAdapter);
        setContentView(lv);
    }
	
	void onListItemSelected(View view, String itemText)
	{		
		Intent i = new Intent(this, CGroupMgr.class);
		
		Bundle b = new Bundle();

		String newPath = strBasePath + "/" + itemText;
		b.putString("cgroupPath", newPath);

		i.putExtras(b);
		
		startActivityForResult(i, 0);
	}
}
