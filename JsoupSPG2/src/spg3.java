// test code
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Calendar;
import java.util.Date;

// version with first website

public class spg3 {

	public static void main(String[] args) throws IOException, ParseException {
		// URL parameters
		// Corporate codes imported from http://www.hotelcorporatecodes.com/16/spg-starwood-hotels-corporate-codes.html
		//String[] rate_plan = {"corporateAccountNumber%253A18000"};
		String[] rate_plan = {"corporateAccountNumber%253A253151", "corporateAccountNumber%253A18000", "corporateAccountNumber%253A4313", "corporateAccountNumber%253A9360"};
		//String[] rate_plan = {"253151", "18000"};
		//String[] rate_plan = {"corporateAccountNumber%253A253151"};
		String[] cities = {"SEA", "TPA"};
		int nos_of_child = 0;
		int num_of_rooms = 1;
		int window = 3;
		long range = 1;
		SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy");
		Date endDate = fmt.parse("08/13/2017");

		
		// Today's Date 
		
		Date today = new Date();
		//String arr_date = fmt.format(today);
		//System.out.println(arr_date);
		
		// Date diff in Days
		range = endDate.getTime() - today.getTime();
		long diffDays = 2 + (range / (60 * 60 * 1000 * 24));
		System.out.println(diffDays); 
		
		// Open file
		String csvFile = "C:/Users/adadegok/OneDrive - Microsoft/HackHotel/spg3.csv";
        FileWriter writer = new FileWriter(csvFile);
        
        try {
        	for (int i = 0; i < diffDays; i++) {
				Thread.sleep(7000);
        		Calendar z = Calendar.getInstance();
        		z.setTime(today);
        		z.add(Calendar.DATE, i);
        		String arr_date = fmt.format(z.getTime());
        		System.out.println(arr_date);
        		
        		// Departure date
        		Calendar c = Calendar.getInstance();
        		c.setTime(z.getTime()); // Now use today date.
        		c.add(Calendar.DATE, window); // Adding 3 days
        		String dep_date = fmt.format(c.getTime());
        		System.out.println(dep_date); 
        		
        		getRates(arr_date, dep_date, cities, rate_plan, writer);
        		
			}
        	
        	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		
		//} // rate en_US
			writer.flush();
			writer.close();
			System.out.println("Complete"); 
			

	}
	
	// Function to 
	public static void getRates(String arr_date, String dep_date, String[] cities, String[] rate_plan, FileWriter writer) throws Exception {
		int len_of_stay = 1;
		int occupancy = 1;
		for (String rate : rate_plan){	
			Thread.sleep(4000);
			for (String town : cities){	
				Thread.sleep(4000);
				//Document d=Jsoup.connect("http://www.starwoodhotels.com/preferredguest/search/results/detail.html?refPage=search&numberOfAdults=1&rp=corporateAccountNumber%253A253151&numberOfChildren=0&numberOfRooms=1&arrivalDate=08%2F19%2F2017&departureDate=08%2F20%2F2017&city=Seattle&lengthOfStay=1&roomOccupancyTotal=1").timeout(6000).get();
				// Change airport
				//Document d=Jsoup.connect("http://www.starwoodhotels.com/preferredguest/search/results/detail.html?refPage=search&numberOfAdults=1&rp="+rate_plan+"&numberOfChildren="+nos_of_child+"&numberOfRooms="+num_of_rooms+"&arrivalDate="+arr_date+"&departureDate="+dep_date+"&city="+cities+"&lengthOfStay="+len_of_stay+"&roomOccupancyTotal="+occupancy).timeout(6000).get();
				// Change airport and put in corp code
				//Document d=Jsoup.connect("http://www.starwoodhotels.com/preferredguest/search/results/detail.html?refPage=search&numberOfAdults=1&rp="+rate_plan+"&numberOfChildren=0&numberOfRooms=1&arrivalDate=08%2F19%2F2017&departureDate=08%2F20%2F2017&city="+town+"&lengthOfStay=1&roomOccupancyTotal=1").timeout(6000).get();
				// Change 
				Document d=Jsoup.connect("http://www.starwoodhotels.com/preferredguest/search/results/detail.html?refPage=search&numberOfAdults=1&rp="+rate+"&numberOfChildren=0&numberOfRooms=1&arrivalDate="+arr_date+"&departureDate="+dep_date+"&city="+town+"&lengthOfStay=1&roomOccupancyTotal=1").timeout(6000).get();
				//Elements ele = d.select("div#primaryContainer");
				Elements ele = d.select("div#layoutBody");
				Elements temp = d.select("div.headingContainer");
				
				
				for (Element element : ele.select("div.propertyOuter")) {
					//System.out.println("In for loop!");
					
					// Hotel name
					String h_name = element.select("h2.noSifr a").text();
					System.out.println(h_name);
					h_name = h_name.replaceAll(",", "");
					writer.append(h_name+",");
					
					// Hotel address
					String h_location = element.select("p.propertyLocation").text();
					System.out.println(h_location);	
					h_location = h_location.replaceAll(",", "");
					writer.append(h_location+",");
					
					// Room price
					String h_price = element.select("span.currency").text();
					System.out.println(h_price);
					h_price = h_price.replaceAll(",", "");
					String[] price = h_price.split(" ");
					
					if(price.length == 4 ){
						writer.append(price[1]+","+price[3]+",");
					} else if (price.length == 2) {
						writer.append(price[1]+",,");
					} else {
						writer.append(",,");
					}
					
					// Rate Plan/ESP 
					String h_rp = element.select("span.ratePlan").text();
					System.out.println(h_rp);	
					h_rp = h_rp.replaceAll(",", "");
					String[] rp = h_rp.split("#:");
					writer.append(rp[1]+",");
					
					// City
					for (Element city:temp) {
						System.out.println(city.select("span.textCriteria").text());
						String x = city.select("span.textCriteria").text();
					    x = x.replaceAll(",", "");
						writer.append(x+",");
					}
					
					// Check-in Date
					for (Element h_date:temp) {
						System.out.println(h_date.select("span.date").text());
					}
					
					// Checkout Date
					writer.append(arr_date+","+dep_date);
					writer.append("\n");
					System.out.println("\n");
				}
			}
		}
	}
}
