package cmg.gReason;

import java.io.File;

import cmg.gReason.inputs.drawio.DrawIOReader;

public class g2dt {
	
	static String inputFile = "";
	static String configFile ="";
	static boolean printUsage = false;
	
	
	private static String printUsage() {
		String s = "";
		s = "Usage: dtg [-options]\n" + 
				"where options are:\n" + 
				"    -f filename \t draw.io XML file \n" + 
				"    -c filename \t configuration file \n" + 
				"    -h \t\t\t prints this help \n";
		return(s);
	}
	
	private static void processArgs(String[] args) throws Exception {
       for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("-")) {
                char option = arg.charAt(1);
                switch (option) {
	                case 'f':
	                    if (i + 1 < args.length) {
	                        inputFile = args[i + 1];
	                        i++;
	                    } else {
	                    	printUsage = true;
		                	throw new Exception("Option -f requires a file name.");
	                    }
	                	break;
	            	case 'c':
	                    if (i + 1 < args.length) {
	                        configFile = args[i + 1];
	                        i++;
	                    } else {
	                    	printUsage = true;
		                	throw new Exception("Option -c requires a file name.");
	                    }
	                    break;
	            	case 'h':
	                    System.out.println(printUsage());          		
	                default:
                    	printUsage = true;
	                	throw new Exception("Unknown option: " + option);
                }
            } else {
            	printUsage = true;
                throw new Exception("Invalid argument: " + arg);
            }
        }
       
       if (args.length == 0) {
       		printUsage = true;
       		throw new Exception("No input and config files");
       }

       if (inputFile.isEmpty())
    	   throw new Exception("No input file.");
       if (configFile.isEmpty())
    	   throw new Exception("No config file.");       
       
       File inpF = new File(inputFile);
       if (!inpF.exists()) throw new Exception("Input file does not exist:" + inputFile);
       File conF = new File(configFile);
       if (!conF.exists()) throw new Exception("Configuration file does not exist:" + configFile);
	}
	
	

	public static void main(String[] args) {
	  DrawIOReader app = new DrawIOReader();
	  
	  try {
		  processArgs(args);
		  app.setInFile(inputFile);
		  app.getProperties(configFile);
	  } catch (Exception e1) {
		  System.err.println(e1.getMessage());
		  if (printUsage)
		  	System.out.println(printUsage());
          try {
			app.setInFile("./files/OrganizeTravel.xml");
			app.getProperties("./config/config.txt");
		  	} catch (Exception e) {
		  		System.out.println("Current directory:" + System.getProperty("user.dir"));
		  	}
	  }
	  app.processFile();
	}
}
