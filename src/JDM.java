import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Program starts here
 *
 * @author Mahsa Bazzaz 9631405
 * @version 0.1
 */
public class JDM {
    // Run the Download Manager.
    public static void main(String[] args) {
        DownloadManager downloadManager = DownloadManager.getInstance();
        ImageIcon img = new ImageIcon(JDM.class.getResource("/icons/EagleGetIcons/icon.png"));
        downloadManager.setIconImage(img.getImage());
        downloadManager.show();
    }
}
