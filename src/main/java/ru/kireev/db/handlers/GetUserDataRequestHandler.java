package ru.kireev.db.handlers;

import ru.kireev.app.common.Serializers;
import ru.kireev.db.DBService;
import ru.kireev.messagesystem.Message;
import ru.kireev.messagesystem.MessageType;
import ru.kireev.messagesystem.RequestHandler;

import java.util.Optional;


public class GetUserDataRequestHandler implements RequestHandler {
  private final DBService dbService;

  public GetUserDataRequestHandler(DBService dbService) {
    this.dbService = dbService;
  }

  @Override
  public Optional<Message> handle(Message msg) {
    long id = Serializers.deserialize(msg.getPayload(), Long.class);
    String data = dbService.getUserData(id);
    return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.USER_DATA.getValue(), Serializers.serialize(data)));
  }
}
