package Main_and_Drawing;

import java.util.concurrent.Callable;

public class Command {
	Runnable runnable;
	public Command(Callable<Void> function){
	
	}
	
	public Command(Runnable method){
		runnable = method;
	}
	
	public void execute(){
		runnable.run();
	}
	
	
	
}
