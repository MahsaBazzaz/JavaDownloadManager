import javafx.scene.control.SelectionModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;


/**
 * the setting panel class
 * @author Mahsa Bazzaz 9631405
 * @version 0.3
 */
public class SettingPanel extends JFrame{
    SettingInfo settingInfo = new SettingInfo(SettingPanel.UNLIMITED,workingDirectory,0);
    private ArrayList<String> filterAddress = new ArrayList<>();
    Filtered filtered= new Filtered(filterAddress);
    private JLabel numberOfD;
    public static final int UNLIMITED = -1;
    public static int numbersAllowed = UNLIMITED;
    private JLabel saveAddress;
    private JLabel layout;
    private int selectedIndex=0;
    UImanager uImanager = new UImanager();
    JTextArea i;
    private static File workingDirectory = new File("C:\\Users\\MahsaH\\Documents\\JDM-Downloads");

//    private String[] toArray = new String[]{};
    JFileChooser fileChooser;
    JList<String> list;
    JTable illegals;
    String[] column = {"FILTERED ADDRESSES"};
    private boolean isEdit= false;
    DefaultTableModel tableModel;
    JTextArea address;
    public SettingPanel() {
        super("Setting");
         tableModel = new DefaultTableModel(column, 0);
        illegals =new JTable(tableModel);
        JScrollPane sp=new JScrollPane(illegals);
        ListSelectionModel table = illegals.getSelectionModel();

//        illegals.addMouseMotionListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                super.mouseClicked(e);
//                tableModel.
//                illegals.rowAtPoint(e.getPoint());
//            }
//        });
         i = new JTextArea("UNLIMITED");
        i.setRows(1);
        ImageIcon img = new ImageIcon(JDM.class.getResource("/icons/EagleGetIcons/settings.png"));
        setIconImage(img.getImage());
        setSize(610, 530);
        numberOfD = new JLabel("Number of Synergistic Downloads:");
        saveAddress = new JLabel("Default Download Folder:");
        layout = new JLabel("Theme:");
        DefaultListModel<String> themes = new DefaultListModel<>();
        themes.addElement("SILVER");
        themes.addElement("BLUE");
        themes.addElement("WINDOWS");
        themes.addElement("WINDOWS CLASSIC");
        int[] lookAndFeelIndex;
        list = new JList<>(themes);

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedIndex = list.getSelectedIndex();
                uImanager.changeLookAndFeel(selectedIndex);
                settingInfo.lookAndFeelIndex = selectedIndex;
            }
        });

        setLayout(new BorderLayout());
        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setCurrentDirectory(workingDirectory);
        JTabbedPane utilities = new JTabbedPane();
        JPanel downlods = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(numberOfD);
        panel.add(i);
        downlods.add(panel, BorderLayout.NORTH);
        JPanel save = new JPanel(new BorderLayout());
        save.add(saveAddress, BorderLayout.NORTH);
        save.add(fileChooser, BorderLayout.CENTER);
        JPanel appearance = new JPanel(new BorderLayout());
        appearance.add(layout, BorderLayout.NORTH);
        appearance.add(list, BorderLayout.CENTER);
        JPanel filter = new JPanel(new BorderLayout());
         address = new JTextArea();
        address.setText("");
        JButton add = new JButton("ADD NEW FILTER");
        JButton edit = new JButton("DELETE");
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int d = illegals.getSelectedRow();
                if (d != -1) {
                        filterAddress.remove(d);

                    tableModel.removeRow(illegals.getSelectedRow());
                }
            }
        });

        JPanel bg = new JPanel(new GridLayout(1,2));
        bg.add(add);
        bg.add(edit);
        JDialog toFilter = new JDialog();
        toFilter.setSize(610,90);
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toFilter.setVisible(true);
            }
        });
        toFilter.setLayout(new BorderLayout());
        JButton addNew = new JButton("ADD");
        JPanel toMakeItBetter = new JPanel(new GridLayout(2,1));
        toMakeItBetter.add(address);
        toMakeItBetter.add(addNew);
        toFilter.add(toMakeItBetter,BorderLayout.CENTER);
        addNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(address.getText() != "") {
                    filterAddress.add(address.getText());
                    tableModel.addRow(new Object[]{filterAddress.get(filterAddress.indexOf(address.getText()))});
                    address.setText("");
                }
                toFilter.setVisible(false);
            }
        });
        filter.add(sp,BorderLayout.CENTER);
        filter.add(bg,BorderLayout.SOUTH);
        utilities.setTabPlacement(SwingConstants.LEFT);
        utilities.add("Downloads", downlods);
        utilities.add("Save and Storage", save);
        utilities.add("Appearance", appearance);
        utilities.add("Filter",filter);

        add(utilities, BorderLayout.NORTH);
        JButton apply = new JButton("APPLY");
        add(apply,BorderLayout.WEST);
        apply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    numbersAllowed = Integer.parseInt(i.getText());
                    settingInfo.numberOfDownloads = numbersAllowed;
                    workingDirectory = fileChooser.getCurrentDirectory();
                    settingInfo.address = workingDirectory;
                    //todo:
                    writeSetting();
                    writeIllegalLinks();
                    setVisible(false);
                } catch (IOException e1) {
                    System.out.println("IO");
                    //e1.printStackTrace();
                }
            }
        });
   }

    /**
     *
     * @throws IOException
     */
    public void writeSetting() throws IOException {
        FileOutputStream fos = new FileOutputStream("C:\\Users\\MahsaH\\Desktop\\JDM\\src\\bin\\setting.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(settingInfo);
        fos.close();
        oos.close();

    }

    /**
     *
     * @throws IOException
     */
    public void readSetting() throws IOException, ClassNotFoundException {
        SettingInfo inf = null;
        FileInputStream fis  = new FileInputStream("C:\\Users\\MahsaH\\Desktop\\JDM\\src\\bin\\setting.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        inf = (SettingInfo) ois.readObject();
        fis.close();
        ois.close();
        setCurrentState(inf);
        }
    /**
     *
     * @throws IOException
     */
    public void writeIllegalLinks() throws IOException {
        FileOutputStream fos = new FileOutputStream("C:\\Users\\MahsaH\\Desktop\\JDM\\src\\bin\\filter.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(filtered);
        fos.close();
        oos.close();
    }

    /**
     *
     * @throws IOException
     */
    public void readIllegalLinks() throws IOException, ClassNotFoundException {
        Filtered fil;
        FileInputStream fis  = new FileInputStream("C:\\Users\\MahsaH\\Desktop\\JDM\\src\\bin\\filter.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
         fil = (Filtered) ois.readObject();
         fis.close();
         ois.close();
         setFiltered(fil);

    }
    /**
     * make setting panel visible
     */
    public void makeSettingPanelVisible() {
        this.setVisible(true);
    }

    /**
     * get file directory
     *
     * @return file directory
     */
    public static File getWorkingDirectory() {
        return workingDirectory;
    }

    public Integer getNumbersAllowed() {
        return numbersAllowed;
    }

    public Integer getSelectedIndex() {
        return selectedIndex;
    }

    public ArrayList<String> getFilterAddress() {
        return filterAddress;
    }
    private void setCurrentState(SettingInfo setting){
        try {
            i.setText(setting.numberOfDownloads.toString());
            //list.setSelectedIndex(setting.lookAndFeelIndex);
            fileChooser.setCurrentDirectory(setting.address);
        }
        catch (NullPointerException e){
            System.out.println("here");
        }

    }
    private void setFiltered(Filtered filtered){
        filterAddress.clear();
        for(int i=0;i<filtered.getSize();i++) {
            filterAddress.add(i,filtered.getByIndex(i));
            tableModel.addRow(new Object[]{filterAddress.get(i)});
        }
    }
    public ArrayList<String> getFilteredOnes(){
        return filterAddress;
    }
}
