package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import android.content.Intent;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import java.net.URI;

public class PlayerActivity extends AppCompatActivity {
    ExoPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Intent intent=getIntent();
        Uri fileUri=intent.getParcelableExtra("fileUri");
        player= new ExoPlayer.Builder(this).build();
        PlayerView playerView=findViewById(R.id.playerView);
        playerView.setPlayer(player);
        MediaItem mediaItem=MediaItem.fromUri(fileUri);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);

    }

    @Override
    protected void onStop() {
        super.onStop();
        player.setPlayWhenReady(false);
        player.release();
        player=null;
    }
}