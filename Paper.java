/**
	Author: Khanh Duy Huynh
	Date: 25/11/2011
	File Name: Paper.java
*/

public class Paper
{
	/**
		Declare class variables
	*/
 	private int id;
    private String topic;
    private String author;
    private String date;
    private int rank;

	/**
		Use Constructor to initialize class variables
	*/
    public Paper(int pId, String pTopic, String pAuthor, String pDate, int pRank)
    {
		id = pId;
        topic = pTopic;
        author = pAuthor;
        date = pDate;
        rank = pRank;
    }

	/**
		Get id of paper
	*/
    public int getId()
    {
        return id;
    }

	/**
		Get topic of paper
	*/
    public String getTopic()
    {
        return topic;
    }

	/**
		Get author of paper
	*/
    public String getAuthor()
    {
		return author;
	}

	/**
		Get date of paper
	*/
	public String getDate()
    {
		return date;
	}

	/**
		Get rank of paper
	*/
	public int getRank()
	{
		return rank;
	}

	/**
		Display paper information
	*/
	public String toString()
	{
		return "\n" + " " + id + "\t" + topic + "\t\t" + author + "\t\t" + date + "\t" + rank + "\n";
	}
}