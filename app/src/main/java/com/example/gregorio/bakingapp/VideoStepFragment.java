package com.example.gregorio.bakingapp;

import static com.example.gregorio.bakingapp.DetailActivity.DESCRIPTION_KEY_BUNDLE;
import static com.example.gregorio.bakingapp.DetailActivity.VIDEO_KEY_BUNDLE;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.gregorio.bakingapp.retrofit.Steps;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import java.util.ArrayList;

/**
 * Created by Gregorio on 05/12/2017.
 */

public class VideoStepFragment extends Fragment implements ExoPlayer.EventListener {

  private static final String LOG_TAG = VideoStepFragment.class.getSimpleName();
  private static MediaSessionCompat mMediaSession;
  private Context mContext;
  private SimpleExoPlayer mExoPlayer;
  private SimpleExoPlayerView mPlayerView;
  private TextView tvLongDescription;
  private Button previousStep;
  private Button nextStep;
  private String longDescription;
  private String videoUrl;
  private PlaybackStateCompat.Builder mStateBuilder;
  private ArrayList<Steps> stepsArrayList;

  public VideoStepFragment() {
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    Bundle bundle = this.getArguments();
    if (bundle != null) {
      //Retrieving the RecipeModel sent from the MainActivity Intent Bundle
      String videoUrl = bundle.getString(VIDEO_KEY_BUNDLE);
      longDescription = bundle.getString(DESCRIPTION_KEY_BUNDLE);
      Log.d(LOG_TAG, "The Video Url is: " + videoUrl);
      Log.d(LOG_TAG, "The Long Description is: " + longDescription);

    }

    mContext = getActivity().getApplicationContext();
    //inflating the ingredient fragment layout within its container in the activity_detail
    View rootView = inflater.inflate(R.layout.fragment_video_description, container, false);
    // Initialize the player view.
    mPlayerView = rootView.findViewById(R.id.exo_player);

    mPlayerView
        .setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.mediaoffline));
    mPlayerView.setPlayer(mExoPlayer);

    tvLongDescription = rootView.findViewById(R.id.long_description);
    tvLongDescription.setText(longDescription);

    return rootView;

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
