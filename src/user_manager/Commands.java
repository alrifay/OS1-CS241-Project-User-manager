package user_manager;

//<editor-fold defaultstate="collapsed" desc="import">
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
//</editor-fold>

public class Commands {

//<editor-fold defaultstate="collapsed" desc="Mohamed Abd El-latif">
    public static ArrayList<String> Execute(String args) throws IOException {
        ArrayList<String> Result = new ArrayList<>();
        String[] command = {"bash", "-c", args};
        Process p = new ProcessBuilder(command).start();
        Scanner err = new Scanner(p.getErrorStream());
        Scanner out = new Scanner(p.getInputStream());
        if (err.hasNext()) {
            //console.Error(err.useDelimiter("\\A").next());
            throw new IOException();
        }
        while (out.hasNext()) {
            Result.add(out.next());
        }
        return Result;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Ahmed Salah">
//<editor-fold defaultstate="collapsed" desc="Group">
    public static boolean assign_user_to_group(String group_name, String user_name) {

        try {
            Execute("sudo usermod -a -G " + group_name + " " + user_name);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean delete_user_from_group(String group_name, String user_name) {

        try {
            Execute("sudo deluser  " + user_name + " " + group_name);

        } catch (Exception e) {
            return false;
        }
        return true;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="User">
    //<editor-fold defaultstate="collapsed" desc="Extra">
    public static boolean lock_user_account(String user_name) {
        try {
            Execute("usermod -L " + user_name);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean unlock_user_account(String user_name) {
        try {
            Execute("usermod -U " + user_name);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean expiry_date_account(String date, String user_name) {
        try {
            Execute("usermod -e " + date + " " + user_name);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

//</editor-fold>
    public static boolean change_user_directory(String new_directory, String user_name) {
        try {
            Execute("usermod -d " + new_directory + " -m " + user_name);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean change_user_name(String old_user_name, String new_user_name) {

        try {
            Execute("usermod -l " + new_user_name + " " + old_user_name);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean change_user_id(int new_id, String user_name) {

        try {
            Execute("usermod -u " + new_id + " " + user_name);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean change_room_number(String new_room_number, String user_name) {

        try {
            Execute("chfn -r " + new_room_number + " " + user_name);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean change_home_number(String new_home_number, String user_name) {

        try {
            Execute("chfn -h " + new_home_number + " " + user_name);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean change_other(String new_other, String user_name) {

        try {
            Execute("chfn -o " + new_other + " " + user_name);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /*
     * Minimum number of days between password change          : Minimum
     * Maximum number of days between password change          : Maximum
     * Number of days of warning before password expires       : WARN
     */
    public static boolean password_expiration(int Minimum, int Maximum, int WARN, String username) {

        try {
            Execute("chage -m " + Minimum + " -M " + Maximum + " -W " + WARN + " " + username);

        } catch (Exception e) {
            return false;
        }
        return true;
    }
//</editor-fold>
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Ahmed Ayman">
//<editor-fold defaultstate="collapsed" desc="Group">
    public static boolean add_group(String name) {
        boolean done = true;
        String command[] = {"addgroup", name};
        ProcessBuilder pb;
        Process p;
        try {
            pb = new ProcessBuilder(command);
            p = pb.start();
        } catch (Exception e) {
            return false;
        }
        Scanner error = new Scanner(p.getErrorStream());
        if (error.hasNext()) {
            done = false;
        }
        return done;
    }

    public static boolean delete_group(String name) {
        boolean done = true;
        String command[] = {"delgroup", name};
        ProcessBuilder pb;
        Process p;
        try {
            pb = new ProcessBuilder(command);
            p = pb.start();
        } catch (Exception e) {
            return false;
        }
        Scanner error = new Scanner(p.getErrorStream());
        if (error.hasNext()) {
            done = false;
        }
        return done;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="User">
    public static boolean add_user(User user) {
        boolean done = true;
        String command[] = {"adduser", user.getUsername()};
        ProcessBuilder pb;
        Process p;
        try {
            pb = new ProcessBuilder(command);
            p = pb.start();
        } catch (Exception e) {
            return false;
        }
        Scanner error = new Scanner(p.getErrorStream());
        OutputStream os = p.getOutputStream();
        PrintStream ps = new PrintStream(os);
        ps.println(user.getPassword());
        ps.flush();
        ps.println(user.getPassword());
        ps.flush();
        ps.println(user.getName());
        ps.flush();
        ps.println(user.getRoomNumber());
        ps.flush();
        ps.println(user.getPhoneNumber());
        ps.flush();
        ps.println(user.getHomePhoneNumber());
        ps.flush();
        ps.println(user.getOtherInformation());
        ps.flush();
        ps.println('y');
        ps.flush();
        if (error.hasNext()) {
            if(!"Enter new UNIX password: Retype new UNIX password: passwd: password updated successfully".toLowerCase().trim()
                    .equals(error.nextLine().toLowerCase().trim())){
                done = false;
            }
        }
        return done;
    }

    public static boolean delete_user(String name, boolean all) {
        boolean done = true;
        ProcessBuilder pb;
        Process p;
        String command[] = {"deluser", name, ""};
        if (all) {
            command[1] = "--remove-home";
            command[2] = name;
        } else {
            command[1] = name;
        }
        try {
            pb = new ProcessBuilder(command);
            p = pb.start();
        } catch (Exception e) {
            return false;
        }
        Scanner error = new Scanner(p.getErrorStream());
        if (error.hasNext()) {
            done = false;
        }
        return done;
    }

//</editor-fold>
//</editor-fold>

}
