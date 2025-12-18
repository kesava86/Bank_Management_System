package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class Mini extends JFrame implements ActionListener {

    JButton button;

    String pin;
    Mini(String pin){
        this.pin = pin;

        // Scrollable Transaction Area
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("System", Font.PLAIN, 14));
        textArea.setBackground(new Color(255, 255, 255));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(20, 90, 700, 300);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane);


        JLabel label2 = new JLabel("STATE BANK OF INDIA");
        label2.setFont(new Font("System",Font.BOLD,16));
        label2.setBounds(300,20,200,20);
        add(label2);

        JLabel label3 = new JLabel();
        label3.setBounds(20,50,300,20);
        add(label3);

        JLabel label4 = new JLabel();
        label4.setBounds(20,400,300,20);
        add(label4);

        try {
            Con c = new Con();
            ResultSet resultSet = c.statement.executeQuery("select * from login where pin = '"+pin+"'");
            while (resultSet.next()){
                label3.setText("Card Number : "+resultSet.getString("card_number").substring(0,4)+"XXXXXXXX"+resultSet.getString("card_number").substring(12));

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        try {

            int balance = 0;
            Conn c = new Conn();
            ResultSet resultSet = c.statement.executeQuery("select * from bank where pin = '"+pin+"'");

            while (resultSet.next()){
                textArea.append(
                        resultSet.getString("date") + "    " +
                                resultSet.getString("type") + "    Rs. " +
                                resultSet.getString("amount") + "\n\n"
                );

                if(resultSet.getString("type").equals("Deposit")){
                    balance += Integer.parseInt(resultSet.getString("amount"));
                }else {
                    balance -= Integer.parseInt(resultSet.getString("amount"));
                }
            }

            label4.setText("Your Total Balance is Rs. "+balance);

        }catch (Exception e){
            e.printStackTrace();
        }

        button = new JButton("Exit");
        button.setBounds(20,500,100,25);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.addActionListener(this);
        add(button);



        getContentPane().setBackground(new Color(251, 234, 234));
        setSize(800,600);
        setLayout(null);
        setLocation(20,20);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        new main_Class(pin);
    }

    public static void main(String[] args) {
        new Mini("");
    }
}
