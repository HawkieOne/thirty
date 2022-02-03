
package se.umu.hali0151.thirtygame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * The activity which shows the results of the game
 * @author id18hll
 * @version 1.0
 */
public class GameResults extends AppCompatActivity {

    /**
     * Overrides onCreate and shows the result of the game
     * @param savedInstanceState Bundle of data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_results);

        ArrayList<Round> rounds = getIntent().getParcelableArrayListExtra("rounds");

        TextView[] roundTextView = new TextView[]{
                (TextView)findViewById(R.id.round1Results),
                (TextView)findViewById(R.id.round2Results),
                (TextView)findViewById(R.id.round3Results),
                (TextView)findViewById(R.id.round4Results),
                (TextView)findViewById(R.id.round5Results),
                (TextView)findViewById(R.id.round6Results),
                (TextView)findViewById(R.id.round7Results),
                (TextView)findViewById(R.id.round8Results),
                (TextView)findViewById(R.id.round9Results),
                (TextView)findViewById(R.id.round10Results)
        };
        TextView totalScoreTextView = (TextView) findViewById(R.id.totalScore);
        Button newGameButton = (Button) findViewById(R.id.newGameButton);


        int totalScore = 0;
        for(int i = 0; i < rounds.size(); i++){
            roundTextView[i].setText(
                    MessageFormat.format("{0} {1}: Score {2} gave you {3} points",
                    getResources().getString(R.string.roundNumber), i + 1,
                     rounds.get(i).getTypeOfPoints(), rounds.get(i).getPoints()));
            totalScore += rounds.get(i).getPoints();
        }
        totalScoreTextView.setText(MessageFormat.format("You got a total score of {0} points", totalScore));

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}