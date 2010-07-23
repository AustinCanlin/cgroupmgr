package steve.cgroups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;

public class AddChildDialog extends Activity
{
	private EditText txtChildName;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_child);
        
        Intent i = getIntent();
        String basePath = new String("");
        
        if(null != i)
        {
	        Bundle b = i.getExtras();
	     
	        if(null != b)
	        {
		        if(null != b.getString("cgroupPath"))
		        	basePath = b.getString("cgroupPath");        	
	        }
        }
        
        this.setTitle("Adding child to: " + basePath);
        
        txtChildName = (EditText)findViewById(R.id.txtChildName);
        
        final Button childAddButton = (Button) findViewById(R.id.btnAdd);
        childAddButton.setOnClickListener(new View.OnClickListener() 
        {
        	public void onClick(View v) 
        	{
        		onAddChild();
        	}
        }); 
    }
	
	private void onAddChild()
	{
		String strName = txtChildName.getText().toString();
		
		Intent intent = new Intent();
		
		Bundle b = new Bundle();
		
		b.putString("childName", strName);
		
		intent.putExtras(b);
		
		setResult(CGroupMgr.ADD_CHILD_REQUEST, intent);
		finish();
	}
}
