//  Author: Khanh Duy Huynh
//  Date: 25/11/2011
//  File Name: MergeSorter.java

import java.util.*; // For LinkedList support
public class MergeSorter
{
	// Declare class variables
	LinkedList<Paper> pList;
	private LinkedList<Paper> pFirst;
	private LinkedList<Paper> pSecond;

	/**
			Use Constructor to initialize class variable
			@param anList a LinkedList of Paper objects
	*/
	public MergeSorter(LinkedList<Paper> anList)
	{
		pList = anList;
	}

	/**
		Sorts the array managed by this merge sorter
	*/
	public void sort()
	{
		if (pList.size() > 1)
		{
			pFirst = new LinkedList<Paper>();
			pSecond = new LinkedList<Paper>();
			int half = pList.size()/2;
			int i = 0;
			while (i < half)
			{
				pFirst.addFirst(pList.getFirst());
				pList.remove(pList.getFirst());
				i++;
			}
			while(!pList.isEmpty())
			{
				pSecond.addFirst(pList.getFirst());
				pList.remove(pList.getFirst());
			}

			MergeSorter firstSorter = new MergeSorter(pFirst);
			MergeSorter secondSorter = new MergeSorter(pSecond);
			firstSorter.sort();
			secondSorter.sort();
			merge(pFirst, pSecond);
		}
	}

	/**
		Merges two sorted links into the link managed by this
		merge sorter.
		@param first the first sorted link
		@param second the second sorted link
	*/

	public void merge(LinkedList<Paper> pFirst, LinkedList<Paper> pSecond)
	{
		// Merge both halves into the temporary link
		int iFirst = 0;
		// Next element to consider in the first linked list
		int iSecond = 0;
		// Next element to consider in the second linked list
		int j = 0;
		// Next open position in pList
		// As long as neither iFirst nor iSecond past the end, move the smaller element into pList
		while (iFirst < pFirst.size() && iSecond < pSecond.size())
		{
			if(pFirst.get(iFirst).getId() < pSecond.get(iSecond).getId())
			{
				Paper paper = pFirst.get(iFirst);
				pList.add(j, paper);
				iFirst++;
			}
			else
			{
				Paper paper = pSecond.get(iSecond);
				pList.add(j, paper);
				iSecond++;
			}
			j++;
		}
		// Copy any remaining entries of the first linked list
		for(int f = iFirst; f < pFirst.size(); f++)
		{
			Paper paper = pFirst.get(f);
			pList.add(j, paper);
			j++;
		}

		// Copy any remaining entries of the second linked list
		for(int s = iSecond; s < pSecond.size(); s++)
		{
			Paper paper = pSecond.get(s);
			pList.add(j, paper);
			j++;
		}
	}

}