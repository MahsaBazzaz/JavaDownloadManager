import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This class downloads a file from a URL
 *
 * @author Mahsa Bazzaz 9631405
 * @version 0.1
 */

class Download  implements Runnable,Serializable {
    transient private static final int MAX_BUFFER_SIZE = 1024;
    // These are the status codes.

    transient public static final int DOWNLOADING = 0;
    transient public static final int PAUSED = 1;
    transient public static final int COMPLETE = 2;
    transient public static final int CANCELLED = 3;
    transient public static final int ERROR = 4;
    transient public static final int PENDING = 5;
    private static final long serialVersionUID = -590664042906647151L;

    transient private URL url; // download URL
    private String name;
    private Integer size; // size of download in bytes
    private Integer downloaded; // number of bytes downloaded
    transient private long readSinceStart; // number of bytes downloaded since startTime
    transient private long initTime; //inital time when download started or resumed
    private Integer status; // current status of download
    transient private long elapsedTime = 0; // elapsed time till now
    transient private long prevElapsedTime = 0; // time elapsed before resuming download
    transient private long remainingTime = -1; //time remaining to finish download
    transient private float speed = 0; //download speed in KB/s


    transient private long startTime;
    transient private long finishTime;
    transient private File address;
    transient public boolean isSearched = false;

    /**
     * Constructor for Download
     *
     * @param url the url
     */
    public Download(URL url, int status) {
        this.url = url;
        name = getFileName(url);
        size = -1;
        downloaded = 0;
        this.status = status;
        address = SettingPanel.getWorkingDirectory();
       // download();
    }

    /**
     * Get file name portion of URL.
     */

    private String getFileName(URL url) {
        String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }

    /**
     * Format time
     *
     * @param time time in seconds
     * @return time in formatted shape
     */
    public String formatTime(long time) {
        String s = "";
        s += (String.format("%02d", time / 3600)) + ":";
        time %= 3600;
        s += (String.format("%02d", time / 60)) + ":";
        time %= 60;
        s += String.format("%02d", time);
        return s;
    }


    /**
     * Pause this download
     */
    public void pause() {
        prevElapsedTime = elapsedTime;
        setStatus(PAUSED);

    }

    /**
     * Resume this download
     */
    public void resume() {
        setStatus(DOWNLOADING);
        download();
    }

    /**
     * Cancel this download
     */
    public void cancel() {
        prevElapsedTime = elapsedTime;
        setStatus(CANCELLED);
        //stateChanged();
    }

    /**
     * Mark this download as having an error
     */
    private void error() {
        prevElapsedTime = elapsedTime;
        setStatus(ERROR);
    }

    /**
     * Start or resume downloading
     */
    protected void download() {
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * Download file
     */
    public void run() {
        RandomAccessFile file = null;
        InputStream stream = null;

        try {
            // Open connection to URL.
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Specify what portion of file to download.
            connection.setRequestProperty("Range", "bytes=" + downloaded + "-");

            // Connect to server.
            connection.connect();

            // Make sure response code is in the 200 range.
            if (connection.getResponseCode() / 100 != 2) {
                error();
            }

            // Check for valid content length.
            int contentLength = connection.getContentLength();
            if (contentLength < 1) {
                error();
            }

      /* Set the size for this download if it
         hasn't been already set. */
            if (size == -1) {
                size = contentLength;

                DownloadManager.getInstance().list.setModel(DownloadManager.getInstance().downloadList.getModel());

            }
            // used to update speed at regular intervals
            int i = 0;
            // Open file and seek to the end of it.
            file = new RandomAccessFile(SettingPanel.getWorkingDirectory() +"/" + getFileName(url), "rw");
            file.seek(downloaded);

            stream = connection.getInputStream();
            initTime = System.nanoTime();
            while (status == DOWNLOADING) {
                DownloadManager.getInstance().list.setModel(DownloadManager.getInstance().downloadList.getModel());
        /* Size buffer according to how much of the
           file is left to download. */
                if (i == 0) {
                    startTime = System.nanoTime();
                    readSinceStart = 0;
                }
                byte buffer[];
                if (size - downloaded > MAX_BUFFER_SIZE) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    buffer = new byte[(int) (size - downloaded)];
                }
                // Read from server into buffer.
                int read = stream.read(buffer);
                if (read == -1)
                    break;
                // Write buffer to file.
                file.write(buffer, 0, read);
                downloaded += read;
                readSinceStart += read;
                //update speed
                i++;
                if (i >= 50) {
                    speed = (readSinceStart * 976562.5f) / (System.nanoTime() - startTime);
                    if (speed > 0) remainingTime = (long) ((size - downloaded) / (speed * 1024));
                    else remainingTime = -1;
                    elapsedTime = prevElapsedTime + (System.nanoTime() - initTime);
                    i = 0;
                }
            }

      /* Change status to complete if this point was
         reached because downloading has finished. */
            if (status == DOWNLOADING) {
                status = COMPLETE;
                finishTime = System.nanoTime();
                DownloadManager.getInstance().downloadList.remove(this);
                DownloadManager.getInstance().completeList.addDownload(this);
                DownloadManager.getInstance().completeInfo.downloadsInfo.add(this);
                DownloadManager.getInstance().list.setModel(DownloadManager.getInstance().downloadList.getModel());
            }
        } catch (Exception e) {
            System.out.println(e);
            error();
        } finally {
            // Close file.
            if (file != null) {
                try {
                    file.close();
                } catch (Exception e) {
                }
            }

            // Close connection to server.
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                }
            }
        }
    }

    //////////SETTERS
    /**
     * for test
     *
     * @param status the status
     */
    public void setStatus(int status) {
        this.status = status;
    }
    ////////GETTERS

    /**
     * Get this download's URL
     *
     * @return url
     */
    public URL getUrl() {
        return url;
    }

    /**
     * get download size
     *
     * @return size
     */
    public long getSize() {
        return size;
    }

    /**
     * how much is downloaded
     *
     * @return downloaded
     */
    public long getDownloaded() {
        return downloaded;
    }

    /**
     * where the download was saved
     *
     * @return address
     */
    public File getAddress() {
        return address;
    }

    /**
     * get the download speed
     *
     * @return speed
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * get the finished time
     *
     * @return finished time
     */
    public long getFinishTime() {
        return finishTime;
    }

    /**
     * Get remaining time
     *
     * @return remaining time
     */

    public String getRemainingTime() {
        if (remainingTime < 0) return "Unknown";
        else return formatTime(remainingTime);
    }
    /**
     * get the name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get this download's progress
     *
     * @return progress percentages
     */
    public float getProgress() {
        return (downloaded/size)*100;
    }

    /**
     * Get this download's status
     *
     * @return status
     */
    public String getStatus() {
        String s = null;
        switch (status) {
            case 0:
                s = "DOWNLOADING";
                break;
            case 1:
                s = "PAUSED";
                break;
            case 2:
                s = "COMPLETE";
                break;
            case 3:
                s = "CANCELLED";
                break;
            case 4:
                s = "ERROR";
                break;
            case 5:
                s = "PENDING";
                break;
        }
        return s;
    }

    /**
     * get the status number
     *
     * @return status number
     */
    public int getStatusNum() {
        return status;
    }

    /**
     * get the start time
     *
     * @return start time
     */

    public long getStartTime() {
        return startTime;
    }


}