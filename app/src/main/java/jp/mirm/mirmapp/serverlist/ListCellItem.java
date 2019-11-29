package jp.mirm.mirmapp.serverlist;

public class ListCellItem {

    private String serverId;
    private String date;

    public ListCellItem(String serverId, String date) {
        this.serverId = serverId;
        this.date = date;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
