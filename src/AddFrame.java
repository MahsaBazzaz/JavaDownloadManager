import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AddFrame extends JFrame{
    private JTextField urlTextField = new JTextField();
    private JTextField nameTextfield = new JTextField();
    public AddFrame(){
        //SET UP ADD FRAME
        urlTextField.grabFocus();
        urlTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                String fileName =urlTextField.getText();
                fileName.substring(fileName.lastIndexOf('/') + 1);
                setNameTextfield(fileName);
            }
        });
        JButton startButton;
        setLayout(new GridBagLayout());
        setSize(400, 400);
        ImageIcon ic = new ImageIcon(DownloadManager.class.getResource("/icons/add.png"));
        setIconImage(ic.getImage());
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new BorderLayout());
        startButton = new JButton(new ImageIcon(DownloadManager.class.getResource("/icons/startIcon.png")));
        startButton.setToolTipText("Start Downloading");
        startButton.setSize(20, 20);
        JButton addToQueue = new JButton(new ImageIcon(DownloadManager.class.getResource("/icons/EagleGetIcons/addToqueue.png")));
        addToQueue.setSize(20, 20);
        addToQueue.setToolTipText("Add to queue");
        JLabel urlLable = new JLabel();
        urlLable.setIcon(new ImageIcon(DownloadManager.class.getResource("/icons/url.png")));
        urlLable.setText("URL");
        JLabel nameLable = new JLabel();
        nameLable.setIcon(new ImageIcon(DownloadManager.class.getResource("/icons/webPage.png")));
        nameLable.setText("NAME");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 20;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(urlLable, gbc);
        gbc.ipadx = 100;
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(urlTextField, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 20;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(nameLable, gbc);
        gbc.ipadx = 100;
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(nameTextfield, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(startButton, gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(addToQueue, gbc);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DownloadManager.getInstance().actionAdd(Download.DOWNLOADING);
            }
        });
        addToQueue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DownloadManager.getInstance().actionAdd(Download.PENDING);
            }
        });

    }

    public JTextField getNameTextfield() {
        return nameTextfield;
    }

    public JTextField getUrlTextField() {
        return urlTextField;
    }

    public void eraseURLtextField(){
        urlTextField.setText("");
    }

    public void setNameTextfield(String name) {
        nameTextfield.setText(name);
    }

    public void eraseNameTextField() {
        nameTextfield.setText("");
    }
}
