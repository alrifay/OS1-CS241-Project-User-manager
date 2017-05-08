
package GUI;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import user_manager.Group;

/**
 *
 * @author Mohammed
 */
public class GroupDialog extends javax.swing.JDialog {

    int SelectedOption = JOptionPane.CANCEL_OPTION;

    public GroupDialog() {
        initComponents();
    }

    public GroupDialog(String Group_Name) {
        initComponents();
        this.Group_Name.setText(Group_Name);
    }

    public String getGroupName() {
        return Group_Name.getText();
    }

    public int Show() {
        setLocationRelativeTo(null);
        this.setVisible(true);
        return SelectedOption;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Group_Name = new javax.swing.JTextField();
        Btn_OK = new javax.swing.JToggleButton();
        Btn_Cancel = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Group Dialog");
        setAlwaysOnTop(true);
        setModal(true);

        jLabel1.setText("Please, enter group name : ");

        Btn_OK.setText("Ok");
        Btn_OK.setPreferredSize(new java.awt.Dimension(80, 23));
        Btn_OK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_OKActionPerformed(evt);
            }
        });

        Btn_Cancel.setText("Cancel");
        Btn_Cancel.setPreferredSize(new java.awt.Dimension(80, 23));
        Btn_Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_CancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 144, Short.MAX_VALUE)
                        .addComponent(Btn_Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Btn_OK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Group_Name, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Group_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Btn_OK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Btn_Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Close() {
        this.setVisible(false);
        dispose();
    }

    private void Btn_OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_OKActionPerformed
        if (Group_Name.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Please, fill the name field.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ArrayList<user_manager.Group> groups = MainForm.LoadGroups();
        for (Group group : groups) {
            if (group.getName().equals(getGroupName().toLowerCase())) {
                JOptionPane.showMessageDialog(this, "Group name alrady exist!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        SelectedOption = JOptionPane.OK_OPTION;
        Close();
    }//GEN-LAST:event_Btn_OKActionPerformed

    private void Btn_CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_CancelActionPerformed
        SelectedOption = JOptionPane.CANCEL_OPTION;
        Close();
    }//GEN-LAST:event_Btn_CancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton Btn_Cancel;
    private javax.swing.JToggleButton Btn_OK;
    private javax.swing.JTextField Group_Name;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
