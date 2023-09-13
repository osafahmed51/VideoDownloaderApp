package com.example.myapplication.Activities

import android.R
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.example.myapplication.Fragments.Downloadbottmsheet
import com.example.myapplication.Model.VideoInfo
import com.example.myapplication.databinding.FragmentVideoPlayerBinding


class VideoPlayer : Fragment(com.example.myapplication.R.layout.fragment_video_player) {
    private lateinit var binding: FragmentVideoPlayerBinding
    private lateinit var videoinfoodata : VideoInfo
    private var videoinfofilepath : String?=null
    private var mediaControls : MediaController?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentVideoPlayerBinding.bind(view)


        if(arguments != null)
        {
            videoinfoodata=arguments?.getSerializable(Downloadbottmsheet.Bottom_Sheet_Data_Key) as VideoInfo
            videoinfofilepath=videoinfoodata.filePath
        }


        if (mediaControls == null) {

            mediaControls = MediaController(requireContext())

            mediaControls!!.setAnchorView(binding.simpleVideoView)
        }


        binding.simpleVideoView.setMediaController(mediaControls)


        binding.simpleVideoView.setVideoURI(
            Uri.parse(
                videoinfofilepath
            )
        )

        binding.simpleVideoView.requestFocus()


        binding.simpleVideoView.start()


        binding.simpleVideoView.setOnCompletionListener(
            OnCompletionListener {
                Toast
                    .makeText(
                        requireContext(),
                        "Video Completed",
                        Toast.LENGTH_LONG
                    )
                    .show()
            })

        binding.simpleVideoView.setOnErrorListener(
            MediaPlayer.OnErrorListener { mediaPlayer, i, i1 ->
                Toast
                    .makeText(
                        requireContext(), """An Error Occurred " +
                    "While Playing Video !!!""",
                        Toast.LENGTH_SHORT
                    )
                    .show()
                false
            })


    }
}