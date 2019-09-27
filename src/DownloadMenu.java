import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DownloadMenu extends JMenu {
    JMenuItem newDownload, pause, resume, cancel, remove, setting, exit,export;
    private File filterFile = new File("C:\\Users\\MahsaH\\Desktop\\JDM\\src\\bin\\filter.txt");
    private File settingFile = new File("C:\\Users\\MahsaH\\Desktop\\JDM\\src\\bin\\setting.txt");
    private File listFile = new File("C:\\Users\\MahsaH\\Desktop\\JDM\\src\\bin\\list.txt");
    private File completeFile = new File("C:\\Users\\MahsaH\\Desktop\\JDM\\src\\bin\\complete.txt");
    private File queueFile = new File("C:\\Users\\MahsaH\\Desktop\\JDM\\src\\bin\\queue.txt");
    private File removedFile = new File("C:\\Users\\MahsaH\\Desktop\\JDM\\src\\bin\\removed.txt");
    private File[] files = {filterFile,settingFile,listFile,completeFile,queueFile,removedFile};

    public DownloadMenu() {
        super("Download");
        setMnemonic('D');
        newDownload = new JMenuItem("new download");
        newDownload.setMnemonic('N');
        newDownload.setToolTipText("New Download");
        newDownload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DownloadManager.getInstance().addNewDownload.setVisible(true);
            }
        });
        pause = new JMenuItem("pause");
        pause.setToolTipText("Pause");
        pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DownloadManager.getInstance().actionPause();
                DownloadManager.getInstance().updateButtons();
            }
        });
        resume = new JMenuItem("resume");
        resume.setToolTipText("Resume");
        resume.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DownloadManager.getInstance().actionResume();
                DownloadManager.getInstance().updateButtons();
            }
        });
        cancel = new JMenuItem("cancel");
        cancel.setToolTipText("cancel");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DownloadManager.getInstance().actionCancel();
                DownloadManager.getInstance().updateButtons();
            }
        });
        remove = new JMenuItem("remove");
        remove.setToolTipText("Remove");
        remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DownloadManager.getInstance().actionClear();
                DownloadManager.getInstance().updateButtons();
            }
        });

        setting = new JMenuItem("setting");
        setting.setToolTipText("Setting");

        setting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DownloadManager.getInstance().settingPanel.makeSettingPanelVisible();
            }
        });

        exit = new JMenuItem("exit");
        exit.setToolTipText("Exit");
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DownloadManager.getInstance().actionExit();
            }
        });

        export = new JMenuItem("export");
        export.setToolTipText("Export");
        export.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                export();
                Desktop desktop = Desktop.getDesktop();
                File file = new File("C:\\Users\\MahsaH\\Desktop\\JDM\\src\\bin");
                if (file.exists()) {
                    try {
                        desktop.open(file);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        add(newDownload);
        newDownload.setMnemonic(KeyEvent.VK_N);
        newDownload.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        add(pause);
        pause.setMnemonic(KeyEvent.VK_P);
        pause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        add(resume);
        resume.setMnemonic(KeyEvent.VK_R);
        resume.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        add(cancel);
        cancel.setMnemonic(KeyEvent.VK_C);
        cancel.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        add(remove);
        remove.setMnemonic(KeyEvent.VK_M);
        remove.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
        add(setting);
        setting.setMnemonic(KeyEvent.VK_S);
        setting.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        add(exit);
        exit.setMnemonic(KeyEvent.VK_X);
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        add(export);
        export.setMnemonic(KeyEvent.VK_E);
        export.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
    }
    private void export() {
        new File("C:\\Users\\MahsaH\\Desktop\\JDM\\src\\bin\\zip.zip").delete();
        ZipOutputStream zos = null;
        File zip = new File("C:\\Users\\MahsaH\\Desktop\\JDM\\src\\bin\\zip.zip");

        try {
            zos = new ZipOutputStream(new FileOutputStream(zip, true));

            for (int i = 0; i < 6; i++) {
                String name = String.valueOf(files[i].toPath());
                File file = new File(name);
                ZipEntry entry = new ZipEntry(name);
                zos.putNextEntry(entry);
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    byte[] byteBuffer = new byte[1024];
                    int bytesRead = -1;
                    while ((bytesRead = fis.read(byteBuffer)) != -1) {
                        zos.write(byteBuffer, 0, bytesRead);
                    }
                    zos.flush();
                } finally {
                    try {
                        fis.close();
                    } catch (Exception e) {
                    }
                }
                zos.closeEntry();

                //zos.flush();
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
        } finally {
            try {
                zos.close();
            } catch (Exception e) {
            }
        }
    }
}
