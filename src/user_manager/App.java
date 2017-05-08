/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user_manager;

import GUI.MainForm;
import java.io.InputStream;
import java.util.Scanner;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Mohammed
 */
public class App {


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        if (args.length > 0) {
            if (args[0].equals("-g") || args[0].equals("--gui")) {
                try {
                    UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[3].getClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    System.out.println(ex);
                }
                new MainForm().setVisible(true);
            } else if (args[0].equals("-h") || args[0].equals("--help")) {
                InputStream s = new Object().getClass().getResourceAsStream("/resources/Help.txt");
                System.out.println(s == null ? "Help not found" : new Scanner(s).useDelimiter("\\A").next());
            } else {
                System.out.println(ANSI_CYAN + "User_Manager:" + ANSI_RESET + " invalid option -- '"
                        + ANSI_RED + args[0] + ANSI_RESET + "'\n" + "Try '" + ANSI_YELLOW
                        + "User_Manager.jar -h" + ANSI_RESET + "' for more information.");
            }
        } else {
            new console();
        }
    }
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
}
