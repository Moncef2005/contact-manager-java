import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ContactManager extends JFrame {

    
    private JTextField txtNom, txtPrenom, txtTel, txtEmail;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnActualiser;

    // Connexion à la base de données
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_contacts";
    private static final String USER = "root";
    private static final String PASSWORD = "";  

    public ContactManager() {
        setTitle("Gestion de Contacts");
        setSize(900, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        
        JPanel panelForm = new JPanel(new GridLayout(5, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelForm.add(new JLabel("Nom :"));
        txtNom = new JTextField();
        panelForm.add(txtNom);

        panelForm.add(new JLabel("Prénom :"));
        txtPrenom = new JTextField();
        panelForm.add(txtPrenom);

        panelForm.add(new JLabel("Téléphone :"));
        txtTel = new JTextField();
        panelForm.add(txtTel);

        panelForm.add(new JLabel("Email :"));
        txtEmail = new JTextField();
        panelForm.add(txtEmail);

        // Boutons
        JPanel panelBoutons = new JPanel();
        btnAjouter = new JButton("Ajouter");
        btnModifier = new JButton("Modifier sélection");
        btnSupprimer = new JButton("Supprimer sélection");
        btnActualiser = new JButton("Actualiser");

        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnActualiser);

        panelForm.add(new JLabel(""));
        panelForm.add(panelBoutons);

        add(panelForm, BorderLayout.NORTH);

        // Tableau des contacts
        String[] colonnes = {"ID", "Nom", "Prénom", "Téléphone", "Email"};
        model = new DefaultTableModel(colonnes, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Actions des boutons 
        btnAjouter.addActionListener(e -> ajouterContact());
        btnModifier.addActionListener(e -> modifierContact());
        btnSupprimer.addActionListener(e -> supprimerContact());
        btnActualiser.addActionListener(e -> chargerContacts());

        
        chargerContacts();
    }

    //AJOUTER 
    private void ajouterContact() {
        String nom = txtNom.getText().trim();
        String prenom = txtPrenom.getText().trim();
        String tel = txtTel.getText().trim();
        String email = txtEmail.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nom et prénom obligatoires !");
            return;
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO contacts (nom, prenom, telephone, email) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nom);
            pst.setString(2, prenom);
            pst.setString(3, tel.isEmpty() ? null : tel);
            pst.setString(4, email.isEmpty() ? null : email);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Contact ajouté !");
            viderChamps();
            chargerContacts();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }

    //MODIFIER 
    private void modifierContact() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un contact à modifier !");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        String nomActuel = (String) model.getValueAt(row, 1);
        String prenomActuel = (String) model.getValueAt(row, 2);
        String telActuel = model.getValueAt(row, 3) != null ? (String) model.getValueAt(row, 3) : "";
        String emailActuel = model.getValueAt(row, 4) != null ? (String) model.getValueAt(row, 4) : "";

        // Pré-remplir le formulaire
        txtNom.setText(nomActuel);
        txtPrenom.setText(prenomActuel);
        txtTel.setText(telActuel);
        txtEmail.setText(emailActuel);

        btnAjouter.setText("Enregistrer modification");
        btnAjouter.removeActionListener(btnAjouter.getActionListeners()[0]);
        btnAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = txtNom.getText().trim();
                String prenom = txtPrenom.getText().trim();
                String tel = txtTel.getText().trim();
                String email = txtEmail.getText().trim();

                if (nom.isEmpty() || prenom.isEmpty()) {
                    JOptionPane.showMessageDialog(ContactManager.this, "Nom et prénom obligatoires !");
                    return;
                }

                try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                    String sql = "UPDATE contacts SET nom = ?, prenom = ?, telephone = ?, email = ? WHERE id = ?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, nom);
                    pst.setString(2, prenom);
                    pst.setString(3, tel.isEmpty() ? null : tel);
                    pst.setString(4, email.isEmpty() ? null : email);
                    pst.setInt(5, id);
                    pst.executeUpdate();

                    JOptionPane.showMessageDialog(ContactManager.this, "Contact modifié avec succès !");

                    // Remettre tout comme avant
                    btnAjouter.setText("Ajouter");
                    viderChamps();
                    chargerContacts();
                    btnAjouter.removeActionListener(this);
                    btnAjouter.addActionListener(ev -> ajouterContact());

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(ContactManager.this, "Erreur : " + ex.getMessage());
                }
            }
        });
    }

    //SUPPRIMER
    private void supprimerContact() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un contact !");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer ce contact ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "DELETE FROM contacts WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Contact supprimé !");
            chargerContacts();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }

    //CHARGER LES CONTACTS
    private void chargerContacts() {
        model.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM contacts ORDER BY nom";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Object[] ligne = {
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("telephone"),
                    rs.getString("email")
                };
                model.addRow(ligne);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur de chargement : " + ex.getMessage());
        }
    }

    private void viderChamps() {
        txtNom.setText("");
        txtPrenom.setText("");
        txtTel.setText("");
        txtEmail.setText("");
    }

    //MAIN
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Driver MySQL manquant !\nTéléchargez mysql-connector-j-x.x.xx.jar");
            System.exit(0);
        }

        SwingUtilities.invokeLater(() -> {
            new ContactManager().setVisible(true);
        });
    }
}


