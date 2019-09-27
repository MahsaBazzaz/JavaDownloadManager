
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * the left side toolbar class
 * @author Mahsa Bazzaz 9631405
 * @version 0.3
 */
public class JDMtoolbar extends JToolBar{
    public static final int PROCESSING = 0;
    public static final int COMPLETED = 1;
    public static final int QUEUES = 2;
    public static final int SEARCH = 4;
    public static final int DEFAULT = 5;
    JPanel tool;
    public static int selectedButton = PROCESSING;
    public JDMtoolbar() {
        setOrientation(SwingConstants.VERTICAL);
        tool = new JPanel();
        tool.setLayout(new BorderLayout());
        ImageIcon toolb = new ImageIcon(JDMtoolbar.class.getResource("/icons/EagleGetIcons/logoCopy.png"));
        setLayout(new GridLayout(15, 1));
        setFloatable(false);
        JButton processing = new JButton("Processing");
        processing.setIcon(new ImageIcon(JDMtoolbar.class.getResource("/icons/EagleGetIcons/processing.png")));
        processing.setBorderPainted(false);
        processing.setOpaque(false);
        processing.setContentAreaFilled(true);
        processing.setForeground(Color.LIGHT_GRAY);
        processing.getDisabledSelectedIcon();
        JButton completed = new JButton("Completed");
        completed.setIcon(new ImageIcon(JDMtoolbar.class.getResource("/icons/EagleGetIcons/completed.png")));
        completed.setBorderPainted(false);
        completed.setContentAreaFilled(true);
        completed.setOpaque(false);
        completed.setForeground(Color.LIGHT_GRAY);
        JButton queues = new JButton("      Queues");
        queues.setIcon(new ImageIcon(JDMtoolbar.class.getResource("/icons/EagleGetIcons/queue.png")));
        queues.setBorderPainted(false);
        queues.setContentAreaFilled(true);
        queues.setOpaque(false);
        queues.setForeground(Color.LIGHT_GRAY);
        JButton aDefault = new JButton("Default");
        aDefault.setSelected(true);
        aDefault.setBorderPainted(false);
        aDefault.setOpaque(false);
        aDefault.setContentAreaFilled(true);
        aDefault.setForeground(Color.LIGHT_GRAY);
        setBorderPainted(true);
        processing.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedButton = PROCESSING;
                DownloadManager.refresh();
            }
        });
        completed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedButton= COMPLETED;
                DownloadManager.refresh();
            }
        });
        queues.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedButton =QUEUES;
                DownloadManager.refresh();
            }
        });
        aDefault.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedButton = DEFAULT;
                DownloadManager.refresh();
            }
        });
        add(processing);
        add(completed);
        add(queues);
        add(aDefault);
        setBackground(new Color(0x32363f));
        JLabel label = new JLabel(toolb);
        tool.add(label, BorderLayout.NORTH);
        tool.add(this, BorderLayout.CENTER);
    }

    /**
     * get the toolbar
     * @return toolbar
     */
    public JPanel getToolBar() {
        return tool;
    }

    public int getSelectedButton() {
        return selectedButton;
    }


}
