package steve.cgroups;

public class CGroupInfo
{
	static 
	{
        System.loadLibrary("cgroupmgr");
    }
	
	public native String[]  getProcessList();
	public native String getProcessInfo(String szPID);
}
