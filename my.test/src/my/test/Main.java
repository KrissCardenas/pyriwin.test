package my.test;

import java.io.IOException;

import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackMessageHandle;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.ReactionAdded;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.ReactionAddedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import com.ullink.slack.simpleslackapi.replies.SlackChannelReply;

public class Main {

	public static void main(String[] args) {
		System.out.println("Hello World");

		SlackSession session = SlackSessionFactory
				.createWebSocketSlackSession("token_here");
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

			session.addReactionAddedListener(new ReactionAddedListener() {

				public void onEvent(ReactionAdded pReaction, SlackSession pSession) {
					System.out.println("Hello" + pReaction.getEmojiName());
					SlackUser lUser = pReaction.getUser();
					
					SlackMessageHandle<SlackChannelReply> reply = pSession.openDirectMessageChannel(lUser);

					// get the channel
					SlackChannel channel = reply.getReply().getSlackChannel();

					SlackAttachment lAttachment = new SlackAttachment("title", "fallback", "test", "pretext");
					// send the message to this channel
					pSession.sendMessage(channel, pReaction.getEmojiName() + " yourself ", null);
					
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
