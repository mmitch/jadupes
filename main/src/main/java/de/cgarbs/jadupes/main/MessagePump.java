package de.cgarbs.jadupes.main;

import static java.util.Collections.emptySet;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Stream;

import de.cgarbs.jadupes.contract.message.Message;
import de.cgarbs.jadupes.contract.message.MessageHandling;
import de.cgarbs.jadupes.contract.message.command.Command;

public class MessagePump
{
	private final Map<Class, Collection<MessageHandling>> allHandlers = new HashMap<>();
	private final Deque<Message> messages = new ArrayDeque<>();

	public <T extends Message> void registerMessageHandler(Class<T> messageType, MessageHandling<T> handler)
	{
		allHandlers.computeIfAbsent(messageType, (type) -> new HashSet<MessageHandling>()).add(handler);
	}

	public void executeCommand(Command command)
	{
		queueMessage(command);
		runPump();
	}

	private void runPump()
	{
		Message message;
		while ((message = getNextMessage()) != null)
		{
			handleMessage(message.getClass(), message);
		}
	}

	private <T extends Message> void handleMessage(Class<T> messageType, Message message)
	{
		allHandlers.getOrDefault(message.getClass(), emptySet()) //
				.stream() //
				.flatMap(handler -> (Stream<Message>) handler.handle(message)) //
				.forEach(this::queueMessage);
	}

	private void queueMessage(Message message)
	{
		messages.addFirst(message);
	}

	private Message getNextMessage()
	{
		return messages.pollLast();
	}

}
