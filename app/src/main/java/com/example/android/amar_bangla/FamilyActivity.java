package com.example.android.amar_bangla;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private AudioManager mAudioManager;
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focuschange) {
            if (focuschange == AudioManager.AUDIOFOCUS_GAIN) {
                mediaPlayer.start();
            } else if (focuschange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focuschange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            } else if (focuschange == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
            }

        }
    };
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

// Create a list of words
        final ArrayList<Word> fams = new ArrayList<Word>();
        fams.add(new Word("Baba", "Father",R.drawable.family_father,R.raw.family_father));
        fams.add(new Word("Maa", "Mother",R.drawable.family_mother,R.raw.family_mother));
        fams.add(new Word("Dadu", "Grandfather",R.drawable.family_grandfather,R.raw.family_grandfather));
        fams.add(new Word("Thakuma", "Grandmother",R.drawable.family_grandmother,R.raw.family_grandmother));
        fams.add(new Word("Chele", "Son",R.drawable.family_son,R.raw.family_son));
        fams.add(new Word("Mei", "Duaghter",R.drawable.family_daughter,R.raw.family_daughter));
        fams.add(new Word("Bhagna", "Nephew",R.drawable.family_older_brother,R.raw.family_older_brother));
        fams.add(new Word("Bhai", "Brother",R.drawable.family_younger_brother,R.raw.family_younger_brother));
        fams.add(new Word("Bon", "Sister",R.drawable.family_younger_sister,R.raw.family_younger_sister));

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        Word_Adapter adapter = new Word_Adapter(this, fams,R.color.category_family, mAudioManager);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        ListView listView = (ListView) findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                releaseMediaPlayer();
                Word word = fams.get(position);
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer = MediaPlayer.create(FamilyActivity.this, word.getmAudioResourceId());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mOnCompletionListener);
                }
            }
        });
        /**
         * Clean up the media player by releasing its resources.
         */

    }
    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}