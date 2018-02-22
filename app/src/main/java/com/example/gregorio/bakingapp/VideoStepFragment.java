package com.example.gregorio.bakingapp;

import static com.example.gregorio.bakingapp.DetailActivity.DESCRIPTION_KEY_BUNDLE;
import static com.example.gregorio.bakingapp.DetailActivity.RECIPE_NAME_KEY;
import static com.example.gregorio.bakingapp.DetailActivity.STEPS_ARRAY_LIST_KEY;
import static com.example.gregorio.bakingapp.DetailActivity.STEPS_SIZE_BUNDLE;
import static com.example.gregorio.bakingapp.DetailActivity.VIDEO_ID_BUNDLE;
import static com.example.gregorio.bakingapp.DetailActivity.VIDEO_KEY_BUNDLE;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.gregorio.bakingapp.retrofit.Steps;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;

/**
 * Created by Gregorio on 05/12/2017.
 */

public class VideoStepFragment extends Fragment implements ExoPlayer.EventListener {

  private static final String LOG_TAG = VideoStepFragment.class.getSimpleName();
  private static final String VIDEO_URL_KEY = "Video Url Key";
  private static final String DESCRIPTION_KEY = "Description Key";
  private static final String CURRENT_PLAYER_POSITION_KEY = "Current Player Position Key";
  private static final String CURRENT_STEP_SIZE_KEY = "Current Step Size Position Key";
  private static final String CURRENT_STEP_POSITION_KEY = "Current Step Position Key";
  private static final String CURRENT_RECIPE_NAME_KEY = "Current Recipe Name Key";
  private static final String STEP_ARRAY_LIST_KEY = "Step Array List";
  private static final String PLAYER_STATE_KEY = "Exo Player State";


  private static MediaSessionCompat mMediaSession;
  private Context mContext;
  private SimpleExoPlayer mExoPlayer;
  private SimpleExoPlayerView mPlayerView;
  private TextView tvLongDescription;
  private Button previousStep;
  private Button nextStep;
  private String longDescription;
  private String videoUrl;
  private int stepId;
  private int stepsSize;
  private PlaybackStateCompat.Builder mStateBuilder;
  private ArrayList<Steps> stepsArrayList;
  private Uri videoUrlUri;
  private FrameLayout stepsFrame;
  private View rootView;
  private long currentPlayerPosition;
  private String recipeName;
  private boolean playWhenReady;


  public VideoStepFragment() {
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    //On Phone Rotation Restore the values saved in the savedInstanceSate bundle
    if (savedInstanceState != null) {

      currentPlayerPosition = savedInstanceState.getLong(CURRENT_PLAYER_POSITION_KEY);
      stepsArrayList = savedInstanceState.getParcelableArrayList(STEP_ARRAY_LIST_KEY);
      stepId = savedInstanceState.getInt(CURRENT_STEP_POSITION_KEY);

      Steps savedInstanceStep = stepsArrayList.get(stepId);
      videoUrl = savedInstanceStep.getVideoURL();
      longDescription = savedInstanceStep.getDescription();
      playWhenReady = savedInstanceState.getBoolean(PLAYER_STATE_KEY);

      recipeName = savedInstanceState.getString(RECIPE_NAME_KEY);
      stepsSize = savedInstanceState.getInt(CURRENT_STEP_SIZE_KEY);

      Log.i(LOG_TAG, "ON VIEW CREATE RESTORE STEP is: " + stepId);

    } else {

      Bundle bundle = this.getArguments();

      if (bundle != null) {
        //Retrieving the RecipeModel sent from the MainActivity Intent Bundle
        videoUrl = bundle.getString(VIDEO_KEY_BUNDLE);
        longDescription = bundle.getString(DESCRIPTION_KEY_BUNDLE);
        stepId = bundle.getInt(VIDEO_ID_BUNDLE);
        stepsSize = bundle.getInt(STEPS_SIZE_BUNDLE);
        stepsArrayList = bundle.getParcelableArrayList(STEPS_ARRAY_LIST_KEY);
        recipeName = bundle.getString(RECIPE_NAME_KEY);

        Log.i(LOG_TAG, "The Video Url is: " + videoUrl);
        Log.i(LOG_TAG, "The Step ID is: " + stepId);
        Log.i(LOG_TAG, "The Number of steps are: " + stepsSize);
        Log.i(LOG_TAG, "The Step Array List is : " + stepsArrayList);

      }
    }

    //inflating the ingredient fragment layout within its container in the activity_detail
    rootView = inflater.inflate(R.layout.fragment_video_description, container, false);
    // Initialize the player view.
    mPlayerView = rootView.findViewById(R.id.exo_player);
    mContext = getActivity().getApplicationContext();
    //Initialize the UI elements
    tvLongDescription = rootView.findViewById(R.id.long_description);
    nextStep = rootView.findViewById(R.id.next_steps_btn);
    previousStep = rootView.findViewById(R.id.previous_steps_btn);

    //Setting the OnClickListeners on the Next and Previous Btns
    nextStep.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        recipeNextStep();
      }
    });
    previousStep.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        recipePreviousStep();
      }
    });

    //Setting the TextView with the Long Description
    tvLongDescription.setText(longDescription);
    //Parse the video url
    videoUrlUri = Uri.parse(videoUrl);

    //SetUp of the Exo Player
    if (mExoPlayer == null) {
      BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
      TrackSelection.Factory videoTrackSelectionFactory =
          new AdaptiveTrackSelection.Factory(bandwidthMeter);
      TrackSelector trackSelector =
          new DefaultTrackSelector(videoTrackSelectionFactory);
      //Create the player
      mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

    }
      //passing the video url to the exo player
      initializePlayer(videoUrlUri);

    return rootView;
  }

  //Get the next Video Step Data
  private void recipeNextStep() {

    int maxStepId = stepsSize - 1;
    if (stepId < maxStepId) {
      int index = ++stepId;
      Steps steps = stepsArrayList.get(index);
      setStep(steps);
    } else {
      stepId = 0;
      Steps steps = stepsArrayList.get(stepId);
      setStep(steps);
    }
  }

  //Get the previous Video Step Data
  private void recipePreviousStep() {
    int totStepNo = stepsSize - 1;
    if (stepId < totStepNo && stepId > 0) {
      int index = --stepId;
      Steps steps = stepsArrayList.get(index);
      setStep(steps);
    } else {
      Steps steps = stepsArrayList.get(stepId);
      setStep(steps);
    }
  }

  // Provide the step object to populate the UI
  private void setStep(Steps steps) {

    String stepDescription = steps.getDescription();
    String stepVideoUrl = steps.getVideoURL();
    //Setting the TextView with the Long Description
    tvLongDescription.setText(stepDescription);
    //Parse the video url
    Uri previousVideoUrl = Uri.parse(stepVideoUrl);
    initializePlayer(previousVideoUrl);
  }

  /**
   * Initialize ExoPlayer.
   *
   * @param mediaUri The URI of the sample to play.
   */
  private void initializePlayer(Uri mediaUri) {

//    if (mExoPlayer == null) {

      // 1. Create a default TrackSelector
      Handler mainHandler = new Handler();

      mPlayerView
          .setDefaultArtwork(
              BitmapFactory.decodeResource(getResources(), R.drawable.baking_app_logo));

//      BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

//      TrackSelection.Factory videoTrackSelectionFactory =
//          new AdaptiveTrackSelection.Factory(bandwidthMeter);
//
//      TrackSelector trackSelector =
//          new DefaultTrackSelector(videoTrackSelectionFactory);

//      // 2. Create the player
//      mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

      LoadControl loadControl = new DefaultLoadControl();

      //Setting the Player to a SimpleExoPlayerView
      mPlayerView.setPlayer(mExoPlayer);

      // Measures bandwidth during playback. Can be null if not required.
      DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();

      // Produces DataSource instances through which media data is loaded.
      DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
          Util.getUserAgent(getContext(), "BakingApp"), defaultBandwidthMeter);

      // Produces Extractor instances for parsing the media data.
      ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

      // This is the MediaSource representing the media to be played.
      MediaSource videoSource = new ExtractorMediaSource(mediaUri,
          dataSourceFactory, extractorsFactory, null, null);

      //if the player is not null go to saved position after rotation.
      if (mExoPlayer != null) {
        mExoPlayer.seekTo(currentPlayerPosition);
        // Prepare the player with the source.
        mExoPlayer.prepare(videoSource);
        mExoPlayer.setPlayWhenReady(true);
      }

    // Prepare the player with the source.
    mExoPlayer.prepare(videoSource);
    mExoPlayer.setPlayWhenReady(playWhenReady);
  }


  /**
   * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
   * and media controller.
   */
  private void initializeMediaSession() {

    // Create a MediaSessionCompat.
    mMediaSession = new MediaSessionCompat(mContext, LOG_TAG);

    // Enable callbacks from MediaButtons and TransportControls.
    mMediaSession.setFlags(
        MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
            MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

    // Do not let MediaButtons restart the player when the app is not visible.
    mMediaSession.setMediaButtonReceiver(null);

    // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
    mStateBuilder = new PlaybackStateCompat.Builder()
        .setActions(
            PlaybackStateCompat.ACTION_PLAY |
                PlaybackStateCompat.ACTION_PAUSE |
                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                PlaybackStateCompat.ACTION_PLAY_PAUSE);

    mMediaSession.setPlaybackState(mStateBuilder.build());

    // MySessionCallback has methods that handle callbacks from a media controller.
    // mMediaSession.setCallback(new MySessionCallback());

    // Start the Media Session since the activity is active.
    mMediaSession.setActive(true);

  }

  @Override
  public void onTimelineChanged(Timeline timeline, Object manifest) {

  }

  @Override
  public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

  }

  @Override
  public void onLoadingChanged(boolean isLoading) {

  }

  @Override
  public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {


  }

  @Override
  public void onRepeatModeChanged(int repeatMode) {

  }

  @Override
  public void onPlayerError(ExoPlaybackException error) {
  }

  @Override
  public void onPositionDiscontinuity() {
  }

  @Override
  public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
  }

  /**
   * Release ExoPlayer.
   */
  private void releasePlayer() {
    mExoPlayer.stop();
    mExoPlayer.release();
    mExoPlayer = null;
  }

  @Override
  public void onPause() {
    super.onPause();
    //save the current video player position to reinstate on phone rotation
    currentPlayerPosition = mExoPlayer.getCurrentPosition();
    playWhenReady = mExoPlayer.getPlayWhenReady();
    mExoPlayer.release();

  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    releasePlayer();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    //outState.putString(VIDEO_URL_KEY, videoUrl);
    //outState.putString(DESCRIPTION_KEY, longDescription);
    outState.putLong(CURRENT_PLAYER_POSITION_KEY, currentPlayerPosition);
    outState.putParcelableArrayList(STEP_ARRAY_LIST_KEY, stepsArrayList);
    outState.putString(CURRENT_RECIPE_NAME_KEY, recipeName);
    outState.putInt(CURRENT_STEP_POSITION_KEY, stepId);
    outState.putInt(CURRENT_STEP_SIZE_KEY, stepsSize);
    outState.putBoolean(PLAYER_STATE_KEY, playWhenReady);

    Log.d(LOG_TAG, "VideoFragment OnSaved Instance Url: " + videoUrl);
    Log.d(LOG_TAG,
        "VideoFragment OnSaved Instance Current Player Position: " + currentPlayerPosition);
    Log.d(LOG_TAG, "VideoFragment OnSaved Instance Steps Number: " + stepsSize);
  }

  /**
   * Media Session Callbacks, where all external clients control the player.
   */
  private class MySessionCallback extends MediaSessionCompat.Callback {

    @Override
    public void onPlay() {
      mExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onPause() {
      mExoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onSkipToPrevious() {
      mExoPlayer.seekTo(0);
    }
  }


}

