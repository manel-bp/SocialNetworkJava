import delivery.HttpHandler;
import repository.Repository;
import service.SnService;

/**
 * The main function is the one calling the HTTP server to wake up.
 */
public class main {
    public void main(String[] args){
        Repository repository = new Repository();
        SnService service = new SnService(repository);
        new HttpHandler(service);
    }
}
