package seaFood.PTseafood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seaFood.PTseafood.entity.Message;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.repository.IMessageRepository;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private IMessageRepository messageRepository;

    public List<Message> getChatHistory(User sender, User receiver) {
        return messageRepository.findBySenderAndReceiver(sender, receiver);
    }
}
