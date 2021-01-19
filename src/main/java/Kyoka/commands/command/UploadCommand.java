package Kyoka.commands.command;

import java.io.File;
import java.util.List;

import Kyoka.commands.CommandContext;
import Kyoka.commands.ICommand;
import net.dv8tion.jda.api.entities.Message.Attachment;

public class UploadCommand implements ICommand{
	
	
	public boolean isSecret() {
		return true;
	}

	@Override
	public void handle(CommandContext ctx) {
		if(!Kyoka.utils.DataLoader.admin.contains(ctx.getAuthor().getIdLong())) {
			ctx.getChannel().sendMessage("You don't have enough permissions to do this").queue();
			return;
		}
		
		
		List<Attachment> lista= ctx.getMessage().getAttachments();
		
		if(lista.size()==1) {
		Attachment attachment = lista.get(0);
	     attachment.downloadToFile("tmp//" + attachment.getFileName())
         .thenAccept(file -> System.out.println("Saved attachment to " + file.getName()))
         .exceptionally(t ->
         { // handle failure
             t.printStackTrace();
             return null;
         });
	     Kyoka.utils.DataLoader.files.add(new File("tmp//" + attachment.getFileName()));
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "upload";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Alda1";
	}
	
	

}
