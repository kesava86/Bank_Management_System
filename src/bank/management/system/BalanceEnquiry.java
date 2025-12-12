package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class BalanceEnquiry extends JFrame implements ActionListener {

    String pin;

    JLabel label2;
    JButton b1;

    BalanceEnquiry(String pin){
        this.pin = pin;

        ImageIcon i1 =new ImageIcon(ClassLoader.getSystemResource("icon/card1.jpeg"));
        Image i2 = i1.getImage().getScaledInstance(1550,1080,Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0,0,1550,1080);
        add(l3);

        JLabel label1 = new JLabel("Your Current Balance is Rs. ");
        label1.setFont(new Font("System",Font.BOLD,18));
        label1.setBounds(600,180,700,35);
        label1.setForeground(Color.WHITE);
        l3.add(label1);

        label2 = new JLabel();
        label2.setFont(new Font("System",Font.BOLD,18));
        label2.setBounds(860,180,400,35);
        label2.setForeground(Color.WHITE);
        l3.add(label2);

        b1 = new JButton("Back");
        b1.setBounds(720,350,150,40);
        b1.setBackground(new Color(65,125,128));
        b1.setForeground(Color.WHITE);
        b1.addActionListener(this);
        l3.add(b1);

        int balance = 0;
        try {
            Conn c = new Conn();
            ResultSet resultSet = c.statement.executeQuery("select * from bank where pin = '"+pin+"'");
            while (resultSet.next()){
                if (resultSet.getString("type").equals("Deposit")){
                    balance += Integer.parseInt(resultSet.getString("amount"));
                }else {
                    balance -= Integer.parseInt(resultSet.getString("amount"));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        label2.setText(""+balance);

        setSize(1550, 1080);
        setLocation(0,0);
        setLayout(null);
        setVisible(true);


    }

    @Override
    public void actionPerformed(ActionEvent e) {

        setVisible(false);
        new main_Class(pin);

    }

    public static void main(String[] args) {
        new BalanceEnquiry("");
    }

}
