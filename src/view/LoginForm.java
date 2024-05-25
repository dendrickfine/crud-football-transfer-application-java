//LoginForm.java
package view;

import model.User;
import controller.LoginController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JDialog {
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton btnOK;
    private JButton btnCancel;
    private JPanel loginPanel;
    private JButton btnRegister;
    private JCheckBox showPasswordCheckBox;
    private JFrame parentFrame; // Untuk menyimpan referensi ke frame utama

    public LoginForm(JFrame parent) {
        super(parent);
        this.parentFrame = parent; // Simpan referensi ke frame utama
        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword());

                User user = LoginController.getAuthenticatedUser(email, password); // Panggil metode dari DatabaseController

                if (user != null) {
                    if (user.getRole() == User.Role.admin) {
                        // Alihkan ke AdminForm
                        AdminForm adminForm = new AdminForm(parentFrame);
                        adminForm.setVisible(true);
                    } else if (user.getRole() == User.Role.customer) {
                        // Alihkan ke CustomerForm dengan menyediakan User
                        CustomerForm customerForm = new CustomerForm(parentFrame, user); // Menyediakan User ke CustomerForm
                        customerForm.setVisible(true);
                    }
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Email or password invalid",
                            "Try Again!",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegistrationForm();
                dispose();
            }
        });

        showPasswordCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPasswordCheckBox.isSelected()) {
                    // Jika checkbox dicentang, tampilkan karakter password
                    pfPassword.setEchoChar((char) 0);
                } else {
                    // Jika checkbox tidak dicentang, sembunyikan karakter password
                    pfPassword.setEchoChar('*');
                }
            }
        });
    }

    public void showForm() {
        setVisible(true);
    }

    private void openRegistrationForm() {
        dispose();
        RegistrationForm registrationForm = new RegistrationForm(parentFrame);
        registrationForm.showForm();
    }
}
