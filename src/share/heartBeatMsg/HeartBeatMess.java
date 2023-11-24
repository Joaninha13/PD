package share.heartBeatMsg;

import java.io.Serializable;

public class HeartBeatMess implements Serializable{

    private static final long serialVersionUID = 1L;

    private int listeningPortRMI;
    private String serviceNameRMI;
    private int databaseVersion;


    public HeartBeatMess(int listeningPortRMI, String serviceNameRMI, int databaseVersion) {
        this.listeningPortRMI = listeningPortRMI;
        this.serviceNameRMI = serviceNameRMI;
        this.databaseVersion = databaseVersion;
    }

}
