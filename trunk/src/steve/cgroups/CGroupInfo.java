package steve.cgroups;

public class CGroupInfo
{
	static 
	{
        System.loadLibrary("cgroupmgr");
    }
	
	public native String[]  getProcessList();
	public native String getProcessInfo(String szPID);
	public native String[] getCGroupChildList(String szContainer);
	public native void addChildToCGroup(String szBasePath, String szChildName);
	public native void addProcessToCGroup(String szBasePath, long ulPID);
	
	public native String getCGroupTasklist(String szCGroup);
}
