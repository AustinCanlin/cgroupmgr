package steve.cgroups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import java.lang.String;
import java.io.File;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CGroupMgr extends Activity 
{		
	private TextView cgroupInfo;
	private TextView cgroupPath;
	private String strCGroupPath = "/dev/cpuctl";
	
	static final int SELECT_PROCESS_REQUEST = 0;
	static final int ADD_CHILD_REQUEST = 1;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Intent i = getIntent();
        
        if(null != i)
        {
	        Bundle b = i.getExtras();
	     
	        if(null != b)
	        {
		        if(null != b.getString("cgroupPath"))
		        {
		        	strCGroupPath = b.getString("cgroupPath");        	
		        }
	        }
        }       
        
        cgroupPath = (TextView)findViewById(R.id.txtCGroupPath);
        cgroupPath.setText(strCGroupPath);
        
        cgroupInfo = (TextView)findViewById(R.id.txtCGroupInfo);
        showCGroupInfo();        
        
        final Button procListButton = (Button) findViewById(R.id.btnOpenProcList);
        procListButton.setOnClickListener(new View.OnClickListener() 
        {
        	public void onClick(View v) 
        	{
        		openProcessList();
        	}
        });
        
        final Button childListButton = (Button) findViewById(R.id.btnOpenChildList);
        childListButton.setOnClickListener(new View.OnClickListener() 
        {
        	public void onClick(View v) 
        	{
        		openChildrenList();
        	}
        });   
        
        final Button childAddButton = (Button) findViewById(R.id.btnAddChild);
        childAddButton.setOnClickListener(new View.OnClickListener() 
        {
        	public void onClick(View v) 
        	{
        		addChildDialog();
        	}
        });
    }    
    
    private void openProcessList()
    {
    	Intent i = new Intent(this, ProcessListView.class);
    	
    	Bundle b = new Bundle();
		b.putString("action", "select");
		i.putExtras(b);
		
    	startActivityForResult(i, SELECT_PROCESS_REQUEST);
    }

    private void openChildrenList()
    {
    	Intent i = new Intent(this, CGroupListView.class);
    	
    	Bundle b = new Bundle();
		b.putString("cgroupPath", strCGroupPath);
		i.putExtras(b);
		
    	startActivity(i);
    }
    
    private void addChildDialog()
    {
    	Intent i = new Intent(this, AddChildDialog.class);
    	
    	Bundle b = new Bundle();
		b.putString("cgroupPath", strCGroupPath);
		i.putExtras(b);
		
    	startActivityForResult(i, ADD_CHILD_REQUEST);
    }
    
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) 
    {
        if (requestCode == SELECT_PROCESS_REQUEST) 
        {
        	if(null != data)
        	{
	            Bundle b = data.getExtras();
	            
	            if(null != b)
	            {
	            	String strPID = b.getString("myPID");
	            	
	            	// Add the process to the tasks file
	            	addProcessToCGroup(strPID);
	            }
        	}
        }
        else if(requestCode == ADD_CHILD_REQUEST)
        {
        	if(null != data)
        	{
	            Bundle b = data.getExtras();
	            
	            if(null != b)
	            {
	            	String childName = b.getString("childName");
	            	
	            	CGroupInfo info = new CGroupInfo();
	            	
	            	info.addChildToCGroup(strCGroupPath, childName);
	            }
        	}
        }
    }
    
    protected void addProcessToCGroup(String strPID)
    {      	
    
    	FileOutputStream fileOut;
    	 try 
    	 {
    		 File file = new File(strCGroupPath + "/tasks");
    		 fileOut = new FileOutputStream(file);
    		 DataOutputStream outStream = new DataOutputStream(fileOut);
    		 
    		 long ulPID = Long.parseLong(strPID);
    		 
    		 outStream.writeLong(ulPID);
    		 //outStream.writeChars(strPID);
    		 
    		 outStream.close();
    		 fileOut.close();
    	 } 
    	 catch(IOException e) 
    	 {
    		 System.out.println("Cannot open destination file");
    		 return;
    	 }    
  	 
    	//CGroupInfo info = new CGroupInfo();
    	
    	//info.addProcessToCGroup(strCGroupPath, Long.parseLong(strPID));
    }
    
    protected void showCGroupInfo()
    {
    	CGroupInfo info = new CGroupInfo();
    	
    	String strInfo = new String();
    	
    	strInfo += "Task list:\n__________\n";
    	
    	String strTasks = info.getCGroupTasklist(strCGroupPath); 
    	
    	if(null != strTasks)
    		strInfo += strTasks; 
    	
    	cgroupInfo.setText(strInfo);    	
    }
}