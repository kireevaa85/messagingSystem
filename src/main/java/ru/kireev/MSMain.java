package ru.kireev;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kireev.db.handlers.GetUserDataRequestHandler;
import ru.kireev.front.FrontendService;
import ru.kireev.front.FrontendServiceImpl;
import ru.kireev.db.DBService;
import ru.kireev.db.DBServiceImpl;
import ru.kireev.front.handlers.GetUserDataResponseHandler;
import ru.kireev.messagesystem.*;

public class MSMain {
  private static final Logger logger = LoggerFactory.getLogger(MSMain.class);

  private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
  private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

  public static void main(String[] args) throws InterruptedException {
    MessageSystem messageSystem = new MessageSystemImpl();

    MsClient databaseMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem);
    DBService dbService = new DBServiceImpl();
    databaseMsClient.addHandler(MessageType.USER_DATA, new GetUserDataRequestHandler(dbService));
    messageSystem.addClient(databaseMsClient);


    MsClient frontendMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem);
    FrontendService frontendService = new FrontendServiceImpl(frontendMsClient, DATABASE_SERVICE_CLIENT_NAME);
    frontendMsClient.addHandler(MessageType.USER_DATA, new GetUserDataResponseHandler(frontendService));
    messageSystem.addClient(frontendMsClient);

    frontendService.getUserData(1, data -> logger.info("got data:{}", data));

    Thread.sleep(100);
    messageSystem.dispose();
    logger.info("done");
  }
}
