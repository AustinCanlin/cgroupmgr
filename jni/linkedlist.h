#pragma once

#define LL_STATUS_SUCCESS 	0
#define LL_STATUS_ERROR		100

typedef struct list_node_t
{
	void *pPayload;
	struct list_node_t *pNext;
} list_node;

// Definiteion of callback function for comparing two payload structures
typedef int (*ll_payloadMatchFunc)(void *pPayload1, void *pPayload2);

// Definition of callback function for freeing a payload structure
typedef void (*ll_payloadFreeFunc)(void *pPayload);

// Appends an item to the linked list
int ll_appendItem(list_node **pList, void *pPayload);

// Inserts an item into the list.  Sorting based on the results of pFunc
int ll_insertItem(list_node **pList, void *pPayload, ll_payloadMatchFunc *pfunc);

// Finds an item in the list.  Match criteria determined by pFunc
int ll_findItem(list_node *pList, void *pPayload, list_node **pFound, ll_payloadMatchFunc *pFunc);

// Destroys the contents of a list
int ll_destroyList(list_node **pList, ll_payloadFreeFunc *pFunc);