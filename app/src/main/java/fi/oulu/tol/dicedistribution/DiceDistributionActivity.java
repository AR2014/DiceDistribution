package fi.oulu.tol.dicedistribution;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DiceDistributionActivity extends Activity
{

    public static int thisRound=0;

    // The amount of rounds to cast the dices.
    public static final int ROUNDS = 100; //try values 100 and 1000

    // The delay between casts in milliseconds.
    public static final int ROUND_DELAY = 1000;

    // A TextView to display the current amount of casted rounds..
    private TextView roundView;

    // ProgressBars that display the current counts of various dice sums (2-12).
    private ProgressBar[] bars;

    // Array of counts storing the current counts of various dice sums (2-12).
    private int counts[];

    // The highest value of the counts array to be used for scaling the ProgressBars.
    private int maximumCount = 0;

    public void showRounds()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                roundView.setText(("Rounds: " + Integer.toString(thisRound + 1)));
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        createUI(); //creates UI with Java without XML
//        castDice();
        thread:thread.start();
    }

    private void createUI()
    {
        TableLayout tableLayout = new TableLayout(this);

        bars = new ProgressBar[11]; //used to display the count of each sum
        for (int i = 0; i < 11; i++)
        {
            TableRow tableRow = new TableRow(this);
            tableLayout.addView(tableRow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            TextView textView = new TextView(this);
            textView.setText(Integer.toString(i + 2));
            tableRow.addView(textView, new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            bars[i] = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
            bars[i].setMax(1);
            tableRow.addView(bars[i], new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
        }

        TableRow tableRow = new TableRow(this);
        tableLayout.addView(tableRow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        roundView = new TextView(this);
        roundView.setText("Rounds: 0"); //used to display the round number
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.span = 2;
        tableRow.addView(roundView, layoutParams);

        setContentView(tableLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
    //actual operation. Contains a loop that cast two dice, calculates sum, updated the progress bar and text view
    private void castDice()
    {
        counts = new int[11];
        Random random = new Random();
//        for (int i = 0; i < ROUNDS; i++)
//        {
             int die1 = random.nextInt(6);
             int die2 = random.nextInt(6);
             int sum = die1 + die2;
             counts[sum]++;
             if (counts[sum] > maximumCount)
                 maximumCount = counts[sum];

             for (int k = 0; k < 11; k++)
                {
                    bars[k].setMax(maximumCount);
                    bars[k].setProgress(counts[k]);
                }
             thisRound++;
             DiceDistributionActivity.this.showRounds();


//        }
    }
    // added code fragment
    private Thread thread = new Thread()
    {
        @Override
        public void run()
        {

            for (int i = 0; i < ROUNDS; i++) {
                try {
                    castDice();

                    Thread.sleep(ROUND_DELAY); //simulate long-running operation. Make app to display progress of casting

                } catch (InterruptedException ex) {
                    System.err.println("An InterruptedException was caught: " + ex.getMessage());

                }
            }
        };

    };
}

