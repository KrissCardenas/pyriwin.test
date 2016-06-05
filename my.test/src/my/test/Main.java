package my.test;

import java.io.IOException;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackFile;
import com.ullink.slack.simpleslackapi.SlackMessageHandle;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.PinAdded;
import com.ullink.slack.simpleslackapi.events.ReactionAdded;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.PinAddedListener;
import com.ullink.slack.simpleslackapi.listeners.ReactionAddedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import com.ullink.slack.simpleslackapi.replies.SlackChannelReply;

public class Main {

	public static final String BACK_TICK = "`";
	private static final String RANDOM_COMMAND = "random";
	private static final String TOPYEAR_COMMAND = "topyear";
	private static final String TOPMONTH_COMMAND = "topmonth";
	private static final String TOPWEEK_COMMAND = "topweek";
	public static final String HELP_COMMAND = "help";
	public static final String BOT_TOKEN = "TOKEN_HERE";
	public static final String BOT_NAME = "@saga";

	public static void main(String[] args) {

		SlackSession session = SlackSessionFactory.createWebSocketSlackSession(BOT_TOKEN);
		try {
			session.connect();

			MongoClient mongo = new MongoClient("localhost", 27017);
			DB db = mongo.getDB("sagabot");
			final DBCollection col = db.getCollection("posts");

			session.addMessagePostedListener(new SlackMessagePostedListener() {

				public void onEvent(SlackMessagePosted pEvent, SlackSession pSession) {

					SlackChannel lChannel = pEvent.getChannel();

					if (lChannel.isDirect()) {
						mManageDirectMessages(pEvent, pSession);
					} else {
						mManageGenericsMsgs(pEvent, pSession);
					}
				}

			});

			session.addReactionAddedListener(new ReactionAddedListener() {

				public void onEvent(ReactionAdded pReaction, SlackSession pSession) {

					updateDBObject(col, pReaction.getFileID(), pReaction.getEmojiName());

				}
			});

			session.addPinAddedListener(new PinAddedListener() {

				public void onEvent(PinAdded event, SlackSession session) {
					// event.
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void mManageDirectMessages(SlackMessagePosted pEvent, SlackSession pSession) {

		SlackUser lUser = pEvent.getSender();
		if (lUser == null || (lUser != null && lUser.isBot())) {
			return;
		}

		// SlackChannel generalChannel = pSession.findChannelByName("general");

		String lMessageContent = pEvent.getMessageContent();
		SlackMessageHandle<SlackChannelReply> lReply = pSession.openDirectMessageChannel(lUser);
		SlackChannel lReplyingChannel = lReply.getReply().getSlackChannel();

		if (lMessageContent.contains(HELP_COMMAND)) {
			String lResponse = "*Hi, this is how you can interact with me*";
			String lText = "To capture a memory upload a file in channel, then add @saga in the file title. It will be added automatically to your company SAGA page.";
			String lText2 = "You can, also send me private commands. Here write `topweek` `topmonth` `topyear` to list the top memories or you can write `random` to receive a random memory.";

			SlackAttachment lAttachment = new SlackAttachment(lText, null, null, null);

			pSession.sendMessage(lReplyingChannel, lResponse, lAttachment);
			pSession.sendMessage(lReplyingChannel, lText2, null);

		} else if (lMessageContent.contains(TOPWEEK_COMMAND)) {

			// TODO : SHOW THE WEEK'S TOP 5

			pSession.sendMessage(lReplyingChannel, "*Here are your company's top5 memories this week.*", null);

			for (int i = 0; i < 5; i++) {
				String lPostTitle = "Generic Title" + i;
				String lPostURLOnSaga = "https://files.slack.com/files-pri/T1DRWCVEH-F1E7WCC8Z/download/blessures-du-furet.jpg";// "localhost:8080/postid";
				Integer lNbVotes = 1000 - (i + 1) * 100;

				SlackAttachment lAttachment = new SlackAttachment("See the post here: " + lPostURLOnSaga, null, null,
						null);

				String lResponse = (i + 1) + "- " + lPostTitle + " with " + lNbVotes + " votes.";
				pSession.sendMessage(lReplyingChannel, lResponse, lAttachment);
			}

		} else if (lMessageContent.contains(TOPMONTH_COMMAND)) {

			// TODO : SHOW THE MONTH'S TOP 5

			pSession.sendMessage(lReplyingChannel, "*Here are your company's top5 memories this month.*", null);

			for (int i = 0; i < 5; i++) {
				String lPostTitle = "Generic Title" + i;
				String lPostURLOnSaga = "https://files.slack.com/files-pri/T1DRWCVEH-F1E7WCC8Z/download/blessures-du-furet.jpg";// "localhost:8080/postid";
				Integer lNbVotes = 1000 - (i + 1) * 100;

				SlackAttachment lAttachment = new SlackAttachment("See the post here: " + lPostURLOnSaga, null, null,
						null);

				String lResponse = (i + 1) + "- " + lPostTitle + " with " + lNbVotes + " votes.";
				pSession.sendMessage(lReplyingChannel, lResponse, lAttachment);
			}

		} else if (lMessageContent.contains(TOPYEAR_COMMAND)) {

			// TODO : SHOW THE YEAR'S TOP 5
			pSession.sendMessage(lReplyingChannel, "*Here are your company's top5 memories this year.*", null);

			for (int i = 0; i < 5; i++) {
				String lPostTitle = "Generic Title" + i;
				String lPostURLOnSaga = "https://files.slack.com/files-pri/T1DRWCVEH-F1E7WCC8Z/download/blessures-du-furet.jpg";// "localhost:8080/postid";
				Integer lNbVotes = 1000 - (i + 1) * 100;

				SlackAttachment lAttachment = new SlackAttachment("See the post here: " + lPostURLOnSaga, null, null,
						null);

				String lResponse = (i + 1) + "- " + lPostTitle + " with " + lNbVotes + " votes.";
				pSession.sendMessage(lReplyingChannel, lResponse, lAttachment);
			}

		} else if (lMessageContent.contains(RANDOM_COMMAND)) {
			// TODO : POST A RANDOM POST

			String lPostTitle = "Generic Random Title";

			String lResponse = "*Here's a random memory!!!*";
			String lPrefix = "Memory from 5 years ago: " + lPostTitle;
			String lPostURLOnSaga = "https://files.slack.com/files-pri/T1DRWCVEH-F1E7WCC8Z/download/blessures-du-furet.jpg";// "localhost:8080/postid";

			SlackAttachment lAttachment = new SlackAttachment("See the post here: " + lPostURLOnSaga, null, null,
					lPrefix);
			pSession.sendMessage(lReplyingChannel, lResponse, lAttachment);

		} else {

			pSession.sendMessage(lReplyingChannel, "Type `help` to know how to interact with me", null);
		}

	}

	private static DBObject createDBObject(String photoId, String name, String channel, String url, String title,
			String comments) {

		BasicDBObjectBuilder docBuilder = BasicDBObjectBuilder.start();

		docBuilder.append("photoId", photoId);
		docBuilder.append("name", name);
		docBuilder.append("channel", channel);
		docBuilder.append("url", url);
		docBuilder.append("title", title);
		docBuilder.append("comments", comments);
		BasicDBList dbl = new BasicDBList();
		docBuilder.append("reactions", dbl);
		return docBuilder.get();
	}

	private static void mManageGenericsMsgs(SlackMessagePosted pEvent, SlackSession pSession) {

		SlackUser lUser = pEvent.getSender();
		if (lUser == null || (lUser != null && lUser.isBot())) {
			return;
		}

		// SlackChannel generalChannel = pSession.findChannelByName("general");

		String lMessageContent = pEvent.getMessageContent();

		if (lMessageContent.contains(BOT_NAME)) {

			SlackFile lUploadedFile = pEvent.getSlackFile();
			if (lUploadedFile == null) {

				return;
			} else {

				String realName = lUser.getRealName();
				lUploadedFile.getPermalinkPublic();
				System.out.println(lUploadedFile.getId());
				// if(!chObj.isDirect())
				// channelName = chObj.getName();
				// col.insert(createDBObject(event.getSlackFile().getId(),
				// realName, channelName,
				// event.getSlackFile().getUrlPrivate(),
				// event.getSlackFile().getTitle(),
				// event.getSlackFile().getComment()));

				SlackAttachment lAttachment = new SlackAttachment(lUploadedFile.getUrlPrivateDownload(), null,
						"Don't forget to react in order to make it last forever. :elephant:", null);
				pSession.sendMessage(pEvent.getChannel(),
						"*A new memory was added by " + realName + " to your company's page *", lAttachment);
			}
		}
	}

	private static void updateDBObject(DBCollection col, String photoId, String reaction) {

		BasicDBObject query = new BasicDBObject();
		query.put("photoId", photoId);

		BasicDBObject update = new BasicDBObject();
		update.put("$push", new BasicDBObject("reactions", reaction));

		col.update(query, update);
	}

}
