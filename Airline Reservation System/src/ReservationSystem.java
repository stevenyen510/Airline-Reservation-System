/** CS151 Programming Assignment 1: Airline Reservation System
*@author Steven Yen <steven.yen@sjsu.edu>
*@version 1.0
*Copyright 2016 Steven Yen
*/

import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;

/**An airline seat reservation system. Interacts with the user and imports previous info from file.

*/
public class ReservationSystem
{
	/**
		Main method that acts as the liason between the user and the supporting class and methods of this program.
	*/
	public static void main(String[] args) throws FileNotFoundException
	{
		Plane currentPlane = new Plane();
		
		importFromFile(args[0],currentPlane); //importing data from file (if existing) to data structure.

		Scanner consoleInput = new Scanner(System.in);
		
		boolean repeat = true;
		
		while(repeat)
		{
			System.out.println("\nPlease select from the following main menu options:");
			System.out.println("[P] Add Passenger\n[G] Add Group\n[C] Cancel Reservation\n[A] Print Seating Availability Chart\n[M] Print Manifest\n[Q] Quit");
			
			String input = consoleInput.next();
			
			if(input.equalsIgnoreCase("P"))
				{
					String newName,  serviceClass, seatPref;
					
					System.out.println("Please enter following information to add passenger:");
					System.out.print("Name: ");
					consoleInput.nextLine();//advance a line, since when P is read it, cursor still on that line.
					newName = consoleInput.nextLine();
					System.out.print("\nService Class: ");
					serviceClass = consoleInput.next();
					System.out.print("\nSeat Preference: ");
					seatPref = consoleInput.next();
					System.out.println();
					
					boolean seatReserved = currentPlane.addPassenger(newName, serviceClass,seatPref);

					while(!seatReserved)
					{
						System.out.println("No seat matching passenger seat preference is available.");
						System.out.println("Please enter alternative seat preference:");
						System.out.print("Seat Preference:");
						seatPref = consoleInput.next();
						seatReserved = currentPlane.addPassenger(newName, serviceClass, seatPref);
					}
					
				}
			else if(input.equalsIgnoreCase("G"))
				{
					String groupName, names, serviceClass;	
					System.out.println("Please enter following information to add group:");
					System.out.print("Group Name: ");
					groupName = consoleInput.next();
					consoleInput.nextLine();
					System.out.print("\nNOTE: please enter names seperated by a comma followed by one space (example: John Doe, Jane Doe)");
					System.out.print("\nNames: ");
					names = consoleInput.nextLine();
					System.out.print("\nService Class: ");
					serviceClass = consoleInput.next();
					System.out.println();					
					
					currentPlane.addGroup(groupName, names, serviceClass);
					
				}
			else if(input.equalsIgnoreCase("C"))
				{
					String type="";
					String name="";
					System.out.println("You've selected [C]ancel Reservations. Please select:");
					System.out.print("Cancel [I]ndividual or [G]roup? ");
					type = consoleInput.next();
					System.out.println("User selected:"+type);
					if(type.equals("I"))
					{
						System.out.print("\nName:");
						consoleInput.nextLine(); //advance cursor by a line.
						name = consoleInput.nextLine();
					}
					else if(type.equals("G"))
					{
						System.out.print("\nGroup:");
						consoleInput.nextLine(); //advance cursor by a line.
						name = consoleInput.nextLine();
					}
					else
					{
						System.out.println("\nInvalid type entered. Please enter I or G");
					}
			
					currentPlane.cancelReservations(type,name);	
				}
			else if(input.equalsIgnoreCase("A"))
				{
					String serviceClass;
					System.out.println("\nPlease enter the service class for which Seating Availbility Chart is desired:");
					System.out.print("Service Class:");
					serviceClass = consoleInput.next();
					System.out.println();
					currentPlane.printSeatAvailChart(serviceClass);
				}	
			else if(input.equalsIgnoreCase("M"))
				{
					String serviceClass=" ";
					System.out.println("\nPlease enter service class for which manifest is desired:");
					
					System.out.print("Service Class:");
					serviceClass=consoleInput.next();
					System.out.println();
					currentPlane.printManifest(serviceClass);
									
				}
			else if(input.equalsIgnoreCase("Q"))
				{
					currentPlane.saveData2File(args[0]);
					repeat=false;
				}
			else
			{System.out.println("Invalid Input. Please try again.");}
			
		}
	
	}
	
	/**
		Helping method used to read file from the specified file name and store to specific Plane object.
		@param fileName name of the file without the extension (.txt).
		@param somePlane specific Plane object to which the data imported will be stored.
	*/
	public static void importFromFile(String fileName, Plane somePlane) throws FileNotFoundException
	{
		Plane thisPlane = somePlane;
		
		
		fileName = fileName+".txt";
		
		File file = new File(fileName);
		//System.out.println(file.exists()); //checks current directory.
		
		if(file.exists())
		{
		
			Scanner fileInput = new Scanner(new FileInputStream(fileName)); 
			
			String headerText=fileInput.nextLine();
			
			while(fileInput.hasNextLine())
			{
				//fileInput.nextLine(); //advance cursor by a line.
				String rowOfText = fileInput.nextLine();
				//System.out.println("Line read:" +rowOfText);
				//fileInput.nextLine(); //advance cursor by a line.
				
				String[] SubstringsArr;
				String delimiter =", ";
				SubstringsArr = rowOfText.split(delimiter);
				
				//for(int i=0;i<SubstringsArr.length;i++)
				//{System.out.println(SubstringsArr[i]);}
				

				//System.out.println(SubstringsArr[0].length());
				
				String s1=" ";
				String s2=" ";
					
				if(SubstringsArr[0].length()>2)
				{
					s1 = SubstringsArr[0].substring(0,2);
					s2 = SubstringsArr[0].substring(2,3);
				}
				else
				{
					s1 = SubstringsArr[0].substring(0,1);
					s2 = SubstringsArr[0].substring(1,2);
				}
				
				//System.out.println("s1="+s1); //confirmed that this parses correctly. s1=10..for econ class. s1=1,2 for first class.
				//System.out.println("s2="+s2); //confirmed that this parses correctly and is set to seat designate letter
				
				int colNumber=0;
				
				if(s2.equals("A")){colNumber=0;}
				else if(s2.equals("B")){colNumber=1;}
				else if(s2.equals("C")){colNumber=2;}
				else if(s2.equals("D")){colNumber=3;}
				else if(s2.equals("E")){colNumber=4;}
				else if(s2.equals("F")){colNumber=5;}
				else{System.out.println("invalid column");}
				
				
				//System.out.println("colNumber="+colNumber); //correctly associates (0,A)(1,B)(2,C)(3,D)....
				
				//System.out.println(s2.equalsIgnoreCase("A"));
				
				int x;  //row number
				x= Integer.parseInt(s1); //row number
				
				//System.out.println("x+1="+(x+1));
				
				if(SubstringsArr[1].equals("I"))
				{
					Passenger prevPass = new Passenger(SubstringsArr[2],SubstringsArr[1],"n/a,indv");
					thisPlane.addFromFile(prevPass,(x-1),colNumber);
				}
				else if(SubstringsArr[1].equals("G"))
				{
					Passenger prevPass = new Passenger(SubstringsArr[3],SubstringsArr[1],SubstringsArr[2]);
					thisPlane.addFromFile(prevPass,(x-1),colNumber);
				}
				else
				{
					System.out.println("invalide type. Need to be I or G");
				}
			
			}
			
		
			fileInput.close();
		
		}
		
	}	
	
	
	
	
}