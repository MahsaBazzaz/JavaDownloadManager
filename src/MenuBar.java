import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MenuBar extends JMenuBar {
    DownloadMenu downloadMenu;
    HelpMenu helpMenu;

    public MenuBar() {
        downloadMenu = new DownloadMenu();
        helpMenu = new HelpMenu();
        add(downloadMenu);
        add(helpMenu);
    }
}
