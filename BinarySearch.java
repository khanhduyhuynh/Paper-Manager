/**
	Author: Khanh Duy Huynh
	Date: 25/11/2011
	File Name: BinarySearch.java
*/

import java.util.*; // For LinkedList support
public class BinarySearch
{
	/**
		Declare class variable
	*/
	private LinkedList<Paper> pList;

	/**
		Use Constructor to initialize class variable,
		@param anList a sorted LinkedList of Paper objects
	*/
	public BinarySearch(LinkedList<Paper> anList)
	{
		pList = anList;
	}

	/**
		Finds a value in a sorted linked list, using the binary search algorithm.
		@param intId the value to search
		@return the index at which the value occurs, or -1 if it does not occur in the array
	*/
	public int search(int intId)
	{
		int low = 0;
		int high = pList.size() - 1;
		while (low <= high)
		{
			int mid = (low + high) / 2;
			int midId = pList.get(mid).getId();

			if (midId == intId)
				return mid;
			else if (midId < intId)
				low = mid + 1;
			else
				high = mid - 1;
		}
		return -1;
	}
}



