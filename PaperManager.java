/**
	Author: Khanh Duy Huynh
	Date: 25/11/2011
	File Name: PaperManager.java
*/

import java.awt.*; // For GUI components support
import java.awt.event.*; // For event handling support
import javax.swing.*; // For Swing support
import javax.swing.border.*; // For border support
import java.util.*; // For LinkedList support
import java.io.*; // For IO support

import java.util.regex.Matcher; // For Matcher suport
import java.util.regex.Pattern; // For Pattern support

public class PaperManager extends JFrame
{
	/**
		Declare class components, variables and constants
	*/
	private JPanel Main_Panel;
	private JPanel Top_Panel;
	private JPanel Middle_Panel;
	private JPanel Bottom_Panel;

	private JLabel Search_Label;
	private JTextField Search_TextField;
	private JButton Search_Button;

	private JTextArea Output_TextArea;
	private JScrollPane Output_ScrollPane;

	private JButton Show_Button;
	private JButton Add_Button;
	private JButton Delete_Button;
	private JButton Save_Button;

	private LinkedList<Paper> pList;

	private final String FILE_NAME = "Paper.txt";
	private String strTitleOutput = " " + "Paper ID" + "\t" + "Paper Topic" + "\t\t\t" + "Corresponding Author" + "\t" + "Date" + "\t" + "Rank" + "\n";

	private Pattern pattern;
  	private Matcher matcher;
	private final String DATE_PATTERN ="(0[1-9]|[12][0-9]|3[01])[/](0[1-9]|1[12])[/]\\d{2}$";

	/**
		Create an instance of the class
	*/
	public static void main (String[] args)
	{
	   JFrame My_PaperManager = new PaperManager ();
	}

	/**
		Use Constructor to initialize and add GUI components to frame, initialize class variables
	*/
	public PaperManager()
	{
		pList = new LinkedList<Paper>();

		pattern = Pattern.compile(DATE_PATTERN);

		Main_Panel = new JPanel();
		Main_Panel.setBorder(createTitleBorder("ICAI Paper Manager"));
		Main_Panel.setLayout(new BorderLayout());

		Top_Panel = new JPanel();
		Top_Panel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		Search_Label = new JLabel("Search Paper");
		Search_Label.setFont(new Font("Times New Roman", Font.BOLD, 18));
		Search_TextField = new JTextField(16);
		Search_TextField.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		Search_Button = createSearchButton();

		Top_Panel.add(Search_Label);
		Top_Panel.add(Search_TextField);
		Top_Panel.add(Search_Button);

		Middle_Panel = new JPanel();
		Middle_Panel.setLayout(new FlowLayout(FlowLayout.CENTER));

		Output_TextArea = new JTextArea("", 24, 60);
		Output_TextArea.setEditable(false);
		Output_ScrollPane = new JScrollPane(Output_TextArea);
		Middle_Panel.add(Output_ScrollPane);

		Bottom_Panel = new JPanel();
		Bottom_Panel.setLayout(new GridLayout(1, 4, 0, 0));

		Show_Button = createShowButton();
		Add_Button = createAddButton();
		Delete_Button = createDeleteButton();
		Save_Button = createSaveButton();

		Bottom_Panel.add(Show_Button);
		Bottom_Panel.add(Add_Button);
		Bottom_Panel.add(Delete_Button);
		Bottom_Panel.add(Save_Button);

		Main_Panel.add(Top_Panel, BorderLayout.NORTH);
		Main_Panel.add(Middle_Panel, BorderLayout.CENTER);
		Main_Panel.add(Bottom_Panel, BorderLayout.SOUTH);

		this.setLayout(new GridLayout(1,1));
		add(Main_Panel);

		setTitle ("Paper Manager Application");
		pack();
		setLocationRelativeTo(null);
		setSize(700, 500);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   	setVisible(true);

	   	readFile();
	   	loadData();

	}

	/**
		Create TitledBorder for panel
	*/
	private TitledBorder createTitleBorder(String str)
	{
		Font font = new Font("Serif", Font.ITALIC, 20);
		TitledBorder titled = new TitledBorder(null, str,TitledBorder.LEFT, TitledBorder.TOP, font, Color.BLUE);
		return titled;
	}

	/**
		Initialize, add listeners for Search button and handle event when this button is clicked
	*/
	private JButton createSearchButton()
	{
		JButton Search_Button = new JButton("Go");
		Search_Button.setMargin(new Insets(0, 24, 0, 24));
		Search_Button.setFont(new Font("Times New Roman", Font.BOLD, 16));
		class SearchListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				String strTopic = Search_TextField.getText();
				if(strTopic.compareTo("") != 0)
				{
					String strOutput = "";
					String data = "";
					int count = 0;
					for(int i = 0; i < pList.size(); i++)
					{
						if(pList.get(i).getTopic().indexOf(strTopic) != -1)
						{
							count++;
							data = data + pList.get(i).toString();
						}
					}
					if(count == 0)
					{
						strOutput = "No results found";
					}
					else
					{
						if(count == 1)
						{
							strOutput = count + " result found for \'" + strTopic + "\' :\n";
						}
						else
						{
							strOutput = count + " results found for \'" + strTopic + "\' :\n";
						}
						strOutput = strOutput + data;
					}
					Output_TextArea.setText(strOutput);
				}
				else
				{
					showErrorMessage("Please enter paper topic");
				}
			}
		}
		ActionListener mySearch = new SearchListener();
		Search_Button.addActionListener(mySearch);
		return Search_Button;
	}

	/**
		Initialize, add listeners for Show button and handle event when this button is clicked
	*/
	private JButton createShowButton()
	{
		JButton Show_Button = new JButton("Show All Paper");
		Show_Button.setFont(new Font("Times New Roman", Font.BOLD, 16));
		class ShowListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				loadData();
			}
		}
		ActionListener myShow = new ShowListener();
		Show_Button.addActionListener(myShow);
		return Show_Button;
	}

	/**
		Initialize, add listeners for Add button and handle event when this button is clicked
	*/
	private JButton createAddButton()
	{
		JButton Add_Button = new JButton("Add Paper");
		Add_Button.setFont(new Font("Times New Roman", Font.BOLD, 16));
		class AddListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				Paper paper = checkInput();
				if(paper != null)
				{
					pList.addLast(paper);
					MergeSorter ms = new MergeSorter(pList);
					ms.sort();
					loadData();
				}
			}
		}
		ActionListener myAdd = new AddListener();
		Add_Button.addActionListener(myAdd);
		return Add_Button;
	}

	/**
		Initialize, add listeners for Delete button and handle event when this button is clicked
	*/
	private JButton createDeleteButton()
	{
		JButton Delete_Button = new JButton("Delete Paper");
		Delete_Button.setFont(new Font("Times New Roman", Font.BOLD, 16));
		class DeleteListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				boolean check = false;
				while(check == false)
				{
					String strId = JOptionPane.showInputDialog(null, "Please input paper id (an integer between 0 to 300): ", "Input", 3);
					if(strId != null)
					{
						boolean checkType = checkInt(strId, "Id");
						if(checkType == true)
						{
							MergeSorter ms = new MergeSorter(pList);
							ms.sort();
							BinarySearch bs = new BinarySearch(pList);
							int rs = bs.search(Integer.parseInt(strId));
							if(rs == -1)
							{
								showErrorMessage("Paper ID not exists. No record is deleted");
							}
							else
							{
								pList.remove(rs);
								showErrorMessage("Paper ID "+ strId + " has been deleted");
								loadData();
							}
							check = true;
						}
					}
					else
					{
						check = true;
					}
				}
			}
		}
		ActionListener myDelete = new DeleteListener();
		Delete_Button.addActionListener(myDelete);
		return Delete_Button;
	}

	/**
		Initialize, add listeners for Save button and handle event when this button is clicked
	*/
	private JButton createSaveButton()
	{
		JButton Save_Button = new JButton("Save File");
		Save_Button.setFont(new Font("Times New Roman", Font.BOLD, 16));
		class SaveListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				boolean blWrite = writeFile();
				if(blWrite == true)
				{
					showErrorMessage("Data saved successfully");
				}
			}
		}
		ActionListener mySave = new SaveListener();
		Save_Button.addActionListener(mySave);
		return Save_Button;
	}

	/**
		Read data from text file and add data to LinkedList
	*/
	private void readFile()
	{
		try
		{
			FileReader reader = new FileReader(FILE_NAME);
			Scanner in = new Scanner(reader);
			while (in.hasNextLine())
			{
				String input = in.nextLine();
				String[] temp = input.split(",");
				int intId = Integer.parseInt(temp[0]);
				int intRank = Integer.parseInt(temp[4]);

				Paper paper = new Paper(intId, temp[1], temp[2], temp[3], intRank);
				pList.add(paper);
			}

		}
		catch(Exception e)
		{
			showErrorMessage("File not found");
		}
	}

	/**
		Write data from LinkedList to text file
	*/
	private boolean writeFile()
	{
		boolean check = false;
		try
		{
			PrintWriter out = null;
			out = new PrintWriter (new FileOutputStream(FILE_NAME));
			for (int i=0; i < pList.size(); i++)
			{
				out.println(pList.get(i).getId() + ","+pList.get(i).getTopic() + "," + pList.get(i).getAuthor() + "," + pList.get(i).getDate() + "," + pList.get(i).getRank());
			}
			out.close();
			check = true;
		}
		catch(FileNotFoundException exception)
		{
			showErrorMessage("File not found");
		}
		return check;
	}

	/**
		Display data from LinkedList on text area
	*/
	private void loadData()
	{
		Output_TextArea.setText("");
		String strOutput = "";
		if(pList.isEmpty())
		{
			strOutput = "No paper exists";
		}
		else
		{
			int count = 0;
			strOutput = strTitleOutput;
			for(int i = 0; i < pList.size(); i++)
			{
				strOutput = strOutput + pList.get(i).toString();
				count++;
			}
			strOutput = strOutput + "\n " + "Total " + count + " entries!";
		}
		Output_TextArea.setText(strOutput);
	}

	/**
		Show input dialogs for user entering data and call methods to check if input data is valid
	 	If all input data is valid, create and return a new Paper object
		Otherwise, return a null Paper object
	*/
	private Paper checkInput()
	{
		Paper paper = null;
		boolean blId = false;
		while(blId == false)
		{
			String strId = JOptionPane.showInputDialog(null, "Please input paper id (an integer between 0 to 300): ", "Input", 3);
			if(strId != null)
			{
				boolean checkType = checkInt(strId, "Id");
				if(checkType == true)
				{
					MergeSorter ms = new MergeSorter(pList);
					ms.sort();
					BinarySearch bs = new BinarySearch(pList);
					int rs = bs.search(Integer.parseInt(strId));
					if(rs == -1)
					{
						blId = true;
						int intId = Integer.parseInt(strId);
						boolean blTopic = false;
						while(blTopic == false)
						{
							String strTopic = JOptionPane.showInputDialog(null, "Please input paper topic: ", "Input", 3);
							if(strTopic != null)
							{
								if(strTopic.compareTo("") != 0)
								{
									blTopic = true;
									boolean blAuthor = false;
									while(blAuthor == false)
									{
										String strAuthor = JOptionPane.showInputDialog(null, "Please input author name: ", "Input", 3);
										if(strAuthor != null)
										{
											if(strAuthor.compareTo("") != 0)
											{
												blAuthor =true;
												boolean blDate = false;
												while(blDate == false)
												{
													String strDate = JOptionPane.showInputDialog(null, "Please input date (dd/mm/yy): ", "Input", 3);
													if(strDate != null)
													{
														boolean checkDateFormat = checkDate(strDate);
														if(checkDateFormat == true)
														{
															blDate =true;
															boolean blRank = false;
															while(blRank == false)
															{
																String strRank = JOptionPane.showInputDialog(null, "Please input paper rank (an integer from 1 to 5): ", "Input", 3);
																if(strRank != null)
																{
																	boolean checkRank = checkInt(strRank, "Rank");
																	if(checkRank == true)
																	{
																		blRank =true;
																		int intRank = Integer.parseInt(strRank);
																		paper = new Paper(intId, strTopic, strAuthor, strDate, intRank);
																	}
																}
																else
																{
																	blRank = true;
																}
															}
														}
													}
													else
													{
														blDate = true;
													}
												}
											}
										}
										else
										{
											blAuthor = true;
										}
									}
								}
							}
							else
							{
								blTopic = true;
							}
						}
					}
					else
					{
						showErrorMessage("Paper ID exists. You must enter an different ID");
					}
				}
			}
			else
			{
				blId = true;
			}
		}
		return paper;
	}

	/**
		Check if the argument str having integer data type as well as the range of this integer number is valid based on argument type
		If it is valid, return true
		Otherwise, return false
	*/
	private boolean checkInt(String str, String type)
	{
		boolean check = true;
		try
		{
			int intId = Integer.parseInt(str);
			if(type.compareTo("Id") == 0)
			{
				if(intId < 0 || intId > 300)
				{
					check = false;
				}
			}
			if(type.compareTo("Rank") == 0)
			{
				if(intId < 1 || intId > 5)
				{
					check = false;
				}
			}
		}
		catch(Exception e)
		{
			check = false;
		}
		return check;
	}

	/**
		Check if argument strDate having pattern same as the predefined pattern (dd/mm/yy)
		If it is valid, return true;
		Otherwise, return false
	*/
	private boolean checkDate(String strDate)
	{
		boolean check = false;
		matcher = pattern.matcher(strDate);
		if(matcher.matches())
		{
			check = true;
		}
		return check;
	}

	/**
		Show message dialog
	*/
	private void showErrorMessage(String str)
	{
		JOptionPane.showMessageDialog(this, str, "Error", 1);
	}
}