#include "linkedlist.h"
#include "stdlib.h"

//  Appends an item to the list
int ll_appendItem(list_node **pList, void *pPayload)
{
	int nRet = LL_STATUS_SUCCESS;

	// Create list node to insert
	list_node *pNew = (list_node*)malloc(sizeof(list_node));
	
	while(1)  // Empty loop makes error handling cleaner.  Don't forget the break!
	{
		if(NULL == pNew)
		{
			nRet = LL_STATUS_ERROR;
			break;
		}

		// Assume that payload will not be destroyed by the caller until
		// the list is destroyed
		//
		// Add the payload to the new list node
		pNew->pPayload = pPayload;
		pNew->pNext = NULL;		
		
		// if the list is empty, add new node to the beginning
		if(NULL == *pList)
		{
			*pList = pNew;
		}
		// Otherwise, need to locate last node and add new node there
		else
		{
			list_node *pCur = NULL;

			for(pCur = *pList; pCur->pNext != NULL; pCur = pCur->pNext); // Intentionally empty for loop

			// Add new node to the list
			pCur->pNext = pNew;
		}

		break;
	}

	return nRet;
}

// Inserts an item into the list
int ll_insertItem(list_node **pList, void *pPayload, ll_payloadMatchFunc *pFunc)
{
	int nRet = LL_STATUS_SUCCESS;

	// Create list node to insert
	list_node *pNew = (list_node*)malloc(sizeof(list_node));
	
	while(1)  // Empty loop makes error handling cleaner.  Don't forget the break!
	{
		if(NULL == pNew)
		{
			nRet = LL_STATUS_ERROR;
			break;
		}

		// Assume that payload will not be destroyed by the caller until
		// the list is destroyed
		//
		// Add the payload to the new list node
		pNew->pPayload = pPayload;
		pNew->pNext = NULL;		
		
		// if the list is empty, add new node to the beginning
		if(NULL == *pList)
		{
			*pList = pNew;
		}
		// If new item is less than or equal to list head, then insert it before
		else if((*pFunc)(pNew->pPayload, (*pList)->pPayload) <= 0)
		{
			pNew->pNext = *pList;
			*pList = pNew;
		}
		// Otherwise, need to locate insert point and add it there
		else
		{
			list_node *pCur = NULL;
			list_node *pInsertAfter = NULL;

			for(pCur = *pList; pCur != NULL; pCur = pCur->pNext)
			{				
				// Break when we find a node that is greater than or equal
				// than new node
				if((*pFunc)(pNew->pPayload, pCur->pPayload) > 0)					
					pInsertAfter = pCur;
				else
					break;
			}

			// pCur is now either an insertAfter node or the end of the list
			pNew->pNext = pInsertAfter->pNext;
			pInsertAfter->pNext = pNew;
		}

		break;
	}

	return nRet;
}

// Finds an item in the list
int ll_findItem(list_node *pList, void *pPayload, list_node **pFound, ll_payloadMatchFunc *pFunc)
{
	int nRet = LL_STATUS_SUCCESS;

	while(1)  // Empty loop makes error handling cleaner.  Don't forget the break!
	{
		if(NULL == pList 
			|| NULL == pFunc 
			|| NULL == pPayload 
			|| NULL == pFound)
		{
			nRet = LL_STATUS_ERROR;
			break;
		}

		list_node *pCur = NULL;

		for(pCur = pList; pCur != NULL; pCur = pCur->pNext)
		{				
			// Break when we find a node that is equal
			// to the new node
			if((*pFunc)(pPayload, pCur->pPayload) == 0)					
			{
				*pFound = pCur;
				break;
			}
		}
	
		break;
	}

	return nRet;
}

// Destroys the list
int ll_destroyList(list_node **pList, ll_payloadFreeFunc *pFunc)
{
	int nRet = LL_STATUS_SUCCESS;

	while(1)  // Empty loop makes error handling cleaner.  Don't forget the break!
	{
		if(NULL == pList || NULL == *pList)
		{
			nRet = LL_STATUS_ERROR;
			break;
		}

		list_node *pCur = NULL;
		list_node *pNext = NULL;

		for(pCur = *pList; pCur != NULL;)//pCur = pCur->pNext)
		{				
			// Save pointer to next node
			pNext = pCur->pNext;

			// Destroy the current node
			(*pFunc)(pCur->pPayload);
			pCur->pPayload = NULL;
			free(pCur);
			pCur = NULL;

			// Reset pCur to the next node
			pCur = pNext;
		}
	
		*pList = NULL;

		break;
	}

	return nRet;
}