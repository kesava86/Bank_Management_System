package bank.management.system;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Date;

public class Withdrawl extends JFrame implements ActionListener {

    String pin;
    JTextField textField;
    JButton b1, b2;

    class NumericDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
                throws BadLocationException {
            if (text == null) return;
            if (text.matches("\\d+")) {
                super.insertString(fb, offset, text, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text == null || text.equals("") || text.matches("\\d+")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length); // allow deletions
        }
    }

    public Withdrawl(String pin) {
        this.pin = pin;
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/card1.jpeg"));
        Image i2 = i1.getImage().getScaledInstance(1550, 1080, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0, 0, 1550, 1080);
        add(l3);

        JLabel label1 = new JLabel("MAXIMUM WITHDRAWAL IS RS.10,000");
        label1.setFont(new Font("System", Font.BOLD, 16));
        label1.setBounds(610, 180, 700, 35);
        label1.setForeground(Color.WHITE);
        l3.add(label1);

        JLabel label2 = new JLabel("PLEASE ENTER YOUR AMOUNT");
        label2.setFont(new Font("System", Font.BOLD, 16));
        label2.setBounds(630, 220, 400, 35);
        label2.setForeground(Color.WHITE);
        l3.add(label2);

        // Use JTextField and attach NumericDocumentFilter
        textField = new JTextField();
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        textField.setBackground(new Color(65, 125, 128));
        textField.setForeground(Color.WHITE);
        textField.setBounds(600, 260, 320, 35);
        textField.setFont(new Font("Raleway", Font.BOLD, 22));
        l3.add(textField);

        b1 = new JButton("WITHDRAW");
        b1.setBounds(600, 350, 150, 35);
        b1.setBackground(new Color(65, 125, 128));
        b1.setForeground(Color.WHITE);
        b1.addActionListener(this);
        l3.add(b1);

        b2 = new JButton("BACK");
        b2.setBounds(770, 350, 150, 35);
        b2.setBackground(new Color(65, 125, 128));
        b2.setForeground(Color.WHITE);
        b2.addActionListener(this);
        l3.add(b2);

        setSize(1550, 1080);
        setLocation(0, 0);
        setLayout(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == b1) {
            try {
                String amtStr = textField.getText().trim();
                Date date = new Date();

                // Basic presence check
                if (amtStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please Enter the Amount you want to withdraw");
                    return;
                }

                int amount;
                try {
                    amount = Integer.parseInt(amtStr);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Please Enter a valid Numeric amount");
                    return;
                }

                if (amount <= 0) {
                    JOptionPane.showMessageDialog(null, "Please Enter an Amount greater than 0");
                    return;
                }

                int maxPerTransaction = 10000;
                if (amount > maxPerTransaction) {
                    JOptionPane.showMessageDialog(null, "Maximum withdrawal per transaction is Rs. " + maxPerTransaction);
                    return;
                }

                Conn c = new Conn();
                ResultSet resultSet = c.statement.executeQuery("select * from bank where pin = '" + pin + "'");
                int balance = 0;
                while (resultSet.next()) {
                    if ("Deposit".equalsIgnoreCase(resultSet.getString("type"))) {
                        balance += Integer.parseInt(resultSet.getString("amount"));
                    } else {
                        balance -= Integer.parseInt(resultSet.getString("amount"));
                    }
                }

                if (balance < amount) {
                    JOptionPane.showMessageDialog(null, "Insufficient Balance");
                    return;
                }

                c.statement.executeUpdate("insert into bank values('" + pin + "','" + date + "','Withdrawl','" + amount + "')");
                JOptionPane.showMessageDialog(null, "Rs. " + amount + " Debited Successfully");
                setVisible(false);
                new main_Class(pin);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage());
            }

        } else if (e.getSource() == b2) {
            setVisible(false);
            new main_Class(pin);
        }

    }

    public static void main(String[] args) {
        new Withdrawl("");
    }
}
