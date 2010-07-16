#include "linkedlist.h"
#include <string.h>
#include <stdio.h>
#include <jni.h>
#include <sys/types.h>
#include <dirent.h>

// Puts list of processes in linked list.  Returns number of entries
int getDirList(char *szDir, list_node **pList);

int isNumber(char *szStr);

jstring
Java_steve_cgroups_CGroupInfo_stringFromJNI( JNIEnv* env,
                                                  jobject thiz )
{
    return (*env)->NewStringUTF(env, "This might just work.");
}

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
   		char szStr[20];
   		sprintf(szStr, "Hello %d", i);
		jstring str = (*env)->NewStringUTF(env, (char*)(pCur->pPayload));		
		(*env)->SetObjectArrayElement(env, processList, i, str);

		i++;
    }

    ll_payloadFreeFunc pFunc = free;
    ll_destroyList(&pList, &pFunc);
  	
  	return processList;
}

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