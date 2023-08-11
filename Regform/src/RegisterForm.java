import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import static javax.management.remote.JMXConnectorFactory.connect;

public class RegisterForm extends JDialog {

    private JButton btnupdate;
    private JButton btnsubmit;
    private JPanel registerpanel;
    private JTextField tfname;
    private JTextField tfemail;
    private JTextField tfphone;
    private JTextField tfprnno;




    private JButton deleteButton;
    private JButton searchButton;
    private JTextField txtprn;

   String prepareStatement = null;
   String con = null;

    public RegisterForm() {

        connect();
        setTitle("Create a new account");
        setContentPane(registerpanel);
        setMinimumSize(new Dimension(600, 500));
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnsubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        btnupdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String prnno,name,email,phone;

                name = tfname.getText();
                email = tfemail.getText();
                phone = tfphone.getText();
                prnno = txtprn.getText();

                try {
                    Connection con = null;
                    PreparedStatement pst = con.prepareStatement("update student set name =?,email=? where prnno=?");
                    pst.setString(1,name);
                    pst.setString(2,email);
                    pst.setString(3,phone);
                    pst.setString(4, String.valueOf(txtprn));

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Record Updated !!!");
                    tfname.setText("");
                    tfemail.setText("");
                    tfphone.setText("");
                    tfname.requestFocus();
                }
                catch (SQLException e1){
                    e1.printStackTrace();
                }

            }
        });


        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection conn = null;
                    String prn = txtprn.getText();
                    PreparedStatement pst = conn.prepareStatement("select name,email,phone =? where prnno=?");
                    String prnno = null;
                    pst.setString(4, prnno);
                    ResultSet rs = pst.executeQuery();

                    if (rs.next() == true) {
                        String name = rs.getString(1);
                        String email = rs.getString(2);
                        String phone = rs.getString(3);

                        tfname.setText(name);
                        tfemail.setText(email);
                        tfphone.setText(phone);

                    } else {
                        tfname.setText("");
                        tfemail.setText("");
                        tfphone.setText("");
                        JOptionPane.showMessageDialog(null, "Invalid PRN NO");

                    }
                } catch (SQLException ex) {

                }

            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String prnno;

                prnno = txtprn.getText();

                try {
                    Connection con = null;
                    PreparedStatement pst = con.prepareStatement("delete from student  where prnno =?");
                    pst.setString(4, prnno);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Record Deleted !!!");
                    tfname.setText("");
                    tfemail.setText("");
                    tfphone.setText("");
                    txtprn.requestFocus();

                }
                catch (SQLException e1){
                    e1.printStackTrace();
                }

            }
        });
        setVisible(true);

    }




    private void connect() {
    }

    private void registerUser() {
        String name = tfname.getText();
        String email = tfemail.getText();
        String phone = tfphone.getText();
        String prnNo = tfprnno.getText();

        tfname.requestFocus();

        String prnno = null;
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || prnno.isEmpty() ) {
            JOptionPane.showMessageDialog(this,
                    "Please enter all fields",
                    "try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        user = addUserToDatabase(name, email, phone, prnno);
        if (user != null) {
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to register new user",
                    "try again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    public User user;

    private User addUserToDatabase(String name, String email, String phone, String prnno) {
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost?MyStore?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost?MyStore?serverTimezone=UTC", "root", "123");

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO users(name,email,phone,prn)" +
                    "VALUES(?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, prnno);
            preparedStatement.executeUpdate();

            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                user = new User();
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.prnno = prnno;

            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }


    public static void main(String[] args) {
        RegisterForm myForm = new RegisterForm();
        User user = myForm.user;
        if (user != null) {
            System.out.println("Successful registration of:" + user.name);
        } else {
            System.out.println("Regestration cancled");
        }

    }
}




