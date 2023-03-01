import java.util.*;

class train {
    String train_name;
    String train_id;
    ArrayList<String>stations=new ArrayList<>();
    int number_of_seats = 1;
    LinkedHashMap<String,Integer>station_book_map=new LinkedHashMap<>();
    int waiting_list=5;
    ArrayList<passenger>waiting=new ArrayList<>();
    int waiting_tick_id=200;

    train(String train_name, String train_id)
    {
        this.train_name = train_name;
        this.train_id = train_id;
    }
    void add_stations(String Station_name)
    {
        Station_name=Station_name.toUpperCase();
        stations.add(Station_name);
        station_book_map.put(Station_name,1);
    }
    void display_stations()
    {
        int id=1;
        for(int i=0;i<stations.size()-1;i++)
        {
            System.out.println("STATION_"+id+": "+stations.get(i));
            id++;
        }
    }

    boolean check(String name)
    {
        return stations.contains(name);
    }

    boolean check2(String source,String dest)
    {
        int ind=-1;
        for(int i=0;i<stations.size();i++)
        {
            if(stations.get(i).equals(source))
            {
                ind=0;
            }
            if(ind!=-1 && stations.get(i).equals(dest))return true;
        }
        return false;
    }
    void display_map()
    {
        for(String str:station_book_map.keySet())
        {
            System.out.println(str+" "+station_book_map.get(str));
        }
    }
    void addwaiting(passenger p)
    {
        waiting.add(p);
    }
    void add_passenger(String source,String dest)
    {
        boolean assigned=false;
        for(passenger p:waiting)
        {
            if(can_accom(source,dest))
            {
                assigned=true;
                int source_ind = stations.indexOf(source);
                int dest_ind = stations.indexOf(dest);
                for (int i = source_ind; i < dest_ind; i++)
                {
                    station_book_map.put(stations.get(i),station_book_map.get(stations.get(i)) - 1);
                }
                System.out.println("TICKET HAS BEEN ASSIGNED TO: "+p.name);
                ticket temp=new ticket(p.name,waiting_tick_id,source,dest,train_name);
                p.ticket_id=temp;
                waiting_tick_id+=1;
            }
        }
    }
    boolean can_accom(String source,String dest)
    {
        int source_ind=stations.indexOf(source);
        int dest_ind=stations.indexOf(dest);
        for(int i=source_ind;i<dest_ind;i++)
        {
            if(station_book_map.get(stations.get(i))<=0)
            {
                return false;
            }
        }
        return true;
    }
}
class ticket {
    String passenger_name;
    int ticket_id;
    String source_name;
    String destination_name;
    String train_name;
    ticket(String passenger_name,int ticket_id, String source_name, String destination_name,String train_name)
    {
        this.passenger_name=passenger_name;
        this.ticket_id = ticket_id;
        this.source_name = source_name;
        this.destination_name = destination_name;
        this.train_name=train_name;
    }
}
class waiting_list_object
{
    passenger p;
    String source;
    String destination;
    waiting_list_object(passenger p,String source,String destination)
    {
        this.p=p;
        this.source=source;
        this.destination=destination;
    }
}
class passenger {
    String name;
    String gender;
    int age;
    String password;
    ticket ticket_id;
    String source;
    String destination;
    String train_name;

    passenger(String name, String gender, int age, String password) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.password = password;
    }
    void add_sd(String source,String destination,String train_name)
    {
        this.source=source;
        this.destination=destination;
        this.train_name=train_name;
    }
}

public class train_console {
    static ArrayList<waiting_list_object>waiting_list_list=new ArrayList<>();
    static ArrayList<passenger> pass = new ArrayList<>();
    static ArrayList<train> train = new ArrayList<>();
    static ArrayList<ticket> ticket = new ArrayList<>();
    static HashMap<String, Integer> ticket_map = new HashMap<>();
    static Queue<passenger>que=new ArrayDeque<>();
    static int tick_id=100;
    static void passenger_create()
    {
        Scanner pass_create=new Scanner(System.in);
        System.out.println("ENTER YOUR NAME:");
        String name = pass_create.next();
        System.out.println("ENTER YOUR GENDER:");
        String gender = pass_create.next();
        System.out.println("ENTER YOUR AGE:");
        String age = pass_create.next();
        System.out.println("ENTER YOUR PASSWORD:");
        String password = pass_create.next();
        pass.add(new passenger(name, gender, Integer.parseInt(age), password));
    }
    static boolean isticketavail(String train_name,String source,String dest)
    {
        for(train t:train)
        {
            if(t.train_name.equals(train_name))
            {
                int source_ind=t.stations.indexOf(source);
                int dest_ind=t.stations.indexOf(dest);
                for(int i=source_ind;i<dest_ind+1;i++)
                {
                    if(t.station_book_map.get(t.stations.get(i))<=0)
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    static void waiting_list_assigner(String name,String source,String dest,String train_name)
    {
        for(train t:train)
        {
            if(t.train_name.equals(train_name))
            {
                if(t.waiting.size()<5)
                {
                    for (passenger p : pass)
                    {
                        if (p.name.equals(name))
                        {
                            t.addwaiting(p);
                            System.out.println("YOU HAVE BEEN ADDED TO THE WAITING LIST");
                        }
                    }
                }
                else
                {
                    System.out.println("SORRY!!!! THE WAITING LIST IS ALSO FULL....NO TICKETS ARE AVAILABLE");
                }
            }
        }
    }
    static void book_ticket2(String name,String password,String source,String dest)
    {
        passenger temp=null;
        boolean train_avail=false;
        String train_name="";
        boolean already_not_booked=false;
        for(passenger p:pass)
        {
            if(p.name.equals(name) && p.password.equals(password))
            {
                temp = p;
                if(p.ticket_id==null)already_not_booked=true;
            }
        }
        if(already_not_booked)
        {
            for (train t : train) {
                if (t.check2(source, dest)) {
                    train_avail = true;
                    train_name = t.train_name;
                }
            }
            boolean ticket_avail = isticketavail(train_name, source, dest);
            if (train_avail)
            {
                if (ticket_avail)
                {
                    for (train t : train) {
                        if (t.train_name.equals(train_name))
                        {
                            int source_ind = t.stations.indexOf(source);
                            int dest_ind = t.stations.indexOf(dest);
                            for (int i = source_ind; i < dest_ind; i++)
                            {
                                t.station_book_map.put(t.stations.get(i), t.station_book_map.get(t.stations.get(i)) - 1);
                            }
                        }
                    }
                    System.out.println("TICKET HAS BEEN BOOKED SUCESSFULLY");
                    ticket create = new ticket(name, tick_id, source, dest, train_name);
                    ticket.add(create);
                    for (passenger p : pass)
                    {
                        if (p.name.equals(name))
                        {
                            p.ticket_id = create;
                            break;
                        }
                    }
                    for (train t : train)
                    {
                        if (t.train_name.equals(train_name))
                        {
                            t.display_map();
                            break;
                        }
                    }
                }
                else
                {
                    waiting_list_assigner(name,source,dest,train_name);
                }
            } else {
                System.out.println("NO TRAIN PASSES FROM SOURCE TO DESTINATION!!!!");
            }
        }
        else
        {
            System.out.println("ONE PERSON CAN BOOK ONLY ONE TICKET!!!! SORRY ABOUT THAT");
        }
    }
    static void cancelticket(String name)
    {
        ticket temp_tick_id=null;
        String train_name="";
        for(passenger p:pass)
        {
            if(p.name.equals(name))
            {
                if(p.ticket_id!=null)temp_tick_id = p.ticket_id;
                train_name=p.ticket_id.train_name;
                p.ticket_id = null;
                System.out.println("TICKET CANCELLED SUCESSFULLY");
            }
        }
        for(train t:train)
        {
            if(t.train_name.equals(train_name))
            {
                int source_ind=t.stations.indexOf(temp_tick_id.source_name);
                int dest_ind=t.stations.indexOf(temp_tick_id.destination_name);
                for(int i=source_ind;i<dest_ind;i++)
                {
                    t.station_book_map.put(t.stations.get(i),t.station_book_map.get(t.stations.get(i))+1);
                }
                t.add_passenger(temp_tick_id.source_name, temp_tick_id.destination_name);
                t.display_map();
            }
        }
        cancelled_ticket_assigner(name);

    }
    static void cancelled_ticket_assigner(String name2)
    {
        passenger per=null;
        for(passenger p:pass)
        {
            if(p.name.equals(name2))
            {
                per=p;
                break;
            }
        }

        String name= per.name;
        String source= per.source;
        String dest= per.destination;
        String train_name= per.train_name;
        for(train t:train)
        {
            if(t.train_name.equals(train_name))
            {
                t.add_passenger(source,dest);
            }
        }
    }
    static boolean signin(String name,String password)
    {
        for (passenger p:pass)
        {
            if (p.name.equals(name) && p.password.equals(password))
            {
                return true;
            }
        }
        return false;
    }
    static void passenger()
    {
        Scanner input = new Scanner(System.in);
        boolean passenger_flag = true;
        String choice = "";
        while (passenger_flag) {
            System.out.println("1.SIGNIN \n2.SIGNUP \n3.LOGOUT");
            choice = input.next();
            if (choice.equals("3")) passenger_flag = false;
            if (choice.equals("2")) passenger_create();
            if (choice.equals("1")) {
                System.out.println("ENTER YOUR NAME:");
                String name = input.next();
                System.out.println("ENTER YOUR PASSWORD:");
                String password = input.next();
                boolean pas = signin(name,password);
                if (pas)
                {
                    String pass_choice = "";
                    boolean user_login = true;
                    while (user_login)
                    {
                        System.out.println("1.VIEW TRAIN \n2.VIEW TICKET \n3.BOOK TICKET \n4.CANCEL TICKET \n5.EXIT");
                        pass_choice = input.next();
                        if (pass_choice.equals("1")) viewtrain(train);
                        if (pass_choice.equals("3"))
                        {
                            System.out.println("ENTER THE SOURCE:");
                            String source=input.next();
                            System.out.println("ENTER THE DESTINATION:");
                            String destination= input.next();
                            book_ticket2(name,password,source,destination);
                        }
                        if (pass_choice.equals("2")) viewticket(name,password);
                        if(pass_choice.equals("4")) cancelticket(name);
                        if (pass_choice.equals("5")) user_login = false;
                    }
                } else {
                    System.out.println("INCORRECT NAME OR PASSWORD");
                }
            }
            if(!choice.equals("1") && !choice.equals("2") && !choice.equals("3"))System.out.println("INVALID COMMAND");
        }
    }
    static void admin()
    {
        Scanner obj = new Scanner(System.in);
        String choice1;
        boolean admin_flag = true;
        while (admin_flag) {
            System.out.println("1.VIEW TRAIN \n2.VIEW PASSENGER DETAILS \n3.EXIT");
            choice1 = obj.next();
            if (choice1.equals("1")) viewtrain(train);
            if (choice1.equals("2")) view_passenger(pass);
            if (choice1.equals("3")) admin_flag = false;
            if(!choice1.equals("1") && !choice1.equals("2") && !choice1.equals("3")) System.out.println("Enter a valid command !!");
        }
    }
    static void viewtrain (ArrayList < train > train)
    {
        Scanner out = new Scanner(System.in);
        String choice2 = "";
        int map_value = 1;
        HashMap<Integer, String> map = new HashMap<>();
        for (train t : train)
        {
            map.put(map_value, t.train_name);
            System.out.println(map_value + "." + t.train_name);
            map_value++;
        }
        System.out.println("ENTER YOUR CHOICE:");
        String input5 = out.next();
        for (train t : train) {
            if (t.train_name.equals(map.get(Integer.parseInt(input5))))
            {
                System.out.println("TRAIN NAME: " + t.train_name + "\nSOURCE: " + t.stations.get(0)+ "\nDESTINATION: " + t.stations.get(t.stations.size()-1));
                t.display_stations();
                System.out.println("NUMBER OF TICKETS:" + t.number_of_seats);
                System.out.println("NUMBER OF WAITING_LIST: "+ t.waiting_list);
                System.out.println("-----------------------------");
                break;
            }
        }
    }
    static void view_passenger (ArrayList < passenger > pass)
    {
        Scanner n = new Scanner(System.in);
        if(pass.size()==0)
        {
            System.out.println("NO PASSENGERS YET:");
        }
        for (passenger p : pass)
        {
            System.out.println(p.name);
        }
        String choice5 = n.next();
        for (passenger p : pass) {
            if (p.name.equals(choice5)) {
                System.out.println("NAME:" + p.name);
                System.out.println("AGE:" + p.age);
                System.out.println("GENDER:" + p.gender);
            }
        }
    }
    static void viewticket (String name,String password)
    {
        ticket id=null;
        boolean view=false;
        for(passenger p:pass)
        {
            if (p.name.equals(name) && p.password.equals(password))
            {
                if (p.ticket_id == null)
                {
                    System.out.println("NO BOOKED TICKET AVAILABLE");
                }
                else
                {
                    id = p.ticket_id;
                    view = true;
                }
            }
        }
        if(view)
        {
            System.out.println("NAME: " + id.passenger_name);
            System.out.println("TICKET ID: " + id.ticket_id);
            System.out.println("SOURCE: " + id.source_name);
            System.out.println("DESTINATION: " + id.destination_name);
            System.out.println("TRAIN NAME: " + id.train_name);
        }
    }
    public static void main(String[] args)
    {
        train t1 = new train("CHENNAI EXPRESS", "001");
        t1.add_stations("CHENNAI");
        t1.add_stations("COVAI");
        t1.add_stations("SOMANUR");
        t1.add_stations(("SALEM"));
        t1.add_stations("MADURAI");
        train t2 = new train("DELHI EXPRESS", "002");
        t2.add_stations("CHENNAI");
        t2.add_stations("TELANGANA");
        t2.add_stations("JAIPUR");
        t2.add_stations("DELHI");
        Scanner in = new Scanner(System.in);
        ticket_map.put("CHENNAI EXPRESS", 1);
        ticket_map.put("SALEM EXPRESS", 1);
        train.add(t1);
        train.add(t2);
        passenger p1=new passenger("SURIYA","MALE",20,"123");
        passenger p2=new passenger("JEYAN","MALE",20,"123");
        pass.add(p1);
        pass.add(p2);

        boolean flag1=true;
        while (flag1)
        {
            System.out.println("1.ADMIN \n2.PASSENGER\n3.EXIT");
            String user = in.next();
            if (user.equals("1")) admin();
            if (user.equals("2")) passenger();
            if(user.equals("3")) flag1=false;
            if(!user.equals("1") && !user.equals("2") && !user.equals("3")) System.out.println("INVALID COMMAND");
        }
    }
}
