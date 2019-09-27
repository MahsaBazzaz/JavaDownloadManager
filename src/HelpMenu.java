import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class HelpMenu extends JMenu {
    JMenuItem about;
    Help aboutDialoge;

    public HelpMenu() {
        super("Help");
        aboutDialoge = new Help();
        setMnemonic(KeyEvent.VK_H);
        about = new JMenuItem("about");
        about.setToolTipText("about");
        about.setMnemonic(KeyEvent.VK_A);
        about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        add(about);
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aboutDialoge.setVisible(true);
            }
        });
    }
}
