import javax.swing.*;

/**
 * the ui manager for changing look and feel
 *
 * @author Mahsa Bazzaz 9631405
 * @version 0.3
 */
public class UImanager {
    public final static int SILVER = 0;
    public final static int BLUE = 1;
    public final static int WINDOWS = 2;
    public final static int WINDOWS_CLASSIC = 3;

    public UImanager() {
    }

    /**
     * change the look and feel
     *
     * @param lookAndFeelIndex which look and feel
     */
    public void changeLookAndFeel(int lookAndFeelIndex) {
        switch (lookAndFeelIndex) {
            case SILVER:
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                    SwingUtilities.updateComponentTreeUI(DownloadManager.getInstance());
                } catch (Exception e) {

                }
                break;
            case BLUE:
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                    SwingUtilities.updateComponentTreeUI(DownloadManager.getInstance());
                } catch (Exception e) {
                }
                break;
            case WINDOWS:
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    SwingUtilities.updateComponentTreeUI(DownloadManager.getInstance());
                } catch (Exception e) {

                }
                break;
            case WINDOWS_CLASSIC:
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
                    SwingUtilities.updateComponentTreeUI(DownloadManager.getInstance());
                } catch (Exception e) {

                }
                break;
        }
    }
}
