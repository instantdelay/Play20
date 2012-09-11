package play.core.server;
/**
 * provides a stopable Server
 */
public interface ServerWithStop {
  public void stop();
  public java.net.InetSocketAddress mainAddress();
}
