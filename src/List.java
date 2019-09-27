import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class List extends JList<Download>{

    DefaultListModel<Download> defaultListModel = new DefaultListModel<>();
    JTextArea information;
    JFrame info;
    JPanel show;
    JPanel center, down;
    ImageIcon d;
    JLabel img, name, size, speed, downloaded, status, percent, time;
    JProgressBar progressBar;

    public List() {
        show = new JPanel();
        center = new JPanel(new GridLayout(3, 1));
        down = new JPanel(new GridLayout(1, 4));
        d = new ImageIcon(DownloadList.class.getResource("/icons/5.png"));
        img = new JLabel();
        img.setIcon(d);
        setBorder(new LineBorder(Color.GRAY));
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setSelectionBackground(Color.RED);
        setModel(defaultListModel);
        renderer ren = new renderer();
        setCellRenderer(ren);
        progressBar= new JProgressBar(0, 100);
        //INFO PANEL
        info = new JFrame();
        information = new JTextArea();
        info.setSize(500, 200);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                listener(e);
            }
        });
    }

    public void listener(MouseEvent e) {
        int index = locationToIndex(e.getPoint());
        setSelectedIndex(index);
        DownloadManager.selectedDownload = getSelectedValue();
        if (e.getButton() == 3) {
            information.setText(getInfo(index));
            information.setFont(new Font("Courier New", Font.BOLD, 14));
            information.setEditable(false);
            info.add(information);
            info.setVisible(true);
        } else if (e.getClickCount() == 2 && defaultListModel.get(index).getStatusNum() == 2) {
            try {
                if (!Desktop.isDesktopSupported()) {
                    System.out.println("Desktop is not supported");
                    return;
                }
                Desktop desktop = Desktop.getDesktop();
                File file = new File(String.valueOf(defaultListModel.get(index).getAddress()) + "/" +
                        defaultListModel.get(index).getName());
                if (file.exists())
                    desktop.open(file);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * make the location of action handler an index of a jlist
     *
     * @param location the location
     * @return the index
     */
    @Override
    public int locationToIndex(Point location) {
        return super.locationToIndex(location);
    }

    /**
     * get the info for info panel
     *
     * @param index the index of download
     * @return information
     */
    public String getInfo(int index) {
        String info;
        if (defaultListModel.get(index).getStatusNum() != 2) {
            info = "FILE:\t" + defaultListModel.get(index).getName() +
                    "\nURL:\t" + defaultListModel.get(index).getUrl() +
                    "\nStatus:\t" + defaultListModel.get(index).getStatus() +
                    "\nSIZE:\t" + defaultListModel.get(index).getSize() +
                    "\nSAVE TO:\t" + defaultListModel.get(index).getAddress() +
                    "\nCREATED:\t" + defaultListModel.get(index).getStartTime() +
                    "\nPROGRESS:\t" + defaultListModel.get(index).getProgress() +
                    "\nSPEED:\t" + defaultListModel.get(index).getSpeed();
        } else {
            info = "FILE:\t" + defaultListModel.get(index).getUrl() +
                    "\nURL:\t" + defaultListModel.get(index).getUrl() +
                    "\nStatus:\t" + defaultListModel.get(index).getStatus() +
                    "\nSIZE:\t" + defaultListModel.get(index).getSize() +
                    "\nSAVE TO:\t" + defaultListModel.get(index).getAddress() +
                    "\nCREATED:\t" + defaultListModel.get(index).getStartTime() +
                    "\nFINISHED:\t" + defaultListModel.get(index).getFinishTime();
        }
        return info;
    }

    @Override
    public void setModel(ListModel<Download> model) {
        super.setModel(model);
    }

    public void changeSelectedColor(Download value) {
        name.setBackground(Color.ORANGE);
        size.setBackground(Color.ORANGE);
        speed.setBackground(Color.ORANGE);
        downloaded.setBackground(Color.ORANGE);
        status.setBackground(Color.ORANGE);
        center.setBackground(Color.ORANGE);
        show.setBackground(Color.ORANGE);
        down.setBackground(Color.ORANGE);
        DownloadManager.selectedDownload = value;
    }

    public void remove(Download download) {
        defaultListModel.removeElement(download);
    }

    private class renderer implements ListCellRenderer<Download> {

        @Override
        public Component getListCellRendererComponent(JList list, Download value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            progressBar.setValue((int) value.getProgress());
            if (isSelected) {
                changeSelectedColor(value);
            }
            return makeShow(value);
        }
    }

    public int getListSize() {
        return defaultListModel.getSize();
    }

    public void addDownload(Download download) {
       defaultListModel.addElement(download);
    }

    public void removeDownload(int index) {
        defaultListModel.remove(index);
    }

    @Override
    public Download getSelectedValue() {
        return super.getSelectedValue();
    }

    @Override
    public int getSelectedIndex() {
        return super.getSelectedIndex();
    }

    public Download getDownload(int index) {
        return defaultListModel.get(index);
    }

    public ArrayList<Download> getArr() {
        ArrayList<Download> arrayList = null;
        for (int i = 0; i < defaultListModel.getSize(); i++) {
            arrayList.add(i, defaultListModel.get(i));
        }
        return arrayList;
    }
    public void setDefaultListModel(ArrayList<Download> list) {
        for (int i = 0; i < list.size(); i++) {
            defaultListModel.add(i, list.get(i));
        }
    }

    @Override
    public ListModel<Download> getModel() {
        return super.getModel();
    }
    private JPanel makeShow(Download value){
        name = new JLabel(value.getName());
        name.setFont(new Font("Courier New", Font.BOLD, 14));
        name.setOpaque(false);

        size = new JLabel("Size " + value.getSize() + "");
        size.setOpaque(false);
        size.setFont(new Font("Courier New", Font.BOLD, 12));

        speed = new JLabel("Speed " + value.getSpeed() + "");
        speed.setFont(new Font("Courier New", Font.BOLD, 12));

        downloaded = new JLabel(value.getDownloaded() + "/" + value.getSize());
        downloaded.setFont(new Font("Courier New", Font.BOLD, 12));
        status = new JLabel("Status " + value.getStatus());

        status.setFont(new Font("Courier New", Font.BOLD, 12));
        show.setLayout(new BorderLayout());

        percent = new JLabel(value.getProgress() + "");
        percent.setOpaque(false);
        percent.setFont(new Font("Courier New", Font.BOLD, 12));

        time = new JLabel(value.getStartTime() + "-" + value.getFinishTime());

        JLabel remainig = new JLabel(value.getRemainingTime());
        time = new JLabel(value.getFinishTime() + "");
        down.add(status);
        if (value.getStatusNum() == Download.COMPLETE) {
            down.add(time);
            down.add(size);
        } else {
            down.add(speed);
            down.add(downloaded);
            down.add(remainig);
        }
        center.add(name);
        center.add(progressBar);
        center.add(down);

        show.add(img, BorderLayout.WEST);
        show.add(center, BorderLayout.CENTER);
        show.add(percent, BorderLayout.EAST);
        return show;
    }
}
