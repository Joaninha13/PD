package share.RMI;

import java.io.IOException;
import java.rmi.Remote;

public interface IRmiClient extends Remote {
    void writeFileChunk(byte [] fileChunk, int nbytes) throws IOException;
}
