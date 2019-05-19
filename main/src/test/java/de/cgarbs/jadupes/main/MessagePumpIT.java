package de.cgarbs.jadupes.main;

import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import de.cgarbs.jadupes.contract.message.Message;
import de.cgarbs.jadupes.contract.message.MessageHandling;
import de.cgarbs.jadupes.contract.message.command.Command;
import de.cgarbs.jadupes.contract.message.command.CommandHandling;
import de.cgarbs.jadupes.contract.message.event.Event;
import de.cgarbs.jadupes.contract.message.event.EventHandling;

public class MessagePumpIT
{
	private final MessagePump sut = new MessagePump();

	private static class CountdownEvent implements Event
	{
		private int counter;

		CountdownEvent(int counter)
		{
			this.counter = counter;
		}

		int getCounter()
		{
			return counter;
		}
	}

	private static class CountdownEventHandler implements EventHandling<CountdownEvent>
	{

		@Override
		public Stream<Message> handle(CountdownEvent event)
		{
			if (event.counter > 1)
			{
				return Stream.of(new CountdownEvent(event.counter - 1));
			}
			return Stream.empty();
		}

	}

	private static class StartCountdownCommand implements Command
	{
		private int counter;

		StartCountdownCommand(int counter)
		{
			this.counter = counter;
		}

		int getCounter()
		{
			return counter;
		}
	}

	private static class StartCountdownCommandHandler implements CommandHandling<StartCountdownCommand>
	{

		@Override
		public Stream<Message> handle(StartCountdownCommand command)
		{
			return Stream.of(new CountdownEvent(command.counter));
		}

	}

	private static class SpyHandler<T extends Message> implements MessageHandling<T>
	{
		int count = 0;

		@Override
		public Stream<Message> handle(T message)
		{
			count++;
			return Stream.empty();
		}

		int getCount()
		{
			return count;
		}
	}

	@Test
	public void pumpSimpleCountdownEvent()
	{
		// given
		SpyHandler<CountdownEvent> spy = new SpyHandler<CountdownEvent>();
		sut.registerMessageHandler(CountdownEvent.class, spy);
		sut.registerMessageHandler(CountdownEvent.class, new CountdownEventHandler());

		sut.registerMessageHandler(StartCountdownCommand.class, new StartCountdownCommandHandler());
		StartCountdownCommand command = new StartCountdownCommand(4);

		// when
		sut.executeCommand(command);

		// then
		Assertions.assertThat(spy.getCount()).isEqualTo(4);
	}
}
