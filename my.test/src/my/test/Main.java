package my.test;

import java.io.IOException;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackMessageHandle;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import com.ullink.slack.simpleslackapi.replies.SlackChannelReply;

public class Main {

	public static void main(String[] args) {
		System.out.println("Hello World");

		SlackSession session = SlackSessionFactory
				.createWebSocketSlackSession("xoxb-47885928065-Fxo19ntsklYlCuqvH0KGwfto");
		try {
			session.connect();

			// get a user
			SlackUser user = session.findUserByUserName("kriss");

			// get its direct message channel
			SlackMessageHandle<SlackChannelReply> reply = session.openDirectMessageChannel(user);

			// get the channel
			SlackChannel channel = reply.getReply().getSlackChannel();

			// send the message to this channel
			session.sendMessage(channel, "Be aware sti!", null);

			SlackChannel lchannel = session.findChannelByName("random");

			session.sendMessage(lchannel, "Hey there");

			session.addMessagePostedListener(new SlackMessagePostedListener() {

				public void onEvent(SlackMessagePosted event, SlackSession session) {
					SlackUser lUser = event.getSender();

					SlackMessageHandle<SlackChannelReply> reply = session.openDirectMessageChannel(lUser);

					// get the channel
					SlackChannel channel = reply.getReply().getSlackChannel();

					// send the message to this channel
					session.sendMessage(channel, event.getMessageContent() + " toi même ", null);
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
