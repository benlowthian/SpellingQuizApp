package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

// class providing functionality to the rewards screen in the app
public class RewardController {
	
	@FXML
	Label messageLabel;
	@FXML
	Label englishLabel;
	@FXML
	Label scoreLabel;
	
	// shows the score to the user as well as a message dependent on their score
	protected void setScore(double score) {
		scoreLabel.setText("Score = " + (double)score + " / 5.0");
		
		if ( score>=0 && score<1 ) {
			messageLabel.setText("Kia manawanui!");
			englishLabel.setText("Hang in there!");
			return;
		} else if (score>=1 && score<2) {
			messageLabel.setText("Okea ururoatia!");
			englishLabel.setText("Don't give up");
			return;
		} else if (score>=2 && score<3) {
			messageLabel.setText("Karawhiua!");
			englishLabel.setText("Give it heaps! Give it all you got!");
			return;
		} else if (score >= 3 && score<4) {
			messageLabel.setText("Kei reira!");
			englishLabel.setText("Right on! That's the one!");
			return;
		} else if (score >=4 && score <=5) {
			messageLabel.setText("Ka mau te wehi!");
			englishLabel.setText("That's outstanding!");
			return;
		}
		
	}
	
	// providing functionality to the play again button
	public void playAgain(ActionEvent event) throws IOException {
		Main.sceneSwitcher.loadTopicScene();
	}
	
	// providing functionality to the main menu button
	public void mainMenu(ActionEvent event) throws IOException {
		Main.sceneSwitcher.loadMenuScene();
	}
	
}
