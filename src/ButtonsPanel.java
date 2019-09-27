import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ButtonsPanel extends JPanel {
    private JButton addButton, exitButton, folder;
    private JButton pauseButton, resumeButton, settingButton;
    private JButton cancelButton, clearButton, sortButton;
    public JTextArea search;
    private JButton go;
    JFrame sortFrame;

    public boolean orderOne ;
    public boolean orderTwo ;

    public ButtonsPanel() {
        setBackground(new Color(0xd0dff8));
        addButton = new JButton(new ImageIcon(DownloadManager.class.getResource("/icons/EagleGetIcons/add.png")));
        addButton.setToolTipText("new download");
        addButton.setOpaque(false);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DownloadManager.getInstance().addNewDownload.setVisible(true);
            }
        });
        add(addButton);

        resumeButton = new JButton(new ImageIcon(DownloadManager.class.getResource("/icons/EagleGetIcons/play.png")));
        resumeButton.setToolTipText("resume");
        resumeButton.setOpaque(false);
        resumeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(DownloadManager.selectedDownload.getName());
                DownloadManager.getInstance().actionResume();
                updateSelectedButton();
            }
        });

        pauseButton = new JButton(new ImageIcon(DownloadManager.class.getResource("/icons/EagleGetIcons/pause.png")));
        pauseButton.setToolTipText("pause");
        pauseButton.setOpaque(false);
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(DownloadManager.selectedDownload.getName());
                DownloadManager.getInstance().actionPause();
                updateSelectedButton();
            }
        });
        pauseButton.setEnabled(false);
        add(pauseButton);

        resumeButton.setEnabled(false);
        add(resumeButton);
        cancelButton = new JButton(new ImageIcon(DownloadManager.class.getResource("/icons/EagleGetIcons/remove.png")));
        cancelButton.setToolTipText("cancel");
        cancelButton.setOpaque(false);
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DownloadManager.getInstance().actionCancel();
                updateSelectedButton();
            }
        });
        cancelButton.setEnabled(false);
        add(cancelButton);

        sortButton = new JButton(new ImageIcon(DownloadManager.class.getResource("/icons/EagleGetIcons/sort.png")));
        sortButton.setToolTipText("Sort");
        sortButton.setOpaque(false);
        JLabel method = new JLabel("FIRST METHOD OF SORTING");
        ButtonGroup firstMethodOfSorting = new ButtonGroup();
        JRadioButton name = new JRadioButton("name", false);
        name.setActionCommand("name");
        JRadioButton size = new JRadioButton("size", false);
        JRadioButton time = new JRadioButton("time", true);
        time.setSelected(true);

        ButtonGroup secondMethodOfSorting = new ButtonGroup();
        JLabel method2 = new JLabel("SECOND METHOD OF SORTING");
        JRadioButton name2 = new JRadioButton("name", false);
        JRadioButton size2 = new JRadioButton("size", false);
        JRadioButton time2 = new JRadioButton("time", true);
        time2.setSelected(true);

        ButtonGroup cbg = new ButtonGroup();
        JLabel order = new JLabel("ORDER");
        JRadioButton ascending = new JRadioButton("Ascending", false);
        JRadioButton descending = new JRadioButton("Descending", true);

        sortFrame = new JFrame();
        sortFrame.setSize(400, 400);
        sortFrame.setResizable(false);
        firstMethodOfSorting.add(name);
        firstMethodOfSorting.add(size);
        firstMethodOfSorting.add(time);
        JPanel first = new JPanel();
        first.setLayout(new GridLayout(4, 1));
        first.add(method);
        first.add(name);
        first.add(size);
        first.add(time);

        JPanel second = new JPanel();
        second.setLayout(new GridLayout(4, 1));
        second.add(method2);
        secondMethodOfSorting.add(name2);
        secondMethodOfSorting.add(size2);
        secondMethodOfSorting.add(time2);
        second.add(name2);
        second.add(size2);
        second.add(time2);

        cbg.add(ascending);
        cbg.add(descending);
        JPanel o = new JPanel();
        o.setLayout(new GridLayout(3, 1));
        o.add(order);
        o.add(ascending);
        o.add(descending);


        JButton ok = new JButton("ok");
        sortFrame.setLayout(new BorderLayout());
        JPanel fThis = new JPanel(new GridLayout(1,2));
        JPanel mh = new JPanel(new GridLayout(2, 1));
        mh.add(first);
        mh.add(second);
        fThis.add(mh);
        fThis.add(o);

        sortFrame.add(fThis, BorderLayout.CENTER);
        sortFrame.add(ok, BorderLayout.SOUTH);

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortFrame.setVisible(false);
                if (ascending.isSelected()) {
                    orderOne = true;
                } else {
                    orderOne = false;
                }
                if (name.isSelected() && size2.isSelected()){
                    sortDownloads(DownloadManager.getInstance().downloadList.getArr(),byName,bySize,orderOne,orderTwo);
                }
                else if (name.isSelected() && time2.isSelected()) {
                    sortDownloads(DownloadManager.getInstance().downloadList.getArr(),byName,byTime,orderOne,orderTwo);

                } else if(name.isSelected() && name2.isSelected()){
                    sortDownloads(DownloadManager.getInstance().downloadList.getArr(),byName,byName,orderOne,orderTwo);
                }
                if (size.isSelected() && name2.isSelected()) {
                    sortDownloads(DownloadManager.getInstance().downloadList.getArr(),bySize,byName,orderOne,orderTwo);

                } else if (size.isSelected() && time2.isSelected()) {
                    sortDownloads(DownloadManager.getInstance().downloadList.getArr(),bySize,byTime,orderOne,orderTwo);

                } else if(size.isSelected() && size2.isSelected()){
                    sortDownloads(DownloadManager.getInstance().downloadList.getArr(),bySize,bySize,orderOne,orderTwo);
                }
                else if(time.isSelected() && name2.isSelected()){
                    sortDownloads(DownloadManager.getInstance().downloadList.getArr(),byTime,byName,orderOne,orderTwo);
                }
                else if(time.isSelected() && size2.isSelected()){
                    sortDownloads(DownloadManager.getInstance().downloadList.getArr(),byTime,bySize,orderOne,orderTwo);
                }
                else if(time.isSelected() && time2.isSelected()){
                    sortDownloads(DownloadManager.getInstance().downloadList.getArr(),byTime,byTime,orderOne,orderTwo);
                }
            }
        });

        sortButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortFrame.setVisible(true);

            }
        });
        sortButton.setEnabled(true);
        add(sortButton);

        clearButton = new JButton(new ImageIcon(DownloadManager.class.getResource("/icons/EagleGetIcons/taskcleaner.png")));
        clearButton.setToolTipText("remove");
        clearButton.setOpaque(false);
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DownloadManager.getInstance().actionClear();
            }
        });
        clearButton.setEnabled(false);
        add(clearButton);

        folder = new JButton(new ImageIcon(DownloadList.class.getResource("/icons/3.png")));
        folder.setToolTipText("Open Folder");
        folder.setOpaque(false);
        folder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!Desktop.isDesktopSupported()) {
                        System.out.println("Desktop is not supported");
                        return;
                    }
                    Desktop desktop = Desktop.getDesktop();
                    File file = new File(String.valueOf(SettingPanel.getWorkingDirectory()));
                    if (file.exists())
                        desktop.open(file);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        folder.setEnabled(true);
        add(folder);

        settingButton = new JButton(new ImageIcon(DownloadManager.class.getResource("/icons/EagleGetIcons/settings.png")));
        settingButton.setToolTipText("settings");
        settingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DownloadManager.getInstance().settingPanel.makeSettingPanelVisible();
            }
        });
        add(settingButton);

        exitButton = new JButton(new ImageIcon(DownloadManager.class.getResource("/icons/exit.png")));
        exitButton.setToolTipText("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DownloadManager.getInstance().actionExit();
            }
        });
        add(exitButton);

        search = new JTextArea("write here");
        search.setColumns(20);
        search.setToolTipText("SEARCH");
        go = new JButton("Search");
        go.setToolTipText("Start Search");
        JPanel gp = new JPanel(new BorderLayout());
        gp.add(search, BorderLayout.CENTER);
        gp.add(go, BorderLayout.EAST);
        add(gp);
        go.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DownloadManager.getInstance().searchTheString();
                DownloadManager.getInstance().list.setModel(DownloadManager.getInstance().searchList.getModel());

            }
        });

    }

    /**
     * Update each button's state based off of the
     * currently selected download's status.
     */
    public void updateSelectedButton() {
        int status = DownloadManager.getInstance().selectedDownload.getStatusNum();
        switch (status) {
            case 0: //DOWNLOADING
                pauseButton.setEnabled(true);
                resumeButton.setEnabled(false);
                cancelButton.setEnabled(true);
                clearButton.setEnabled(false);
                break;
            case 1: //PAUSED
                pauseButton.setEnabled(false);
                resumeButton.setEnabled(true);
                cancelButton.setEnabled(true);
                clearButton.setEnabled(false);
                break;
            case 2:// COMPLETE
                pauseButton.setEnabled(false);
                resumeButton.setEnabled(false);
                cancelButton.setEnabled(false);
                clearButton.setEnabled(true);
                break;
            case 3: //CANCELLED
                pauseButton.setEnabled(false);
                resumeButton.setEnabled(true);
                cancelButton.setEnabled(false);
                clearButton.setEnabled(true);

            case 4: //ERROR
                pauseButton.setEnabled(false);
                resumeButton.setEnabled(false);
                cancelButton.setEnabled(false);
                clearButton.setEnabled(true);
                break;
            case 5: //PENDING
                pauseButton.setEnabled(false);
                resumeButton.setEnabled(false);
                cancelButton.setEnabled(true);
                clearButton.setEnabled(true);
                break;


        }
    }

    public void updateAll() {
        // No download is selected in table.
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(false);
        cancelButton.setEnabled(false);
        clearButton.setEnabled(false);
        sortButton.setEnabled(true);
    }

    public static Comparator<Download> byName = new Comparator<Download>() {
        @Override
        public int compare(Download o1, Download o2) {
            return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
        }
    };
    public static Comparator<Download> bySize = new Comparator<Download>() {
        @Override
        public int compare(Download o1, Download o2) {
            return (int) (o1.getSize() - o2.getSize());
        }
    };
    public static Comparator<Download> byTime = new Comparator<Download>() {
        @Override
        public int compare(Download o1, Download o2) {
            return (int) (o1.getStartTime() - o2.getStartTime());
        }
    };

    public static void sortDownloads(ArrayList<Download> downloads, Comparator<Download> firstPriority, Comparator<Download>
            secondPriority, boolean ifFirstOneIsAscending, boolean ifSecondOneIsAscending) {
        Collections.sort(downloads, firstPriority);
        for (int i = 0; i < downloads.size(); i++) {
            Download dl1 = downloads.get(i);
            for (int j = 1; j < downloads.size(); j++) {
                Download dl2 = downloads.get(j);
                int c;
                c = firstPriority.compare(dl1, dl2);
                if (c == 0) {
                    c = secondPriority.compare(dl1, dl2);
                }
                Collections.swap(downloads, i, j);
            }
        }

    }


}
