package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Pin extends JFrame implements ActionListener {

    JButton b1,b2;

    JPasswordField p1,p2;
    String pin;



    Pin(String pin){
        this.pin = pin;

        ImageIcon i1 =new ImageIcon(ClassLoader.getSystemResource("icon/card1.jpeg"));
        Image i2 = i1.getImage().getScaledInstance(1550,1080,Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0,0,1550,1080);
        add(l3);

        JLabel label1 = new JLabel("CHANGE YOUR PIN");
        label1.setFont(new Font("System",Font.BOLD,16));
        label1.setBounds(700,180,400,35);
        label1.setForeground(Color.WHITE);
        l3.add(label1);

        JLabel label2 = new JLabel("NEW PIN");
        label2.setFont(new Font("System",Font.BOLD,16));
        label2.setBounds(600,250,150,35);
        label2.setForeground(Color.WHITE);
        l3.add(label2);

        p1 = new JPasswordField();
        p1.setBackground(new Color(65,125,128));
        p1.setForeground(Color.WHITE);
        p1.setBounds(800,250,250,40);
        p1.setFont(new Font("Raleway",Font.BOLD,16));
        l3.add(p1);

        JLabel label3 = new JLabel("RE-ENTER NEW PIN");
        label3.setFont(new Font("System",Font.BOLD,16));
        label3.setBounds(600,330,400,35);
        label3.setForeground(Color.WHITE);
        l3.add(label3);

        p2 = new JPasswordField();
        p2.setBackground(new Color(65,125,128));
        p2.setForeground(Color.WHITE);
        p2.setBounds(800,330,250,40);
        p2.setFont(new Font("Raleway",Font.BOLD,16));
        l3.add(p2);


        b1 = new JButton("CHANGE");
        b1.setBounds(600,450,150,40);
        b1.setBackground(new Color(65,125,128));
        b1.setForeground(Color.WHITE);
        b1.addActionListener(this);
        l3.add(b1);

        b2 = new JButton("BACK");
        b2.setBounds(900,450,150,40);
        b2.setBackground(new Color(65,125,128));
        b2.setForeground(Color.WHITE);
        b2.addActionListener(this);
        l3.add(b2);

        setSize(1550,1080);
        setLocation(0,0);
        setLayout(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // If BACK clicked → go back immediately and skip validations
            if (e.getSource() == b2) {
                setVisible(false);
                new main_Class(pin);
                return;
            }

            // Now we handle CHANGE button (b1)
            if (e.getSource() == b1) {
                String pin1 = new String(p1.getPassword()).trim();
                String pin2 = new String(p2.getPassword()).trim();

                if (pin1.equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter New PIN");
                    return;
                }

                if (pin2.equals("")) {
                    JOptionPane.showMessageDialog(null, "Re-Enter New PIN");
                    return;
                }

                if (!pin1.equals(pin2)) {
                    JOptionPane.showMessageDialog(null, "Entered PIN doesn't match");
                    return;
                }

                if (!pin1.matches("\\d{4}")) {
                    JOptionPane.showMessageDialog(null, "PIN must contain only Digits and be exactly 4 digits");
                    return;
                }

                // perform DB updates
                Conn c = new Conn();
                String q1 = "update bank set pin = '" + pin1 + "' where pin = '" + pin + "'";
                String q2 = "update login set pin = '" + pin1 + "' where pin = '" + pin + "'";
                String q3 = "update signupthree set pin = '" + pin1 + "' where pin = '" + pin + "'";

                c.statement.executeUpdate(q1);
                c.statement.executeUpdate(q2);
                c.statement.executeUpdate(q3);

                JOptionPane.showMessageDialog(null, "PIN Changed Successfully");
                setVisible(false);
                new main_Class(pin1); // maybe pass new pin?
            }

        } catch (Exception E) {
            E.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new Pin("");

    }
}
