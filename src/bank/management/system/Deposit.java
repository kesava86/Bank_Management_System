package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.text.*;

public class Deposit extends JFrame implements ActionListener {

    String pin;
    JTextField textField;   // <-- use Swing JTextField

    JButton b1, b2;

    // Allow only digits; allow empty replacement so deletions/backspace work
    class NumericDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
                throws BadLocationException {
            if (text == null) return;
            if (text.matches("\\d+")) {
                super.insertString(fb, offset, text, attr);
            } // else ignore non-digits
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attr)
                throws BadLocationException {
            // text == null may happen in some situations; treat as empty (deletion)
            if (text == null) {
                super.replace(fb, offset, length, text, attr);
                return;
            }
            // allow deletion (empty string) or digits only
            if (text.equals("") || text.matches("\\d+")) {
                super.replace(fb, offset, length, text, attr);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length); // allow deletions
        }
    }

    Deposit(String pin) {
        this.pin = pin;

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/card1.jpeg"));
        Image i2 = i1.getImage().getScaledInstance(1550, 1080, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0, 0, 1550, 1080);
        add(l3);

        JLabel label1 = new JLabel("ENTER AMOUNT YOU WANT TO DEPOSIT");
        label1.setFont(new Font("System", Font.BOLD, 16));
        label1.setBounds(600, 180, 400, 35);
        label1.setForeground(Color.WHITE);
        l3.add(label1);

        // Use JTextField and attach DocumentFilter
        textField = new JTextField();
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new NumericDocumentFilter());

        textField.setBackground(new Color(65, 125, 128));
        textField.setForeground(Color.WHITE);
        textField.setBounds(600, 230, 320, 35);
        textField.setFont(new Font("Raleway", Font.BOLD, 22));
        l3.add(textField);

        b1 = new JButton("DEPOSIT");
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
        try {
            String amountStr = textField.getText().trim();
            Date date = new Date();

            if (e.getSource() == b1) {
                if (amountStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please Enter the Amount you want to deposit");
                    return;
                }

                // Optional: ensure numeric (should be by filter) and > 0
                long amount;
                try {
                    amount = Long.parseLong(amountStr);
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(null, "Enter a valid amount Greater than 0");
                        return;
                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Enter a valid Numeric Amount");
                    return;
                }

                Conn c = new Conn();
                // Keep your existing insert (prefer PreparedStatement in future)
                c.statement.executeUpdate("insert into bank values('" + pin + "','" + date + "','Deposit','" + amountStr + "')");
                JOptionPane.showMessageDialog(null, "Rs. " + amountStr + " Deposited Successfully");
                setVisible(false);
                new main_Class(pin);

            } else if (e.getSource() == b2) {
                setVisible(false);
                new main_Class(pin);
            }
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Deposit("");
    }
}
