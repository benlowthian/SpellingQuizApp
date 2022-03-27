package application;
/** Class for all festival bash calls
 * Includes default messages in english and Maori
 * **/
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class Festival {


    protected static String maori = "(voice_akl_mi_pk06_cg)";
    protected static String english = "(voice_cmu_us_awb_cg)";
    protected static String speed = "(Parameter.set 'Duration_Stretch 1.0)"; // default

    protected static String speed(double speed) {
        Festival.speed = "(Parameter.set 'Duration_Stretch " + String.valueOf(speed) + ")";

        return Festival.speed;
    }

    protected static String word(String word) {
        return "(SayText " + '"' + word + '"' + ")";
    }

    protected static void ttsMaori(double speed, String word, boolean isFirstWord) throws IOException, InterruptedException {
        word = word.toLowerCase();
        writeSchemeFile(Arrays.asList(maori, speed(speed), word(word)), ".maori.scm");
        
        if (!isFirstWord) {
        	Main.bashCmd("festival -b .maori.scm");
        } else {
        	ttsThread("festival -b .maori.scm"); // call in new thread to allow GUI to update scene
        }
    }

    protected static void ttsEnglish(double speed, String word) throws IOException {
        writeSchemeFile(Arrays.asList(english, speed(speed), word(word)), ".english.scm");
        Main.bashCmd("festival -b .english.scm");
    }

    //IMPORTANT
    private static void writeSchemeFile(List<String> list, String filename) throws IOException {
        FileWriter fw = new FileWriter(filename);

        for (String command : list) {
            fw.write(command + "\n");
        }

        fw.close();
    }
    
    private static void ttsThread(String cmd) throws InterruptedException {
		Thread taskThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// NOTE : test around with sleep time 
					Thread.sleep(250);
					Main.bashCmd(cmd);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		taskThread.start();
	}
}