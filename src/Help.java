import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Help extends JDialog{
    public Help(){
        getGlassPane();
        ImageIcon ic2 = new ImageIcon(DownloadManager.class.getResource("/icons/about.png"));
        setIconImage(ic2.getImage());
        JTextArea aboutText = new JTextArea("JAVA DOWNLOAD MANAGER(PHASE 1)\n" +
                "DEVELOPED BY Mahsa Bazzaz\n" +
                "9631405\n" +
                "Amirkabir University of Technology(Tehran Polytechnic)\n" +
                "start date: 12 May,2018\n" +
                "finish date(Phase1): 20 May,2018");
        aboutText.setEnabled(false);
        aboutText.setDisabledTextColor(Color.BLACK);
        add(aboutText);
        setSize(400, 150);

    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
    }
}
