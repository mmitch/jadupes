package de.cgarbs.jadupes.contract.message;

import java.util.stream.Stream;

public interface MessageHandling<T extends Message>
{
	Stream<Message> handle(T message);
}
