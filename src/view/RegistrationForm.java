package view;

import model.User;
import model.User.Role;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controller.RegistrationController;

public class RegistrationForm extends JDialog {
    private JFrame parentFrame;

    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfPhone;
    private JTextField tfAddress;
    private JPasswordField pfPassword;
    private JPasswordField pfConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel registerPanel;
    private JButton btnLogin;
    private JCheckBox showPasswordCheckBox;

    public RegistrationForm(JFrame parent) {
        super(parent);
        setTitle("Create a new account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                registerUser();
                clearFields();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Menutup RegistrationForm saat btnCancel ditekan
            }
        });

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLoginForm();
                dispose();
            }
        });

        showPasswordCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPasswordCheckBox.isSelected()) {
                    // Jika checkbox dicentang, tampilkan karakter password dan confirmPassword
                    pfPassword.setEchoChar((char) 0);
                    pfConfirmPassword.setEchoChar((char) 0);
                } else {
                    // Jika checkbox tidak dicentang, sembunyikan karakter password dan confirmPassword
                    pfPassword.setEchoChar('*');
                    pfConfirmPassword.setEchoChar('*');
                }
            }
        });
    }

    public void showForm() {
        setVisible(true);
    }

    private void openLoginForm() {
        dispose();
        LoginForm loginForm = new LoginForm(parentFrame);
        loginForm.setVisible(true);
    }

    private void registerUser() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phone = tfPhone.getText();
        String address = tfAddress.getText();
        String password = String.valueOf(pfPassword.getPassword());
        String confirmPassword = String.valueOf(pfConfirmPassword.getPassword());

        if (name.isEmpty() || email.isEmpty() || address.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter all fields",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Confirm Password does not match",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        user = RegistrationController.addUserToDatabase(name, email, phone, address, password, Role.customer); // Inisialisasi user setelah pendaftaran

        if (user != null) {
            // Tampilkan notifikasi
            JOptionPane.showMessageDialog(this,
                    "Registration successful for user: " + user.getName(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to Register new user",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public User user;

    public static void main(String[] args) {
        RegistrationForm myForm = new RegistrationForm(null);
        myForm.showForm();
        if (myForm.user != null) {
            System.out.println("Successful registration of: " + myForm.user.getName());
        } else {
            System.out.println("Registration canceled");
        }
    }
    private void clearFields() {
        tfName.setText("");
        tfEmail.setText("");
        tfPhone.setText("");
        tfAddress.setText("");
        pfPassword.setText("");
        pfConfirmPassword.setText("");
    }
}
