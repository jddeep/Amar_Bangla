package com.example.android.amar_bangla;

public class Word {
    private String mDefaulttranslation;
    private String mBengalitranslation;
    private int mAudioResourceId;
    /** Image resource ID for the word */
    private int mImageResourceId = NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED=-1;
    public Word(String Bengali,String Default,int audioid)
    {
        mDefaulttranslation=Default;
        mBengalitranslation=Bengali;
        mAudioResourceId=audioid;
    }
    public Word(String Bengali,String Default,int imgId,int audioid)
    {
        mDefaulttranslation=Default;
        mBengalitranslation=Bengali;
        mImageResourceId=imgId;
        mAudioResourceId=audioid;
    }
    public String getmDefaulttranslation()
    {
        return mDefaulttranslation;

    }
    public String getmBengalitranslation()
    {
        return mBengalitranslation;
    }
    public int getmImageResourceId()
    {
        return mImageResourceId;
    }
    public int getmAudioResourceId(){return mAudioResourceId;}
    /**
     * Returns whether or not there is an image for this word.
     */
    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }
}