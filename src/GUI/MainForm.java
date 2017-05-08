package GUI;

//<editor-fold defaultstate="collapsed" desc="import">
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import user_manager.Commands;
import user_manager.Group;
import user_manager.User;
//</editor-fold>

public class MainForm extends javax.swing.JFrame {

//<editor-fold defaultstate="collapsed" desc="Attributes">
    ArrayList<User> Users = new ArrayList<>();
    ArrayList<Group> Groups = new ArrayList<>();
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Constructor">
    public MainForm() {
        Groups = LoadGroups();
        Users = LoadUsers(Groups);
        initComponents();
        ListModel<User> UserModel = new AbstractListModel<User>() {
            @Override
            public int getSize() {
                return Users.size();
            }

            @Override
            public User getElementAt(int index) {
                return Users.get(index);
            }
        };
        ListModel<Group> GroupModel = new AbstractListModel<Group>() {
            @Override
            public int getSize() {
                return Groups.size();
            }

            @Override
            public Group getElementAt(int index) {
                return Groups.get(index);
            }
        };
        List_Groups.setModel(GroupModel);
        List_Groups.setSelectedIndex(0);
        List_Users.setModel(UserModel);
        List_Users.setSelectedIndex(0);
        Panel_View.add(User_Panel);
        setLocationRelativeTo(null);
        Help_Dialog.setLocationRelativeTo(null);
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Other functions">
    private void Error(String Msg) {
        JOptionPane.showMessageDialog(this, Msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void UpdateGroupFields(int i) {
        if (Groups.size() < 1) {
            Group_Name.setText("");
            Group_ID.setText("");
            Group_EnrolledUsers_Panel.removeAll();
        }
        Group_Name.setText(Groups.get(i).getName());
        Group_ID.setText(Groups.get(i).getID() + "");
        Group_EnrolledUsers_Panel.removeAll();
        for (String u : Groups.get(i).getUsers()) {
            JButton n = new JButton(u);
            n.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    String U = ((JButton) e.getSource()).getText();
                    System.out.println(U);
                    for (User get : Users) {
                        if (U.equals(get.getUsername())) {
                            List_Users.setSelectedValue(get, true);
                            break;
                        }
                    }
                    //List_Users.setSelectedIndex(5);
                    jTabbedPane1.setSelectedIndex(0);
                }
            });
            Group_EnrolledUsers_Panel.add(n);
        }
        List_Groups.setSelectedIndex(i);
        List_Groups.ensureIndexIsVisible(i);
        Group_EnrolledUsers_Panel.updateUI();
    }

    private void UpdateUserFields(int i) {
        if (Users.size() < 1) {
            User_Username.setText("");
            User_HomeDir.setText("");
            User_Name.setText("");
            User_UserID.setText("");
            User_Home_Phone_Number.setText("");
            User_Other_Info.setText("");
            User_Phone_Number.setText("");
            User_Room_Number.setText("");
            User_PE_Min.setText("");
            User_PE_Max.setText("");
            User_PE_Warn.setText("");
            User_Groups.removeAll();
        }
        User_Username.setText(Users.get(i).getUsername());
        User_HomeDir.setText(Users.get(i).getHomeDir());
        User_Name.setText(Users.get(i).getName());
        User_UserID.setText(Users.get(i).getID() + "");
        User_Home_Phone_Number.setText(Users.get(i).getHomePhoneNumber());
        User_Other_Info.setText(Users.get(i).getOtherInformation());
        User_Phone_Number.setText(Users.get(i).getPhoneNumber());
        User_Room_Number.setText(Users.get(i).getRoomNumber());
        User_PE_Min.setText(Users.get(i).getMinExpireDays() + "");
        User_PE_Max.setText(Users.get(i).getMaxExpireDays() + "");
        User_PE_Warn.setText(Users.get(i).getWarningExpireDays() + "");
        User_Groups.removeAll();
        for (Map.Entry<Integer, String> entrySet : Users.get(i).getGroups().entrySet()) {
            //Integer key = entrySet.getKey();
            String value = entrySet.getValue();
            JButton n = new JButton(value);
            n.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String U = ((JButton) e.getSource()).getText();
                    System.out.println(U);
                    for (Group get : Groups) {
                        if (U.equals(get.getName())) {
                            List_Groups.setSelectedValue(get, true);
                            break;
                        }
                    }
                    jTabbedPane1.setSelectedIndex(1);
                }
            });
            User_Groups.add(n);
        }
        List_Users.setSelectedIndex(i);
        List_Users.ensureIndexIsVisible(i);
        User_Groups.updateUI();
    }

    public void updateLists() {
        Groups = LoadGroups();
        Users = LoadUsers(Groups);
        List_Users.updateUI();
        List_Groups.updateUI();
        UpdateGroupFields(0);
        UpdateUserFields(0);
    }

    //<editor-fold defaultstate="collapsed" desc="Gat User & Group Data">
    public static ArrayList<User> LoadUsers() {
        return LoadUsers(new ArrayList<Group>());
    }

    public static ArrayList<User> LoadUsers(ArrayList<Group> Groups) {
        try {
            ArrayList<User> Users = new ArrayList<>();
            List<String> UserFile = Files.readAllLines(Paths.get("/etc/passwd"), Charset.defaultCharset());
            List<String> PEFile = Files.readAllLines(Paths.get("/etc/shadow"), Charset.defaultCharset());
            for (String U1 : UserFile) {
                String[] cols = U1.split(":");
                String[] OtherDetails = cols[4].split(",");
                User tmp = new User().setUsername(cols[0]).setHomeDir(cols[5]).setID(Integer.valueOf(cols[2]));
                for (String PEData : PEFile) {
                    String[] PEA = PEData.split(":");
                    if (PEA[0].equals(cols[0])) {
                        if (PEA.length > 3) {
                            tmp.setMinExpireDays(Integer.valueOf(PEA[3]));
                        }
                        if (PEA.length > 4) {
                            tmp.setMaxExpireDays(Integer.valueOf(PEA[4]));
                        }
                        if (PEA.length > 5) {
                            tmp.setWarningExpireDays(Integer.valueOf(PEA[5]));
                        }
                        PEFile.remove(PEData);
                        break;
                    }
                }
                if (OtherDetails.length > 0) {
                    tmp.setName(OtherDetails[0]);
                }
                if (OtherDetails.length > 1) {
                    tmp.setRoomNumber(OtherDetails[1]);
                }
                if (OtherDetails.length > 2) {
                    tmp.setPhoneNumber(OtherDetails[2]);
                }
                if (OtherDetails.length > 3) {
                    tmp.setHomePhoneNumber(OtherDetails[3]);
                }
                if (OtherDetails.length > 4) {
                    tmp.setOtherInformation(OtherDetails[4]);
                }
                List<String> groups = Commands.Execute("grep " + cols[0] + " /etc/group");
                HashMap<Integer, String> _g = new HashMap<>();
                for (String group : groups) {
                    String[] x = group.split(":");
                    _g.put(Integer.valueOf(x[2]), x[0]);
                    for (Group g : Groups) {
                        if (g.getName().equals(x[0]) && !g.getUsers().contains(cols[0])) {
                            g.getUsers().add(cols[0]);
                        }
                    }
                }
                tmp.setGroups(_g);
                Users.add(tmp);
            }
            return Users;
        } catch (IOException | NumberFormatException ex) {
            if (ex.getMessage().equals("\\etc\\passwd")) {
                JOptionPane.showMessageDialog(null, "You should open the program from linux distribution", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            if (ex.getMessage().equals("/etc/shadow")) {
                JOptionPane.showMessageDialog(null, "<html>You should open the program with super user privlages<br><strong><code>sudo java -jar User_manager.jar</code></strong><html>", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            //ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static ArrayList<Group> LoadGroups() {
        try {
            ArrayList<Group> Groups = new ArrayList<>();
            List<String> U = Files.readAllLines(Paths.get("/etc/group"), Charset.defaultCharset());
            for (String U1 : U) {
                String[] cols = U1.split(":");
                Group tmp = new Group()
                        .setName(cols[0])
                        .setID(Integer.valueOf(cols[2]));
                String[] user_details = {};
                if (cols.length > 3) {
                    user_details = cols[3].split(",");
                }
                ArrayList<String> user_list = new ArrayList<>();
                user_list.addAll(Arrays.asList(user_details));
                tmp.setUsers(user_list);
                Groups.add(tmp);
            }
            return Groups;
        } catch (IOException | NumberFormatException ex) {
            // System.out.println(ex);
        }
        return new ArrayList<>();
    }
//</editor-fold>
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Generated Code">
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        User_Panel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        User_HomeDir = new javax.swing.JTextField();
        User_Username = new javax.swing.JTextField();
        User_UserID = new javax.swing.JTextField();
        User_Name = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        User_Groups = new javax.swing.JPanel();
        Btn_Delete_User = new javax.swing.JButton();
        btn_Edit_User = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        User_Phone_Number = new javax.swing.JTextField();
        User_Home_Phone_Number = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        User_Room_Number = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        User_Other_Info = new javax.swing.JTextField();
        Password_Panel = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        User_PE_Warn = new javax.swing.JTextField();
        User_PE_Min = new javax.swing.JTextField();
        User_PE_Max = new javax.swing.JTextField();
        Group_Panel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Group_Name = new javax.swing.JTextField();
        Group_EnrolledUsers_Panel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        Btn_Delete_Group = new javax.swing.JButton();
        Btn_Add_User_To_Group = new javax.swing.JButton();
        Btn_Delete_User_From_Group = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        Group_ID = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        Help_Dialog = new javax.swing.JDialog();
        Btn_OK_Help_Dialog = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        DialogContent = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        List_Users = new javax.swing.JList();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        List_Groups = new javax.swing.JList();
        Panel_View = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        MI_Exit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        MI_Add_User = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        MI_Add_Group = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        MI_Help = new javax.swing.JMenuItem();
        MI_About = new javax.swing.JMenuItem();

        jLabel3.setText("Username : ");

        jLabel4.setText("UserID : ");

        jLabel5.setText("Name : ");

        jLabel6.setText("Home directory : ");

        User_HomeDir.setEditable(false);

        User_Username.setEditable(false);

        User_UserID.setEditable(false);

        User_Name.setEditable(false);

        jLabel7.setText("Groups : ");

        User_Groups.setBackground(new java.awt.Color(254, 254, 254));
        User_Groups.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        User_Groups.setAutoscrolls(true);
        User_Groups.setMinimumSize(new java.awt.Dimension(100, 100));
        User_Groups.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        Btn_Delete_User.setText("Delete");
        Btn_Delete_User.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_Delete_UserActionPerformed(evt);
            }
        });

        btn_Edit_User.setText("Edit");
        btn_Edit_User.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Edit_UserActionPerformed(evt);
            }
        });

        jLabel8.setText("Phone Number : ");

        User_Phone_Number.setEditable(false);

        User_Home_Phone_Number.setEditable(false);

        jLabel9.setText("Home Number : ");

        jLabel10.setText("Room Number : ");

        User_Room_Number.setEditable(false);

        jLabel11.setText("Other Info : ");

        User_Other_Info.setEditable(false);

        Password_Panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Password Expiration details"));

        jLabel14.setText("Min days : ");

        jLabel15.setText("Max days : ");

        jLabel16.setText("Warning days : ");

        User_PE_Warn.setEditable(false);

        User_PE_Min.setEditable(false);

        User_PE_Max.setEditable(false);

        javax.swing.GroupLayout Password_PanelLayout = new javax.swing.GroupLayout(Password_Panel);
        Password_Panel.setLayout(Password_PanelLayout);
        Password_PanelLayout.setHorizontalGroup(
            Password_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Password_PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Password_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Password_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(User_PE_Warn, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                    .addComponent(User_PE_Min)
                    .addComponent(User_PE_Max)))
        );
        Password_PanelLayout.setVerticalGroup(
            Password_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Password_PanelLayout.createSequentialGroup()
                .addGroup(Password_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(User_PE_Min, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Password_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(User_PE_Max, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Password_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(User_PE_Warn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout User_PanelLayout = new javax.swing.GroupLayout(User_Panel);
        User_Panel.setLayout(User_PanelLayout);
        User_PanelLayout.setHorizontalGroup(
            User_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(User_PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(User_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(User_PanelLayout.createSequentialGroup()
                        .addGroup(User_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel10)
                            .addGroup(User_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(User_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8)
                                    .addGroup(User_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel4)
                                        .addComponent(Btn_Delete_User, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btn_Edit_User, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel7))
                                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(User_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(User_Name, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(User_HomeDir, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(User_UserID)
                            .addComponent(User_Room_Number, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(User_Home_Phone_Number, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(User_Phone_Number, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(User_Username, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(User_Groups, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(User_Other_Info, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(Password_Panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        User_PanelLayout.setVerticalGroup(
            User_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(User_PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(User_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(User_Username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(User_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(User_UserID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(User_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(User_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(User_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(User_HomeDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(User_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(User_Phone_Number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(User_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(User_Home_Phone_Number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(User_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(User_Room_Number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(User_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(User_Other_Info, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Password_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(User_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(User_PanelLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addComponent(btn_Edit_User)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Btn_Delete_User))
                    .addComponent(User_Groups, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel1.setText("Group Name : ");

        Group_Name.setEditable(false);

        Group_EnrolledUsers_Panel.setBackground(new java.awt.Color(255, 255, 255));
        Group_EnrolledUsers_Panel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        Group_EnrolledUsers_Panel.setAutoscrolls(true);
        Group_EnrolledUsers_Panel.setMinimumSize(new java.awt.Dimension(100, 100));
        Group_EnrolledUsers_Panel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jButton1.setText("jButton1");
        Group_EnrolledUsers_Panel.add(jButton1);

        jButton2.setText("jButton1");
        Group_EnrolledUsers_Panel.add(jButton2);

        jButton3.setText("jButton1");
        Group_EnrolledUsers_Panel.add(jButton3);

        jButton4.setText("jButton1");
        Group_EnrolledUsers_Panel.add(jButton4);

        jButton5.setText("jButton1");
        Group_EnrolledUsers_Panel.add(jButton5);

        Btn_Delete_Group.setText("Delete");
        Btn_Delete_Group.setMinimumSize(new java.awt.Dimension(55, 23));
        Btn_Delete_Group.setPreferredSize(new java.awt.Dimension(55, 23));
        Btn_Delete_Group.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_Delete_GroupActionPerformed(evt);
            }
        });

        Btn_Add_User_To_Group.setText("Add User");
        Btn_Add_User_To_Group.setMinimumSize(new java.awt.Dimension(55, 23));
        Btn_Add_User_To_Group.setPreferredSize(new java.awt.Dimension(55, 23));
        Btn_Add_User_To_Group.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_Add_User_To_GroupActionPerformed(evt);
            }
        });

        Btn_Delete_User_From_Group.setText("Delete User");
        Btn_Delete_User_From_Group.setMinimumSize(new java.awt.Dimension(55, 23));
        Btn_Delete_User_From_Group.setPreferredSize(new java.awt.Dimension(55, 23));
        Btn_Delete_User_From_Group.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_Delete_User_From_GroupActionPerformed(evt);
            }
        });

        jLabel12.setText("Group ID : ");

        Group_ID.setEditable(false);

        jLabel13.setText("Enrolled users : ");

        javax.swing.GroupLayout Group_PanelLayout = new javax.swing.GroupLayout(Group_Panel);
        Group_Panel.setLayout(Group_PanelLayout);
        Group_PanelLayout.setHorizontalGroup(
            Group_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Group_PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Group_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(Btn_Delete_Group, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                    .addComponent(Btn_Add_User_To_Group, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Btn_Delete_User_From_Group, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Group_PanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(Group_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Group_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Group_Name)
                    .addComponent(Group_EnrolledUsers_Panel, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                    .addComponent(Group_ID))
                .addContainerGap())
        );
        Group_PanelLayout.setVerticalGroup(
            Group_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Group_PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Group_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Group_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Group_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(Group_ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Group_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Group_PanelLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 143, Short.MAX_VALUE)
                        .addComponent(Btn_Delete_User_From_Group, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Btn_Add_User_To_Group, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Btn_Delete_Group, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Group_EnrolledUsers_Panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        Help_Dialog.setTitle("User Manager Help");
        Help_Dialog.setAlwaysOnTop(true);
        Help_Dialog.setMinimumSize(new java.awt.Dimension(550, 350));
        Help_Dialog.setModal(true);
        Help_Dialog.setResizable(false);

        Btn_OK_Help_Dialog.setText("OK");
        Btn_OK_Help_Dialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_OK_Help_DialogActionPerformed(evt);
            }
        });

        DialogContent.setBackground(new java.awt.Color(52, 52, 52));
        DialogContent.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        DialogContent.setText("jbkjbkjbk");
        DialogContent.setAutoscrolls(true);
        DialogContent.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jScrollPane3.setViewportView(DialogContent);

        javax.swing.GroupLayout Help_DialogLayout = new javax.swing.GroupLayout(Help_Dialog.getContentPane());
        Help_Dialog.getContentPane().setLayout(Help_DialogLayout);
        Help_DialogLayout.setHorizontalGroup(
            Help_DialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Help_DialogLayout.createSequentialGroup()
                .addGap(0, 440, Short.MAX_VALUE)
                .addComponent(Btn_OK_Help_Dialog, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        Help_DialogLayout.setVerticalGroup(
            Help_DialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Help_DialogLayout.createSequentialGroup()
                .addComponent(jScrollPane3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Btn_OK_Help_Dialog)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("User Manager");
        setAlwaysOnTop(true);
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(440, 300));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTabbedPane1.setFocusable(false);
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jScrollPane1.setBorder(null);

        List_Users.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        List_Users.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                List_UsersValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(List_Users);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        jTabbedPane1.addTab("Users", jPanel4);

        jScrollPane2.setBorder(null);

        List_Groups.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        List_Groups.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                List_GroupsValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(List_Groups);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );

        jTabbedPane1.addTab("Groups", jPanel5);

        Panel_View.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                Panel_ViewComponentAdded(evt);
            }
        });
        Panel_View.setLayout(new java.awt.BorderLayout());

        jMenu1.setText("File");

        MI_Exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        MI_Exit.setText("Exit");
        MI_Exit.setToolTipText("");
        MI_Exit.setPreferredSize(new java.awt.Dimension(150, 22));
        MI_Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MI_ExitActionPerformed(evt);
            }
        });
        jMenu1.add(MI_Exit);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("User");

        MI_Add_User.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        MI_Add_User.setText("Add");
        MI_Add_User.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MI_Add_UserActionPerformed(evt);
            }
        });
        jMenu2.add(MI_Add_User);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Group");

        MI_Add_Group.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        MI_Add_Group.setText("Add");
        MI_Add_Group.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MI_Add_GroupActionPerformed(evt);
            }
        });
        jMenu3.add(MI_Add_Group);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Help");

        MI_Help.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        MI_Help.setText("Help");
        MI_Help.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MI_HelpActionPerformed(evt);
            }
        });
        jMenu4.add(MI_Help);

        MI_About.setText("About");
        MI_About.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MI_AboutActionPerformed(evt);
            }
        });
        jMenu4.add(MI_About);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Panel_View, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
            .addComponent(Panel_View, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Actions">
    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        Panel_View.removeAll();
        switch (jTabbedPane1.getSelectedIndex()) {
            case 0:
                Panel_View.add(User_Panel);
                break;
            case 1:
                Panel_View.add(Group_Panel);
                break;
        }
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void Panel_ViewComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_Panel_ViewComponentAdded
        Panel_View.updateUI();
    }//GEN-LAST:event_Panel_ViewComponentAdded

    private void List_UsersValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_List_UsersValueChanged
        int i = List_Users.getSelectedIndex();
        UpdateUserFields(i);
    }//GEN-LAST:event_List_UsersValueChanged

    private void List_GroupsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_List_GroupsValueChanged
        int i = List_Groups.getSelectedIndex();
        UpdateGroupFields(i);
    }//GEN-LAST:event_List_GroupsValueChanged

    private void MI_ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MI_ExitActionPerformed
        formWindowClosing(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_MI_ExitActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (JOptionPane.showConfirmDialog(this, "Do you wont to exit?", "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_formWindowClosing

    private void Btn_Delete_UserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_Delete_UserActionPerformed
        int i = List_Users.getSelectedIndex();
        if (i == -1) {
            Error("No user selected.");
            return;
        }
        DeleteDialog dialog = new DeleteDialog(this, User_Username.getText(), DeleteDialog.User);
        if (dialog.showDialog() == JOptionPane.YES_OPTION) {
            if (!Commands.delete_user(Users.get(i).getUsername(), dialog.DeleteDirectory())) {
                Error("Unable to delete user '" + Users.get(i).getUsername() + "' .");
                return;
            }
        }
        updateLists();
    }//GEN-LAST:event_Btn_Delete_UserActionPerformed

    private void btn_Edit_UserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Edit_UserActionPerformed
        int i = List_Users.getSelectedIndex();
        if (i == -1) {
            Error("edit error");
            return;
        }
        UserDialog dialog = new UserDialog();
        dialog.setLocationRelativeTo(this);
        User ou = Users.get(i);
        if (dialog.EditUser(Users.get(i)) == JOptionPane.OK_OPTION) {
            User u = dialog.getUser();

            if (!ou.getHomeDir().equals(u.getHomeDir())) {
                Commands.change_user_directory(u.getHomeDir(), ou.getUsername());
            }
            if (ou.getID() != u.getID()) {
                Commands.change_user_id(u.getID(), ou.getUsername());
            }
            if (!ou.getHomePhoneNumber().equals(u.getHomePhoneNumber())) {
                Commands.change_home_number(u.getHomePhoneNumber(), ou.getUsername());
            }
            if (!ou.getUsername().equals(u.getUsername())) {
                Commands.change_user_name(ou.getUsername(), u.getUsername());
            }
            if (!ou.getRoomNumber().equals(u.getRoomNumber())) {
                Commands.change_room_number(u.getRoomNumber(), u.getUsername());
            }
            if (!ou.getOtherInformation().equals(u.getOtherInformation())) {
                Commands.change_other(u.getOtherInformation(), u.getUsername());
            }
        }
        updateLists();
        UpdateUserFields(Users.size() - 1);
    }//GEN-LAST:event_btn_Edit_UserActionPerformed

    private void MI_HelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MI_HelpActionPerformed
        InputStream HelpStream = getClass().getResourceAsStream("/resources/Help_Dialog.txt");
        if (HelpStream == null) {
            DialogContent.setText("<html><strong>Sorry<strong>, Help file not found<br>"
                    + "use \"<code>man user_manager</code>\"</html>");
        } else {
            DialogContent.setText(new Scanner(HelpStream).useDelimiter("\\A").next());
        }
        Help_Dialog.setTitle("User Manager Help");
        Help_Dialog.setVisible(true);
    }//GEN-LAST:event_MI_HelpActionPerformed

    private void MI_AboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MI_AboutActionPerformed
        InputStream HelpStream = getClass().getResourceAsStream("/resources/About_Dialog.txt");
        if (HelpStream == null) {
            DialogContent.setText("<html><h1>User Manager</h1>"
                    + "Team :<br>"
                    + "Mohamed<br>"
                    + "Ahmed<br>"
                    + "Mahmoud<br></html>");
        } else {
            DialogContent.setText(new Scanner(HelpStream).useDelimiter("\\A").next());
        }
        Help_Dialog.setTitle("About User Manager");
        Help_Dialog.setVisible(true);
    }//GEN-LAST:event_MI_AboutActionPerformed

    private void Btn_OK_Help_DialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_OK_Help_DialogActionPerformed
        Help_Dialog.dispose();
    }//GEN-LAST:event_Btn_OK_Help_DialogActionPerformed

    private void Btn_Delete_GroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_Delete_GroupActionPerformed
        if (List_Groups.getSelectedIndex() == -1) {
            return;
        }
        Group g = Groups.get(List_Groups.getSelectedIndex());
        DeleteDialog dialog = new DeleteDialog(this, Group_Name.getText(), DeleteDialog.Group);
        if (dialog.showDialog() == JOptionPane.YES_OPTION) {
            System.out.println("Yes");
            if (!Commands.delete_group(g.getName())) {
                Error("Unable to delete group '" + g.getName() + "'");
                return;
            }
            Groups.remove(List_Groups.getSelectedIndex());
            if (Groups.size() > 0) {
                List_Groups.setSelectedIndex(0);
            } else {
                Group_EnrolledUsers_Panel.removeAll();
                Group_ID.setText("");
                Group_Name.setText("");
            }
        }
        updateLists();
    }//GEN-LAST:event_Btn_Delete_GroupActionPerformed

    private void MI_Add_UserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MI_Add_UserActionPerformed
        UserDialog dialog = new UserDialog();
        dialog.setLocationRelativeTo(this);
        if (dialog.AddUser() == JOptionPane.OK_OPTION) {
            User u = dialog.getUser();
            if (!Commands.add_user(u)) {
                Error("Error while adding user '" + u.getUsername() + "' \n user must consist of lower case letters, digits and  _");
                updateLists();
                return;
            }
            if (u.getMinExpireDays() > 0) {
                Commands.password_expiration(u.getMinExpireDays(), u.getMaxExpireDays(), u.getWarningExpireDays(), u.getUsername());
            }
        }
        updateLists();
        List_Users.setSelectedIndex(Users.size() - 1);
        List_Users.ensureIndexIsVisible(Users.size() - 1);
    }//GEN-LAST:event_MI_Add_UserActionPerformed

    private void Btn_Add_User_To_GroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_Add_User_To_GroupActionPerformed
        int i = List_Groups.getSelectedIndex();
        if (i < 0) {
            return;
        }
        ArrayList<User> l = new ArrayList<>();
        for (User u : Users) {
            if (!Groups.get(i).getUsers().contains(u.getUsername())) {
                l.add(u);
            }
        }
        if (l.size() < 1) {
            return;
        }
        JComboBox<User> User_ComboBox = new JComboBox<>(l.toArray(new User[l.size()]));
        if (JOptionPane.showConfirmDialog(this, User_ComboBox, "Select a user!", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            String user_name = l.get(User_ComboBox.getSelectedIndex()).getUsername();
            Group group = Groups.get(i);
            if (!Commands.assign_user_to_group(group.getName(), user_name)) {
                Error("Error adding selected user to current group");
                return;
            }
        }
        updateLists();
        List_Groups.setSelectedIndex(i);
        List_Groups.ensureIndexIsVisible(i);

    }//GEN-LAST:event_Btn_Add_User_To_GroupActionPerformed

    private void Btn_Delete_User_From_GroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_Delete_User_From_GroupActionPerformed
        if (List_Groups.getSelectedIndex() < 0) {
            return;
        }
        ArrayList<String> l = Groups.get(List_Groups.getSelectedIndex()).getUsers();
        if (l.size() < 1) {
            return;
        }
        JComboBox<String> User_ComboBox = new JComboBox<>(l.toArray(new String[l.size()]));
        if (JOptionPane.showConfirmDialog(this, User_ComboBox, "Select a user!", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            String user_name = l.get(User_ComboBox.getSelectedIndex());
            Group group = Groups.get(List_Groups.getSelectedIndex());
            if (!Commands.delete_user_from_group(group.getName(), user_name)) {
                Error("Error deleting selected user from current group");
                return;
            }
        }
        updateLists();
    }//GEN-LAST:event_Btn_Delete_User_From_GroupActionPerformed

    private void MI_Add_GroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MI_Add_GroupActionPerformed
        GroupDialog G_Dialog = new GroupDialog();
        if (G_Dialog.Show() == JOptionPane.OK_OPTION) {
            Group g = new Group().setName(G_Dialog.getGroupName());
            if (!Commands.add_group(g.getName())) {
                Error("Can't add group '" + g.getName() + "'");
                return;
            }
            updateLists();
            jTabbedPane1.setSelectedIndex(1);
        }
        List_Groups.setSelectedIndex(Groups.size() - 1);
        List_Groups.ensureIndexIsVisible(Groups.size() - 1);
    }//GEN-LAST:event_MI_Add_GroupActionPerformed
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Variables declaration">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Btn_Add_User_To_Group;
    private javax.swing.JButton Btn_Delete_Group;
    private javax.swing.JButton Btn_Delete_User;
    private javax.swing.JButton Btn_Delete_User_From_Group;
    private javax.swing.JButton Btn_OK_Help_Dialog;
    private javax.swing.JLabel DialogContent;
    private javax.swing.JPanel Group_EnrolledUsers_Panel;
    private javax.swing.JTextField Group_ID;
    private javax.swing.JTextField Group_Name;
    private javax.swing.JPanel Group_Panel;
    private javax.swing.JDialog Help_Dialog;
    private javax.swing.JList List_Groups;
    private javax.swing.JList List_Users;
    private javax.swing.JMenuItem MI_About;
    private javax.swing.JMenuItem MI_Add_Group;
    private javax.swing.JMenuItem MI_Add_User;
    private javax.swing.JMenuItem MI_Exit;
    private javax.swing.JMenuItem MI_Help;
    private javax.swing.JPanel Panel_View;
    private javax.swing.JPanel Password_Panel;
    private javax.swing.JPanel User_Groups;
    private javax.swing.JTextField User_HomeDir;
    private javax.swing.JTextField User_Home_Phone_Number;
    private javax.swing.JTextField User_Name;
    private javax.swing.JTextField User_Other_Info;
    private javax.swing.JTextField User_PE_Max;
    private javax.swing.JTextField User_PE_Min;
    private javax.swing.JTextField User_PE_Warn;
    private javax.swing.JPanel User_Panel;
    private javax.swing.JTextField User_Phone_Number;
    private javax.swing.JTextField User_Room_Number;
    private javax.swing.JTextField User_UserID;
    private javax.swing.JTextField User_Username;
    private javax.swing.JButton btn_Edit_User;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
//</editor-fold>
}
