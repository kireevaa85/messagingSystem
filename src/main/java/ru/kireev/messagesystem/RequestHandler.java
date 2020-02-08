package ru.kireev.messagesystem;


import java.util.Optional;

public interface RequestHandler {
  Optional<Message> handle(Message msg);
}
