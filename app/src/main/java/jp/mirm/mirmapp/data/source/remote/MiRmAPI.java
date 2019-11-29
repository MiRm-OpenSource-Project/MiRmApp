package jp.mirm.mirmapp.data.source.remote;

import jp.mirm.mirmapp.backup.BackupPresenter;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import jp.mirm.mirmapp.utils.URLHolder;
import jp.mirm.mirmapp.utils.Utils;

import static jp.mirm.mirmapp.utils.URLHolder.serverDomain;

public class MiRmAPI {

    public static final int LOGIN_SUCCESS = 0;
    public static final int LOGIN_BANNED = 1;
    public static final int LOGIN_ERROR = 2;

    private static String serverId = "null";
    private static boolean isLoggedIn = false;
    public static CookieManager cookie;

    static {
        if (CookieHandler.getDefault() == null) {
            cookie = new CookieManager();
            CookieHandler.setDefault(cookie);
        } else {
            cookie = (CookieManager) CookieHandler.getDefault();
        }
    }

    public static int login(String serverId, String password) {
        MiRmAPI.serverId = serverId;
        get(true, serverDomain + "/panel", "lgsrvid=" + serverId + "&lgpasswd=" + password);
        String txt = readFromUrl(serverDomain + "/info/loggined?srvid=" + serverId);

        int status = LOGIN_ERROR;

        if (txt.trim().contains("true")) {
            status = LOGIN_SUCCESS;
        } else if (txt.trim().contains("banned")) {
            status = LOGIN_BANNED;
        }

        if (status == LOGIN_SUCCESS) {
            MiRmAPI.serverId = serverId;
            isLoggedIn = true;
        }

        return status;
    }

    public static void sendToken(String appToken) {
        post(URLHolder.URL_TOKEN, "srvid=" + serverId + "&app_token=" + appToken + "6delete=false");
    }

    public static void deleteToken(String appToken) {
        post(URLHolder.URL_TOKEN, "srvid=" + serverId + "&app_token=" + appToken + "6delete=true");
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static String readFromUrl(String url) {
        InputStream is = null;
        try {
            is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            return readAll(rd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    //serverDomain+"/panel"
    public static void startServer(String url) {
        get(true, url + "?srvid=" + serverId, "action=start");
    }

    //serverDomain+"/panel"
    public static void stopServer(String url) {
        get(true, url + "?srvid=" + serverId, "action=stop");
    }

    //serverDomain+"/panel"
    public static void reStartServer(String url) {
        get(true, url + "?srvid=" + serverId, "action=restart");
    }

    //serverDomain+"/panel"
    public static void forceStop(String url) {
        get(true, url + "?srvid=" + serverId, "action=kill");
    }

    public static boolean isServerRunning(String url) {
        return getText(url + "?srvid=" + serverId).contains("true");
    }

    public static int getExpiredTime(String url) {
        String time = getText(url + "?srvid=" + serverId);
        if (time.equals("")) return -1;

        try {
            return Integer.parseInt(time);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static int getPort() {
        try {
            String text = getText(URLHolder.URL_PORT + "?srvid=" + serverId);
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void sendCommand(String url, String commands) {
        try {
            get(true, url + "?srvid=" + serverId + "&cmd=" + URLEncoder.encode(commands.replaceAll(" ", "+"), "UTF-8"), "cmd=" + URLEncoder.encode(commands.replaceAll(" ", "+"), "UTF-8").replaceAll("%2B", "+"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String getServerId() {
        return serverId;
    }

    public static String getConsoleText(String urlText) throws Exception {
        URL url = new URL(urlText + "log?srvid=" + serverId + "&_=" + System.currentTimeMillis());
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();

        uc.setUseCaches(false);

        System.setProperty("sun.net.client.defaultReadTimeout", "3000");

        InputStream is = uc.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String s;
        StringBuffer buf = new StringBuffer();
        while ((s = reader.readLine()) != null) {
            buf.append(s);
        }

        reader.close();

        String result = buf.toString();

        if (result.length() < 1) return null;

        return result;
    }

    /**
     * @return token (length: 16)
     */
    public static String sendBackupRequest() {
        return post(URLHolder.URL_BACKUP_REQUEST, "srvid=" + serverId + "&zip_code=" + Utils.encrypt("15Zip32Lion76", "HamHamMusyaMusya")).split(":")[1];
    }

    public static String reward() {
        return post(URLHolder.URL_REWARD, "srvid=" + serverId + "&app_token=nipplef*ck2018");
    }

    public static boolean canDownloadBackup() {
        String text = post(URLHolder.URL_BACKUP_STATUS, "srvid=" + serverId);
        return text.contains("true");
    }

    public static boolean callbackBackup(String token) {
        return post(URLHolder.URL_BACKUP_CALLBACK, "srvid=" + serverId + "&token=" + token).contains("true");
    }

    public static void downloadBackup(String path, String token, BackupPresenter presenter) {
        download(URLHolder.URL_BACKUP_DOWNLOAD + serverId + "_" + token + ".zip", path, token, presenter);
    }

    private static boolean get(boolean isPost, String to, String data) {
        try {
            URL url;
            if (isPost) url = new URL(to);
            else url = new URL(to + data);

            URLConnection uc = url.openConnection();

            if (isPost) {
                uc.setDoOutput(true);
                OutputStream os = uc.getOutputStream();
                PrintStream ps = new PrintStream(os);
                ps.print(data);
            }

            System.setProperty("sun.net.client.defaultReadTimeout", "3000");

            InputStream is = uc.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String s;
            while ((s = reader.readLine()) != null) {
                if (s.contains("そのサーバIDが存在しません") || s.contains("サーバIDかパスワードが違います")) return false;
            }

            reader.close();

        } catch (MalformedURLException e) {
            System.err.println("Invalid URL format: " + to);

        } catch (IOException e) {
            System.err.println("Can't connect to " + to);
        }

        return true;
    }

    private static String getText(String urlText) {
        StringBuffer buf = new StringBuffer();

        try {
            URL url = new URL(urlText);

            URLConnection uc = url.openConnection();

            System.setProperty("sun.net.client.defaultReadTimeout", "3000");

            InputStream is = uc.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String s;
            while ((s = reader.readLine()) != null) {
                buf.append(s);
            }

            reader.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return buf.toString();
    }

    public static String post(String urlText, String data) {
        StringBuffer buf = new StringBuffer();

        try {
            URL url = new URL(urlText);

            URLConnection uc = url.openConnection();

            uc.setDoOutput(true);
            OutputStream os = uc.getOutputStream();
            PrintStream ps = new PrintStream(os);
            ps.print(data);
            ps.close();
            os.close();

            System.setProperty("sun.net.client.defaultReadTimeout", "3000");

            InputStream is = uc.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String s;
            String result = "";
            while ((s = reader.readLine()) != null) {
                buf.append(s);
            }

            reader.close();

        } catch (MalformedURLException e) {
            System.err.println("Invalid URL format: " + urlText);

        } catch (IOException e) {
            System.err.println("Can't connect to " + urlText);
        }

        return buf.toString();
    }

    private static void download(final String urlText, final String path, final String token, final BackupPresenter presenter) {
        try {
            URL url = new URL(urlText);

            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    if (hostname.contains("mirm.jp")) return true;
                    else return false;
                }
            };

            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
            FileUtils.copyURLToFile(url, new File(path));
            MiRmAPI.callbackBackup(token);

            presenter.onFinishBackup(new File(path).getName());

        } catch (IOException e) {
            presenter.onFinishBackup(null);
            e.printStackTrace();
        }
    }

    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    public static void logout() {
        serverId = null;
        isLoggedIn = false;

        cookie = new CookieManager();
        CookieHandler.setDefault(cookie);
    }
}
