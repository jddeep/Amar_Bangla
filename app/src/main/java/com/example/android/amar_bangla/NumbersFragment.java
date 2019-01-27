package com.example.android.amar_bangla;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class NumbersFragment extends Fragment {
    private MediaPlayer mediaPlayer;
    private AudioManager mAudioManager;
    private Word_Adapter adapter;


    public NumbersFragment() {
        // Required empty public constructor
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
//
//
//    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
//        @Override
//        public void onCompletion(MediaPlayer mediaPlayer) {
//            releaseMediaPlayer();
//        }
//    };





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.list_view, container, false);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);



        // Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("Aak", "One",R.drawable.number_one,R.raw.number_one));
        words.add(new Word("Dui", "Two",R.drawable.number_two,R.raw.number_two));
        words.add(new Word("Teen", "Three",R.drawable.number_three,R.raw.number_three));
        words.add(new Word("Chaar", "Four",R.drawable.number_four,R.raw.number_four));
        words.add(new Word("Paanch", "Five",R.drawable.number_five,R.raw.number_five));
        words.add(new Word("Choye", "Six",R.drawable.number_six,R.raw.number_six));
        words.add(new Word("Saat", "Seven",R.drawable.number_seven,R.raw.number_seven));
        words.add(new Word("Aatt", "Eight",R.drawable.number_eight,R.raw.number_eight));
        words.add(new Word("Noe", "Nine",R.drawable.number_nine,R.raw.number_nine));
        words.add(new Word("Doss", "Ten",R.drawable.number_ten,R.raw.number_ten));

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        adapter = new Word_Adapter(getActivity(), words,R.color.category_numbers, mAudioManager);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        ListView listView = (ListView) rootview.findViewById(R.id.list);



        listView.setAdapter(adapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                releaseMediaPlayer();
//                makeSound(words, position);
//
//            }
//        });


        return rootview;
    }

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
//
//    public void makeSound(ArrayList<Word> words, int position){
//        Word word = words.get(position);
//        // Request audio focus so in order to play the audio file. The app needs to play a
//        // short audio file, so we will request audio focus with a short amount of time
//        // with AUDIOFOCUS_GAIN_TRANSIENT.
//        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
//                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
//        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//            mediaPlayer = MediaPlayer.create(getActivity(), word.getmAudioResourceId());
//            mediaPlayer.start();
//            mediaPlayer.setOnCompletionListener(mOnCompletionListener);
//        }
//    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}