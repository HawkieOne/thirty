
package se.umu.hali0151.thirtygame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * The main screen for the game
 * @author id18hll
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Overrides onCreate and creates an Intent which starts the game
     * @param savedInstanceState Bundle of data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startGame = (Button) findViewById(R.id.startGame);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameStarted = new Intent(MainActivity.this, GameStarted.class);
                startActivity(gameStarted);
            }
        });
    }
}