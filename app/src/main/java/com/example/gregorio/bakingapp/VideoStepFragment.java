package com.example.gregorio.bakingapp;

import static com.example.gregorio.bakingapp.DetailActivity.CURRENT_VIDEO_BUNDLE_KEY;
import static com.example.gregorio.bakingapp.DetailActivity.CURRENT_VIDEO_DESCRIPTION_BUNDLE_KEY;
import static com.example.gregorio.bakingapp.DetailActivity.CURRENT_VIDEO_ID_BUNDLE_KEY;
import static com.example.gregorio.bakingapp.DetailActivity.CURRENT_VIDEO_PLAYER_POSITION_BUNDLE_KEY;
import static com.example.gregorio.bakingapp.DetailActivity.CURRENT_VIDEO_READY_TO_PLAY_BUNDLE_KEY;
import static com.example.gregorio.bakingapp.DetailActivity.CURRENT_VIDEO_URL_BUNDLE_KEY;
import static com.example.gregorio.bakingapp.DetailActivity.DESCRIPTION_KEY_BUNDLE;
import static com.example.gregorio.bakingapp.DetailActivity.RECIPE_NAME_KEY;
import static com.example.gregorio.bakingapp.DetailActivity.START_VIDEO_BUNDLE_KEY;
import static com.example.gregorio.bakingapp.DetailActivity.STEPS_ARRAY_LIST_KEY;
import static com.example.gregorio.bakingapp.DetailActivity.STEPS_SIZE_BUNDLE;
import static com.example.gregorio.bakingapp.DetailActivity.VIDEO_ID_BUNDLE;
import static com.example.gregorio.bakingapp.DetailActivity.VIDEO_KEY_BUNDLE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;
import com.example.gregorio.bakingapp.retrofit.Steps;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
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
import com.squareup.picasso.Picasso;
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
  private static final String VIDEO_STEP_KEY = "Video Step Key";

  private static final String STEP_DESCRIPTION_KEY = "Step Description Key";
  private static final String CURRENT_VIDEO_URL_KEY = "Current Video Url Key";

  private static MediaSessionCompat mMediaSession;
  public OnCurrentVideoListener mCallbackHostActivity3;
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
  private ImageView stepImage;
  private String thumbnailUrl;
  private Steps steps;
  private Bundle startVideoBundle;
  private Bundle currentVideoBundle;
  private Integer thumbnail;
  private Drawable drawable;

  public VideoStepFragment() {
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {


    //inflating the ingredient fragment layout within its container in the activity_detail
    rootView = inflater.inflate(R.layout.fragment_video_description, container, false);
    // Initialize the player view.
    mPlayerView = rootView.findViewById(R.id.exo_player);
    mContext = getActivity().getApplicationContext();
    //Initialize the UI elements
    tvLongDescription = rootView.findViewById(R.id.long_description);
    nextStep = rootView.findViewById(R.id.next_steps_btn);
    previousStep = rootView.findViewById(R.id.previous_steps_btn);
    stepImage = rootView.findViewById(R.id.recipe_step_image);

    return rootView;
  }


  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    Bundle bundle = this.getArguments();

    if (bundle != null) {

      startVideoBundle = bundle.getBundle(START_VIDEO_BUNDLE_KEY);
      currentVideoBundle = bundle.getBundle(CURRENT_VIDEO_BUNDLE_KEY);

      if (startVideoBundle != null) {

        //Retrieving the RecipeModel sent from the MainActivity Intent Bundle
        videoUrl = startVideoBundle.getString(VIDEO_KEY_BUNDLE);
        longDescription = startVideoBundle.getString(DESCRIPTION_KEY_BUNDLE);
        currentPlayerPosition = startVideoBundle.getLong(CURRENT_VIDEO_PLAYER_POSITION_BUNDLE_KEY);
        stepId = startVideoBundle.getInt(VIDEO_ID_BUNDLE);
        stepsSize = startVideoBundle.getInt(STEPS_SIZE_BUNDLE);
        stepsArrayList = startVideoBundle.getParcelableArrayList(STEPS_ARRAY_LIST_KEY);
        recipeName = startVideoBundle.getString(RECIPE_NAME_KEY);

        if (stepsArrayList != null) {
          Steps step = stepsArrayList.get(stepId);
          thumbnailUrl = step.getThumbnailURL();
          setThumbnail(thumbnailUrl);

        }

        Log.d(LOG_TAG,
            " TEST *** onActivityCreated (startVideoBundle != null) The Video Url is: " + videoUrl);
        Log.d(LOG_TAG,
            " TEST *** onActivityCreated (startVideoBundle != null) The Step ID is: " + stepId);
        Log.d(LOG_TAG,
            " TEST *** onActivityCreated (startVideoBundle != null) The Description is: "
                + longDescription);
        Log.d(LOG_TAG,
            " TEST *** onActivityCreated (startVideoBundle != null) The current position is: "
                + currentPlayerPosition);
        Log.d(LOG_TAG,
            " TEST *** onActivityCreated (startVideoBundle != null) The Number of steps are: "
                + stepsSize);
        Log.d(LOG_TAG,
            " TEST *** onActivityCreated (startVideoBundle != null) The Step Array List is : "
                + stepsArrayList);

      } else if (currentVideoBundle != null) {

        //Retrieving the RecipeModel sent from the MainActivity Intent Bundle
        videoUrl = currentVideoBundle.getString(CURRENT_VIDEO_URL_BUNDLE_KEY);
        longDescription = currentVideoBundle.getString(CURRENT_VIDEO_DESCRIPTION_BUNDLE_KEY);
        currentPlayerPosition = currentVideoBundle
            .getLong(CURRENT_VIDEO_PLAYER_POSITION_BUNDLE_KEY);
        stepId = currentVideoBundle.getInt(CURRENT_VIDEO_ID_BUNDLE_KEY);
        currentPlayerPosition = currentVideoBundle
            .getLong(CURRENT_VIDEO_PLAYER_POSITION_BUNDLE_KEY);
        playWhenReady = currentVideoBundle.getBoolean(CURRENT_VIDEO_READY_TO_PLAY_BUNDLE_KEY);
        stepsArrayList = currentVideoBundle.getParcelableArrayList(STEPS_ARRAY_LIST_KEY);
        stepsSize = stepsArrayList.size();

        Log.d(LOG_TAG,
            " TEST *** onActivityCreated (currentVideoBundle != null) The videoUrl ID is: "
                + videoUrl);
        Log.d(LOG_TAG,
            " TEST *** onActivityCreated (currentVideoBundle != null) The longDescription ID is: "
                + longDescription);
        Log.d(LOG_TAG,
            " TEST *** onActivityCreated (currentVideoBundle != null) The stepId is: " + stepId);

        setThumbnail(thumbnailUrl);

      }

    }


    //On Phone Rotation Restore the values saved in the savedInstanceSate bundle
    if (savedInstanceState != null) {

      currentPlayerPosition = savedInstanceState.getLong(CURRENT_PLAYER_POSITION_KEY);
      stepsArrayList = savedInstanceState.getParcelableArrayList(STEP_ARRAY_LIST_KEY);
      stepId = savedInstanceState.getInt(CURRENT_STEP_POSITION_KEY);
      steps = savedInstanceState.getParcelable(VIDEO_STEP_KEY);

      if (stepsArrayList != null) {
        Steps savedInstanceStep = stepsArrayList.get(stepId);
        videoUrl = savedInstanceStep.getVideoURL();
        longDescription = savedInstanceStep.getDescription();
        thumbnailUrl = savedInstanceStep.getThumbnailURL();
      }

      playWhenReady = savedInstanceState.getBoolean(PLAYER_STATE_KEY);
      recipeName = savedInstanceState.getString(RECIPE_NAME_KEY);
      stepsSize = savedInstanceState.getInt(CURRENT_STEP_SIZE_KEY);

      Log.d(LOG_TAG, "TEST *** onActivityCreated (savedInstanceState != null) STEP is: " + stepId);
      Log.d(LOG_TAG,
          "TEST *** onActivityCreated (savedInstanceState != null) videoUrl is: " + videoUrl);
      Log.d(LOG_TAG, "TEST *** onActivityCreated (savedInstanceState != null) longDescription is: "
          + longDescription);
      Log.d(LOG_TAG, "TEST *** onActivityCreated (savedInstanceState != null) steps is: "
          + steps);

      //if the image is available set the thumbnail else set a place holder image
      setThumbnail(thumbnailUrl);
    }

    //SetUp of the Exo Player
    if (mExoPlayer == null) {

      mPlayerView
          .setDefaultArtwork(
              BitmapFactory.decodeResource(getResources(), R.drawable.baking_app_logo));
      BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
      TrackSelection.Factory videoTrackSelectionFactory =
          new AdaptiveTrackSelection.Factory(bandwidthMeter);
      TrackSelector trackSelector =
          new DefaultTrackSelector(videoTrackSelectionFactory);
      //Create the player
      mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

    }

    //this interface call back the Detail Activity passing the current data to load in the Media player
    mCallbackHostActivity3
        .onCurrentVideoPlaying(stepId, currentPlayerPosition, playWhenReady, longDescription,
            videoUrl);

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

    tvLongDescription.setText(longDescription);
    //passing the video url to the exo player
    if (videoUrl != null && !videoUrl.isEmpty()) {
      //Parse the video url
      videoUrlUri = Uri.parse(videoUrl);
    } else {
      videoUrlUri = Uri.parse("");
    }

    initializePlayer(videoUrlUri);

  }


  //Load the image is the Url is not empty
  private void setThumbnail(String thumbnailUrl) {

    if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
      Picasso.with(stepImage.getContext()).load(thumbnailUrl).into(stepImage);
    } else {
      thumbnail = R.drawable.coming_soon;
      Picasso.with(stepImage.getContext()).load(thumbnail).placeholder(R.drawable.coming_soon)
          .into(stepImage);
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    // This makes sure that the host activity has implemented the callback interface
    // If not, it throws an exception
    try {
      mCallbackHostActivity3 = (OnCurrentVideoListener) context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString()
          + " must implement OnCurrentVideoListener");
    }
  }

  //Get the next Video Step Data
  private void recipeNextStep() {

    if (stepsArrayList != null) {
      int maxStepId = stepsSize - 1;
      if (stepId < maxStepId) {
        int index = ++stepId;
        steps = stepsArrayList.get(index);
        setStep(steps);
        Log.d(LOG_TAG, "recipeNextStep() stepId =  " + stepId);
      } else {
        stepId = 0;
        steps = stepsArrayList.get(stepId);
        setStep(steps);
      }
    }

  }

  //Get the previous Video Step Data
  private void recipePreviousStep() {

    if (stepsArrayList != null) {
      int totStepNo = stepsSize - 1;
      if (stepId < totStepNo && stepId > 0) {
        int index = --stepId;
        steps = stepsArrayList.get(index);
        setStep(steps);
      } else {
        steps = stepsArrayList.get(stepId);
        setStep(steps);
      }
    }

  }

  // Provide the step object to populate the UI
  private void setStep(Steps steps) {
    longDescription = steps.getDescription();
    videoUrl = steps.getVideoURL();
    //Setting the TextView with the Long Description
    tvLongDescription.setText(longDescription);
    //Parse the video url
    Uri currentVideoUri = Uri.parse(videoUrl);
    initializePlayer(currentVideoUri);
  }

  /**
   * Initialize ExoPlayer.
   *
   * @param mediaUri The URI of the sample to play.
   */
  private void initializePlayer(Uri mediaUri) {

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
        mExoPlayer.setPlayWhenReady(playWhenReady);
      }

    // Prepare the player with the source.
    mExoPlayer.seekTo(currentPlayerPosition);
    mExoPlayer.prepare(videoSource);
    mExoPlayer.setPlayWhenReady(playWhenReady);
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

    outState.putLong(CURRENT_PLAYER_POSITION_KEY, currentPlayerPosition);
    outState.putParcelableArrayList(STEP_ARRAY_LIST_KEY, stepsArrayList);
    outState.putString(CURRENT_RECIPE_NAME_KEY, recipeName);
    outState.putInt(CURRENT_STEP_POSITION_KEY, stepId);
    outState.putInt(CURRENT_STEP_SIZE_KEY, stepsSize);
    outState.putBoolean(PLAYER_STATE_KEY, playWhenReady);
    outState.putString(VIDEO_URL_KEY, videoUrl);
    outState.putString(DESCRIPTION_KEY, longDescription);
    outState.putParcelable(VIDEO_STEP_KEY, steps);

    Log.d(LOG_TAG, "onSaveInstanceState(Bundle outState) videoUrl is = " + videoUrl);
    Log.d(LOG_TAG, "onSaveInstanceState(Bundle outState) stepsSize = " + stepsSize);
    Log.d(LOG_TAG, "onSaveInstanceState(Bundle outState) stepId = " + stepId);
  }

  /**
   * **** NOT IMPLEMENTED ****
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

  // OnCurrentVideoListener interface, calls a method in the host activity named onCurrentVideoPlaying
  public interface OnCurrentVideoListener {

    void onCurrentVideoPlaying(int stepId, long currentPlayerPosition, boolean playWhenReady,
        String stepDescription, String videoUrl);
  }

  /**
   * **** NOT IMPLEMENTED ****
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

