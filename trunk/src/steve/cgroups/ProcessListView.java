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

public final class ProcessListView extends Activity 
{
	private Bundle bundle;
	private String action = "info";
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        Intent i = getIntent();
        
        if(null != i)
        {
	        bundle = i.getExtras();
	     
	        if(null != bundle)
	        {
		        if(null != bundle.getString("action"))
		        {
		        	action = bundle.getString("action");        	
		        }
	        }
        }
        
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
        
    	String procArray[] = cgInfo.getProcessList();
    	
    	ArrayAdapter<String> procAdapter = 
    				new ArrayAdapter<String>(this, 
    										android.R.layout.simple_list_item_1, 
    										procArray);
    	
        lv.setAdapter(procAdapter);
        setContentView(lv);
    }  
	
	void onListItemSelected(View view, String itemText)
	{
		StringTokenizer strTok = new StringTokenizer(itemText, " ");
		
		String strPID = strTok.nextToken();
		if(strTok.hasMoreTokens())
		{
			if(0 == action.compareToIgnoreCase("info"))
			{
				Intent i = new Intent(this, ProcessInfoView.class);
				
				if(null == bundle)
					bundle = new Bundle();
	
				bundle.putString("myPID", strPID);
	
				i.putExtras(bundle);
			
				startActivityForResult(i, 0);
			}
			else if(0 == action.compareToIgnoreCase("select"))
			{
				Intent intent = new Intent();
				
				Bundle b = new Bundle();
				
				b.putString("myPID", strPID);
				
				intent.putExtras(b);
				
				setResult(CGroupMgr.SELECT_PROCESS_REQUEST, intent);
				finish();
			}
		}
	}
}
