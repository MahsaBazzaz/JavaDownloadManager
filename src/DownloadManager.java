
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListDataListener;


/**
 * GUI part
 *
 * @author Mahsa Bazzaz 9631405
 * @version 0.1
 */

public class DownloadManager extends JFrame {
    private static DownloadManager downloadManager = null;
    SettingPanel settingPanel = new SettingPanel();
    JDMtoolbar toolbar;
    public DownloadList downloadList;
    public QueueList queueList;
    public CompleteList completeList;
    public SearchList searchList;
    public static TheList list;
    // Currently selected download
    public static Download selectedDownload;
    AddFrame addNewDownload;
    ButtonsPanel buttonsPanel;
    MenuBar menuBar;

    public ArrayList<Download> removed;


    DownloadInfo downloadInfo;
    QueueInfo queueInfo;
    CompleteInfo completeInfo;


    private DownloadManager() {
        list = new TheList();
        downloadList = new DownloadList();
        downloadInfo = new DownloadInfo(downloadList.defaultListModel);
        queueList = new QueueList();
        queueInfo = new QueueInfo(queueList.defaultListModel);
        completeList = new CompleteList();
        completeInfo = new CompleteInfo(completeList.defaultListModel);
        searchList = new SearchList();
        removed = new ArrayList<>();
        try {
            settingPanel.readSetting();
            settingPanel.readIllegalLinks();
            //readDownloads();
            //readQueue();
            //readComplete();
        } catch (IOException e) {

        } catch (ClassNotFoundException e) {

        } catch (NullPointerException e) {

        }
        addNewDownload = new AddFrame();
        toolbar = new JDMtoolbar();
        JDMtoolbar.selectedButton = JDMtoolbar.PROCESSING;
        buttonsPanel = new ButtonsPanel();
        menuBar = new MenuBar();
        //SET UP THE MAIN FRAME APPEARANCE
        Image image = Toolkit.getDefaultToolkit().getImage(System.class.getResource("/icons/EagleGetIcons/icon.png"));
        TrayIcon trayIcon = new TrayIcon(image, "JDM");
        //set up main frame
        setTitle("Download Manager");
        setSize(1310, 760);
        //EXIT BUTTON MAKES PROGRAM GO TO SYSTEM TRAY
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (SystemTray.isSupported()) {
                    SystemTray tray = SystemTray.getSystemTray();
                    trayIcon.setImageAutoSize(true);
                    trayIcon.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            super.mousePressed(e);
                            tray.remove(trayIcon);
                            setVisible(true);
                        }
                    });
                    try {
                        tray.add(trayIcon);
                    } catch (AWTException a) {
                        System.err.println(a);
                    }
                }
            }
        });


        setJMenuBar(menuBar);

        //  Set up Downloads Jlist.
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedDownload = list.getSelectedValue();
                updateButtons();
            }
        });


        // Set up downloads panel.
        getContentPane().setLayout(new BorderLayout());
        JPanel right = new JPanel();
        right.setLayout(new BorderLayout());
        right.add(buttonsPanel, BorderLayout.NORTH);
        right.add(new JScrollPane(list), BorderLayout.CENTER);
        right.setBackground(new Color(0xe7effb));
        getContentPane().add(toolbar.getToolBar(), BorderLayout.WEST);
        getContentPane().add(right, BorderLayout.CENTER);

    }

    /**
     * Exit this program
     */
    public void actionExit() {
        try {
            writeDownloads();
            writeRemovedDownloads();
            writeQueue();
            writeComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    /**
     * Add a new download
     */
    public void actionAdd(int status) {
        URL verifiedUrl = verifyUrl(addNewDownload.getUrlTextField().getText());
        if (verifiedUrl != null) {
            if (isAllowed(verifiedUrl)) {
                Download dl = new Download(verifiedUrl, status);

                if (status == Download.DOWNLOADING) {
                    if(isLessThanLimit()){
                    //downloads.add(dl);
                    downloadList.addDownload(dl);
                    downloadInfo.downloadsInfo.add(dl);}
                    else{
                        JOptionPane.showMessageDialog(addNewDownload, "YOU REACHED YOUR LIMIT",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        addNewDownload.eraseURLtextField();
                    }
                } else if (status == Download.PENDING) {
                    //queue.add(dl);
                    queueList.addDownload(dl);
                    queueInfo.queueInfo.add(dl);
                }
                addNewDownload.eraseURLtextField(); // reset add text field
                addNewDownload.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(addNewDownload, "YOU ARE NOT ALLOWED TO DOWNLOAD THIS",
                        "Error", JOptionPane.ERROR_MESSAGE);
                addNewDownload.eraseURLtextField();
            }
        } else {
            JOptionPane.showMessageDialog(addNewDownload, "Invalid Download URL", "Error", JOptionPane.ERROR_MESSAGE);
            addNewDownload.eraseURLtextField();
        }
        addNewDownload.setVisible(false);
        addNewDownload.eraseNameTextField();
        refresh();
    }

    /**
     * Pause the selected download
     */
    public void actionPause() {
        selectedDownload.pause();
        updateButtons();
    }

    /**
     * Resume the selected download
     */
    public void actionResume() {
        selectedDownload.resume();
        updateButtons();
    }

    /**
     * Cancel the selected download
     */
    public void actionCancel() {
        selectedDownload.cancel();
        updateButtons();
    }

    /**
     * Clear the selected download
     */
    public void actionClear() {
        removed.add(downloadList.getSelectedValue());
        if (selectedDownload.getStatusNum() == Download.CANCELLED) {
            downloadList.remove(selectedDownload);
        } else if (selectedDownload.getStatusNum() == Download.COMPLETE) {
            completeList.remove(selectedDownload);
        }
        selectedDownload = null;
        JDMtoolbar.selectedButton = JDMtoolbar.PROCESSING;
        refresh();
        updateButtons();
    }

    public void updateButtons() {
        if (selectedDownload != null) {
            buttonsPanel.updateSelectedButton();
        } else {
            buttonsPanel.updateAll();
        }
    }

    public int getSelectedButton() {
        return toolbar.getSelectedButton();
    }

    /**
     * Verify download URL.
     *
     * @param url the url
     * @return the url version of string that have been read
     */
    private URL verifyUrl(String url) {
        // Only allow HTTP URLs.
        if (!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://"))
            return null;

        // Verify format of URL.
        URL verifiedUrl = null;
        try {
            verifiedUrl = new URL(url);
        } catch (Exception e) {
            return null;
        }

        // Make sure URL specifies a file.
        if (verifiedUrl.getFile().length() < 2)
            return null;

        return verifiedUrl;
    }

    /**
     * check if that download is not in filtered ones
     *
     * @param url the download url
     * @return true or false
     */
    private boolean isAllowed(URL url) {
        for (int i = 0; i < settingPanel.getFilterAddress().size(); i++) {
            if (url.toString().contains(settingPanel.getFilterAddress().get(i))) {
                return false;
                //todo: reg
            }
        }
        return true;
    }

    /**
     * for searvh bar
     */
    public void searchTheString() {
        String str = buttonsPanel.search.getText();
        for (int i = 0; i < downloadList.getListSize(); i++) {
            if (downloadList.getDownload(i).getName().toLowerCase().contains(str.toLowerCase()) ||
                    downloadList.getDownload(i).getUrl().toString().toLowerCase().contains(str.toLowerCase())) {
                downloadList.getDownload(i).isSearched = true;
                searchList.addDownload(downloadList.getDownload(i));
            }
        }
        for (int i = 0; i < completeList.getListSize(); i++) {
            if (completeList.getDownload(i).getName().toLowerCase().contains(str.toLowerCase()) ||
                    completeList.getDownload(i).getUrl().toString().toLowerCase().contains(str.toLowerCase())) {
                completeList.getDownload(i).isSearched = true;
                searchList.addDownload(completeList.getDownload(i));
            }
        }
        for (int i = 0; i < queueList.getListSize(); i++) {
            if (queueList.getDownload(i).getName().toLowerCase().contains(str.toLowerCase()) ||
                    queueList.getDownload(i).getUrl().toString().toLowerCase().contains(str.toLowerCase())) {
                queueList.getDownload(i).isSearched = true;
                searchList.addDownload(queueList.getDownload(i));
            }
        }
    }

    public static void refresh() {

        switch (JDMtoolbar.selectedButton) {
            case JDMtoolbar.PROCESSING:
                DownloadManager.getInstance().list.setModel(DownloadManager.getInstance().downloadList.getModel());
                break;
            case JDMtoolbar.COMPLETED:
                DownloadManager.getInstance().list.setModel(DownloadManager.getInstance().completeList.getModel());
                break;
            case JDMtoolbar.QUEUES:
                DownloadManager.getInstance().list.setModel(DownloadManager.getInstance().queueList.getModel());
                break;
            case JDMtoolbar.SEARCH:
                DownloadManager.getInstance().list.setModel(DownloadManager.getInstance().searchList.getModel());
                break;
            case JDMtoolbar.DEFAULT:
                DownloadManager.getInstance().searchList.removeAll();
                DownloadManager.getInstance().list.setModel(DownloadManager.getInstance().downloadList.getModel());
        }
        SwingUtilities.updateComponentTreeUI(list);
    }
    //////////////FILE HANDELING

    /**
     * writes downloads to a file
     *
     * @throws IOException
     */
    public void writeDownloads() throws IOException {
        FileOutputStream fos = new FileOutputStream("C:\\Users\\MahsaH\\Desktop\\JDM\\src\\bin\\list.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(downloadInfo);
        fos.close();
        oos.close();

    }

    /**
     * reads downloads from a file
     *
     * @throws IOException
     */
    public void readDownloads() throws IOException, ClassNotFoundException {
        DownloadInfo inf = null;
        FileInputStream fis = new FileInputStream("C:\\Users\\MahsaH\\Desktop\\JDM\\src\\bin\\list.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        inf = (DownloadInfo) ois.readObject();
        fis.close();
        ois.close();
        setList(inf);
    }

    /**
     * set downloads that have been read to program list
     *
     * @param downloadInfo
     */
    public void setList(DownloadInfo downloadInfo) {
        if (downloadInfo != null && downloadInfo.downloadsInfo.size() != 0) {
            downloadList.setDefaultListModel(downloadInfo.downloadsInfo);
            refresh();
        }
    }

    /**
     * writes downloads to a file
     *
     * @throws IOException
     */
    public void writeComplete() throws IOException {
        FileOutputStream fos = new FileOutputStream("C:\\Users\\MahsaH\\Desktop\\JDM\\src\\bin\\complete.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(completeInfo);
        fos.close();
        oos.close();

    }

    /**
     * reads downloads from a file
     *
     * @throws IOException
     */
    public void readComplete() throws IOException, ClassNotFoundException {
        CompleteInfo inf = null;
        FileInputStream fis = new FileInputStream("C:\\Users\\MahsaH\\Desktop\\JDM\\src\\bin\\complete.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        inf = (CompleteInfo) ois.readObject();
        fis.close();
        ois.close();
        setCompleted(inf);
    }

    /**
     * set downloads that have been read to program list
     *
     * @param downloadInfo
     */
    public void setCompleted(CompleteInfo downloadInfo) {
        if (downloadInfo != null && downloadInfo.downloadsInfo.size() != 0) {
            completeList.setDefaultListModel(downloadInfo.downloadsInfo);
            refresh();
        }
    }

    /**
     * writes the queue in a file
     *
     * @throws IOException
     */
    public void writeQueue() throws IOException {
        FileOutputStream fos = new FileOutputStream("C:\\Users\\MahsaH\\Desktop\\JDM\\src\\bin\\queue.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(queueInfo);
        fos.close();
        oos.close();
    }

    /**
     * reads a gueue from a file
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void readQueue() throws IOException, ClassNotFoundException {
        QueueInfo inf = null;
        FileInputStream fis = new FileInputStream("C:\\Users\\MahsaH\\Desktop\\JDM\\src\\bin\\queue.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        inf = (QueueInfo) ois.readObject();
        fis.close();
        ois.close();
        setQueue();
    }

    /**
     * set the queue that have been read in program list
     */
    public void setQueue() {
        if (queueInfo != null && queueInfo.queueInfo.size() != 0) {
            queueList.setDefaultListModel(queueInfo.queueInfo);
            refresh();
        }
    }

    /**
     * write removed downloads in a file
     *
     * @throws IOException
     */
    public void writeRemovedDownloads()
            throws IOException {
        BufferedWriter writer = new BufferedWriter(
                new FileWriter("C:\\Users\\MahsaH\\Desktop\\JDM\\src\\bin\\removed.txt", true));
        for (int i = 0; i < removed.size(); i++) {
            String str = removed.get(i).getName() + "\t" + removed.get(i).getUrl();
            writer.append("\n");
            writer.append(str);
        }
        writer.close();
    }


    /**
     * get the only instance of this single tone class
     *
     * @return Download Manager instance
     */
    public static DownloadManager getInstance() {

        if (downloadManager == null) {
            downloadManager = new DownloadManager();

        }

        return downloadManager;
    }

    public boolean isLessThanLimit() {
        if (SettingPanel.numbersAllowed == SettingPanel.UNLIMITED)
            return true;
        else if (downloadList.getListSize() <= SettingPanel.numbersAllowed) {
            return true;
        }
        return false;

    }
}
