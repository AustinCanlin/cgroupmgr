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
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
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
		
		if(strTok.hasMoreTokens())
		{
			String strPID = strTok.nextToken();
			
			Intent i = new Intent(this, ProcessInfoView.class);
			
			Bundle b = new Bundle();

			b.putString("PID", strPID);

			i.putExtras(b);
			
			startActivityForResult(i, 0);
		}
	}
}
