package couk.Adamki11s.Parser;

import couk.Adamki11s.Threads.RAServer;

public class ActionParser {
	
	public static void parseAction(String action, RAServer server){
		System.out.println("Checking action");
		if(action.startsWith("AUTH:")){
			System.out.println("Parsing auth");
			AuthenticationDelegate.parseAuthentication(action, server);
			return;
		}
	}

}
