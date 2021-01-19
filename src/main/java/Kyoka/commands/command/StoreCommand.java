package Kyoka.commands.command;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.bson.Document;

import Kyoka.commands.CommandContext;
import Kyoka.commands.ICommand;
import Kyoka.utils.DataLoader;
import Kyoka.utils.MongoDB;

public class StoreCommand implements ICommand{

	@Override
	public void handle(CommandContext ctx) {
		// TODO Auto-generated method stub
		long id = ctx.getAuthor().getIdLong();
		if(MongoDB.countItems(id, "bag")==0 || !MongoDB.userExists(ctx.getAuthor().getIdLong())) {
			ctx.getChannel().sendMessage("you have no items to sell right now").queue();
			return;
		}
		
		int index=0;
		try {
			
			index = Integer.parseInt(ctx.getArgs().get(0));
		}catch(Exception e) {
			ctx.getChannel().sendMessage("invalid item").queue();
			return;
		}
		if(index<1 || index>30 || index>MongoDB.countItems(id, "bag")) {
			ctx.getChannel().sendMessage("invalid item").queue();
			return;
		}
		
		
		Date one = MongoDB.getDoc(id).getDate("lastUpdate");
		
		long diffInMillies = Math.abs(new Date().getTime() - one.getTime());
		long diff = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		if(diff>=1) {
			if(MongoDB.countItems(id,"store")<10) {
				MongoDB.pushIntoArray("storage", id, MongoDB.getDoc(id).getList("bag", Document.class).get(index-1));
				MongoDB.removeFromArray(index-1, id, "bag");
				MongoDB.getUsers().findOneAndUpdate(new Document("_id", id), new Document("$set", new Document("lastUpdate", new Date())));
				ctx.getChannel().sendMessage("your item has been stored!").queue();
			}else {
				ctx.getChannel().sendMessage("your storage is full! you may only store 10 items at the time").queue();
			}
		}else {
			ctx.getChannel().sendMessage("you have to wait, you may only store 1 item every hour").queue();
		}
		
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "store";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "usage: "+DataLoader.getString("prefix")+getName()+" `ref` \n"+
				"you can find the reference with the inventory command";
	}

}
