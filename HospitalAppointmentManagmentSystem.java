package com.java;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

class Patientnotfoundexception extends Exception
{
    public Patientnotfoundexception(String message)
    {
        super(message);
    }
}
class Doctornotfoundexception extends Exception
{
    public Doctornotfoundexception(String message)
    {
        super(message);
    }
}
class Appointmentnotfoundexception extends Exception
{
    public Appointmentnotfoundexception(String message)
    {
        super(message);
    }
}
class Invalidslotexception extends Exception {
    public Invalidslotexception(String message)
    {
        super(message);
    }
}
class Slotalreadybookedexception extends Exception
{
    public Slotalreadybookedexception(String message)
    {
        super(message);
    }
}
class Pastdateexception extends Exception
{
    public Pastdateexception(String message)
    {
        super(message);
    }
}
class Patient
{
    int id;
    String patientname;
    int age;
    String gender;
    public Patient(int id,String patientname,int age,String gender)
    {
        this.id=id;
        this.patientname=patientname;
        this.age=age;
        this.gender=gender;
    }
    public String toString()
    {
        return "["+id+"] "+patientname+", Age: "+age+", Gender: "+gender;
    }
}
class Doctor
{
    int id;
    String doctorname;
    String specialization;

    public Doctor(int id, String doctorname, String specialization)
    {
        this.id = id;
        this.doctorname = doctorname;
        this.specialization = specialization;
    }
    public String toString()
    {
        return "["+id+"] Dr. "+doctorname+" ("+specialization+")";
    }
}
class Appointment
{
    String appointmentid;
    int patientid;
    int doctorid;
    LocalDate date;
    LocalTime time;
    boolean cancelled=false;
    public Appointment(String appointmentid, int patientid, int doctorid, LocalDate date, LocalTime time)
    {
        this.appointmentid = appointmentid;
        this.patientid = patientid;
        this.doctorid = doctorid;
        this.date = date;
        this.time = time;
    }
    public String toString()
    {
        String status;
        if(cancelled)
            status="Cancelled";
        else
            status="booked";
        return "["+appointmentid+"]"+date+" "+time+"-"+status;
    }
}

class Hospital
{
    Map<Integer,Patient> patients=new HashMap<>();
    Map<Integer,Doctor> doctors=new HashMap<>();
    Map<String,Appointment> appointments=new HashMap<>();
    int patientcounter=1;
    int doctorcounter=1;
    int appointmentcounter=1;
    String[] slots={"09:00","10:00","11:00","12:00","14:00","15:00","16:00"};
    Patient findpatient(int id) throws Patientnotfoundexception
    {
        Patient p=patients.get(id);
        if(p==null)
            throw new Patientnotfoundexception("Patient not found with this id: "+id);
        return p;
    }
    Doctor finddoctor(int id) throws Doctornotfoundexception
    {
        Doctor d=doctors.get(id);
        if(d==null)
            throw new Doctornotfoundexception("Doctor not found with id: "+id);
        return d;
    }
    //method to check whether the slot is valid
    boolean isvalidslot(String time)
    {
        for(String s:slots)
        {
            if(s.equals(time))
                return true;
        }
        return false;
    }
    //method to check whether the slot is already booked or vacant
    boolean isslotbooked(int did, LocalDate date, String time)
    {
        for(Appointment a : appointments.values())
        {
            if(a.doctorid==did && a.date.equals(date) && a.time.equals(LocalTime.parse(time)) && !a.cancelled)
            {
                return true;
            }
        }
        return false;
    }
    //method to register patient
    void registerpatient()
    {
        try
        {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Name,Age,Gender");
            String s[] = sc.nextLine().split(",");
            String name = s[0].trim();
            int age = Integer.parseInt(s[1]);
            String gender = s[2].trim();
            patients.put(patientcounter, new Patient(patientcounter, name, age, gender));
            System.out.println("Patient registered succesfully with ID: "+patientcounter);
            patientcounter++;
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            System.out.println("Invalid input");
        }
        catch(NumberFormatException e)
        {
            System.out.println("Age must valid number");
        }
        System.out.println("-------------------------------");
    }
    //method to add doctor
    void adddoctor()
    {
        try
        {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Name,Specialization");
            String s[] = sc.nextLine().split(",");
            String name = s[0].trim();
            String specialization = s[1].trim();
            doctors.put(doctorcounter, new Doctor(doctorcounter, name, specialization));
            System.out.println("Doctor added sucessfully with id: " + doctorcounter);
            doctorcounter++;
            System.out.println("----------------------------------------");
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            System.out.println("Invalid input");
        }
    }
    //method to show doctors list
    void showdoctorslist()
    {
        for(Doctor d: doctors.values())
        {
            System.out.println(d);
        }
        System.out.println("-------------------------------------");
    }
    //method to book appointment
    void bookappointment()
    {
        try
        {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter patient id: ");
            int pid = sc.nextInt();
            Patient p = findpatient(pid);
            System.out.println("Enter doctor id: ");
            int did = sc.nextInt();
            Doctor d = finddoctor(did);
            System.out.println("Enter date(yyyy-mm-dd)");
            sc.nextLine();
            LocalDate date = LocalDate.parse(sc.nextLine().trim());
            if (date.isBefore(LocalDate.now()))
                throw new Pastdateexception("you cannot book an appointment in the past");
            System.out.println("Avaliable slots: " + Arrays.toString(slots));
            System.out.println("Enter time (HH:MM)");
            String time = sc.nextLine().trim();
            if (!isvalidslot(time))
                throw new Invalidslotexception("Invalid time slot");
            if (isslotbooked(did, date, time))
                throw new Slotalreadybookedexception("Sorry this slot already booked");
            Appointment a = new Appointment("A" + appointmentcounter, pid, did, date, LocalTime.parse(time));
            appointments.put(a.appointmentid, a);
            System.out.println("Appointment booked sucessfully with id: " + a.appointmentid);
            appointmentcounter++;
        }
        catch(Patientnotfoundexception | Doctornotfoundexception | Pastdateexception | Invalidslotexception | Slotalreadybookedexception e)
        {
            System.out.println(e.getMessage());
        }
        catch(NumberFormatException e)
        {
            System.out.println("Patient ID and Doctor ID must be valid numbers");
        }
        catch(DateTimeParseException e)
        {
            System.out.println("Invalid date format");
        }
        System.out.println("--------------------------------------------------");
    }
    //method to cancel appointment
    void cancelappointment()
    {
        try
        {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter appointment id: ");
            String id = sc.nextLine();
            Appointment a = appointments.get(id);
            if (a == null)
                throw new Appointmentnotfoundexception("No appointment found with this appointment id");
            if (a.cancelled)
                throw new Appointmentnotfoundexception("Appointment already cancelled");
            a.cancelled=true;
            System.out.println("Appointment cancellation successfull");
        }
        catch(Appointmentnotfoundexception e)
        {
            System.out.println(e.getMessage());
        }
        System.out.println("---------------------------------");
    }
    //method to view avaliable slots
    void viewavaliableslots()
    {
        try
        {
            Scanner sc=new Scanner(System.in);
            System.out.println("Enter doctor id: ");
            int id=sc.nextInt();
            sc.nextLine();
            Doctor d=finddoctor(id);
            System.out.println("Enter date (yyyy-mm-dd): ");
            LocalDate date=LocalDate.parse(sc.nextLine().trim());
            boolean b=false;
            for(String s:slots)
            {
                if(!isslotbooked(id,date,s))
                {

                    System.out.println("Avaliable slots for this doctor: "+s);
                    b=true;
                }
            }
            if(!b)
            {
                System.out.println("No slots avaliable");
            }
        }
        catch(Doctornotfoundexception e)
        {
            System.out.println(e.getMessage());
        }
        catch(NumberFormatException e)
        {
            System.out.println("Doctor id is not valid");
        }
        catch (DateTimeParseException e)
        {
            System.out.println("Invalid date format");
        }
        System.out.println("------------------------------------");
    }
    //method to view doctor schedule
    void viewdoctorschedule()
    {
        try
        {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter doctor id: ");
            int id = sc.nextInt();
            Doctor d = finddoctor(id);
            System.out.println("Schedule for Doctor "+d);
            boolean status=false;
            for(Appointment a:appointments.values())
            {
                if(a.doctorid==id && !a.cancelled)
                {
                    String pname;
                    try
                    {
                        pname=findpatient(a.patientid).patientname;
                    }
                    catch(Patientnotfoundexception e)
                    {
                        pname="unknown";
                    }
                    System.out.println(" "+a+" | Patient: "+pname);
                    status=true;
                }
            }
            if(!status)
            {
                System.out.println("No booked appointments for this doctor "+d);
            }
        }
        catch(Doctornotfoundexception e)
        {
            System.out.println(e.getMessage());
        }
        catch(NumberFormatException e)
        {
            System.out.println("Doctor id must ba valid number");
        }
        System.out.println("------------------------------------------");
    }
    //method to view patient history
    void viewpatienthistory()
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter patient id: ");
        int id= sc.nextInt();;
        try
        {
            Patient p = findpatient(id);
            boolean status = false;
            for (Appointment a : appointments.values())
            {
                if (a.patientid == id)
                {
                    String dname;
                    try
                    {
                        dname = finddoctor(a.doctorid).doctorname;
                    }
                    catch (Doctornotfoundexception e)
                    {
                        dname = "unknown";
                    }
                    System.out.println(" " + a + " | Doctor: " + dname);
                    status = true;
                }
            }
            if (!status)
            {
                System.out.println("No appointment history found for this patient.");
            }
        }
        catch (Patientnotfoundexception e)
        {
            System.out.println(e.getMessage());
        }

        System.out.println("----------------------------------------");
    }

}

public class HospitalAppointmentManagmentSystem {
    public static void main(String[] args) {
        Hospital h=new Hospital();
        h.patients.put(h.patientcounter,new Patient(h.patientcounter,"Rahul",34,"Male"));
        h.patientcounter++;
        h.patients.put(h.patientcounter,new Patient(h.patientcounter,"Vinay",26,"Male"));
        h.patientcounter++;
        h.doctors.put(h.doctorcounter,new Doctor(h.doctorcounter,"Mahesh","Cardiology"));
        h.doctorcounter++;
        h.doctors.put(h.doctorcounter,new Doctor(h.doctorcounter,"Charan","Orthopedics"));
        h.doctorcounter++;
        Scanner sc=new Scanner(System.in);
        int choice;
        do
        {
            System.out.println("1.Register patient");
            System.out.println("2.Add doctor");
            System.out.println("3.Show doctors list");
            System.out.println("4.Book appointment");
            System.out.println("5.Cancel appointment");
            System.out.println("6.View available slots ");
            System.out.println("7.View doctor schedule");
            System.out.println("8.View patient history");
            System.out.println("0.Exit");
            System.out.println("Enter your choice: ");
            choice=sc.nextInt();
            switch(choice)
            {
                case 1:
                    h.registerpatient();
                    break;
                case 2:
                    h.adddoctor();
                    break;
                case 3:
                    h.showdoctorslist();
                    break;
                case 4:
                    h.bookappointment();
                    break;
                case 5:
                    h.cancelappointment();
                    break;
                case 6:
                    h.viewavaliableslots();
                    break;
                case 7:
                    h.viewdoctorschedule();
                    break;
                case 8:
                    h.viewpatienthistory();
                    break;
                case 0:
                    System.out.println("Exiting....");
                    break;
                default:
                    System.out.println("Select valid option");
                    break;
            }
        }while(choice!=0);
        sc.close();
    }
}
