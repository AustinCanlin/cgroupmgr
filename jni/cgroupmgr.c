#include "linkedlist.h"
#include <string.h>
#include <stdio.h>
#include <jni.h>
#include <sys/types.h>
#include <dirent.h>
#include <fcntl.h>
#include <sys/stat.h>

#define MAX_PATH 1024

// Puts list of processes in linked list.  Returns number of entries
int getDirList(char *szDir, list_node **pList);

void getProcessName(char *szPID, char *szProcInfo);

int getFileSize(char *szFile);

int isNumber(char *szStr);

// Returns a process list to the Java application
jobjectArray Java_steve_cgroups_CGroupInfo_getProcessList( JNIEnv* env,
                                                  jobject thiz)
{	
  	jobjectArray processList = NULL;
  	
  	list_node *pList = NULL;
  	int numProcs = 0;
 
 	numProcs = getDirList("/proc", &pList);
 	
  	processList  = (*env)->NewObjectArray(env, 
  										  numProcs, 
  										  (*env)->FindClass(env, "java/lang/String"), 
  										  0);
 	
  	list_node *pCur = NULL;
  	int i = 0;
 	
   	for(pCur = pList; pCur != NULL; pCur = pCur->pNext)
   	{
   		char procInfo[MAX_PATH + 1];
   		
   		getProcessName((char*)(pCur->pPayload), procInfo);
   		
		jstring str = (*env)->NewStringUTF(env, procInfo);		
		(*env)->SetObjectArrayElement(env, processList, i, str);

		i++;
    }

    ll_payloadFreeFunc pFunc = free;
    ll_destroyList(&pList, &pFunc);
  	
  	return processList;
}

// Returns the statistical information for a running process
jstring Java_steve_cgroups_CGroupInfo_getProcessInfo( JNIEnv* env,
                                                  jobject thiz, jstring pid)
{
	char szProcInfoPath[MAX_PATH+1];
	jstring strRet = NULL;
	
	sprintf(szProcInfoPath, "/proc/%s/status", (*env)->GetStringChars(env, pid, 0));
	
	char szInfo[(MAX_PATH*2) + 1];
	
	FILE *fileStat = fopen(szProcInfoPath, "r");
	
	if(NULL != fileStat)
	{
		unsigned long ulSize = fread(szInfo, 1, MAX_PATH*2, fileStat);
		
		strRet = (*env)->NewStringUTF(env, szInfo);
		
		fclose(fileStat);
	}
	
	return strRet;
}

// Loads the list of files/directories in a directory into a linked list
int getDirList(char *szDir, list_node **pList)
{
	DIR *dp;
    struct dirent *ep;
 	int i = 0;
 	
   	dp = opendir(szDir);
   	if (dp != NULL)
    {
    	while(ep = readdir (dp))
		{	
			if(1 == isNumber(ep->d_name))
			{
				char *szDir = (char*)malloc(strlen(ep->d_name) + 1);
				strcpy(szDir, ep->d_name);			
						
				ll_appendItem(pList, szDir);
				
				i++;
			}
        }
        
       	(void) closedir (dp);
    }
    
    return i;
}

// Returns a process name to be shown in the process list
void getProcessName(char *szPID, char *szProcInfo)
{
	char szProcInfoPath[MAX_PATH + 1];
	int nGotInfo = 0;
	
	sprintf(szProcInfo, "%s ", szPID);
	
	// Set up base path for proc info
	sprintf(szProcInfoPath, "/proc/%s", szPID);
	
	char szCmdLineFile[MAX_PATH + 1];
	
	// Create full path to the /proc/PID/cmdline file
	sprintf(szCmdLineFile, "%s/cmdline", szProcInfoPath);
	
	char *szInfo = (char*)malloc(MAX_PATH + 1);
	
	FILE *cmdFile = fopen(szCmdLineFile, "r");
	
	if(NULL != cmdFile)
	{
		if(0 != fread(szInfo, 1, MAX_PATH, cmdFile))
		{	
			strncat(szProcInfo, szInfo, MAX_PATH);
			nGotInfo = 1;
		}
		
		fclose(cmdFile);
	}
	
	// No command line available, get name from /proc/PID/stat
	if(0 == nGotInfo)
	{
		char szStatFile[MAX_PATH + 1];
		
		sprintf(szStatFile, "%s/stat", szProcInfoPath);
		
		FILE *statFile = fopen(szStatFile, "r");
	
		if(NULL != statFile)
		{
			char szStats[MAX_PATH+1];
			
			if(0 != fread(szStats, 1, MAX_PATH, cmdFile))
			{
				char *szTok = strtok(szStats, " ");
				
				szTok = strtok(NULL, " ");
				
				strncat(szProcInfo, szTok, MAX_PATH);
				
				nGotInfo = 1;
			}
			
			fclose(statFile);
		}
	}
}

// Returns 1 if the string is a number, 0 otherwise
int isNumber(char *szStr)
{
	int nRet = 1;
	int i = 0;
	
	for(i = 0; i < strlen(szStr); i++)
	{
		if(szStr[i] < '0' || szStr[i] > '9')
		{
			nRet = 0; 
			break;
		}
	}
	
	return nRet;
}

// Returns the size, in bytes, of a file
int getFileSize(char *szFile)
{
	struct stat file_status;
	
	stat(szFile, &file_status);
	return file_status.st_size;
}