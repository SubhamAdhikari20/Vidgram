package com.example.vidgram.services

import android.app.Application
import com.example.vidgram.databinding.ActivityMessageBinding
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import java.util.Collections

object ZegoService {

    private var appId: Long = 1210357865L
    private var appSign: String = "91a60c4d71ca388951e2c9947aee21cd46b16f28fdf3f8b75aa6d2eb4f2273f6"
    private var senderId: String? = null

    fun initZego(application: Application, senderId: String) {
        this.senderId = senderId
        val callInvitationConfig = com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig()

        ZegoUIKitPrebuiltCallService.init(
            application,
            appId,
            appSign,
            senderId,
            senderId,
            callInvitationConfig
        )
    }

    fun prepareVideoCall(binding: ActivityMessageBinding, targetUserId: String) {
        binding.videocallbutton.setIsVideoCall(true)
        binding.videocallbutton.setResourceID("zego_uikit_call")
        binding.videocallbutton.setInvitees(
            Collections.singletonList(ZegoUIKitUser(targetUserId, targetUserId))
        )
    }

    fun prepareAudioCall(binding: ActivityMessageBinding, targetUserId: String) {
        binding.voicecallbutton.setIsVideoCall(false)
        binding.voicecallbutton.setResourceID("zego_uikit_call")
        binding.voicecallbutton.setInvitees(
            Collections.singletonList(ZegoUIKitUser(targetUserId, targetUserId))
        )
    }
}
