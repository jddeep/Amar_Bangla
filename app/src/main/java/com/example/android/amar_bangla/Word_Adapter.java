package com.example.android.amar_bangla;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * {@link Word_Adapter} is an {@link ArrayAdapter} that can provide the layout for each list item
 * based on a data source, which is a list of {@link Word} objects.
 */
public class Word_Adapter extends ArrayAdapter<Word> {
    private int mColorResourceId;
    private MediaPlayer mediaPlayer;
    private ArrayList<Word> words;
    private AudioManager mAudioManager;
    private Word_Adapter adapter;
    /**
     * Create a new {@link Word_Adapter} object.
     *
     * @param context is the current context (i.e. Activity) that the adapter is being created in.
     * @param words is the list of {@link Word}s to be displayed.
     */
    public Word_Adapter(Context context, ArrayList<Word> words, int colorId, AudioManager audiomanager) {
        super(context, 0, words);
        mColorResourceId=colorId;
        this.words = words;
        mAudioManager = audiomanager;
    }
    private class ViewHolder{
        public FloatingActionButton floatingActionButton;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder holder;
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.words_list, parent, false);
            holder = new ViewHolder();
            FloatingActionButton fab = listItemView.findViewById(R.id.fab);
            holder.floatingActionButton = fab;
            holder.floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getContext(), "Button clicked", Toast.LENGTH_SHORT).show();
                    releaseMediaPlayer();
                    makeSound(words, position);

                }
            });
            listItemView.setTag(holder);

        }else {
            holder = (ViewHolder) listItemView.getTag();
        }

        // Get the {@link Word} object located at this position in the list
        Word currentWord = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID miwok_text_view.
        TextView miwokTextView = (TextView) listItemView.findViewById(R.id.Bangla_text_view);
        // Get the Miwok translation from the currentWord object and set this text on
        // the Miwok TextView.
        miwokTextView.setText(currentWord.getmBengalitranslation());

        // Find the TextView in the list_item.xml layout with the ID default_text_view.
        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.default_text_view);
        // Get the default translation from the currentWord object and set this text on
        // the default TextView.
        defaultTextView.setText(currentWord.getmDefaulttranslation());

        ImageView iconview = (ImageView) listItemView.findViewById(R.id.img1);
        if(currentWord.hasImage()) {
            iconview.setImageResource(currentWord.getmImageResourceId());
            iconview.setVisibility(View.VISIBLE);
        }
        if(currentWord.hasImage()==false) {
            iconview.setVisibility(View.GONE);
        }
        // Set the theme color for the list item
        View textContainer = listItemView.findViewById(R.id.list_1);
        // Find the color that the resource ID maps to
        int color = ContextCompat.getColor(getContext(), mColorResourceId);
        // Set the background color of the text container View
        textContainer.setBackgroundColor(color);




        // Return the whole list item layout (containing 2 TextViews) so that it can be shown in
        // the ListView.
        return listItemView;
    }

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


    public void releaseMediaPlayer() {
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

    public void makeSound(ArrayList<Word> words, int position){
        Word word = words.get(position);
        // Request audio focus so in order to play the audio file. The app needs to play a
        // short audio file, so we will request audio focus with a short amount of time
        // with AUDIOFOCUS_GAIN_TRANSIENT.
        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mediaPlayer = MediaPlayer.create(getContext(), word.getmAudioResourceId());
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mOnCompletionListener);
        }
    }


}