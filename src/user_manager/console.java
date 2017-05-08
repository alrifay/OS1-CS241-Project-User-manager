package user_manager;

import java.util.Scanner;

/**
 * Console UI Class
 *
 * @author <a href="mailto:fcih.mohammed@gmail.com">Mohammed Al Rifai</a>
 * @since 2016-05-07
 * @version 1.0
 */
public class console {

    /**
     * Scanner to read data from user.
     */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Print error message in red.
     *
     * @param error Error message.
     */
    public static void Error(String error) {
        System.err.println(App.ANSI_RED + "Error: " + error + App.ANSI_RESET);
    }

    /**
     * Take integer number from user.
     *
     * @return Valid integer number.
     */
    private int getNumber() {
        while (!scanner.hasNextInt()) {
            scanner.nextLine();
            System.out.print("Please, Enter a munber : ");
        }
        return scanner.nextInt();
    }

    /**
     * Take password from user and insure it's correct.
     *
     * @return Password entered by user.
     */
    private String getPassword() {
        String Password = "";
        while (true) {
            System.out.println("Please, Enter Password : ");
            Password = scanner.nextLine();
            System.out.println("Please, Enter confirm Password : ");
            if (!Password.equals(scanner.nextLine())) {
                System.out.println("Error: Password doesn't match.");
            } else {
                break;
            }
        }
        return Password;
    }

    /**
     * User Manager's main menu.
     */
    public console() {
        String menu = ""
                + "1. Add user.\n"
                + "2. Add group.\n"
                + "3. Delete group.\n"
                + "4. Delete user.\n"
                + "5. Change account information\n"
                + "6. Assign specific users to specific groups\n"
                + "7. Remove specific users to specific groups\n"
                + "8. Exit.\n";
        int choice = 0;
        while (choice != 8) {
            System.out.println(menu);
            choice = getNumber();
            scanner.nextLine();
            //Add user
            if (choice == 1) {
                User user = new User();

                System.out.print("Please, Enter username : ");
                user.setUsername(scanner.nextLine());

                user.setPassword(getPassword());

                System.out.print("Please, Enter Name : ");
                user.setName(scanner.nextLine());

                System.out.print("Please, Enter Room Number : ");
                user.setRoomNumber(scanner.nextLine());

                System.out.print("Please, Enter Phone number : ");
                user.setPhoneNumber(scanner.nextLine());

                System.out.print("Please, Enter Home Phone number : ");
                user.setHomePhoneNumber(scanner.nextLine());

                System.out.print("Please, Enter Other Information : ");
                user.setOtherInformation(scanner.nextLine());

                if (!Commands.add_user(user)) {
                    Error("while adding user " + user.getUsername()+"\n user must consist of lower case letters, digits and  _");
                }
            } else if (choice == 2) {
                System.out.print("Please, Enter Group name : ");
                String groupName = scanner.nextLine();
                if (!Commands.add_group(groupName)) {
                    Error("while adding group " + groupName);
                }
            } else if (choice == 3) {
                System.out.print("Please, Enter Group name : ");
                String groupName = scanner.nextLine();
                if (!Commands.delete_group(groupName)) {
                    Error("while deleting group " + groupName);
                }
            } else if (choice == 4) {
                System.out.print("Please, Enter username : ");
                String Username = scanner.nextLine();
                System.out.print("Delete user files ? [Yes|No] : ");
                boolean DeleteFiles = "yes".equals(scanner.nextLine().toLowerCase());
                if (!Commands.delete_user(Username, DeleteFiles)) {
                    Error("while deleting user " + Username);
                }
            } else if (choice == 5) {
                System.out.print("Please, Enter Username : ");
                String Username = scanner.nextLine();
                changeUserInformation(Username);
            } else if (choice == 6) {
                System.out.print("Please, Enter Username : ");
                String Username = scanner.nextLine();
                System.out.print("Please, Enter Group name : ");
                String groupName = scanner.nextLine();
                if (!Commands.assign_user_to_group(groupName, Username)) {
                    Error("while adding user " + Username + " to group " + groupName);
                }
            } else if (choice == 7) {
                System.out.print("Please, Enter Username : ");
                String Username = scanner.nextLine();
                System.out.print("Please, Enter Group name : ");
                String groupName = scanner.nextLine();
                if (!Commands.delete_user_from_group(groupName, Username)) {
                    Error("while removing user " + Username + " from group " + groupName);
                }
            }
        }
    }

    /**
     * Change user information.
     *
     * @param Username account's username information.
     */
    private void changeUserInformation(String Username) {
        int choice = 0;
        while (choice != 11) {
            String menu = "[change " + Username + " Information]\n"
                    + "1. Change home directory.\n"
                    + "2. Change Username.\n"
                    + "3. Change id.\n"
                    + "4. Change room number.\n"
                    + "5. Change home number.\n"
                    + "6. Change other information.\n"
                    + "7. Change password expiration information.\n"
                    + "8. Lock account.\n"
                    + "9. Unlock account.\n"
                    + "10. Change account expiration date.\n"
                    + "11. Back to main menu.\n";
            System.out.println(menu);
            choice = getNumber();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("Please, Enter new directory :");
                    if (!Commands.change_user_directory(scanner.nextLine(), Username)) {
                        Error("while change user directory");
                    }
                    break;
                case 2:
                    System.out.println("Please, Enter new Username :");
                    String NewUsername = scanner.nextLine().toLowerCase();
                    if (!Commands.change_user_name(Username, NewUsername)) {
                        Error("while change user name");
                    }
                    Username = NewUsername;
                    break;
                case 3:
                    System.out.println("Please, Enter new ID:");
                    if (!Commands.change_user_id(getNumber(), Username)) {
                        Error("while change user id");
                    }
                    break;
                case 4:
                    System.out.println("Please, Enter new room number :");
                    if (!Commands.change_room_number(scanner.nextLine(), Username)) {
                        Error("while change room number");
                    }
                    break;
                case 5:
                    System.out.println("Please, Enter new home number :");
                    if (!Commands.change_home_number(scanner.nextLine(), Username)) {
                        Error("while change home number");
                    }
                    break;
                case 6:
                    System.out.println("Please, Enter new other information :");
                    if (!Commands.change_other(scanner.nextLine(), Username)) {
                        Error("while change other information");
                    }
                    break;
                case 7:
                    int min,
                     max,
                     warn;
                    System.out.println("Minimum number of days between password change : ");
                    min = getNumber();
                    System.out.println("Maximum number of days between password change : ");
                    max = getNumber();
                    System.out.println("Number of days of warning before password expires : ");
                    warn = getNumber();
                    if (!Commands.password_expiration(min, max, warn, Username)) {
                        Error("while change password expiration information.");
                    }
                    break;
                case 8:
                    System.out.println("Done");
                    if (!Commands.lock_user_account(Username)) {
                        Error("while lock user account.");
                    }
                    break;
                case 9:
                    System.out.println("Done");
                    if (!Commands.unlock_user_account(Username)) {
                        Error("while unlock user account.");
                    }
                    break;
                case 10:
                    System.out.println("Please, Enter Date :");
                    if (!Commands.expiry_date_account(scanner.nextLine(), Username)) {
                        Error("while change accout's expiration date.");
                    }
                    break;

            }
        }
    }

}
